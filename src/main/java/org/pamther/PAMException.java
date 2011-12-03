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
package org.pamther;

import javax.security.auth.login.LoginException;

/**
 * Library-specific exception class. This is typically thrown whenever a native
 * PAM function returns a value not similar to <code>PAM_SUCCESS</code>.
 * 
 * @see <a href="http://linux.die.net/man/3/pam_start">pam_start</a> method and
 *      his return values.
 * 
 * @author <a href="https://github.com/RCBiczok">Rudolf Biczok</a>
 */
public class PAMException extends LoginException {

	/**
	 * SVUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new {@link PAMException}.
	 * 
	 * @param message
	 *            the detail message.
	 * 
	 * @see {@link Exception#Exception(String)}
	 */
	public PAMException(final String message) {
		super(message);
	}

}
