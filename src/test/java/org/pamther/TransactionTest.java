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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pamther.test.TempUser;

/**
 * The class {@link PAMService} should ...
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public class TransactionTest {

	/**
	 * Dummy user name.
	 */
	private static final String NAME = "pamther_test";

	/**
	 * Dummy password (old).
	 */
	private static final char[] OLD_PASSWORD = "mop".toCharArray();

	/**
	 * Dummy password (new).
	 */

	private static final char[] NEW_PASSWORD = "new_mop".toCharArray();

	/**
	 * Dummy user.
	 */
	private static TempUser user;

	/**
	 * Used {@link CallbackHandler} during the tests.
	 */
	private static DefaultCallbackHandler handler;

	@BeforeClass
	public static void creat() throws LoginException {
		TransactionTest.user = new TempUser(TransactionTest.NAME,
				TransactionTest.OLD_PASSWORD);
		TransactionTest.user.create();
		handler = new DefaultCallbackHandler();
		handler.setName(TransactionTest.NAME);
		handler.setOldPassword(TransactionTest.OLD_PASSWORD);
		handler.setNewPassword(TransactionTest.NEW_PASSWORD);
	}

	@AfterClass
	public static void delet() {
		TransactionTest.user.delete();
		TransactionTest.user = null;
	}

	@Test
	public void haveAService() throws LoginException {
		Transaction transaction = new Transaction("login",
				TransactionTest.NAME, handler);
		Assert.assertEquals("login", transaction.getService());
		transaction.setService("su");
		Assert.assertEquals("su", transaction.getService());
		transaction.close();
	}

	@Test
	public void login() throws LoginException {
		Transaction transaction = new Transaction("login",
				TransactionTest.NAME, handler);
		transaction.authenticate();
		transaction.validate();
		transaction.close();
		System.gc();
	}

	@Test
	public void setcred() throws LoginException {
		Transaction transaction = new Transaction("login",
				TransactionTest.NAME, handler);
		transaction.authenticate();
		transaction.validate();
		transaction.changeAuthTok();
		try {
			transaction.authenticate();
		} catch (LoginException e) {
			return;
		} finally {
			transaction.close();
		}
		Assert.fail();
	}

	// @Test
	// public void beCollectableByGC() throws LoginException {
	// Transaction pam = new Transaction("login", TransactionTest.NAME,
	// handler);
	// pam.authenticate();
	// pam.verify();
	// pam.close();
	// pam = new Transaction("login", TransactionTest.NAME, handler);
	// pam.authenticate();
	// pam.verify();
	// pam.close();
	// pam = null;
	// System.gc();
	// }
}
