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

import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
// import java.util.zip.ZipFile;
// import java.util.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import java.util.Enumeration;

import us.springett.nistdatamirror.internal.StreamUtil;

/**
 * A Zip file extractor for the nistdatamirror.FileExtractor framework.
 * <p>
 * Uses Apache's ZipFile drop-in replacement for Java's ZipFile so as to avoid
 * problems with large Zip archives.  
 * <p>
 * @author Brent Whitmore (bwhitmore@whitware.com)
 */
public class ZipFileExtractorWApache extends FileExtractor {

    /**
     * Default constructor to support reflective creation by package classes.
     *
     * @throws FileExtractionException  thrown when no ZipFileExtractorWApache can be
     *                                  created.
     */
    protected ZipFileExtractorWApache() throws FileExtractionException {
        super();
    }

    /**
     * Create and initialize a new FileExtractor to extract files from
     * a file.  Report progress via a ConsoleAnnunciator created by the
     * new FileExtractor.
     *
     * @param   aFile   the file from which to extract files
     */
    ZipFileExtractorWApache(File aFile) throws FileExtractionException {
        super(aFile);
    }

    /**
     * Create and initialize a new FileExtractor to extract files from
     * a file.  Report progress via the supplied progressReporter that
     * conforms to the Annunciator interface.
     *
     * @param   aFile   the file from which to extract files
     * @param   progressReporter    the object that the new FileExtractor will
     *                              use to report its progress
     */
    ZipFileExtractorWApache(File aFile, Annunciator progressReporter) throws FileExtractionException {
        super(aFile, progressReporter);
    }

    /**
     * Return the suffixes of the types of files that this extractor supports.
     *
     * @return  an array of Strings containing one string - the suffix ".zip".
     *
     * @throws  FileExtractionException     thrown when the FileExtractor encounters an error that 
     *                                      prevents this extractor from extracting ANY file.
     */
    public String[] getSuffixes() throws FileExtractionException {
        return new String[] {".zip"};
        // be sure to include the period separator, so that it gets checked and stripped off
    }

    /**
     * Indicate whether or not this extractor extracts one file or many files.
     *
     * @return  True if and only if this extractor extracts one file only, or False if this extractor
     *          extracts a system of more than one 
     */
    public Boolean extractsFileSystem() {
        return true;
    }

    /**
     * Extracts a file system from a Zip file archive.
     *
     * @param   outDir      the file directory into which to extract the contents of the 
     *                      extractor's file.
     * @param   recursion   an Integer indicating the limit of recursive extractions.  If less
     *                      than or equal to zero, the FileExtractor does no extraction.
     *
     * @throws  FileExtractionException     thrown when the FileExtractor encounters an error that 
     *                                      prevents the extraction.
     */
    protected void extractFileToImpl(File outDir, Integer recursion) throws FileExtractionException {
        ZipFile zf = null;
        if (recursion > 0) {
            try {
                checkSaveLocation(outDir);
                try {
                    zf = new ZipFile(myFile);
                    Enumeration<ZipArchiveEntry> entryEnum = (Enumeration<ZipArchiveEntry>)zf.getEntries();
                    while (entryEnum.hasMoreElements()) {
                        InputStream entryStream = null;
                        FileOutputStream outStr = null;
                        try {
                            ZipArchiveEntry anEntry = entryEnum.nextElement();
                            File outFile = new File(outDir, anEntry.getName());
                            myAnnunciator.busyPrint();
                            if (anEntry.isDirectory()) {
                                outFile.mkdirs();
                            } else {
                                if (outFile.isFile()) {
                                    outFile.delete();
                                }
                                outFile.createNewFile();
                                outStr = new FileOutputStream(outFile);
                                entryStream = zf.getInputStream(anEntry);
                                StreamUtil.streamUntilEof(entryStream, outStr, anEntry.getSize(), myAnnunciator);
                                outStr.close();
                                entryStream.close();
                                recursivelyExtractFile(outFile, recursion);
                            }
                        } finally {
                            if (outStr != null)
                                outStr.close();
                            if (entryStream != null)
                                entryStream.close();
                        }
                    }
                } catch (IOException|IllegalArgumentException e){
                    throw new FileExtractionException(e);
                } finally {
                    if (zf != null) {
                        zf.close();
                        zf = null;
                    }
                    myAnnunciator.endBusy();
                }
            } catch (IOException e) {
                throw new FileExtractionException(e);
            }
            myAnnunciator.announce("Extracted all files from " + myFile);
        }
    }
}