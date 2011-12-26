/*
 *  Copyright 2011 Rudolf Biczok
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
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public final class CryptLibrary {
	
	static {
		Native.register("crypt");
	}

	/**
	 * Private constructor for preventing instantiations.
	 */
	private CryptLibrary() {
	}

	/**
	 * Returns an encrypted representation of a string given by <code>key</code>.
	 * @param key the string we want to encrypt.
	 * @param salt a random sequence of characters used for encryption.
	 * @return an encrypted representation of a string given by <code>key</code>.
	 */
	public static native String crypt(final String key, final String salt);
}
