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
public class NativeConvCallbackHandlerImp implements NativeConvCallbackHandler {

	private final CallbackHandler handler;

	private static final MessageDispatcher dispatcher = new MessageDispatcher();

	public NativeConvCallbackHandlerImp(CallbackHandler handler) {
		this.handler = handler;
	}

	@Override
	public int invoke(int numMsg, PointerByReference msg,
			PointerByReference resp, Pointer appData) {

		Callback[] callbacks = new Callback[numMsg];

		for (int i = 0; i < numMsg; i++) {
			// TODO: Test this on Solaris machines.
			NativeMessage message = new NativeMessage(msg.getPointer()
					.getPointer(i));
			callbacks[i] = NativeConvCallbackHandlerImp.dispatcher.dispatch(
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
		
		NativeResponse tmp = new NativeResponse.ByReference();
		NativeResponse[] responses = (NativeResponse[]) tmp.toArray(numMsg);
		
		System.out.println(callbacks.length);
		for (int i = 0; i < callbacks.length; i++) {
			responses[i].setAutoSynch(true);
			if(callbacks[i] instanceof PasswordCallback) {
				final PasswordCallback passwordCallback = (PasswordCallback)callbacks[i];
				responses[i].resp = new String(passwordCallback.getPassword());
				passwordCallback.clearPassword();
			}
		}

		// reply[i].write();

		tmp.write();
		resp.setValue(tmp.getPointer());
		return ReturnCode.PAM_SUCCESS.getCode();

	}

};
