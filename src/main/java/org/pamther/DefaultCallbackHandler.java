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
package org.pamther;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;

/**
 * The default {@link CallbackHandler} implementation used by utility methods in
 * {@link org.pamther.jaas.PAMLoginModule PAMLoginModule}.
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public final class DefaultCallbackHandler implements CallbackHandler {

	/**
	 * Holds the user name.
	 */
	private String user;

	/**
	 * Holds the (old) password.
	 */
	private char[] oldPassword;

	/**
	 * Holds the new password (only matters in
	 * {@link org.pamther.Transaction#changeAuthTok()}).
	 */
	private char[] newPassword;

	@Override
	public void handle(final Callback[] callbacks) {
		for (Callback callback : callbacks) {
			if (callback instanceof NameCallback) {
				NameCallback nameCallback = (NameCallback) callback;
				nameCallback.setName(user);
			}

			if (callback instanceof PasswordCallback) {
				PasswordCallback passwordCallback = (PasswordCallback) callback;
				if (passwordCallback.getPrompt().contains("new UNIX")) {
					passwordCallback.setPassword(this.newPassword);
				} else {
					passwordCallback.setPassword(this.oldPassword);
				}
			}

		}
	}

	/**
	 * The name property.
	 * 
	 * @return the name
	 */
	public String getName() {
		return user;
	}

	/**
	 * The name property.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.user = name;
	}

	/**
	 * The oldPassword property.
	 * 
	 * @return the oldPassword
	 */
	public char[] getOldPassword() {
		return oldPassword;
	}

	/**
	 * The oldPassword property.
	 * 
	 * @param oldPassword
	 *            the oldPassword to set
	 */
	public void setOldPassword(final char[] oldPassword) {
		this.oldPassword = oldPassword;
	}

	/**
	 * The newPassword property.
	 * 
	 * @return the newPassword
	 */
	public char[] getNewPassword() {
		return newPassword;
	}

	/**
	 * The newPassword property.
	 * 
	 * @param newPassword
	 *            the newPassword to set
	 */
	public void setNewPassword(final char[] newPassword) {
		this.newPassword = newPassword;
	}

}
