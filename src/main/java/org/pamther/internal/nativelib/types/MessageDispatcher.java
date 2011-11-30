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
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;

import org.pamther.internal.nativelib.MsgStyle;


/**
 * @author <a href="https://github.com/RCBiczok">Rudolf Biczok</a>
 */
class MessageDispatcher {

	public Callback dispatch(String message, int type) {

		Callback callback = null;

		if (message.equals("Password: ")
				|| message.equals("Enter new UNIX password: ")
				|| message.equals("Retype new UNIX password: ")) {
			callback = new PasswordCallback(message,
					type == MsgStyle.PAM_PROMPT_ECHO_ON.getCode());
		}
		else if (message.equals("login: ")) {
			callback = new NameCallback(message);
		}

		return callback;
	}
}
