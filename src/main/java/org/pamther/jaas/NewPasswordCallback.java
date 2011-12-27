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
package org.pamther.jaas;

import javax.security.auth.callback.PasswordCallback;

/**
 * A {@link NewPasswordCallback} is used to ask a
 * {@link javax.security.auth.callback.CallbackHandler CallbackHandler} for a
 * new password. <br />
 * Please note that the JAAS-API itself does not provide any mechanism for
 * changing passwords. Only PAM methods like
 * {@link org.pamther.Transaction#changeAuthTok() Transaction.chauthtok()}.
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public class NewPasswordCallback extends PasswordCallback {

	/**
	 * SVUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Construct a {@link NewPasswordCallback} with a prompt and a boolean
	 * specifying whether the password should be displayed as it is being typed.
	 * @param prompt
	 *            the prompt used to request the password.
	 * @param echoOn
	 *            true if the password should be displayed as it is being typed.
	 * 
	 * @throws IllegalArgumentException
	 *                if <code>prompt</code> is null or if <code>prompt</code>
	 *                has a length of 0.
	 * @see javax.security.auth.callback.PasswordCallback#PasswordCallback(String,
	 *      boolean)
	 */
	public NewPasswordCallback(final String prompt, final boolean echoOn) {
		super(prompt, echoOn);
	}

}
