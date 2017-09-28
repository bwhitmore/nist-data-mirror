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

/**
 * @author Brent Whitmore (bwhitmore@whitware.com)
 */


/**
 * An Annunuciator that does nothing.  Serves as mute alternative to the
 * other reporting subclasses of Annunciator.
 */
public class NullAnnunciator implements Annunciator {
    // Progress reporting and messaging

    /**
     * Display no indication of the application's liveness or progress.
     *
     * @param percent   the degree of completion, expressed as an int 
     *                  between zero and one hundred, inclusive.  Ignored
     *                  by this Annunciator.
     */
    public void busyPrint(int percent) {
    }

    /**
     * Display an no indication of the application's liveness.  This 
     * implementation does nothing.
     */
    public void busyPrint() {
    }

    /**
     * Remove any previously-displayed indication of the application's 
     * liveness.  This implementation does nothing.
     */
    public void endBusy() {
    }

    /**
     * Display no announcement to the user.
     * @param aMsg  the message to be displayed to the user.  Ignored in
     *              this implementation.
     *
     */
    public void announce(String aMsg) {
    }

    /**
     * Closes this Annunciator, removing all liveness indications and relinquishing 
     * any underlying resources. A try-with-resources statement invokes this method
     * automatically at the end of its try block. This implementation does nothing.
     */
    public void close() {
    }
}
