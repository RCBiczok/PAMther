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

/**
 * Objects of this {@link Structure} act as a bridge between the caller and the
 * underlying PAM module.
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 * @see <a href="http://linux.die.net/man/3/pam_conv">pam_conv</a>.
 */
public class Conversation extends Structure {

	/**
	 * Holds the file order.
	 */
	private static final String[] FIELD_ORDER = new String[] { "conv" };

	/**
	 * This dummy instance is only used to calculate the structure size.
	 */
	private static final Conversation DUMMY_CONVERSATION = new Conversation();

	/**
	 * "conv" field holding an implemented {@link NativeCallackHandler}
	 * instance.
	 */
	public NativeCallbackHandler conv;

	/* No need for this field */
	/* public Pointer appdata_ptr; */

	/**
	 * Constructs a new {@link Conversation} structure.
	 */
	public Conversation() {
		super();
		this.setFieldOrder(Conversation.FIELD_ORDER);
	}

	/**
	 * Constructs a new {@link Conversation} structure.
	 * 
	 * @param handler
	 *            {@link NativeCallbackHandler} we want to use.
	 */
	public Conversation(final NativeCallbackHandler handler) {
		super(new PermanentMemory(Conversation.DUMMY_CONVERSATION.size()));
		this.conv = handler;
		this.setFieldOrder(Conversation.FIELD_ORDER);
	}

	/**
	 * The {@link Structure.ByReference} version of {@link Conversation}.
	 * 
	 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
	 */
	public static class ByReference extends Conversation implements
			Structure.ByReference {
	};

	/**
	 * The {@link Structure.ByValue} version of {@link Conversation}.
	 * 
	 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
	 */
	public static class ByValue extends Conversation implements
			Structure.ByValue {
	};
}
