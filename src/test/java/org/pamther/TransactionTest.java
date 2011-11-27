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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pamther.PAMException;
import org.pamther.Transaction;
import org.pamther.test.TempUser;

/**
 * The class {@link PAMService} should ...
 * 
 * @author <a href="https://github.com/RCBiczok">Rudolf Biczok</a>
 */
public class TransactionTest {

	private static final String NAME = "pamtest";
	private static final String PASSWORD = "mop";
	private static TempUser user;

	@BeforeClass
	public static void createUser() throws Exception {
		TransactionTest.user = new TempUser(TransactionTest.NAME,
				TransactionTest.PASSWORD);
		TransactionTest.user.create();
	}

	@AfterClass
	public static void deleteUser() {
		TransactionTest.user.delete();
		TransactionTest.user = null;
	}

	@Test
	public void haveAService() throws PAMException {
		Transaction pam = new Transaction();
		Assert.assertEquals("login", pam.getService());
		pam.setService("su");
		Assert.assertEquals("su", pam.getService());
	}

	@Test
	public void login() throws PAMException {
		Transaction pam = new Transaction("login", TransactionTest.NAME, null);
		pam.setPassword(TransactionTest.PASSWORD);
		pam.authenticate();
		pam.verify();
		pam.close();
	}
	
	@Test
	public void setcred() throws PAMException {
		Transaction pam = new Transaction("login", TransactionTest.NAME, null);
		pam.setPassword(TransactionTest.PASSWORD);
		pam.authenticate();
		pam.verify();
		pam.chauthtok();
		pam.authenticate();
		pam.close();
	}
}
