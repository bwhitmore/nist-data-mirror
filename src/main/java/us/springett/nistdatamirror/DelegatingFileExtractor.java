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
import java.io.IOException;
import java.util.ArrayList;
import java.io.StringWriter;
import java.io.PrintWriter;

/**
 * @author Brent Whitmore (bwhitmore@whitware.com)
 */

/**
 * This class uses other FileExtractors to do its work.  It interrogates the available
 * FileExtractors, building up a list of suffixes, indicating file types.  It then delegates
 * its extraction task by dispatching to each FileExtractor based upon the subject
 * file's suffix,  If a delegate's extraction fails, DelegatingFileExtractor keeps trying
 * the remaining delegates until it either has success or all of the delegates fail.  If
 * all of them fail, then DelegatingFileExtractor throws an exception.
 */
public class DelegatingFileExtractor extends FileExtractor {

    /**
     * Protected default constructor to support reflective creation.
     *
     * @throws FileExtractionException  Thrown when no DelegatingFileExtractor can be
     *                                  created.
     */
    protected DelegatingFileExtractor() throws FileExtractionException {
        super();
    }


    /**
     * Create and initialize a new FileExtractor to extract files from
     * a file.  Report progress via a ConsoleAnnunciator created by the
     * new FileExtractor.
     *
     * @param   aFile   the file from which to extract files
     *
     * @throws FileExtractionException  Thrown when no DelegatingFileExtractor can be
     *                                  created.
     */
    DelegatingFileExtractor(File aFile) throws FileExtractionException {
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
    DelegatingFileExtractor(File aFile, Annunciator progressReporter) throws FileExtractionException {
        super(aFile, progressReporter);
    }

    private static String[] suffixes = null;
    /**
     * Return the suffixes of the types of files that this extractor supports.
     *
     * @return  an array of Strings containing the suffixes.
     *
     * @throws  FileExtractionException     thrown when the FileExtractor encounters an error that 
     *                                      prevents this extractor from extracting ANY file.
     */
    public String[] getSuffixes() throws FileExtractionException {
        if (suffixes == null) {
            suffixes = new String[0];
            ArrayList<String> sfxList = new ArrayList<String>();
            ArrayList<Class<? extends FileExtractor>> extractors = getAvailableExtractorClasses();
            for (Class<? extends FileExtractor> xClass: extractors) {
                try {
                    FileExtractor anExtractor = xClass.newInstance();
                    anExtractor.initialize(myFile, myAnnunciator);
                    for (String aString: anExtractor.getSuffixes()) {
                        sfxList.add(aString);
                    }
                } catch (IllegalAccessException e) {
                    throw new FileExtractionException(e);
                } catch (InstantiationException e) {
                    throw new FileExtractionException(e);
                }
            }
            suffixes = sfxList.toArray(suffixes);
        }
        return suffixes;
    }

    /**
     * Indicate whether or not this extractor extracts one file or many files.
     *
     * @return  True if and only if this extractor extracts one file only, or False if this extractor
     *          extracts a system of more than one 
     */
    public Boolean extractsFileSystem() {
        return false;
    }

    private static ArrayList<Class<? extends FileExtractor>> availableExtractorClasses = null;
    /**
     * Subclass helper method that returns an ArrayList containing all the extractor classes 
     * a DelegatingFileExtractor should use when when extracting a file.
     *
     * @return  an ArrayList of Classes to try as extractors when the type of the file
     *          is unknown
     */
    private ArrayList<Class<? extends FileExtractor>> getAvailableExtractorClasses() {
        if (availableExtractorClasses == null) {
            availableExtractorClasses = new ArrayList<Class<? extends FileExtractor>>();
            availableExtractorClasses.add(UdfFileExtractor.class);
            availableExtractorClasses.add(ZipFileExtractorWApache.class);
            availableExtractorClasses.add(GZipFileExtractor.class);
        }
        return availableExtractorClasses;
    }

    /**
     * This extractor's implementation of file extraction. 
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
        if (recursion > 0) {
            FileExtractionException lastExExcept = null;
            FileExtractor anExtractor = null;
            ArrayList<Class<? extends FileExtractor>> extractors = getAvailableExtractorClasses();
            for (Class<? extends FileExtractor> xClass: extractors) {
                try {
                    anExtractor = xClass.newInstance();
                    anExtractor.initialize(myFile, myAnnunciator);
                    anExtractor.extractFile(recursion);
                    anExtractor.close();
                    return;
                } catch (IllegalAccessException e) {
                    throw new FileExtractionException(e);
                } catch (InstantiationException e) {
                    throw new FileExtractionException(e);
                } catch (UnsupportedExtractionException e) {
                    // Skip and move on
                } catch (FormattingException e) {
                    if (anExtractor != null) {
                        myAnnunciator.announce("File extractor " + anExtractor.getClass() 
                            + " could not extract this file due to a formatting error in the file.");
                    }
                    // skip and try another
                } catch (FileExtractionException e) {
                    myAnnunciator.announce("File extractor " + 
                        xClass + " could not fully extract the file " + myFile + ".");
                    myAnnunciator.announce("Reason:  " + e.getMessage());
                    lastExExcept = e;
                    // seve exception, then skip and try another
                } finally {
                    anExtractor = null;
                }
            }
            myAnnunciator.announce("Warning:  Could not extract the contents of file " + myFile.getName() + " using available extractors.");
            throw new UnsupportedExtractionException("Could not extract the contents of file " + myFile + ".", lastExExcept);
        }
    }
}