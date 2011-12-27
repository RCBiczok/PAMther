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

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;

import org.pamther.internal.nativelib.ReturnCode;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * Globally used implementation of a {@link NativeCallbackHandler}. The class is
 * responsible for the following actions:
 * <ul>
 * <li>Receiving PAM {@link NativeMessage NativeMessages} and transform them
 * into JAAS {@link Callback Callbacks}.</li>
 * <li>Passing the {@link Callback Callbacks} to the JAAS
 * {@link CallbackHandler}.</li>NativeResponse
 * <li>Transforming the JAAS {@link Callback Callbacks} into PAM
 * {@link NativeResponse NativeResponses}.</li>
 * <li>Catching exceptions thrown by the JAAS {@link CallbackHandler}.</li>
 * </ul>
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public final class NativeCallbackHandlerImp implements NativeCallbackHandler {

	/**
	 * The dispatcher use transform {@link NativeMessage NativeMessages} into
	 * {@link Callback Callbacks}.
	 */
	private static final MessageDispatcher DISPATCHER = new MessageDispatcher();

	private static final NativeResponse DUMMY_RESPONSE = new NativeResponse();

	/**
	 * Holds the JAAS {@link CallbackHandler}.
	 */
	private CallbackHandler handler;

	/**
	 * Holds the exception recently thrown by the JAAS {@link CallbackHandler}.
	 */
	private Exception lastException;

	@Override
	public int callback(final int numMsg, final PointerByReference msg,
			final PointerByReference resp, final Pointer appData) {

		Callback[] callbacks = new Callback[numMsg];

		for (int i = 0; i < numMsg; i++) {
			// TODO: Test this on Solaris machines.
			NativeMessage message = new NativeMessage(msg.getPointer()
					.getPointer(i));
			callbacks[i] = NativeCallbackHandlerImp.DISPATCHER.dispatch(
					message.msg, message.msg_style);
		}

		/*
		 * We catch any exception here and throw it after the callback execution
		 * so that the native PAM routine can free heap-space.
		 */
		try {
			this.handler.handle(callbacks);
		} catch (Exception e) {
			this.lastException = e;
			return ReturnCode.PAM_CONV_ERR.getCode();
		}

		final NativeResponse[] responses = (NativeResponse[]) new NativeResponse.ByReference(
				new PermanentMemory(DUMMY_RESPONSE.size() * numMsg))
				.toArray(numMsg);
		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof PasswordCallback) {
				final PasswordCallback passwordCallback = (PasswordCallback) callbacks[i];
				PermanentMemory password = new PermanentMemory(
						passwordCallback.getPassword().length * Character.SIZE);
				for (int j = 0; j < passwordCallback.getPassword().length; j++) {
					password.setChar(j, passwordCallback.getPassword()[j]);
				}
				responses[i].resp = password;
				passwordCallback.clearPassword();
			}
			responses[i].setAutoSynch(true);
			responses[i].write();
		}

		resp.setValue(responses[0].getPointer());
		return ReturnCode.PAM_SUCCESS.getCode();

	}

	@Override
	public CallbackHandler getCallbackHandler() {
		return this.handler;
	}

	@Override
	public void setCallbackHandler(final CallbackHandler callbackHandler) {
		this.handler = callbackHandler;
	}

	@Override
	public Exception getLastException() {
		return lastException;
	}

};
