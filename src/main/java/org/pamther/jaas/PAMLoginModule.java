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

import org.pamther.DefaultCallbackHandler;
import org.pamther.Transaction;
import org.pamther.TransactionTest;

/**
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public class PAMLoginModule implements LoginModule {
	private static final String SERVICE_NAME_OPTION = "serviceName";
	private CallbackHandler callbackHandler;
	private Map<String, ?> options;

	private Transaction transaction;

	public static void login(String service, String user, char[] password)
			throws LoginException {
		DefaultCallbackHandler handler = new DefaultCallbackHandler();
		handler.setName(user);
		handler.setOldPassword(password);
		Transaction transaction = new Transaction(service, user, handler);
		transaction.authenticate();
		transaction.verify();
		transaction.close();
	}

	public static void changeCredential(String service, String user,
			char[] oldPassword, char[] newPassword) throws LoginException {
		DefaultCallbackHandler handler = new DefaultCallbackHandler();
		handler.setName(user);
		handler.setOldPassword(oldPassword);
		handler.setNewPassword(newPassword);
		Transaction transaction = new Transaction(service, user, handler);
		transaction.authenticate();
		transaction.verify();
		transaction.chauthtok();
		transaction.close();
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