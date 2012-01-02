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

import javax.security.auth.login.LoginException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pamther.DefaultCallbackHandler;
import org.pamther.test.TempUser;

/**
 * The class {@link PAMLoginModule} should ...
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public final class PAMLoginModuleTest {

	/**
	 * Dummy user.
	 */
	private static TempUser user;

	/**
	 * Used {@link javax.security.auth.callback.CallbackHandler} during the tests.
	 */
	private static DefaultCallbackHandler handler;

	@BeforeClass
	public static void creat() throws LoginException {
		PAMLoginModuleTest.user = TempUser.newInstance("pamther_test",
				"murkspwd01".toCharArray());
		PAMLoginModuleTest.user.create();
		handler = new DefaultCallbackHandler();
		handler.setName(PAMLoginModuleTest.user.getName());
		handler.setOldPassword("murkspwd01".toCharArray());
		handler.setNewPassword("new_murkspwd01".toCharArray());
	}

	@AfterClass
	public static void delet() {
		PAMLoginModuleTest.user.delete();
		PAMLoginModuleTest.user = null;
	}

	@Test
	public void haveAStaticLoginMethod() throws LoginException {
		PAMLoginModule.login("ssh", user.getName(),
				PAMLoginModuleTest.user.getPassword());
		try {
			PAMLoginModule.login("ssh", user.getName(),
					"99BottleOfBear".toCharArray());
		} catch (LoginException e) {
			System.gc();
			return;
		}

		Assert.fail();
	}

	@Test
	public void haveAStaticMethodForChangingPassword() throws LoginException {
		PAMLoginModule.changeCredential("ssh", user.getName(),
				PAMLoginModuleTest.user.getPassword(),
				"99BottleOfBear".toCharArray());
		PAMLoginModule.login("ssh", user.getName(), "99BottleOfBear".toCharArray());
		try {
			PAMLoginModule.login("ssh", user.getName(),
					PAMLoginModuleTest.user.getPassword());
		} catch (LoginException e) {
			System.gc();
			return;
		}

		Assert.fail();
	}
}
