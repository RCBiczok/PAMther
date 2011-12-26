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
package org.pamther.internal.nativelib;

import org.pamther.internal.nativelib.types.Conversation;
import org.pamther.internal.nativelib.types.Handle;
import org.pamther.internal.nativelib.types.HandleByReference;

import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;

/**
 * Native interface to PAM library.
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
	
	public static native int pam_start(String service, String user, Conversation pamConverse,
			HandleByReference pamHandle);
	
	public static native int pam_end(Handle pamHandle, int status);
	
	public static native int pam_authenticate(Handle pamHandle, int flags);
	
	public static native int pam_acct_mgmt(Handle pamHandle, int flags);

	public static native int pam_chauthtok(Handle pamHandle, int flags);
	
	public static native int pam_setcred(Handle pamHandle, int flags);
	
	public static native int pam_get_item(Handle pamHandle, int itemType, PointerByReference item);

	public static native int pam_set_item(Handle pamHandle, int itemType, String item);

	public static native String pam_strerror(Handle pamHandle, int errorNumber);
}
