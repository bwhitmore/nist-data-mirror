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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.Enumeration;

/**
 * A GZip file extractor for the nistdatamirror.FileExtractor framework.
 * <p>
 *  @author Brent Whitmore (bwhitmore@whitware.com)
 */
public class GZipFileExtractor extends FileExtractor {

    /**
     * Default constructor to support reflective creation
     *
     * @throws FileExtractionException  thrown when no GZipFileExtractor can be
     *                                  created.
     */
    protected GZipFileExtractor() throws FileExtractionException {
        super();
    }


    /**
     * Create and initialize a new GZipFileExtractor to extract files from
     * a file.  Report progress via a ConsoleAnnunciator created by the
     * new GZipFileExtractor.
     *
     * @param   aFile   the file from which to extract files
     */
    GZipFileExtractor(File aFile) throws FileExtractionException {
        super(aFile);
    }

    /**
     * Create and initialize a new GZipFileExtractor to extract files from
     * a file.  Report progress via the supplied progressReporter that
     * conforms to the Annunciator interface.
     *
     * @param   aFile   the file from which to extract files
     * @param   progressReporter    the object that the new GZipFileExtractor will
     *                              use to report its progress
     */
    GZipFileExtractor(File aFile, Annunciator progressReporter) throws FileExtractionException {
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
        return new String[] {".gz"};
        // be sure to include the period separator, so that it gets checked and stripped off
    }

   /**
     * Indicate whether or not a GZipFileExtractor extracts one file or many files.
     *
     * @return  True if and only if this extractor extracts one file only, or False if this extractor
     *          extracts a system of more than one 
     */
    public Boolean extractsFileSystem() {
        // Only one file per GZip file.
        return false;
    }

    /**
     * GZipFileExtractor's implementation of file extraction of GZip files. 
     *
     * @param   outDir      the file directory into which to extract the contents of the 
     *                      GZipFileExtractor's file.
     * @param   recursion   an Integer indicating the limit of recursive extractions.  If less
     *                      than or equal to zero, the FileExtractor does no extraction.
     *
     * @throws  FileExtractionException     thrown when the FileExtractor encounters an error that 
     *                                      prevents the extraction.
     */
    protected void extractFileToImpl(File outDir, Integer recursion) throws FileExtractionException {
        if (recursion > 0) {
            GZIPInputStream gzis = null;
            FileOutputStream out = null;
            try {
                try {
                    byte[] buffer = new byte[1024];
                    File outFile = new File(outDir, this.stripSuffix(myFile.getName()));
                    if (outFile.exists() && !outFile.isFile()) {
                        throw new FileExtractionException("Cannot write to existing file " + outFile);
                    } 
                    myAnnunciator.announce("Uncompressing " + myFile.getName());
                    gzis = new GZIPInputStream(new FileInputStream(myFile));
                    out = new FileOutputStream(outFile);
                    int len;
                    while ((len = gzis.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                        myAnnunciator.busyPrint();
                    }
                    recursivelyExtractFile(outFile, recursion);
                } finally {
                    if (gzis != null)
                        gzis.close();
                    if (out != null)
                        out.close();
                    myAnnunciator.endBusy();
                }
            } catch (IOException|IllegalArgumentException e) {
                throw new FileExtractionException(e);
            }
            myAnnunciator.announce("Extracted all files from " + myFile);
        }
    }
}