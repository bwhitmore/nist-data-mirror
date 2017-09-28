/* This file is Copyright (c) 2017 Brent Whitmore. All Rights Reserved.
 *
 * This file is part of nist-data-mirror.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package us.springett.nistdatamirror;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import us.springett.nistdatamirror.internal.MemoryProbe;
import us.springett.nistdatamirror.internal.Udf;
import us.springett.nistdatamirror.internal.KaitaiStream;

/**
 * A UDF file extractor for the nistdatamirror.FileExtractor framework.
 * <p>
 *  @author Brent Whitmore (bwhitmore@whitware.com)
 */
public class UdfFileExtractor extends FileExtractor {

    /**
     * Default constructor to support reflective creation
     *
     * @throws FileExtractionException  thrown when no UdfFileExtractor can be
     *                                  created.
     */
    protected UdfFileExtractor() throws FileExtractionException {
        super();
    }

    /**
     * Create and initialize a new UdfFileExtractor to extract files from
     * a file.  Report progress via a ConsoleAnnunciator created by the
     * new UdfFileExtractor.
     *
     * @param   aFile   the file from which to extract files
     */
    UdfFileExtractor(File aFile) throws FileExtractionException {
        super(aFile);
    }

    /**
     * Create and initialize a new UdfFileExtractor to extract files from
     * a file.  Report progress via the supplied progressReporter that
     * conforms to the Annunciator interface.
     *
     * @param   aFile   the file from which to extract files
     * @param   progressReporter    the object that the new UdfFileExtractor will
     *                              use to report its progress
     */
    UdfFileExtractor(File aFile, Annunciator progressReporter) throws FileExtractionException {
        super(aFile, progressReporter);
    }

    /**
     * Return the suffixes of the types of files that this extractor supports.
     *
     * @return  an array of Strings containing the suffixes.
     *
     * @throws  FileExtractionException     thrown when the FileExtractor encounters an error that 
     *                                      prevents this extractor from extracting ANY file.
     */
    public String[] getSuffixes() throws FileExtractionException {
        return new String[] {".udf", ".iso"};
        // be sure to include the period separator, so that it gets checked and stripped off
    }

    /**
     * Indicate whether or not a UdfFileExtractor extracts one file or many files.
     *
     * @return  True if and only if this extractor extracts one file only, or False if this extractor
     *          extracts a system of more than one 
     */
    public Boolean extractsFileSystem() {
        return true;
    }

    /**
     * UdfFileExtractor's implementation of file extraction of Udf ISO files. 
     *
     * @param   outDir      the file directory into which to extract the contents of the 
     *                      UdfFileExtractor's file.
     * @param   recursion   an Integer indicating the limit of recursive extractions.  If less
     *                      than or equal to zero, the FileExtractor does no extraction.
     *
     * @throws  FileExtractionException     thrown when the FileExtractor encounters an error that 
     *                                      prevents the extraction.
     */
    protected void extractFileToImpl(File outDir, Integer recursion) throws FileExtractionException {
        Udf discFs;
        long currentPos;

        if (recursion > 0) {
            checkSaveLocation(outDir);
            try {
                String filePath = myFile.getCanonicalPath();
                //Give the file and mention if this is treated as a read only file.
                discFs = Udf.fromFile(filePath);
                UdfVolumeInfo volInfo = new UdfVolumeInfo();
                volInfo.extractFrom(discFs);

                Udf.LongAd fSetAccessDesc = volInfo.logicalVolDescriptorBody().fileSetDescExtent();
                long fsetLength = fSetAccessDesc.extentLength();
                Udf.LbAddr fSetDescLocation = fSetAccessDesc.extentLocation();

                long fSetBlockNum = volInfo.partitionDescriptor().partitionStartingLocation() 
                                    + fSetDescLocation.logicalBlockNum();
                currentPos = fSetBlockNum * discFs.sectorSize();
                discFs._io().seek(currentPos);

                // Read and process file set descriptor
                Udf.FileSetDescriptor fsd = new Udf.FileSetDescriptor(discFs._io());
                Udf.LongAd rootAD = fsd.rootDirectoryIcb();
                Udf.LbAddr rootLocation = rootAD.extentLocation();
                long rootSector = rootLocation.logicalBlockNum();
                int rootPartition = rootLocation.partitionRefNum();
                long rootLength = rootAD.extentLength();
                if (rootPartition != volInfo.partitionDescriptor().partitionNumber()) {
                    throw new IOException("Invalid UDF format - partition number for disc image does not" + 
                        " match that of the root directory partition.");
                }

                // Seek to, read, and process root directory ICB
                currentPos = (volInfo.partitionDescriptor().partitionStartingLocation() + rootSector) * discFs.sectorSize();
                discFs._io().seek(currentPos);
                processUdfDirectoryEntry(discFs, volInfo, outDir, recursion);
            } catch (Exception e){
                throw new FileExtractionException(e);
            }
            myAnnunciator.announce("Extracted all files from " + myFile);
        }

    }

    // private elements

    private interface ExtentProcess {
        public void processBytes(byte[] b) throws java.io.IOException;
    }


    private class UdfVolumeInfo {
        private Udf.PartitionDescBody partitionDescriptor;
        private Udf.LogicalVolumeDescBody logicalVolDescriptorBody;

        UdfVolumeInfo() {
            super();
            partitionDescriptor = null;
            logicalVolDescriptorBody = null;
        }

        public Udf.PartitionDescBody partitionDescriptor() {
            return this.partitionDescriptor;
        }

        public Udf.LogicalVolumeDescBody logicalVolDescriptorBody() {
            return this.logicalVolDescriptorBody;
        }

        public UdfVolumeInfo extractFrom(Udf discFs) throws IOException {
            Udf.AnchorVolDescPtr anchor;
            Udf.AnchorPtrExtentAllocDesc mainVolExtent;

            //Give the file and mention if this is treated as a read only file.
            anchor = discFs.anchorVolDescPtrForVolume();
            mainVolExtent = anchor.mainVolDescSeqExtent();
            long position = mainVolExtent.location();

            long volDescCount = mainVolExtent.length() / discFs.sectorSize();
            
             // Position stream to main volume extent.
            long currentPos = position * discFs.sectorSize();
            this.partitionDescriptor = null;
            this.logicalVolDescriptorBody = null;
          processVolDescExtent: 
            for (long ix = 0; ix < volDescCount; ix++) {
                discFs._io().seek(currentPos);
                Udf.VolumeDescHeader aVolDescHeader = new Udf.VolumeDescHeader(discFs._io());

                switch (aVolDescHeader.tag().tagId()) {
                    case PRIMARY_VOLUME_DESCRIPTOR:
                        break;
                    case ANCHOR_VOLUME_DESCRIPTOR_POINTER:
                        break;
                    case VOLUME_DESCRIPTOR_POINTER:
                        break;
                    case IMPLEMENTATION_USE_VOLUME_DESCRIPTOR:
                        break;
                    case PARTITION_DESCRIPTOR:
                        if (this.partitionDescriptor == null) {
                            this.partitionDescriptor = new Udf.PartitionDescBody(discFs._io());
                        } else {
                            throw new IOException("Invalid UDF format - too many partition descriptors.");
                        }
                        break;
                    case LOGICAL_VOLUME_DESCRIPTOR:
                        if (this.logicalVolDescriptorBody == null) {
                            this.logicalVolDescriptorBody = new Udf.LogicalVolumeDescBody(discFs._io());
                        } else {
                            throw new IOException("Invalid UDF format - too many logical volume descriptors.");
                        }
                        break;
                    case UNALLOCATED_SPACE_DESCRIPTOR:
                        break;
                    case TERMINATING_DESCRIPTOR:
                        break processVolDescExtent;
                    case LOGICAL_VOLUME_INTEGRITY_DESCRIPTOR:
                    case FILE_SET_DESCRIPTOR:
                    case FILE_IDENTIFIER_DESCRIPTOR:
                    case ALLOCATION_EXTENT_DESCRIPTOR:
                    case INDIRECT_ENTRY:
                    case TERMINAL_ENTRY:
                    case FILE_ENTRY:
                    case EXTENDED_ATTRIBUTE_HEADER_DESCRIPTOR:
                    case UNALLOCATED_SPACE_ENTRY:
                    case SPACE_BITMAP_DESCRIPTOR:
                    case PARTITION_INTEGRITY_ENTRY:
                    case EXTENDED_FILE_ENTRY:
                        myAnnunciator.announce("Warning:  Unexpected descriptor tag " 
                            + aVolDescHeader.tag().tagId() + " found while scanning volume desciptors.");
                        break;
                    default:
                        throw new IOException("Invalid UDF format - bad descriptor tag found.");
                }
                currentPos += discFs.sectorSize();
            }
            if (this.logicalVolDescriptorBody == null || this.partitionDescriptor == null) {
                throw new IOException("Invalid UDF format - missing logical volume or partition descriptor.");
            }
            
            return this;
        }
    }


    private void processICBInto(Udf discFs, UdfVolumeInfo volInfo, Udf.FileIdentifierDescriptor fileIdDesc, File targetFile, Integer recursion) 
        throws Exception {
        // process ICB to extract file
        long savedPos = discFs._io().pos();
        long icbStreamPos = (volInfo.partitionDescriptor().partitionStartingLocation() 
            + fileIdDesc.icbAd().extentLocation().logicalBlockNum()) * discFs.sectorSize();
        long icbStreamLimit = icbStreamPos + fileIdDesc.icbAd().extentLength();
        int recordedEntriesCount = 0;
        int maxRecordedEntriesCount = -1;
        FileOutputStream targetStream = new FileOutputStream(targetFile);
        boolean captured = false;
        try {
            discFs._io().seek(icbStreamPos);
          processICB:
            while (discFs._io().pos() < icbStreamLimit) {
                if (maxRecordedEntriesCount >= 0 && recordedEntriesCount >= maxRecordedEntriesCount) {
                    break processICB;
                }
                Udf.IcbHeader anIcbHeader = new Udf.IcbHeader(discFs._io());
                Udf.DescriptorTag thisDescTag = anIcbHeader.tag();
                Udf.TagIdentifier thisIcbDescTagId = thisDescTag.tagId();
                Udf.Icbtag thisIcbTag = anIcbHeader.icbTag();

                if (maxRecordedEntriesCount < 0) {
                    maxRecordedEntriesCount = thisIcbTag.maxNumOfEntries();
                    if (maxRecordedEntriesCount < 1) {
                        throw new IOException("Error - maximum number of ICB entries must not be zero.");
                    }
                } else if (maxRecordedEntriesCount != thisIcbTag.maxNumOfEntries()) {
                    throw new IOException("Error - maximum number of ICB entries do not match.");
                }

                switch (thisIcbDescTagId) {
                    case FILE_ENTRY:
                        Udf.IcbFileEntryBody thisEntry = new Udf.IcbFileEntryBody(discFs._io());
                        processUdfExtents(
                            thisEntry.allocationDescriptors(), 
                            thisIcbTag.descriptorUse(),
                            thisEntry.informationLength(), 
                            discFs, 
                            volInfo.partitionDescriptor().partitionStartingLocation(), 
                            (byte[] buf) -> {
                                myAnnunciator.busyPrint();
                                targetStream.write(buf);
                            }
                        );
                        recordedEntriesCount += 1;
                        break;
                    case UNALLOCATED_SPACE_ENTRY:
                    case INDIRECT_ENTRY:
                    case EXTENDED_FILE_ENTRY:
                        throw new IOException(
                            "Error - this implementation does not handle ICB entries tagged as " + 
                            thisIcbDescTagId + " .");
                        // break;
                    case TERMINAL_ENTRY:
                        break processICB;
                    case UNALLOCATED_SPACE_DESCRIPTOR:
                    case TERMINATING_DESCRIPTOR:
                    case PRIMARY_VOLUME_DESCRIPTOR:
                    case ANCHOR_VOLUME_DESCRIPTOR_POINTER:
                    case VOLUME_DESCRIPTOR_POINTER:
                    case IMPLEMENTATION_USE_VOLUME_DESCRIPTOR:
                    case PARTITION_DESCRIPTOR:
                    case LOGICAL_VOLUME_DESCRIPTOR:
                    case LOGICAL_VOLUME_INTEGRITY_DESCRIPTOR:
                    case FILE_SET_DESCRIPTOR:
                    case FILE_IDENTIFIER_DESCRIPTOR:
                    case ALLOCATION_EXTENT_DESCRIPTOR:
                    case EXTENDED_ATTRIBUTE_HEADER_DESCRIPTOR:
                    case SPACE_BITMAP_DESCRIPTOR:
                    case PARTITION_INTEGRITY_ENTRY:
                        throw new IOException(
                            "Invalid UDF format - unexpected descriptor tag " + thisIcbDescTagId + " found.");
                        // break;
                    default:
                        throw new IOException("Invalid UDF format - bad descriptor tag found.");
                }
            }
            captured = true;
        } catch (KaitaiStream.UnexpectedDataError e) {
            throw new FormattingException("File " + myFile  + " is not in UDF format.", e);
        } catch (RuntimeException e) {
            throw new FileExtractionException("A run-time error prevents the extraction of file " + myFile + ".");
        } catch (Exception e) {
            throw new FileExtractionException("Could not extract file " + myFile + ".");
        } finally {
            // End process ICB
            discFs._io().seek(savedPos);
            targetStream.close();
            if (!captured) {
                myAnnunciator.announce("Removing partially-extracted file " + targetFile + ".");
                targetFile.delete();
            }
        }
        recursivelyExtractFile(targetFile, recursion);
    }

    private void processUdfDirectoryEntry(Udf discFs, UdfVolumeInfo volInfo, File whereToSave, Integer recursion) 
        throws Exception {
            
        Udf.IcbHeader aDirIcbHead = new Udf.IcbHeader(discFs._io());
        Udf.Icbtag aDirIcbTag = aDirIcbHead.icbTag();
        Udf.IcbFileEntryBody aDirFileEntry = new Udf.IcbFileEntryBody(discFs._io());
        byte[] ads = aDirFileEntry.allocationDescriptors();
        KaitaiStream dirADStream = new KaitaiStream(ads);
        long idBytesToRead = aDirFileEntry.informationLength();
        long currentPos;

        while (idBytesToRead > 0) {
            while (dirADStream.pos() < dirADStream.size()) {
                Udf.ShortAd anAllocDesc = new Udf.ShortAd(dirADStream);
                currentPos = (volInfo.partitionDescriptor().partitionStartingLocation() 
                    + anAllocDesc.extentBlock()) * discFs.sectorSize();
                discFs._io().seek(currentPos);
                long extentLimit = currentPos + anAllocDesc.extentLength();
                while (currentPos < extentLimit && idBytesToRead > 0) {
                    Udf.FileIdentifierDescriptor fileIdDesc = new Udf.FileIdentifierDescriptor(discFs._io());
                    if (fileIdDesc.tag().tagId() != Udf.TagIdentifier.FILE_IDENTIFIER_DESCRIPTOR) {
                        throw new IOException("Invalid UDF format - bad file identifier descriptor (" 
                            + fileIdDesc.tag().tagId() + ") found.");
                    } else {
                        int rSize = fileIdDesc.recordSize();
                        idBytesToRead -= rSize;
                        currentPos += rSize;
                        String idString = null;
                        if (fileIdDesc.fileIdLength() > 0) {
                            switch (fileIdDesc.cs0Type()) {
                                case 8:
                                    idString = fileIdDesc.fileIdUtf8();
                                    break;
                                case 16:
                                    idString = fileIdDesc.fileIdUtf16();
                                    break;
                                default:
                                    break;
                            }
                        }

                        // extract file
                        if (fileIdDesc.fileIsParentDir() || fileIdDesc.isHidden() || fileIdDesc.fileDeleted()) {
                            // skip .. parent, hidden, deleted entries
                        } else if (fileIdDesc.isDirectory()) {
                            // non-parent directory - save where we are
                            long savedPos = discFs._io().pos();
                            // Build file object for subdirectory and create it.
                            File newSubDir = new File(whereToSave, idString);
                            // Make our new folder if it does not exist
                            if (!newSubDir.exists())
                                newSubDir.mkdirs();
                            // Position to read directory's file entry
                            long icbStreamPos = (volInfo.partitionDescriptor().partitionStartingLocation() 
                                + fileIdDesc.icbAd().extentLocation().logicalBlockNum()) * discFs.sectorSize();
                            // long icbStreamLimit = icbStreamPos + fileIdDesc.icbAd().extentLength();
                            discFs._io().seek(icbStreamPos);
                            // Recurse to process subdirectory contents
                            processUdfDirectoryEntry(discFs, volInfo, newSubDir, recursion);
                            // Restore old position
                            discFs._io().seek(savedPos);
                        } else {
                            File newFile = new File(whereToSave, idString);
                            processICBInto(discFs, volInfo, fileIdDesc, newFile, recursion);
                        }
                    }
                }
            }
        }        
    }

    private long processUdfExtents(
            byte[] ads,
            Udf.IcbDescUse descriptorUse, 
            long bytesLeft, 
            Udf udfFileSys, 
            long partitionLoc, 
            ExtentProcess toDo
        ) throws java.io.IOException {

        long savedPos = udfFileSys._io().pos();
        KaitaiStream adsStream = new KaitaiStream(ads);
        byte[] chunk = null;
        long bytesToRead = bytesLeft;
        final int maxChunkSize = MemoryProbe.maxChunkSizeScaledDn();
        try {
            while (adsStream.pos() < adsStream.size() && bytesToRead > 0) {
                Udf.ShortAd anAllocDesc = new Udf.ShortAd(adsStream);
                long limit = anAllocDesc.extentLength();
                if (limit > bytesToRead) 
                    limit = bytesToRead;
                long extentPos = (partitionLoc + anAllocDesc.extentBlock()) * udfFileSys.sectorSize();
 
                switch(anAllocDesc.extentType()) {
                    case EXTENT_RECORDED_ALLOCATED:
                        {
                            long bytesToWrite = limit;
                            udfFileSys._io().seek(extentPos);
                            while (bytesToWrite > 0) {
                                int chunkSize = (bytesToWrite >= maxChunkSize) ? maxChunkSize : (int) bytesToWrite;
                                chunk = udfFileSys._io().readBytes(chunkSize);
                                // process the extent
                                toDo.processBytes(chunk);
                                bytesToWrite -= chunkSize;
                            }
                        }
                        break;
                    case EXTENT_ALLOCATED_BUT_NOT_RECORDED:
                        {
                            int lastSize = 0;
                            long bytesToWrite = limit;
                            while (bytesToWrite > 0) {
                                int chunkSize = (bytesToWrite >= maxChunkSize) ? maxChunkSize : (int) bytesToWrite;
                                if (chunkSize != lastSize) {
                                    chunk = new byte[chunkSize];
                                    java.util.Arrays.fill(chunk, (byte) 0);
                                    lastSize = chunkSize;
                                }
                                // process the extent
                                toDo.processBytes(chunk);
                                bytesToWrite -= chunkSize;
                            }
                        }
                        break;
                    default:
                        myAnnunciator.announce("Warning:  Cannot process extent type " + anAllocDesc.extentType() + ".");
                        break;
                }
                bytesToRead -= limit;
            }
        } finally {
            chunk = null;
            adsStream.close();
            udfFileSys._io().seek(savedPos);
        }
        if (bytesToRead > 0)
            myAnnunciator.announce("Warning:  " + bytesToRead + " bytes not read.");
        return (bytesToRead);

    }
}