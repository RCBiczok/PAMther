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

import javax.security.auth.login.LoginException;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pamther.DefaultCallbackHandler;
import org.pamther.test.TempUser;

/**
 * The class {@link PAMLoginModule} should ...
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public class PAMLoginModuleTest {

	private static final String NAME = "pamtest";
	private static final char[] OLD_PASSWORD = "mop".toCharArray();
	private static final char[] NEW_PASSWORD = "new_mop".toCharArray();
	private static TempUser user;

	private static DefaultCallbackHandler handler;

	@BeforeClass
	public static void creat() throws LoginException {
		PAMLoginModuleTest.user = new TempUser(PAMLoginModuleTest.NAME,
				PAMLoginModuleTest.OLD_PASSWORD);
		PAMLoginModuleTest.user.create();
		handler = new DefaultCallbackHandler();
		handler.setName(PAMLoginModuleTest.NAME);
		handler.setOldPassword(PAMLoginModuleTest.OLD_PASSWORD);
		handler.setNewPassword(PAMLoginModuleTest.NEW_PASSWORD);
	}

	@AfterClass
	public static void delet() {
		PAMLoginModuleTest.user.delete();
		PAMLoginModuleTest.user = null;
	}

	@Test
	public void haveAStaticLoginMethod() throws LoginException {
		PAMLoginModule.login("login", user.getName(),
				PAMLoginModuleTest.OLD_PASSWORD);
		//TODO: this code produces crashes after running other unit tests.
//		try {
//			PAMLoginModule.login("login", user.getName(),
//					PAMLoginModuleTest.NEW_PASSWORD);
//		} catch (LoginException e) {
//			return;
//		}
//
//		Assert.fail();
	}

	@Test
	public void haveAStaticMethodForChangingPassword() throws LoginException {
		PAMLoginModule.changeCredential("login", user.getName(),
				PAMLoginModuleTest.OLD_PASSWORD,
				PAMLoginModuleTest.NEW_PASSWORD);
		PAMLoginModule.login("login", user.getName(),
				PAMLoginModuleTest.NEW_PASSWORD);
		
//		try {
//			PAMLoginModule.login("login", user.getName(),
//					PAMLoginModuleTest.OLD_PASSWORD);
//		} catch (LoginException e) {
//			return;
//		}
//
//		Assert.fail();
	}
}
