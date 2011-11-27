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
import javax.security.auth.callback.PasswordCallback;

public class DefaultConvCallbackHandler implements CallbackHandler {

	private Transaction transaction;
	
	public DefaultConvCallbackHandler(Transaction transaction)  {
		this.transaction = transaction;
	}
	
	/**
	 * @see org.pamther.ConvCallbackHandler#handle(org.pamther.Message[], org.pamther.Response[])
	 */
	@Override
	public void handle(Callback[] callbacks) {
		for (Callback callback : callbacks) {
			System.out.println(callback.getClass());
			
			if (callback instanceof PasswordCallback) {
				PasswordCallback passwordCallback = (PasswordCallback)callback;
				System.out.println(passwordCallback.getPrompt());
				passwordCallback.setPassword(transaction.getPassword().toCharArray());
			}
		}
	}

}
