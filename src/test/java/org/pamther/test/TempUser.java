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
package org.pamther.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * This class is used to create local user accounts and prepare them for the
 * real tests.<br />
 * <br />
 * Test-cases that make use of this class have the following requirements on the
 * environment:
 * <ul>
 * <li><b>The OS must be Linux | </b>since user account utility applications
 * like <a href="http://linux.die.net/man/8/useradd">useradd</a> and <a
 * href="http://linux.die.net/man/8/userdel">userdel</a> must be called.</li>
 * <li><b>The user must be root | </b>because user creation can not be done as
 * non-root.</li>
 * </ul>
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public class TempUser {

	private static final CLibrary CLIB;
	private static final CryptLibrary CRYPT;

	private static final String DEFAULT_SEED = "ABC";

	private String name;
	private String password;

	private boolean isCreated;

	/**
	 * Check if this platform is supported and and loads some tiny JNA
	 * interfaces to utility functions.
	 */
	static {
		if (!System.getProperty("os.name").startsWith("Linux"))
			throw new RuntimeException(
					"This class requires a Linux-based operating system (System.getProperty(\"os.name\") contains \""
							+ System.getProperty("os.name") + "\")");
		/* load the libraries only if we can be sure that this platform is Linux */
		CLIB = (CLibrary) Native.loadLibrary("c", CLibrary.class);
		CRYPT = (CryptLibrary) Native.loadLibrary("crypt", CryptLibrary.class);
	}

	/**
	 * Create a new temporary user.
	 * 
	 * @param name
	 *            the user's name.
	 * @param password
	 *            the user's password.
	 * @throws RuntimeException
	 *             if user ins not root user.
	 */
	public TempUser(String name, String password) {
		if (TempUser.CLIB.geteuid() != 0)
			throw new RuntimeException("Caller must be root");
		this.name = name;
		this.password = password;
	}

	/**
	 * Returns the user's name.
	 * 
	 * @return the user's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the user password.
	 * 
	 * @return the user password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Creates the user.
	 * 
	 * @throws RuntimeException
	 *             if something went wrong during user creation.
	 */
	public void create() {
		List<String> command = new ArrayList<String>();
		command.add("useradd");
		command.add(this.name);
		command.add("-p");
		command.add(TempUser.CRYPT.crypt(password, TempUser.DEFAULT_SEED));
		command.add("-M");
		TempUser.execAndWait(command);
		this.isCreated = true;
	}

	/**
	 * Deletes the user.
	 * 
	 * @throws IllegalStateException
	 *             if the user was not created by this {@link TempUser}
	 *             instance.
	 * @throws RuntimeException
	 *             if something went wrong during user deletion.
	 */
	public void delete() {
		if (!this.isCreated)
			throw new IllegalStateException(
					"Can only delete users created by this "
							+ this.getClass().getSimpleName() + " instance");

		List<String> command = new ArrayList<String>();
		command.add("userdel");
		command.add(this.name);
		TempUser.execAndWait(command);
		this.isCreated = false;
	}

	private static void execAndWait(List<String> command) {
		Process p = null;
		try {
			p = new ProcessBuilder().command(command).start();
			p.waitFor();
		} catch (Exception e) {
			throw new RuntimeException("Could not launch binary "
					+ command.get(0) + "\".\n", e);
		}
		if (p.exitValue() != 0) {
			StringBuilder msg = new StringBuilder(
					"Error while executing binary \"");
			msg.append(command.get(0));
			msg.append("\".\n");
			msg.append("Error code: ");
			msg.append(p.exitValue());
			msg.append("\n");
			msg.append("Standard output is:\n");
			TempUser.dumpStream(p.getInputStream(), msg);
			msg.append("Standard error is:\n");
			TempUser.dumpStream(p.getErrorStream(), msg);

			throw new RuntimeException(msg.toString());
		}

	}

	private static StringBuilder dumpStream(InputStream in, StringBuilder out) {
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		try {
			while ((line = reader.readLine()) != null) {
				out.append(line);
			}
		} catch (IOException e) {
			out.append("Could not read from input stream.\n");
			out.append("Reason:");
			out.append(e.getMessage());
		}

		return out;
	}

	private interface CLibrary extends Library {
		int geteuid();
	}

	private interface CryptLibrary extends Library {
		String crypt(String key, String salt);
	}
}
