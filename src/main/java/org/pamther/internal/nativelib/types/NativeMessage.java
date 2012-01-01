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

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * Objects of this {@link Structure} holds information from the underlying PAM
 * module.
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 * @see <a href="http://linux.die.net/man/3/pam_conv">pam_conv</a>.
 */
public class NativeMessage extends Structure {

	/**
	 * Holds the file order.
	 */
	private static final String[] FIELD_ORDER = new String[] { "msg_style",
			"msg" };

	/**
	 * Holds an integer value identifying the message style.
	 * {@link org.pamther.internal.nativelib.MsgStyle MsgStyle}.
	 */
	public int msg_style;

	/**
	 * Holds the actual message.
	 */
	public String msg;

	/**
	 * Constructs a new {@link NativeMessage} structure.
	 */
	public NativeMessage() {
	}

	/**
	 * Constructs a new {@link NativeMessage} structure.
	 * 
	 * @param p
	 *            {@link Pointer} to preallocated memory.
	 */
	public NativeMessage(final Pointer p) {
		super(p);
		this.read();
		this.setFieldOrder(NativeMessage.FIELD_ORDER);
	}

	/**
	 * Constructs a new {@link NativeMessage} structure.
	 * 
	 * @param msgStyle an integer value identifying the message style.
	 * @param msg the actual message.
	 */
	public NativeMessage(final int msgStyle, final String msg) {
		super();
		this.msg_style = msgStyle;
		this.msg = msg;
		this.setFieldOrder(NativeMessage.FIELD_ORDER);
	}

	/**
	 * The {@link Structure.ByReference} version of {@link NativeMessage}.
	 * 
	 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
	 */
	public static class ByReference extends NativeMessage implements
			Structure.ByReference {
	};

	/**
	 * The {@link Structure.ByValue} version of {@link NativeMessage}.
	 * 
	 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
	 */
	public static class ByValue extends NativeMessage implements
			Structure.ByValue {
	};
}
