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

/**
 * Utility methods that interrogate the JVM's memory resources to determine appropriate allocation request
 * sizes based upon current usage.
 * <p>
 * @author Brent Whitmore (bwhitmore@whitware.com)
 */
public class MemoryProbe {

    /**
     * Determine a value that approximates the largest allocation of a group of 
     * contiguious bytes that the JVM can currently provide within stated bounds.
     *
     * @throws  Error           a run-time error if an allocation within the range indicated by the
     *                          the parameters is not possible.
     * @param   initialMax      the maximum size, in bytes, of the anticipated memory 
     *                          allocation
     * @param   initialMin      the minimum size, in bytes, of the anticipated memory 
     *                          allocation
     * @return                  an Integer size that the JVM can current allocation within the bounds of the 
     *                          method's parameters
     */
    public static Integer maxChunkSize(int initialMax, int initialMin) {
        String errMsg = "Insufficient resources are available to be able to run.  Aborted.";
        if (initialMax < initialMin) {
            throw new Error(errMsg);
        }
        int hiVal = initialMax;
        int loVal = initialMin;
        int diff = hiVal - loVal;
        boolean maxFound = false;
        do {
            int val = loVal + (diff / 2);
            // Try (val) on for size.
            try {
                byte [] chunk = new byte[val];
                chunk = null;
                loVal = val;
            } catch (java.lang.OutOfMemoryError e) {
                hiVal = val;
            };
            diff = hiVal - loVal;
        } while (diff > 1024);
        if (loVal <= 0) {
            throw new Error(errMsg);
        }
        return new Integer(loVal);
    }

    /**
     * Determine a value that approximates the largest allocation of a group of 
     * contiguious bytes that the JVM can currently provide within the maximum possible
     * allocation and 16K bytes of memory.
     *
     * @throws  Error           a run-time error if an allocation within the stated range is not possible.
     * @return                  an Integer size that the JVM can current allocation within the bounds of the 
     *                          method's parameters
     */
    public static Integer maxChunkSize() {
        return maxChunkSize(Integer.MAX_VALUE, 1024 * 16);
    }

    /**
     * Determine a value that approximates the largest allocation of a group of 
     * contiguious bytes that the JVM can currently provide within the maximum possible
     * allocation and 16K bytes of memory, but scale the value returned down by the indicated scale
     * factor.
     *
     * @throws  Error           a run-time error if an allocation within the range indicated by the
     *                          the parameters is not possible.
     * @param   scaleFactor     the scaling factor used to scale down the method's answer
     * @return                  an Integer size that the JVM can current allocation within the bounds of the 
     *                          method's parameters
     */
    public static Integer maxChunkSizeScaledDnBy(Integer scaleFactor) {
        return maxChunkSize() / scaleFactor;
    }

    /**
     * Determine a value that approximates the largest allocation of a group of 
     * contiguious bytes that the JVM can currently provide within the maximum possible
     * allocation and 16K bytes of memory, but scale the value returned down by a factor of 2^5.
     *
     * @throws  Error           a run-time error if an allocation within the range indicated by the
     *                          the parameters is not possible.
     * @return                  an Integer size that the JVM can current allocation within the bounds of the 
     *                          method's parameters
     */
    public static Integer maxChunkSizeScaledDn() {
        return maxChunkSizeScaledDnBy(1 << 5);
    }
}
