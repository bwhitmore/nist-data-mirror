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

/**
 * @author Brent Whitmore (bwhitmore@whitware.com)
 */

/**
 * Implements much of what is needed to extract one or more files from a
 * file.  Also provides a number of helper methods to to it subclasses.
 *
 * To add support for a file format, subclass this class and implement
 * its abstract methods.  See DelegatingFileExtractor for instructions 
 * regarding adding the new extractor to that class's list of delegates.
 */
public abstract class FileExtractor implements AutoCloseable {
    /**
     * The file from which to extract files.
     */
	protected File myFile;
    /**
     * Object to which this extractor reports its progress.
     */
	protected Annunciator myAnnunciator;

    private Boolean initialized = false;
	private static Annunciator defaultAnnunciator = null;

    /**
     * Represents all exceptions thrown by the FileExtractor class
     * and its subclasses.
     */
	public static class FileExtractionException extends Exception {	
        /**
         * Default constructor.
         */
        public FileExtractionException() {
            super("Could not extract file.");
        }

        /**
         * Message constructor.
         *
         * @param   msg     a String indicating what precipitated the exception
         */
        public FileExtractionException(String msg) {
            super(msg);
        }

        /**
         * Pass-through constructor.
         *
         * @param   e   a Throwable indicating the cause of the exception.
         */
        public FileExtractionException(Throwable e) {
            super(e);
        }

        /**
         * Pass-through constructor with message.
         *
         * @param   msg     a String indicating what precipitated the exception
         * @param   e       a Throwable indicating the cause of the exception.
         */
        public FileExtractionException(String msg, Throwable e) {
            super(msg, e);
        }
	}

    /**
     * Specialization of FileExtractionException indicating that the framework
     * does not support extraction of files from the target archive/file.
     */
    public static class UnsupportedExtractionException extends FileExtractionException {
        /**
         * Default constructor.
         */
        public UnsupportedExtractionException() {
            super("Do not know how to extract file.");
        }

        /**
         * Message constructor.
         *
         * @param   msg     a String indicating what precipitated the exception
         */
        public UnsupportedExtractionException(String msg) {
            super(msg);
        }

        /**
         * Pass-through constructor.
         *
         * @param   e   a Throwable indicating the cause of the exception.
         */
        public UnsupportedExtractionException(Throwable e) {
            super(e);
        }

        /**
         * Pass-through constructor with message.
         *
         * @param   msg     a String indicating what precipitated the exception
         * @param   e       a Throwable indicating the cause of the exception.
         */
        public UnsupportedExtractionException(String msg, Throwable e) {
            super(msg, e);
        }
    }

    /**
     * Specialization of FileExtractionException indicating that the framework
     * detected what it believes to be an ill-formatted file.
     */
    public static class FormattingException extends FileExtractionException {
        /**
         * Default constructor.
         */
        public FormattingException() {
            super("File is not in the expected format.");
        }

        /**
         * Message constructor.
         *
         * @param   msg     a String indicating what precipitated the exception
         */
        public FormattingException(String msg) {
            super(msg);
        }

        /**
         * Pass-through constructor.
         *
         * @param   e   a Throwable indicating the cause of the exception.
         */
        public FormattingException(Throwable e) {
            super(e);
        }

        /**
         * Pass-through constructor with message.
         *
         * @param   msg     a String indicating what precipitated the exception
         * @param   e       a Throwable indicating the cause of the exception.
         */
        public FormattingException(String msg, Throwable e) {
            super(msg, e);
        }
    }


    private static class Options {
        public Integer recursion = Integer.MAX_VALUE;

        @Override
        public String toString() {
            return (
                "Recursion option: " + recursion.toString()
                );
        }
    }

    /** 
     * A static main() method to support command line invocation.
     *
     * @param   args    the command line to be processed.
     */
    public static void main (String[] args) {
        // Ensure at least one argument was specified
        ArrayList<String> files = new ArrayList<String>();
        Options opts = new Options();
        for (int aix = 0; aix < args.length; aix++) {
            String thisArg = args[aix].toLowerCase();
            if (thisArg.equals("-h") || thisArg.equals("--help")) {
                printUsage(0);
                return;
            } else if (thisArg.startsWith("--recursion=")) {
                String extractArg = thisArg.substring(12);
                if ("max".equals(extractArg)) {
                    opts.recursion = Integer.MAX_VALUE;
                } else {
                    try {
                        opts.recursion = new Integer(extractArg);
                    } catch (java.lang.NumberFormatException e) {
                        printUsage(-1);
                    }
                }
            } else if (thisArg.startsWith("-")) {
                System.out.println("Invalid command line argument \"" + thisArg + "\".");
                printUsage(-1);
            } else {
                files.add(args[aix]); // Explicitly NOT lowercase
            }
        }
        if (files.size() < 1) {
            System.out.println("Required list of files to be extracted is missing.");
            printUsage(-1);
            return;
        } else {
            for (String aFileName : files) {
                try {
                    FileExtractor ex = new DelegatingFileExtractor(new File(aFileName));
                    ex.extractFile(opts.recursion);
                } catch (Exception e) {
                    System.out.println("Error encountered:  " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    // Private implementation
    
    private static void printUsage(int exitStatus) {
        System.out.println("Usage: java FileExtractor [options] file-list");
        System.out.println("       Options include:");
        System.out.println("         --recursion=(max|<number of recursive extractions>)");
        System.out.println("             Extract contents of the files in file-list recursively to the maximum");
        System.out.println("             depth indicated.");
        System.out.println("               --recursion=0:    extract no contents from the files,");
        System.out.println("               --recursion=1:    extract the contents of all of the files listed,");
        System.out.println("                                 but do not extract from any of the extracted files.");
        System.out.println("               --recursion=max:  recursively extract all files, including those");
        System.out.println("                                 extracted from the indicated files, to the maximum");
        System.out.println("                                 possible depth.");
        System.out.println("         --help | -h:  Prints this advice.");
        System.out.println("");
        System.out.println("       Default options are --recursion=max.");

        if (exitStatus != 0)
            System.exit(exitStatus);
    }

    /**
     * Default constructor to support reflective creation by subclasses.
     *
     * @throws FileExtractionException  thrown when no FileExtractor can be
     *                                  created.
     */
    protected FileExtractor() throws FileExtractionException {
        myFile = null;
        myAnnunciator = null;
        initialized = false;
    }

    /**
     * Create and initialize a new FileExtractor to extract files from
     * a file.  Report progress via a ConsoleAnnunciator created by the
     * new FileExtractor.
     *
     * @param   aFile   the file from which to extract files
     */
    FileExtractor(File aFile) throws FileExtractionException {
        super();
        initialize(aFile, null);
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
 	FileExtractor(File aFile, Annunciator progressReporter) throws FileExtractionException {
		super();
		initialize(aFile, progressReporter);
	}

    /**
     * Subclass helper that checks the file's name to see if this extractor supports 
     * extraction from files of the types indicated by the name's suffix.  
     *
     * @param   aFile   the file whose name is to be checked
     *
     * @throws  FileExtractionException     when the suffix of the file's name indicates 
     *                                      a type of file that this extractor does not 
     *                                      support.
     */
    protected void checkExtractable(File aFile) throws FileExtractionException {
        String filePath = aFile.getAbsolutePath();
    	String[] suffixes = this.getSuffixes();
        if (suffixes.length == 0) {
            return;     // indicates all are processed and no suffixes stripped.
        }
        for (String aSuffix: suffixes) {
        	if (filePath.endsWith(aSuffix)) {
        		return;
        	}
        }
        throw new UnsupportedExtractionException("Cannot extract file " + aFile + ".");
    }

    /**
     * Subclass helper that initializes the receiving FileExtractor after it has been created,
     * typically using reflection.  
     *
     * @param   aFile   the file from which to extract files
     * @param   progressReporter    the object that the new FileExtractor will
     *                              use to report its progress
     *
     * @throws  FileExtractionException     thrown when the file does not exist or cannot be accessed, or 
     *                                      it is a directory or is otherwise unreadable, or is not a type
     *                                      of file from which this FileExtractor can extract files.
     */
	protected void initialize(File aFile, Annunciator progressReporter) throws FileExtractionException {
        if (initialized) {
            return;
        }
        if (aFile != null) {
            if (!aFile.exists()) {
                throw new FileExtractionException("File to be extracted (" + aFile + ") does not exist or cannot be accessed.");
            }
            if (! (aFile.isFile() && aFile.canRead()) ) {
                throw new FileExtractionException("File " + aFile + " is not a readable file.");
            }
            checkExtractable(aFile);
        }
        myFile = aFile;
        if (progressReporter == null) {
            myAnnunciator = Annunciator.defaultAnnunciator();
        } else {
            myAnnunciator = progressReporter;
        }
        initialized = true;
	}

    /**
     * Subclass helper that throws a FileExtractionException if the receiving FileExtractor has
     * not been initialized after its creation.  
     *
     * @throws  FileExtractionException     thrown when the FileExtractor has not been initialized since
     *                                      its creation.
     */
    protected void checkInitialized() throws FileExtractionException {
        if (!initialized) {
            throw new FileExtractionException("Extractor class was not initialized before extraction.");
        }
    }

    /**
     * Extract the FileExtractor's file in-place, recursively extracting the contents of the
     * extracted files.
     * <p>
     * If the extractor supports a format that can store systems of files, then the extractor
     * removes the file's suffix, and then uses a directory with that resulting name that is 
     * located as a subdirectory of the directory where the file being extracted resides.  
     * The extractor creates the subdirectory if it does not exist. The extractor places the 
     * extracted directory tree in that resulting directory.
     * <p>
     * If the extractor supports a format that can contain only a single file, then extractor
     * places that single extracted file in the same directory that contains the file being
     * extracted.
     * <p>
     * Note that in-place extraction is always used for recursive extractions.
     *
     * @param   recursion       an Integer indicating the limit of recursive extractions.  If less
     *                          than or equal to zero, the FileExtractor does no extraction.
     *
     * @throws  FileExtractionException     thrown when the FileExtractor encounters an error that 
     *                                      prevents the extraction.
     */
    public final void extractFile(Integer recursion) throws FileExtractionException {
        checkInitialized();
        if (recursion > 0) {
            try {
                String filePath = myFile.getCanonicalPath();
                filePath = this.stripSuffix(filePath);
                if (!this.extractsFileSystem()) {
                    int lastSepIx = filePath.lastIndexOf(File.separator);
                    if (lastSepIx >= 0)
                        filePath = filePath.substring(0, lastSepIx);
                }
                File saveLocation = new File(filePath);
                if (saveLocation.exists() &&  !saveLocation.isDirectory()) {
                    saveLocation = new File(filePath + ".dir");
                    myAnnunciator.announce("Setting save location to " + saveLocation);
                }
                checkSaveLocation(saveLocation);
            	this.extractFileTo(saveLocation, recursion);
            } catch (IOException e) {
                throw new FileExtractionException(e);
            }
        }
    }

    /**
     * Extract the FileExtractor's file to the indicated directory, recursively extracting the 
     * contents of the extracted files in directories relative to the indicated directory.
     *
     * @param   outDir      the file directory into which to extract the contents of the 
     *                      extractor's file.
     * @param   recursion   an Integer indicating the limit of recursive extractions.  If less
     *                      than or equal to zero, the FileExtractor does no extraction.
     *
     * @throws  FileExtractionException     thrown when the FileExtractor encounters an error that 
     *                                      prevents the extraction.
     */
    public final void extractFileTo(File outDir, Integer recursion) throws FileExtractionException {
        checkInitialized();
        extractFileToImpl(outDir, recursion);
    }


    /**
     * Releases any resources held by the FileExtractor, making it unfit for further use.
     *
     * @throws  FileExtractionException     thrown when the FileExtractor encounters an error that 
     *                                      prevents a clean closing.
     */
    public void close() throws FileExtractionException {
        if (initialized) {
            try {
                myAnnunciator.close();
            } catch (Exception e) {
                throw new FileExtractionException(e);
            }
            initialized = false;
        }
    }

    // Abstract methods here

    /**
     * Return the suffixes of the types of files that this extractor supports.
     *
     * @return  an array of Strings containing the suffixes.
     *
     * @throws  FileExtractionException     thrown when the FileExtractor encounters an error that 
     *                                      prevents this extractor from extracting ANY file.
     */
    abstract public String[] getSuffixes() throws FileExtractionException;

    /**
     * Indicate whether or not this extractor extracts one file or many files.
     *
     * @return  True if and only if this extractor extracts one file only, or False if this extractor
     *          extracts a system of more than one 
     */
    abstract public Boolean extractsFileSystem();

    /**
     * This extractor's implementation of file extraction.  Subclasses should implement this method.
     *
     * @param   outDir      the file directory into which to extract the contents of the 
     *                      extractor's file.
     * @param   recursion   an Integer indicating the limit of recursive extractions.  If less
     *                      than or equal to zero, the FileExtractor does no extraction.
     *
     * @throws  FileExtractionException     thrown when the FileExtractor encounters an error that 
     *                                      prevents the extraction.
     */
    abstract protected void extractFileToImpl(File outDir, Integer recursion) throws FileExtractionException;

    /**
     * Subclass helper that strips the suffix from a file name and returns it,  The method only 
     * removes those suffixes of the file types supported by this extractor.
     *
     * @param   aFilePath   The file name from which to remove a suffix.
     *
     * @return  The String provided with its suffix removed if that suffix indicates that the
     *          file's type can be extracted by this extractor.
     *
     * @throws  FileExtractionException     thrown when the FileExtractor encounters an error that 
     *                                      prevents the extraction.
     */
    protected String stripSuffix(String aFilePath) throws FileExtractionException {
        for (String aSuffix: this.getSuffixes()) {
            if (aFilePath.endsWith(aSuffix)) {
                return aFilePath.substring(0, aFilePath.length() - aSuffix.length());
            }
        }
        return aFilePath;
    }

    /**
     * Subclass helper method that ensures that the indicated directory exists, creating it if 
     * necessary.  If the method cannot assure the directory's existance, then it throws an
     * exception.  
     *
     * @param   saveLocation    The directory that is to be the new extraction location
     *
     * @throws  FileExtractionException     thrown when the FileExtractor cannot extract files
     *                                      into the indicated directory.
     */
    protected static void checkSaveLocation(File saveLocation) throws FileExtractionException {
        boolean haveDir = saveLocation.exists();
        if (!haveDir)
        	// Make our saving folder if it does not exist
            haveDir = saveLocation.mkdirs();
        if (!haveDir) {
        	throw new FileExtractionException("The destination directory for file extraction could not be accessed.");
        }
    }

    /**
     * Subclass helper method that handles recursive extraction using a DelegatingFileExtractor.  
     *
     * @param   subFile     the file to be recursively extracted
     * @param   recursion   the level of recursion used so far
     *
     * @throws  FileExtractionException     thrown when the DelegatingFileExtractor cannot extract 
     *                                      files from the file subFile.
     */
     protected final void recursivelyExtractFile(File subFile, Integer recursion) 
        throws FileExtractionException {
        if (recursion > 0) {
            try {
                DelegatingFileExtractor delEx = new DelegatingFileExtractor(subFile, myAnnunciator);
                delEx.extractFile(recursion - 1);
                myAnnunciator.busyPrint();
            } catch (UnsupportedExtractionException e) {
                // ignore
            } catch (FormattingException e) {
                 myAnnunciator.announce("Warning:  Could not extract contents of file " + myFile + " - could not determine file's format.");
                // ignore
            }
        }
    }
}