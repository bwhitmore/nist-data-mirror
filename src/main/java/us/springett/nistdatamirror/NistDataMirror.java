/* This file is Copyright (c) 2013-2017 Steve Springett and Brent Whitmore. All Rights Reserved.
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Supplier;

import us.springett.nistdatamirror.internal.StreamUtil;

// import io.kaitai.struct.KaitaiStream;

/**
 * Downloads the contents of NIST's NVD CPE/CVE XML and JSON data as well as 
 * NIST's NSRL hash sets to the specified output path.  Once it has 
 * downloaded the files, NistDataMirror optionally calls its own file 
 * extraction framework to recursively extract the files contained in each 
 * of the downloaded files.
 * <p>
 * This class can be called from the command-line or as a library. 
 *
 * @author Steve Springett (steve.springett@owasp.org) - creator
 * @author Brent Whitmore (bwhitmore@whitware.com) - contributor
 */
public class NistDataMirror 
    implements Supplier {

    // Public interface

    /**
     * Constructor specifying the options that the mirror should use.
     *
     * @param   opts        The options that the mirror should use when
     *                      its get() method is called.  If the value
     *                      null is passed, then the default options are
     *                      used.
     * @param   reporter    The annunciator the mirror should use when
     *                      reporting its progress.  If the value null
     *                      is passed, then the default Annunciator is
     *                      used.
     * @param   dest        The directory where the downloaded and extracted
     *                      files should be stored.
     */
    public NistDataMirror(Options opts, Annunciator reporter, File dest) {
        super();
        if (opts != null) {
            this.options = opts;
        } else {
            this.options = new Options();
        };
        if (reporter == null) {
            annunciator = Annunciator.defaultAnnunciator();
        } else {
            annunciator = reporter;
        };
        destination = dest;
    }

    /**
     * Constructor specifying the options that the mirror should use.
     *
     * @param   opts        The options that the mirror should use when
     *                      its get() method is called.  If the value
     *                      null is passed, then the default options are
     *                      used.
     * @param   reporter    The annunciator the mirror should use when
     *                      reporting its progress.  If the value null
     *                      is passed, then the default Annunciator is
     *                      used.
     */
    public NistDataMirror(Options opts, Annunciator reporter) {
        this(opts, reporter, null);
    }

    /**
     * Constructor specifying the options that the mirror should use.  The
     * default Annunciator is used.
     *
     * @param   opts    The options that the mirror should use when
     *                  its get() method is called.
     */
    public NistDataMirror(Options opts) {
        this(opts, null, null);
    }

    /**
     * Default constructor using the mirror's default options. The
     * default Annunciator is used.  Extract files to the user's current
     * working directory.
     */
    public NistDataMirror() {
        this(null, null, null);
    }

   /**
     * Download and optionally extract the NIST files to a specified directory while
     * observing the options established when the mirror object was created.
     * <p>
     * Implements Java's java.util.function.Supplier interface, making the class
     * suitable for use as a lamda or as the subject of numerous uses of Supplier
     * instances in the Java framework dealing with services and concurrency. 
     *
     * @return  The CompletionStatus of the downloads and extractions when it stops.
     */
    public CompletionStatus get() {
        File where;
        if (destination == null) {
            String whereStr = System.getProperty("user.dir");
            if (whereStr == null) {
                final String msg = "Could not determine the caller's current working directory.";
                annunciator.announce(msg);
                throw new Error(msg);
            }
            where = FileSystems.getDefault().getPath(whereStr).toAbsolutePath().toFile();
        } else {
            where = destination;
        }
        Date startDate = new Date();
        Date stopDate = null;
        this.status.ran = true;
        this.status.completed  = false;
        try {
            checkOutputDir(where);
            annunciator.announce("Downloading files at " + startDate);
            getNvdFiles(where);
            getNsrlFiles(where);
            this.status.completed = true;
            stopDate = new Date();
            annunciator.announce("Downloads complete at " + stopDate);
        } catch (IOException e) {
            stopDate = new Date();
            annunciator.announce("Downloads aborted at " + stopDate);
        }
        return status;
    }

    /**
     * Specifies which NSRL hash sets to download and optionally extract.
     */
    public static enum NsrlOption {
        /**
        * Download no NSRL hash set files.
        */
        NONE(0),
        /**
        * Download the Modern NSRL hash set files.
        */
        MODERN(1),
        /**
        * Download the Legacy hash set files.
        */
        LEGACY(2),
        /**
        * Download the Android NSRL hash set files.
        */
        ANDROID(4),
        /**
        * Download the iOS NSRL hash set files.
        */
        IOS(8),
        /**
        * Download all NSRL hash set files.
        */
        ALL(15);

        private final Integer id;
        NsrlOption(Integer id) { this.id = id; }

        /**
        * Return the integer value used to represent this NsrlOption.
        *
        * @return the integer value representing this NsrlOption.
        */
        public Integer id() { return id; }

        private static final Map<Integer, NsrlOption> byId = new HashMap<Integer, NsrlOption>(6);
        static {
            for (NsrlOption e : NsrlOption.values())
                byId.put(e.id(), e);
        }

        /**
        * Return the NsrlOption enum represented by an integer value.
        *
        * @param id     the Integer value used to represent the returned NsrlOption.  This 
        *               value must be one of the enum values declared by the NsrlOption type.
        *
        * @return the NsrlOption enum constant that represents the integer value.
        */
        public static NsrlOption byId(Integer id) { return byId.get(id); }

        /**
        * Returns the enum constant of this type with the specified name. 
        * 
        *
        * @param aString    the String used to represent the returned NsrlOption enum.  The 
        *                   string must match exactly an identifier used to declare an enum 
        *                   constant in this type. Extraneous whitespace characters are not 
        *                   permitted.
        *
        * @return   the NsrlOption enum constant that represents the specified name, or null
        *           if aString is invalid.
        */
        public static NsrlOption fromString(String aString) {
            NsrlOption answer = null;
            try {
                answer = valueOf(NsrlOption.class, aString.toUpperCase());
            } catch (IllegalArgumentException e) {
            } catch (NullPointerException e) {
            }
            return answer;
        }
    }

    /**
     * Specifies which NVD data to download and to optionally extract.
     */
    public static enum NvdOption {
        /**
        * Download no NVD files.
        */
        NONE(0),
        /**
        * Download those NVD files that are in XML format.
        */
        XML(1),
        /**
        * Download those NVD files that are in JSON format.
        */
        JSON(2),
        /**
        * Download all NVD files.
        */
        ALL(3);

        private final Integer id;
        NvdOption(Integer id) { this.id = id; }

        /**
        * Return the integer value used to represent this NvdOption.
        *
        * @return the integer value representing this NvdOption.
        */
        public Integer id() { return id; }

        private static final Map<Integer, NvdOption> byId = new HashMap<Integer, NvdOption>(6);
        static {
            for (NvdOption e : NvdOption.values())
                byId.put(e.id(), e);
        }

        /**
        * Return the NvdOption enum represented by an integer value.
        *
        * @param id     the Integer value used to represent the returned NvdOption.  This 
        *               value must be one of the enum values declared by the NvdOption type.
        *
        * @return the NvdOption enum constant that represents the integer value.
        */
        public static NvdOption byId(Integer id) { return byId.get(id); }

        /**
        * Returns the enum constant of this type with the specified name. 
        * 
        *
        * @param aString    the String used to represent the returned NvdOption enum.  The 
        *                   string must match exactly an identifier used to declare an enum 
        *                   constant in this type. Extraneous whitespace characters are not 
        *                   permitted.
        *
        * @return   the NvdOption enum constant that represents the specified name, or null
        *           if aString is invalid.
        */
        public static NvdOption fromString(String aString) {
            NvdOption answer = null;
            try {
                answer = valueOf(NvdOption.class, aString.toUpperCase());
            } catch (IllegalArgumentException e) {
            } catch (NullPointerException e) {
            }
            return answer;
        }
    }

    /**
     * Specifies the options that the mirror will observe while downloading and extracting 
     * its data.
     */
    public static class Options {

        /**
         * Options for NSRL downloads.  Default value specifies no NSRL downloads.
         */
        public NsrlOption nsrlOpt = NsrlOption.NONE;

        /**
         * Options for NVD downloads.  Default value specifies that all NVD data will
         * be downloaded.
         */
        public NvdOption nvdOpt = NvdOption.ALL;

        /**
         * Intteger specifying how deeply to recurse the extraction of downloaded files.  The
         * default limits extraction to one level, that is, the downloaded files are extracted,
         * but the files they contain are not.
         */
        public Integer recursion = new Integer(1);

        /**
         *  Provides a string representation suitable for use by System.out.print, etc.
         */
        @Override
        public String toString() {
            return (
                "NSRL option: " + nsrlOpt + 
                ", NVD option: " + nvdOpt +
                ", Extraction recursion option: " + recursion);
        }
    }

    /**
     * Communicates the status of a particular class of attempted operations.
     */
    public static class OpGroupStatus {
        /**
         * The number of operations attempted and succeeded.
         */
        public Long opSucceeded;

        /**
         * The number of operations attempted and failed.
         */
        public Long opFailed;

        /**
         * The number of errors encountered while attempting operations.
         */
        public Long errors;

        /**
         * The status of a particular class of attempted operations.
         * <p>
         * This constructor initializes all of its counts to zero.
         */
        OpGroupStatus() {
            opSucceeded = new Long(0);
            opFailed = new Long(0);
            errors = new Long(0);
        }

        /**
         *  Provides a string representation suitable for use by System.out.print, etc.
         */
        @Override
        public String toString() {
            return (
                "Succeeded " + opSucceeded + " times, " +
                "Failed " + opFailed + " times, " +
                "Encountered " + errors + " errors");
        }
    }

    /**
     * Communicates the degree of the mirror's task completion, once done.  
     */
    public static class CompletionStatus {
        /**
         * Whether or not the task started (at all).
         */
        public Boolean ran;

        /**
         * Whether or not the task completed, partially or completely.
         */
        public Boolean completed;
        
        /**
         * The status of the task's NSRL download attempts.
         */
        public OpGroupStatus nsrlDownloadStat;
        
        /**
         * The status of the task's NVD download attempts.
         */
        public OpGroupStatus nvdDownloadStat;
        
        /**
         * The status of the task's extraction attempts.
         */
        public OpGroupStatus extractStat;

        
        /**
         * The status of the task's operations over the course of its entire execution.
         * <p>
         * This constructor is initialized with zero-ed counts and false <code>ran</code>
         * and <code>completed</code> values.
         */
        CompletionStatus() {
            ran = new Boolean(false);
            completed = new Boolean(false);
            nsrlDownloadStat = new OpGroupStatus();
            nvdDownloadStat = new OpGroupStatus();
            extractStat = new OpGroupStatus();
        }

        /**
         *  Provides a string representation suitable for use by System.out.print, etc.
         */
        @Override
        public String toString() {
            return (
                "Ran: " + ran + 
                ", Completed: " + completed + 
                ", \nNSRL download status: " + nsrlDownloadStat +
                ", \nNVD download status: " + nvdDownloadStat + 
                ", \nExtraction status: " + extractStat);
        }
    }
    

    /**
    * An entry point suitable for use as a command line processor.
    *
    * @param args   an array of Strings that comprise the command line.
    */
    public static void main (String[] args) {

        // Set up to direct all progress reporting to the console.
        Annunciator thisAnnunciator = new ConsoleAnnunciator();

        // Parse the command line, parsing out the --extract-files and --help subcommands first.
        String aDir = null;
        Options opts = new Options();
        for (int aix = 0; aix < args.length; aix++) {
            String thisArg = args[aix].toLowerCase();
            if (thisArg.equals("--extract-files")) {
                String [] fxArgs = java.util.Arrays.copyOfRange(args, aix + 1, args.length);
                FileExtractor.main(fxArgs);
                return;
            } else if (thisArg.equals("-h") || thisArg.equals("--help")) {
                printUsage(0);
                return;
            }
        }
        for (int aix = 0; aix < args.length; aix++) {
            String thisArg = args[aix].toLowerCase();
            if (thisArg.startsWith("--nsrl=")) {
       
                NsrlOption nsrlSelection = 
                    NsrlOption.fromString(thisArg.substring(7));
                if (nsrlSelection != null) {
                    opts.nsrlOpt = nsrlSelection;
                }
            } else if (thisArg.startsWith("--nvd=")) {
                NvdOption nvdSelection = 
                    NvdOption.fromString(thisArg.substring(6));
                if (nvdSelection != null) {
                    opts.nvdOpt = nvdSelection;
                }
            } else if (thisArg.startsWith("--extract=")) {
                String extractSelection = thisArg.substring(10);
                String extractArg = thisArg.substring(10);
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
                printUsage(-2);
                return;
            } else {
                aDir = args[aix]; // Explicitly NOT forced to lowercase
                NistDataMirror mirror = new NistDataMirror(opts, thisAnnunciator, new File(aDir));
                CompletionStatus result = mirror.get();
                System.out.println(result);
            }
        }
        if (aDir == null) {
            // Ensure at least one argument was specified
            System.out.println("Required path to output directory is missing.");
            printUsage(-3);
            return;
        }
    }

    // Private implementation
    
    private static void printUsage(int exitStatus) {
        System.out.println("Usage: java NistDataMirror [options] outputDir [newoptions] outputDir ...");
        System.out.println("         where options include:");
        System.out.println("           --nsrl=(modern|legacy|android|ios|all|none)");
        System.out.println("               Choose National Software Reference Library files to download.");
        System.out.println("           --nvd=(xml|json|all|none)");
        System.out.println("               Choose National Vulnerability Database files to download.");
        System.out.println("           --extract=(max|<numeric depth of recursive extractions>)");
        System.out.println("               Extract contents of downloaded UDF (DVD) and ZIP archives and");
        System.out.println("               GZip-compressed files recursively to a maximum of the indicated");
        System.out.println("               number of times.");
        System.out.println("                 --extract=0:    extracts no contents from the downloaded files,");
        System.out.println("                 --extract=1:    extracts the contents of all downloaded files,");
        System.out.println("                                 but does not extract any further.");
        System.out.println("                 --extract=max:  recursively extracts all files, both from");
        System.out.println("                                 downloaded and extracted files.");
        System.out.println("         Default options are --nvd=all --nsrl=none --extract=1");
        System.out.println("");
        System.out.println("       --or--");
        System.out.println("");
        System.out.println("       java NistDataMirror --extract-files (<args>|-h):  invokes extraction only.");
        System.out.println("              Use \"--extract-files -h\" for options.");
        System.out.println("");
        System.out.println("       --or--");
        System.out.println("");
        System.out.println("       java NistDataMirror --help | -h:  Prints this advice.");

        if (exitStatus != 0)
            System.exit(exitStatus);
    }

    // These are tied to how NIST releases these resources.

    // NVD data
    private static final String CVE_XML_ROOT = "https://nvd.nist.gov/";
    private static final String CVE_XML_12_MODIFIED_URL = CVE_XML_ROOT  + "download/nvdcve-Modified.xml.gz";
    private static final String CVE_XML_20_MODIFIED_URL = CVE_XML_ROOT  + "feeds/xml/cve/nvdcve-2.0-Modified.xml.gz";
    private static final String CVE_XML_12_BASE_URL = CVE_XML_ROOT  + "download/nvdcve-%d.xml.gz";
    private static final String CVE_XML_20_BASE_URL = CVE_XML_ROOT  + "feeds/xml/cve/nvdcve-2.0-%d.xml.gz";

    private static final String CVE_JSON_ROOT = "https://static.nvd.nist.gov/feeds/json/cve/";
    private static final String CVE_JSON_10_MODIFIED_URL = CVE_JSON_ROOT + "1.0/nvdcve-1.0-modified.json.gz";
    private static final String CVE_JSON_10_BASE_URL = CVE_JSON_ROOT + "1.0/nvdcve-1.0-%d.json.gz";

    private static final int START_YEAR = 2002;
    private static final int END_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    // NSRL data
    private static final String NSRL_ISO_CURRENT_ROOT = "https://s3.amazonaws.com/rds.nsrl.nist.gov/RDS/current/";
    private static final String NSRL_ISO_CURRENT_MODERN = NSRL_ISO_CURRENT_ROOT + "RDS_modern.iso";
    private static final String NSRL_ISO_CURRENT_LEGACY = NSRL_ISO_CURRENT_ROOT + "RDS_legacy.iso";
    private static final String NSRL_ISO_CURRENT_ANDROID = NSRL_ISO_CURRENT_ROOT + "RDS_android.iso";
    private static final String NSRL_ISO_CURRENT_IOS = NSRL_ISO_CURRENT_ROOT + "RDS_ios.iso";

    // Private state

    private Annunciator annunciator = new ConsoleAnnunciator(); // Annunciator.defaultAnnunciator();
    private Options options = new Options();
    private CompletionStatus status = new CompletionStatus();
    private File destination = null;


    // private methods

    private void getNsrlFiles(File outputDir) throws IOException {
        if ((options.nsrlOpt.id() & NsrlOption.MODERN.id()) != 0) {
            doDownload(NSRL_ISO_CURRENT_MODERN, outputDir, status.nsrlDownloadStat, status.extractStat);
        }
        if ((options.nsrlOpt.id() & NsrlOption.LEGACY.id()) != 0) {
            doDownload(NSRL_ISO_CURRENT_LEGACY, outputDir, status.nsrlDownloadStat, status.extractStat);
        }
        if ((options.nsrlOpt.id() & NsrlOption.ANDROID.id()) != 0) {
            doDownload(NSRL_ISO_CURRENT_ANDROID, outputDir, status.nsrlDownloadStat, status.extractStat);
        }
        if ((options.nsrlOpt.id() & NsrlOption.IOS.id()) != 0) {
            doDownload(NSRL_ISO_CURRENT_IOS, outputDir, status.nsrlDownloadStat, status.extractStat);
        }
    }


    private void getNvdFiles(File outputDir) throws IOException {
        if ((options.nvdOpt.id() & NvdOption.XML.id()) != 0) {
            doDownload(CVE_XML_12_MODIFIED_URL, outputDir, status.nvdDownloadStat, status.extractStat);
            doDownload(CVE_XML_20_MODIFIED_URL, outputDir, status.nvdDownloadStat, status.extractStat);
            for (int i=START_YEAR; i<=END_YEAR; i++) {
                String cve12BaseUrl = CVE_XML_12_BASE_URL.replace("%d", String.valueOf(i));
                String cve20BaseUrl = CVE_XML_20_BASE_URL.replace("%d", String.valueOf(i));
                doDownload(cve12BaseUrl, outputDir, status.nvdDownloadStat, status.extractStat);
                doDownload(cve20BaseUrl, outputDir, status.nvdDownloadStat, status.extractStat);
            }
        }
        if ((options.nvdOpt.id() & NvdOption.JSON.id()) != 0) {
            doDownload(CVE_JSON_10_MODIFIED_URL, outputDir, status.nvdDownloadStat, status.extractStat);
            for (int i=START_YEAR; i<=END_YEAR; i++) {
                String cveJsonBaseUrl = CVE_JSON_10_BASE_URL.replace("%d", String.valueOf(i));
                doDownload(cveJsonBaseUrl, outputDir, status.nvdDownloadStat, status.extractStat);
            }
        }
    }

    private void checkOutputDir(File outputDir) throws IOException {
        if ( ! outputDir.exists()) {
          outputDir.mkdirs();
        }
        if (!outputDir.exists() || !outputDir.isDirectory()) {
            throw new IOException("Could neither find nor create output directory " + outputDir + ".");
        }
    }

    private long checkHead(String nistUrl) throws IOException {
        try {
            URL url = new URL(nistUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            connection.getInputStream();
            return connection.getContentLengthLong();
        } catch (IOException e) {
            annunciator.announce("Failed to determine content length");
            throw new IOException(e);
        }
    }

    private void downloadUsingHttp(URL url, long expectedLength, File outFile) throws IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            URLConnection connection = url.openConnection();
            annunciator.announce("Downloading " + url.toExternalForm());
            bis = new BufferedInputStream(connection.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(outFile));
            StreamUtil.streamUntilEof(bis, bos, expectedLength, annunciator);
        } finally {
            close(bis);
            close(bos);
        }
    }

    private void doDownload(String nistUrl, File outputDir, OpGroupStatus downloadStat, OpGroupStatus extractStat) throws IOException {
        File outFile = null;
        String filename = null;
        URL url;
        try {
            url = new URL(nistUrl);
        } catch (java.net.MalformedURLException e) {
            annunciator.announce("Could not download file due to an internal error (malformed URL).");
            downloadStat.errors++;
            downloadStat.opFailed++;
            throw e;
        }
        try {
            filename = url.getFile();
            filename = filename.substring(filename.lastIndexOf('/') + 1);
            outFile = new File(outputDir, filename).getCanonicalFile();
            annunciator.announce("Looking for file " + outFile + " downloaded from " + nistUrl + ".");

            long fileLengthFromHTTPHeader = checkHead(nistUrl);
            if (outFile.exists()) {
                if (fileLengthFromHTTPHeader == outFile.length()) {
                    annunciator.announce("Using cached version of " + filename);
                } else {
                    if (outFile.getUsableSpace() < fileLengthFromHTTPHeader) {
                        throw new IOException("Insufficient space for file.");
                    }
                    annunciator.announce("Downloading fresh copy of " + filename);
                    outFile.delete();
                    downloadUsingHttp(url, fileLengthFromHTTPHeader, outFile);
                }
            } else {
                annunciator.announce("Downloading initial copy of " + outFile + " from " + nistUrl + ".");
                downloadUsingHttp(url, fileLengthFromHTTPHeader, outFile);
            }
        } catch (IOException e) {
            annunciator.announce("Could not download file " + filename + " due to an I/O error.");
            downloadStat.errors++;
            downloadStat.opFailed++;
            throw e;
        }
        downloadStat.opSucceeded++;
        if (options.recursion > 0) {
            try {
                DelegatingFileExtractor delFx = new DelegatingFileExtractor(outFile, annunciator);
                delFx.extractFile(options.recursion);
                extractStat.opSucceeded++;
            } catch (Exception e) {
                annunciator.announce(e.getMessage());
                Throwable e2 = e.getCause();
                if (e2 != null) {
                    annunciator.announce(e2.getMessage());
                }
                extractStat.opFailed++;
                extractStat.errors++;
            }
        }
    }

    private void close (Closeable object) {
        if (object != null) {
            try {
                object.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
