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

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;

/**
 * Native interface to PAM library.
 * 
 * @author <a href="https://github.com/RCBiczok">Rudolf Biczok</a>
 */
public interface PAMLibrary extends Library {
	public static final PAMLibrary INSTANCE = (PAMLibrary) Native.loadLibrary(
			"pam", PAMLibrary.class);

	int pam_start(String service, String user, Conversation pamConverse,
			HandleByReference pamHandle);
	
	int pam_end(Handle pamHandle, int status);
	
	int pam_authenticate(Handle pamHandle, int flags);
	
	int pam_acct_mgmt(Handle pamHandle, int flags);

	int pam_chauthtok(Handle pamHandle, int flags);
	
	int pam_setcred(Handle pamHandle, int flags);

	int pam_open_session(Handle pamHandle, int flags);
	
	int pam_close_session(Handle pamHandle, int flags);
	
	int pam_get_item(Handle pamHandle, int itemType, String[] item);

	int pam_get_user(Handle pamHandle, String[] user, String prompt);
	
	int pam_set_item(Handle pamHandle, int itemType, String item);

	String pam_strerror(Handle pamHandle, int errorNumber);
}
