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
package org.pamther.internal.nativelib.types;

import javax.security.auth.callback.CallbackHandler;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * A native {@link NativeCallbackHandler} is an internal class which tries to
 * separate the PAM-specific conversation mechanism from the classic JAAS
 * callback mechanism.
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 * @see <a href="http://linux.die.net/man/3/pam_conv">pam_conv</a>.
 */
public interface NativeCallbackHandler extends Callback {

	/**
	 * This method is called by a PAM module.
	 * 
	 * @param numMsg
	 *            number of messages.
	 * @param msg
	 *            an array of {@link NativeMessage}. (Or an array of pointer of
	 *            {@link NativeMessage}, depending on the OS).
	 * @param resp
	 *            an array of {@link NativeResponce}.
	 * @param appData
	 *            additional application-specific data.
	 * @return {@link ReturnCodePAM_BUF_ERR

	 */
	int callback(final int numMsg, final PointerByReference msg,
			final PointerByReference resp, final Pointer appData);

	/**
	 * Returns the JAAS {@link CallbackHandler}.
	 * 
	 * @return the JAAS {@link CallbackHandler}.
	 */
	CallbackHandler getCallbackHandler();

	/**
	 * Sets the JAAS {@link CallbackHandler}.
	 * 
	 * @param handler
	 *            the {@link CallbackHandler} we want to set.
	 */
	void setCallbackHandler(final CallbackHandler handler);

	/**
	 * Returns the last exception thrown by the JAAS {@link CallbackHandler}
	 * instance.
	 * 
	 * @return the last exception thrown by the JAAS {@link CallbackHandler}
	 *         instance.
	 */
	Exception getLastException();
};
