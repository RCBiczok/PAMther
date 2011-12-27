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
 * Objects of this {@link Structure} holds response information to a
 * corresponding {@link NativeMessage}.
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 * @see <a href="http://linux.die.net/man/3/pam_conv">pam_conv</a>.
 */
public class NativeResponse extends Structure {

	/**
	 * Holds the file order.
	 */
	private static final String[] FIELD_ORDER = new String[] { "resp" };

	/**
	 * Holds a {@link Pointer} to the actual (string) response.
	 */
	public Pointer resp;

	/* No need for this field */
	/* public int resp_retcode; */

	/**
	 * Constructs a new {@link NativeResponse} structure.
	 */
	public NativeResponse() {
		super();
		this.setFieldOrder(NativeResponse.FIELD_ORDER);
	}

	/**
	 * Constructs a new {@link NativeResponse} structure.
	 * 
	 * @param p
	 *            {@link Pointer} to preallocated memory.
	 */
	public NativeResponse(final Pointer p) {
		super(p);
		this.setFieldOrder(NativeResponse.FIELD_ORDER);
	}

	/**
	 * The {@link Structure.ByReference} version of {@link NativeResponse}.
	 * 
	 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
	 */
	public static class ByReference extends NativeResponse implements
			Structure.ByReference {

		/**
		 * Constructs a new {@link Structure.ByReference} instance of
		 * {@link NativeResponse}.
		 * 
		 * @param p
		 *            {@link Pointer} to preallocated memory.
		 */
		public ByReference(final Pointer p) {
			super(p);
		}

		/**
		 * Constructs a new {@link Structure.ByReference} instance of
		 * {@link NativeResponse}.
		 */
		public ByReference() {
			super();
		}
	};

	/**
	 * The {@link Structure.ByValue} version of {@link NativeResponse}.
	 * 
	 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
	 */
	public static class ByValue extends NativeResponse implements
			Structure.ByValue {
	};
}
