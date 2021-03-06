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
package org.pamther.internal.nativelib.types;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;

import org.pamther.internal.nativelib.MsgStyle;
import org.pamther.jaas.NewPasswordCallback;

/**
 * This is the place where incoming PAM messages gets dispatched and transformed
 * into {@link Callback} instances.
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
class MessageDispatcher {

	/*
	 * TODO: Need to be optimized: May search for key words, convert always
	 * messages PasswordCallback's if type is PAM_PROMPT_ECHO_ON
	 */
	/**
	 * Transforms the given message into a {@link Callback}.
	 * 
	 * @param message
	 *            the message returned by PAM module.
	 * @param type
	 *            the PAM message type returned by the PAM module.
	 * @return the {@link Callback} equivalent of the given message.
	 */
	Callback dispatch(final String message, final int type) {

		Callback callback = null;

		if (message.equals("Password: ")) {
			callback = new PasswordCallback(message,
					type == MsgStyle.PAM_PROMPT_ECHO_ON.getCode());
		} else if (message.equals("Enter new UNIX password: ")
				|| message.equals("Retype new UNIX password: ")
				|| message.equals("New Password: ")
				|| message.equals("Re-enter new Password: ")) {
			callback = new NewPasswordCallback(message,
					type == MsgStyle.PAM_PROMPT_ECHO_ON.getCode());
		} else if (message.equals("login: ")) {
			callback = new NameCallback(message);
		} else if (type == MsgStyle.PAM_ERROR_MSG.getCode()) {
			callback = new TextOutputCallback(TextOutputCallback.ERROR, message);
		} else if (type == MsgStyle.PAM_TEXT_INFO.getCode()) {
			callback = new TextOutputCallback(TextOutputCallback.INFORMATION,
					message);
		}

		return callback;
	}
}
