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
package us.springett.nistdatamirror.internal;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import us.springett.nistdatamirror.Annunciator;
/**
 * Utility methods that provide some frequently-occurring stream handling operations that optionally 
 * use an Annuciator.
 * <p>
 * @author Brent Whitmore (bwhitmore@whitware.com)
 */
public class StreamUtil {

    /**
     * Copy the contents of one stream to another, with optional reporting via a provided
     * Annunciator object.
     *
     * @param   inS             the open stream to be copied
     * @param   outS            the open output stream to which the contents of the input stream should be
     *                          copied
     * @param   limit           the number of bytes anticipated to be copied from the input stream to the
     *                          output stream.  If less than zero, then the method reports no progress
     *                          percentage to the Annunciator, only activity.
     * @param   an              the Annunciator to which the method reports its activity.  If the value
     *                          passed via this parameter is null, then the method reports no prograss 
     *                          or activity.
     * @throws IOException      when an I/O error occurs.
     */
    public static void streamUntilEof(
            InputStream inS, OutputStream outS, long limit, Annunciator an) throws IOException {
        int bufSz = MemoryProbe.maxChunkSizeScaledDn();
        byte buf[] = new byte[bufSz];
        int count;
        long soFar = 0;
        long divisor = limit/100;
        try {
            do {
                if (an != null) {
                    if (limit < 0) {
                        an.busyPrint();
                    } else {
                        long percent = soFar/divisor;
                        an.busyPrint((int) percent);
                    }
                }
                count = inS.read(buf, 0, bufSz);
                if (count < 1) 
                    break;
                outS.write(buf, 0, count);
                soFar += count;
            } while (true);
        } finally {
            if (an != null)
                an.endBusy();
            buf = null;
        }
    }

    /**
     * Copy the contents of one stream to another, with optional reporting via a provided
     * Annunciator object.  Only reports activity, not progress percentage.
     *
     * @param   inS             the open stream to be copied
     * @param   outS            the open output stream to which the contents of the input stream should be
     *                          copied
     * @param   an              the Annunciator to which the method reports its activity.  If the value
     *                          passed via this parameter is null, then the method reports no prograss 
     *                          or activity.
     * @throws IOException      when an I/O error occurs.
     */
    public static void streamUntilEof(InputStream inS, OutputStream outS, Annunciator an) throws IOException {
        streamUntilEof(inS, outS, -1, an);
    }

    /**
     * Copy the contents of one stream to another, without reporting progress or activity.
     *
     * @param   inS             the open stream to be copied
     * @param   outS            the open output stream to which the contents of the input stream should be
     *                          copied
     * @throws IOException      when an I/O error occurs.
     */
    public static void streamUntilEof(InputStream inS, OutputStream outS) throws IOException {
        streamUntilEof(inS, outS, -1, null);
    }

}


