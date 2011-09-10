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
package org.pamther;

import org.pamther.internal.nativelib.types.NativeMessage;

/*
 * A {@link PamMessage} contains textual informations from the underlying PAM
 * module.
 * 
 * @author <a href="https://github.com/RCBiczok">Rudolf Biczok</a>
 * @see <a href="http://linux.die.net/man/3/pam_conv">pam_conv</a>
 */
public final class Message {
	
	private NativeMessage nativeMessage;
	
	public Message(NativeMessage nativeMessage) {
		this.nativeMessage = nativeMessage;
	}
	
	public String getMessage() {
		return this.nativeMessage.msg;
	}
	
	//TODO: It is maybe better to replace this with a class hierarchy.
	public int getStyle() {
		return this.nativeMessage.msg_style;
	}
	
}
