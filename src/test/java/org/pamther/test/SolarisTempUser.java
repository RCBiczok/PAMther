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
package org.pamther.test;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link TempUser} implementation for Solaris.
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public final class SolarisTempUser extends TempUser {

	/**
	 * Create a new temporary user.
	 * 
	 * @param name
	 *            the user's name.
	 * @param password
	 *            the user's password.
	 */
	SolarisTempUser(final String name, final char[] password) {
		super(name, password);
	}

	@Override
	public void performCreation() {
		List<String> command = new ArrayList<String>();
		command.add("useradd");
		command.add(this.getName());
		TempUser.execAndWait(command);
                command = new ArrayList<String>();
		command.add("expect");
		command.add("-c");
		command.add("spawn /usr/bin/passwd -r files "
		  + this.getName()
                  + "; expect \"New Password:\"; send \""
		  + new String(this.getPassword())
                  + "\\r\"; expect \"Re-enter new Password:\"; send \""
		  + new String(this.getPassword())
                  + "\\r\"; expect eof");
                TempUser.execAndWait(command);
	}

	@Override
	public void performDeletion() {
	        List<String> command = new ArrayList<String>();
		command.add("userdel");
		command.add(this.getName());
		TempUser.execAndWait(command);
	}

}
