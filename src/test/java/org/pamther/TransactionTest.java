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
public final class TransactionTest {

	/**
	 * Dummy user.
	 */
	private static TempUser user;

	/**
	 * Used {@link javax.security.auth.callback.CallbackHandler CallbackHandler}
	 * during the tests.
	 */
	private static DefaultCallbackHandler handler;

	@BeforeClass
	public static void creat() throws LoginException {
		TransactionTest.user = TempUser.newInstance("pamther_test",
				"murkspwd01".toCharArray());
		TransactionTest.user.create();
		handler = new DefaultCallbackHandler();
		handler.setName(TransactionTest.user.getName());
		handler.setOldPassword(TransactionTest.user.getPassword());
		handler.setNewPassword("new_murkspwd01".toCharArray());
	}

	@AfterClass
	public static void delet() {
		TransactionTest.user.delete();
		TransactionTest.user = null;
	}

	@Test
	public void haveAService() throws LoginException {
		Transaction transaction = new Transaction("su",
				TransactionTest.user.getName(), handler);
		Assert.assertEquals("su", transaction.getService());
		
		// TODO: Causes Permission denied errors on Solaris boxes, dont realy know why
		if(!com.sun.jna.Platform.isSolaris()) {
		    transaction.setService("ssh");
		    Assert.assertEquals("ssh", transaction.getService());
		}
		transaction.close();
		System.gc();
	}

	@Test
	public void login() throws LoginException {
		Transaction transaction = new Transaction("ssh",
				TransactionTest.user.getName(), handler);
		transaction.authenticate();
		transaction.validate();
		transaction.close();
		System.gc();
	}

	@Test
	public void setcred() throws LoginException {
		Transaction transaction = new Transaction("su",
				TransactionTest.user.getName(), handler);
		transaction.authenticate();
		transaction.validate();
		transaction.changeAuthTok();
		try {
			Transaction transaction2 = new Transaction("su",
				TransactionTest.user.getName(), handler);
			transaction2.authenticate();
		} catch (LoginException e) {
			return;
		} finally {
			transaction.close();
		}
		Assert.fail();
	}

	@Test
	public void beCollectableByGC() throws LoginException {
		// Transaction transaction = new Transaction("login",
		// TransactionTest.NAME,
		// handler);
		// transaction.authenticate();
		// transaction.validate();
		// transaction.close();
		// transaction = new Transaction("login", TransactionTest.NAME,
		// handler);
		// transaction.authenticate();
		// transaction.validate();
		// transaction.close();
		// transaction = null;
		System.gc();
	}
}
