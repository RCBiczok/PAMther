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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.sun.jna.Platform;

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
public abstract class TempUser {

	/**
	 * Default seed used for the password encryption. We create dummy users. so
	 * its not that bad when we use non-sense seeds.
	 */
	protected static final String DEFAULT_SEED = "ABC";

	/**
	 * The user's name.
	 */
	private String name;

	/**
	 * The user's password.
	 */
	private char[] password;

	/**
	 * Used to make sure that the user is created by ourselves.
	 */
	private boolean created;

	/**
	 * Creates a new {@link TestUser} instance applicable for this platform.
	 * 
	 * @param name
	 *            the user's name.
	 * @param password
	 *            the user's password.
	 * @return a new {@link TestUser} instance applicable for this platform.
	 * @throws RuntimeException
	 *             if user ins not root user.
	 */
	public static final TempUser newInstance(final String name,
			final char[] password) {
		if (Platform.isLinux()) {
			return new LinuxTempUser(name, password);
		}
		if (Platform.isSolaris()) {
			return new SolarisTempUser(name, password);
		}
		throw new RuntimeException(
				"This platform is not supported for running tests");
	}

	/**
	 * Create a new temporary user.
	 * 
	 * @param name
	 *            the user's name.
	 * @param password
	 *            the user's password.
	 */
	TempUser(final String name, final char[] password) {
		this.name = name;
		this.password = password;
	}

	/**
	 * Returns the user's name.
	 * 
	 * @return the user's name.
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Returns the user password.
	 * 
	 * @return the user password.
	 */
	public final char[] getPassword() {
		return password;
	}

	/**
	 * Creates the user.
	 * 
	 * @throws RuntimeException
	 *             if something went wrong during user creation.
	 */
	public final void create() {
		this.performCreation();
		this.created = true;
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
	public final void delete() {
		if (this.created) {
			this.performDeletion();
			this.created = false;
		}
	}

	/**
	 * Takes an command line command, executes it and wait for it until its
	 * execution is finished.
	 * 
	 * @param command
	 *            an array of command line arguments.
	 * 
	 * @throw RuntimeException if there was an error during execution.
	 */
	protected static void execAndWait(final List<String> command) {
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
			msg.append("\nStandard output is:\n");
			TempUser.dumpStream(p.getInputStream(), msg);
			msg.append("\nStandard error is:\n");
			TempUser.dumpStream(p.getErrorStream(), msg);
			msg.append("\nFull command was: " + command.toString());

			throw new RuntimeException(msg.toString());
		}

	}

	/**
	 * Dumps the stream into a {@link StringBuilder}.
	 * 
	 * @param in
	 *            the {@link InputStream} we want to dump from.
	 * @param out
	 *            the target {@link StringBuilder} which will hold the dumped
	 *            stuff.
	 * @return the {@link StringBuilder} instance passed by <code>out</code>
	 *         containing the dumped information.
	 */
	private static StringBuilder dumpStream(final InputStream in,
			final StringBuilder out) {
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

	/**
	 * Performs the platform specific user creation.
	 */
	public abstract void performCreation();

	/**
	 * performs the platform specific user deletion.
	 */
	public abstract void performDeletion();

}
