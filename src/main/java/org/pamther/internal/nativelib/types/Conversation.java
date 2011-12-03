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

import com.sun.jna.Structure;

public class Conversation extends Structure {

	private static final String[] FIELD_ORDER = new String[] { "conv" };

	public NativeCallbackHandler conv;

	/*
	 * No need for this attribute 
	 * public Pointer appdata_ptr;
	 */

	public Conversation() {
		this(null);
	}

	public Conversation(NativeCallbackHandler nativeCallbackHandler) {
		super(new PermanentMemory(34));
		this.conv = nativeCallbackHandler;
		this.setFieldOrder(Conversation.FIELD_ORDER);
	}

	public static class ByReference extends Conversation implements
			Structure.ByReference {
	};

	public static class ByValue extends Conversation implements
			Structure.ByValue {
	};
}
