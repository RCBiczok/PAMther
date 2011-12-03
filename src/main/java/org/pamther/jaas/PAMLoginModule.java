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

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.pamther.Transaction;

public class PAMLoginModule implements LoginModule {
	private static final String SERVICE_NAME_OPTION = "serviceName";
	private CallbackHandler callbackHandler;
	private Map<String, ?> options;

	private Transaction transaction;

	public boolean login(String service, String userName, char[] password)
			throws LoginException {
		this.transaction = null;
		return true;
	}

	public boolean changeCredential(String service, String userName,
			char[] oldPassword, char[] newPassword) throws LoginException {
		this.transaction = null;
		return true;
	}

	@Override
	public boolean abort() throws LoginException {
		this.transaction = null;
		return true;
	}

	@Override
	public boolean commit() throws LoginException {
		return true;
	}

	@Override
	public boolean login() throws LoginException {
		transaction = new Transaction(
				(String) options.get(SERVICE_NAME_OPTION), null,
				callbackHandler);
		transaction.authenticate();
		transaction.chauthtok();
		return true;
	}

	@Override
	public boolean logout() throws LoginException {
		this.transaction = null;
		return true;
	}

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> sharedState, Map<String, ?> options) {
		this.callbackHandler = callbackHandler;
		this.options = options;
	}

}