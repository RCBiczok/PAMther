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

import org.pamther.ConvCallbackHandler;
import org.pamther.Message;
import org.pamther.Response;
import org.pamther.internal.nativelib.ReturnCode;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * Some doc.
 */
public class NativeConvCallbackHandlerImp implements NativeConvCallbackHandler {

	private final ConvCallbackHandler convCallbackHandler;

	public NativeConvCallbackHandlerImp(ConvCallbackHandler convCallbackHandler) {
		this.convCallbackHandler = convCallbackHandler;
	}

	// @Override
	public int invoke(int numMsg, PointerByReference msg,
			PointerByReference resp, Pointer appData) {
		NativeResponse tmp = new NativeResponse.ByReference();
		NativeResponse[] reply = (NativeResponse[]) tmp.toArray(numMsg);

		Message[] messages = new Message[reply.length];
		Response[] responses = new Response[reply.length];

		for (int i = 0; i < numMsg; i++) {
			reply[i].setAutoSynch(true);
			NativeMessage oneMsg = new NativeMessage(msg.getPointer()
					.getPointer(i));
			
			messages[i] = new Message(oneMsg);
			responses[i] = new Response(reply[i]);
			
			// reply[i].write();
		}

		this.convCallbackHandler.handle(messages, responses);

		tmp.write();
		resp.setValue(tmp.getPointer());
		return ReturnCode.PAM_SUCCESS.getCode();

	}

};
