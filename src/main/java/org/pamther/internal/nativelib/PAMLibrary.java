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
package org.pamther.internal.nativelib;

import org.pamther.internal.nativelib.types.Conversation;
import org.pamther.internal.nativelib.types.Handle;
import org.pamther.internal.nativelib.types.HandleByReference;

import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;

/**
 * The private JNA native interface for accessing the PAM routines.
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public final class PAMLibrary {

	static {
		Native.register("pam");
	}

	/**
	 * Private constructor for preventing instantiations.
	 */
	private PAMLibrary() {
	}

	/**
	 * Starts a new PAM transaction.
	 * 
	 * @param service
	 *            the service configuration name.
	 * @param user
	 *            the user name.
	 * @param pamConverse
	 *            the PAM conversation structure holding the callback function.
	 * @param pamHandle
	 *            a JNA {@link com.sun.jna.Pointer} which will keep the native handler.
	 * @return an integer value identifying an error message or a successful
	 *         invocation.
	 * @see <a href="http://linux.die.net/man/3/pam_start">pam_start()</a>
	 */
	public static native int pam_start(final String service, final String user,
			final Conversation pamConverse, final HandleByReference pamHandle);

	/**
	 * Closes an active PAM transaction.
	 * 
	 * @param handle
	 *            the native PAM handle.
	 * @param status
	 *            the {@link ReturnCode} from the last PAM function call.
	 * @return an integer value identifying an error message or a successful
	 *         invocation.
	 * @see <a href="http://linux.die.net/man/3/pam_end">pam_end()</a>
	 */
	public static native int pam_end(final Handle handle, final int status);

	/**
	 * Performs user authentication.
	 * 
	 * @param handle
	 *            the native PAM handle.
	 * @param flags
	 *            for additional operations.
	 * @return an integer value identifying an error message or a successful
	 *         invocation.
	 * @see <a
	 *      href="http://linux.die.net/man/3/pam_authenticate">pam_authenticate()</a>
	 */
	public static native int pam_authenticate(final Handle handle,
			final int flags);

	/**
	 * Performs user account validation.
	 * 
	 * @param handle
	 *            the native PAM handle.
	 * @param flags
	 *            for additional operations.
	 * @return an integer value identifying an error message or a successful
	 *         invocation.
	 * @see <a
	 *      href="http://linux.die.net/man/3/pam_acct_mgmt">pam_acct_mgmt()</a>
	 */
	public static native int pam_acct_mgmt(final Handle handle, final int flags);

	/**
	 * Performs credentials setup.
	 * 
	 * @param handle
	 *            the native PAM handle.
	 * @param flags
	 *            for additional operations.
	 * @return an integer value identifying an error message or a successful
	 *         invocation.
	 * @see <a href="http://linux.die.net/man/3/pam_setcred">pam_setcred()</a>
	 */
	public static native int pam_setcred(final Handle handle, final int flags);

	/**
	 * Performs authentication token manipulation.
	 * 
	 * @param handle
	 *            the native PAM handle.
	 * @param flags
	 *            for additional operations.
	 * @return an integer value identifying an error message or a successful
	 *         invocation.
	 * @see <a
	 *      href="http://linux.die.net/man/3/pam_chauthtok">pam_chauthtok()</a>
	 */
	public static native int pam_chauthtok(final Handle handle, final int flags);

	/**
	 * Returns the value of an transaction item.
	 * 
	 * @param handle
	 *            the native PAM handle.
	 * @param itemType
	 *            the type of item we want to set.
	 * @param item
	 *            will hold a {@link com.sun.jna.Pointer Pointer} to the item
	 *            value.
	 * @return an integer value identifying an error message or a successful
	 *         invocation.
	 * @see <a href="http://linux.die.net/man/3/pam_get_item">pam_get_item()</a>
	 */
	public static native int pam_get_item(final Handle handle,
			final int itemType, final PointerByReference item);

	/**
	 * Sets the value of a string transaction item.
	 * 
	 * @param handle
	 *            the native PAM handle.
	 * @param itemType
	 *            the type of item we want to set.
	 * @param item
	 *            the string value we want to assign to the item.
	 * @return an integer value identifying an error message or a successful
	 *         invocation.
	 * @see <a href="http://linux.die.net/man/3/pam_set_item">pam_set_item()</a>
	 */
	public static native int pam_set_item(final Handle handle,
			final int itemType, final String item);

	/**
	 * Returns a string representation of the corresponding
	 * <code>errorNumber</code>.
	 * 
	 * @param handle
	 *            the native PAM handle.
	 * @param errorNumber
	 *            an integer value identifying an error message or a successful
	 *            invocation.
	 * @return an integer value identifying an error message or a successful
	 *         invocation.
	 * @see <a href="http://linux.die.net/man/3/pam_strerror">pam_strerror()</a>
	 */
	public static native String pam_strerror(final Handle handle,
			final int errorNumber);
}
