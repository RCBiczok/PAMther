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

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;

/**
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public class DefaultCallbackHandler implements CallbackHandler {

	private String name;

	private char[] oldPassword;
	private char[] newPassword;

	@Override
	public void handle(Callback[] callbacks) {
		for (Callback callback : callbacks) {
			if (callback instanceof NameCallback) {
				NameCallback nameCallback = (NameCallback) callback;
				System.out.println(nameCallback.getPrompt());
				nameCallback.setName(name);
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
		return name;
	}

	/**
	 * The name property.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	public void setOldPassword(char[] oldPassword) {
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
	public void setNewPassword(char[] newPassword) {
		this.newPassword = newPassword;
	}

}
