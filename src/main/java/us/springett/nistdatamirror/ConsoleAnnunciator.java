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

import java.util.Date;

/**
 * @author Brent Whitmore (bwhitmore@whitware.com)
 */


/**
 * Reports progress to the user via System.out.
 */
public class ConsoleAnnunciator implements Annunciator {
    // Progress reporting and messaging
    private int lastBusyCharIx = -1;
    private static final String busyChars = "-\\|/-\\|/";
    private static final String overwriteChars = "\r            \r";
    private Date lastBusyPrint = null;

    /**
     * Display an indication of the application's liveness along with a
     * degree of completion.
     * <p>
     * Uses a combination of carriage-return, line-feed, and backspace
     * characters to produce a "rotating star" followed by the reported
     * percentage of completion passed to the method. The implementation
     * avoids excessive console I/O by never writing to the console more 
     * often than every half-second.
     *
     * @param percent   the degree of completion, expressed as an int 
     *                  between zero and one hundred, inclusive.
     */
    public void busyPrint(int percent) {
        if (lastBusyCharIx < 0) {
            System.out.print(overwriteChars);
            System.out.print(busyChars.charAt(0));
            lastBusyCharIx = 0;
            lastBusyPrint = new Date();
        } else {
            Date now = new Date();
            if (now.getTime() - lastBusyPrint.getTime() >= 500) {
                lastBusyCharIx += 1;
                lastBusyCharIx %= (busyChars.length() - 1);
                if (percent < 0 || percent > 100) {
                    System.out.print(String.format("\r%1$c", busyChars.charAt(lastBusyCharIx)));
                } else {
                    System.out.print(String.format("\r%1$c %2$3d%%", busyChars.charAt(lastBusyCharIx), percent)); 
                }
                lastBusyPrint = now;
            }
        }
    }

    /**
     * Display an indication of the application's liveness.
     * <p>
     * Uses a combination of carriage-return, line-feed, and backspace
     * characters to produce a "rotating star" effect. The implementation
     * avoids excessive console I/O by never writing to the console more 
     * often than every half-second.
     */
    public void busyPrint() {
        busyPrint(-1);
    }

    /**
     * Remove any previously-displayed indication of the application's 
     * liveness.
     */
    public void endBusy() {
        if (lastBusyCharIx >= 0) {
            System.out.print(overwriteChars);
            lastBusyCharIx = -1;
            lastBusyPrint = null;
        }
    }

    /**
     * Display an announcement to the user.
     * <p>
     * ConsoleAnnunciator wipes any liveness indication before printing the 
     * announcement.  It then scrolls the console to resume subsequent 
     * liveness indication while preserving the message scroll.
     *
     * @param aMsg  the message to be displayed to the user.
     *
     */
    public void announce(String aMsg) {
        if (lastBusyCharIx >= 0) {
            System.out.print(overwriteChars);
        }
        System.out.println(aMsg);
    }

    /**
     * Closes this Annunciator, removing all liveness indications and relinquishing 
     * any underlying resources. A try-with-resources statement invokes this method
     * automatically at the end of its try block. 
     */
    public void close() {
    	endBusy();
    }
}
