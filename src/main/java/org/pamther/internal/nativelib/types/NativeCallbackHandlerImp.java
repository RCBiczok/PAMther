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

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.pamther.internal.nativelib.ReturnCode;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * Some doc.
 */
public class NativeCallbackHandlerImp implements NativeCallbackHandler {

	private CallbackHandler handler;

	private static final MessageDispatcher DISPATCHER = new MessageDispatcher();

	private static final NativeResponse DUMMY_RESPONSE = new NativeResponse();

	public CallbackHandler getCallbackHandler() {
		return this.handler;
	}

	public void setCallbackHandler(CallbackHandler handler) {
		this.handler = handler;
	}

	@Override
	public int callback(int numMsg, PointerByReference msg,
			PointerByReference resp, Pointer appData) {

		Callback[] callbacks = new Callback[numMsg];

		for (int i = 0; i < numMsg; i++) {
			// TODO: Test this on Solaris machines.
			NativeMessage message = new NativeMessage(msg.getPointer()
					.getPointer(i));
			callbacks[i] = NativeCallbackHandlerImp.DISPATCHER.dispatch(
					message.msg, message.msg_style);
		}

		try {
			this.handler.handle(callbacks);
		} catch (IOException e) {
			// TODO HOW TO HANDLE???
			e.printStackTrace();
		} catch (UnsupportedCallbackException e) {
			// TODO HOW TO HANDLE???
			e.printStackTrace();
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
};
