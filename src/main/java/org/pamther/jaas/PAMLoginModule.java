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
package org.pamther.jaas;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.pamther.DefaultCallbackHandler;
import org.pamther.Transaction;

/**
 * This is an implementation of the JAAS {@link LoginModule} interface. PAM
 * messages generated from the individual PAM modules get transformed into
 * corresponding {@link Callback} objects hence the {@link CallbackHandler} can
 * interact with these messages directly.<br />
 * <br />
 * Static methods for basic authentication ad password manipulation are also
 * part of this class.
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 * @see The {@link Transaction} class.
 */
public final class PAMLoginModule implements LoginModule {

	/**
	 * The option key for the service name as constant.
	 */
	private static final String SERVICE_NAME = "serviceName";

	/**
	 * The used {@link CallbackHandler} for PAM module interaction.
	 */
	private CallbackHandler handler;

	/**
	 * Holds the available options and corresponding values.
	 */
	private Map<String, ?> opts;

	/**
	 * Holds the internally used {@link Transaction} object.
	 */
	private Transaction transaction;

	/**
	 * The fast way to authenticate a user with an given password. You also have
	 * to specify the used PAM service (one of those in /etc/pam.d).
	 * 
	 * @param service
	 *            the name of the service configuration file which identifies
	 *            the used PAM stack.
	 * @param user
	 *            the user's name.
	 * @param password
	 *            the user's password.
	 * @throws LoginException
	 *             if an error accrued during the authentication.
	 */
	public static void login(final String service, final String user,
			final char[] password) throws LoginException {
		DefaultCallbackHandler handler = new DefaultCallbackHandler();
		handler.setName(user);
		handler.setOldPassword(password);
		Transaction transaction = new Transaction(service, user, handler);
		transaction.authenticate();
		transaction.validate();
		transaction.close();
	}

	/**
	 * Fast way to change the user's authentication token.
	 * 
	 * @param service
	 *            the name of the service configuration file which identifies
	 *            the used PAM stack.
	 * @param user
	 *            the user's name.
	 * @param oldPassword
	 *            the current authentication token.
	 * @param newPassword
	 *            the new authentication token.
	 * @throws LoginException
	 *             if an error accrued during the authentication.
	 */
	public static void changeCredential(final String service,
			final String user, final char[] oldPassword,
			final char[] newPassword) throws LoginException {
		DefaultCallbackHandler handler = new DefaultCallbackHandler();
		handler.setName(user);
		handler.setOldPassword(oldPassword);
		handler.setNewPassword(newPassword);
		Transaction transaction = new Transaction(service, user, handler);
		transaction.authenticate();
		transaction.validate();
		transaction.changeAuthTok();
		transaction.close();
	}

	@Override
	public boolean login() throws LoginException {
		transaction = new Transaction((String) opts.get(SERVICE_NAME), null,
				handler);
		transaction.authenticate();

		return true;
	}

	@Override
	public boolean commit() throws LoginException {
		transaction.validate();
		return true;
	}

	@Override
	public boolean logout() throws LoginException {
		this.transaction.close();
		this.transaction = null;
		return true;
	}

	@Override
	public boolean abort() throws LoginException {
		this.transaction.close();
		this.transaction = null;
		return true;
	}

	@Override
	public void initialize(final Subject subject,
			final CallbackHandler callbackHandler,
			final Map<String, ?> sharedState, final Map<String, ?> options) {
		this.handler = callbackHandler;
		this.opts = options;
	}

}
