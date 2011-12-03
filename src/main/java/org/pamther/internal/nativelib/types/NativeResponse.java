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

public class NativeResponse extends Structure {

	private static final String[] FIELD_ORDER = new String[] { "resp",
			"resp_retcode" };

	public Pointer resp;
	public int resp_retcode;

	public NativeResponse() {
		super();
		this.setFieldOrder(NativeResponse.FIELD_ORDER);
	}
	
	public NativeResponse(Pointer p) {
		super(p);
		this.setFieldOrder(NativeResponse.FIELD_ORDER);
	}

	public NativeResponse(Pointer resp, int resp_retcode) {
		this();
		this.resp = resp;
		this.resp_retcode = resp_retcode;
	}

	public static class ByReference extends NativeResponse implements
			Structure.ByReference {
		public ByReference(Pointer p) {
			super(p);
		}
		
		public ByReference() {
			super();
		}
	};

	public static class ByValue extends NativeResponse implements
			Structure.ByValue {
	};
}
