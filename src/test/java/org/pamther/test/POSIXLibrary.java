/*
 *  Copyright 2011-2012 Rudolf Biczok
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.pamther.test;

import com.sun.jna.Native;

/**
 * JNA native interface to POSIX functions.
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public final class POSIXLibrary {
	
	static {
		Native.register("c");
	}

	/**
	 * Private constructor for preventing instantiations.
	 */
	private POSIXLibrary() {
	}

	/**
	 * Returns the UID of the current process.
	 * 
	 * @return the UID of the current process.
	 * @see <a
	 *      href="http://pubs.opengroup.org/onlinepubs/007904975/functions/getuid.html"
	 *      >setuid()</a> documentation
	 */
	public static native int geteuid();
}
