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
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public class NativeMessage extends Structure {

	private static final String[] FIELD_ORDER = new String[] { "msg_style",
			"msg" };

	public int msg_style;
	public String msg;

	public NativeMessage() {
	}

	public NativeMessage(Pointer p) {
		super(p);
		this.read();
		this.setFieldOrder(NativeMessage.FIELD_ORDER);
	}

	public NativeMessage(int msgStyle, String msg) {
		super();
		this.msg_style = msgStyle;
		this.msg = msg;
		this.setFieldOrder(NativeMessage.FIELD_ORDER);
	}

	public static class ByReference extends NativeMessage implements
			Structure.ByReference {
	};

	public static class ByValue extends NativeMessage implements Structure.ByValue {

	};
}
