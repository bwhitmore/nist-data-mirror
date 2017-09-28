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
 * Reports progress to the user of the tool.
 */
public interface Annunciator extends AutoCloseable {
    // Progress reporting and messaging

    /**
     * If this is the first attempt to get the default annunciator, then
     * the method sets the default annunciator to a new instance of the
     * local class <code>DefaultAnnunciator</code> and returns that.  If, 
     * on the other hand, a previous call already set the default annunciator, 
     * then that annunciator is returned.
     *
     * @return 					the default annunciator
     */
    static public Annunciator defaultAnnunciator() {
        return DefaultAnnunciator.defaultAnnunciator(null);
    }

    /**
     * Sets and returns the default Annunciator.
     * <p>
     * If this is the first attempt to set the default annunciator, then
     * the default annunciator is set to the argument and that annunciator
     * is returned.  If, on the other hand, a previous call set the 
     * default annunciator, then the method argument is ignored and the
     * previously-set annunciator is returned.
     * <p>
     * The usual programming idiom is to call this method with a
     * newly-created annunciator but retaining only the method's returned value
     * as the annunciator to subsequently use.  For example, one might use:
     * <p>
     *  <code>
     *  Annunciator myAnnunciator = defaultAnnunciator(new ConsoleAnnunciator());
     *  </code>
     *
     * @param anAnnunciator		the annunciator to use as the default annunciator.
     *							If the argument is <code>null</code> and no default
     * 							Annunciator has yet been set, then the method
     *                          sets the default annunciator to a new instance of
     *  						the local implementation class <code>DefaultAnnunciator</code> 
     *							and returns that.
     * @return 					the default annunciator
     */
    static public Annunciator defaultAnnunciator(Annunciator anAnnunciator) {
		return DefaultAnnunciator.defaultAnnunciator(anAnnunciator);
    }

    /**
     * Display an indication of the application's liveness.
     *
     * @param percent	the degree of completion, expressed as an int 
     *					between zero and one hundred, inclusive.
     */
    public void busyPrint(int percent);

    /**
     * Display an indication of the application's liveness.
     */
    public void busyPrint();

    /**
     * Remove any previously-displayed indication of the application's 
     * liveness.
     */
    public void endBusy();

    /**
     * Display an announcement to the user.
     * <p>
     * Unlike liveness indications, Annunciators usually preserve old announcements 
     * when displaying new ones by employing a text scroll, or something similar.  
     * <p>
     * Annunciators should ensure that announcements and liveness indications do not
     * overwrite or obscure each other.
     *
     * @param aMsg	the message to be displayed to the user.
     *
     */
    public void announce(String aMsg);

    /**
     * Closes this Annunciator, removing all liveness indications and relinquishing 
     * any underlying resources. A try-with-resources statement invokes this method
     * automatically at the end of its try block. 
     */
   	public void close();

	/**
	 * An internal implementation of the <code>Annunciator</code> interface that 
	 * does something suitable as a default implementation.
	 * <p>
	 * This implementation subclasses <code>NullAnnunciator</code>, which does nothing.
	 * <p>
	 * Only the first to get the default <code>Annunciator</code> can actually set 
	 * it.  Subsequent calls only return the initially-set default.  Consequently 
	 * calling <code>defaultAnnunciator</code> without any arguments or calling 
	 * <code>defaultAnnunciator</code> with a null argument permanently sets the 
	 * default the default Annunciator to a <code>DefaultAnnunciator</code>.  Since
	 * this class extends <code>NullAnnunciator</code>, then doing this is equivalent
	 * to setting the default to a <code>NullAnnunciator</code>.
	 * <p>
	 * This class serves two purposes.  First, it extends the class that should be
	 * used as a default when code calls either of the static <code>defaultAnnunciator</code>
	 * methods without an argument or with a null argument.  Second, it serves as
	 * a place for the (hidden) implementation code for the aforementioned static 
	 * <code>defaultAnnunciator</code> methods of the Annunciator interface and the
	 * static data needed to support them due to Java 8's restriction on interfaces.
	 */
	class DefaultAnnunciator extends NullAnnunciator {
	    // Progress reporting and messaging

	    static private Annunciator defaultAnnunciator;

	    /**
	     * Optionally sets the default annunciator to a new <code>Annunciator.DefaultAnnunciator</code> 
	     * and then returns the default <code>Annunciator</code> .
	     * <p>
	     * This method is equivalent to calling the static method 
	     * <code>Annunciator.defaultAnnunciator(Annunciator)</code> 
	     * with <code>null</code> as its argument.
	     *
	     * @return 					the default annunciator
	     */
	    static protected Annunciator defaultAnnunciator() {
	        return defaultAnnunciator(null);
	    }
	    /**
	     * Sets and returns the default Annunciator.
	     * 
	     * Note that it is this method that actually implements the logic for determining
	     * the default annunciator.
	     *
	     * @param anAnnunciator		the annunciator to use as the default annunciator.
	     *							If the argument is <code>null</code> and no default
	     * 							Annunciator has yet been set, then the method
	     *                          sets the default annunciator to a new instance of
	     *  						the local implementation class <code>DefaultAnnunciator</code> 
	     *							and returns that.
	     * @return 					the default annunciator
	     */
	    static protected Annunciator defaultAnnunciator(Annunciator anAnnunciator) {
	        Annunciator answer = null;
	        if (defaultAnnunciator == null) {
	            if (anAnnunciator == null) {
	                // create a default one
	                defaultAnnunciator = new DefaultAnnunciator();
	            } else {
	                defaultAnnunciator = anAnnunciator;
	            }
	        }
	        return defaultAnnunciator;
	    }
	}

}
