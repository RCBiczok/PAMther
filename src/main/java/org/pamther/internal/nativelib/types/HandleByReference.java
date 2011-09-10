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

import com.sun.jna.ptr.PointerByReference;

/**
 * @author biczok
 */
public class HandleByReference extends PointerByReference {

	private Handle pamHandle;

	/**
	 * Default constructor for {@link HandleByReference} object.<br/>
	 * This is required since JNA needs a non-argument constructor for native
	 * mapping
	 */
	public HandleByReference() {
	}

	public void setPamHandle(Handle pamHandle) {
		this.setValue(pamHandle.getPointer());
		this.pamHandle = pamHandle;
	}

	public Handle getPamHandle() {
		if (this.pamHandle == null)
			this.pamHandle = new Handle(this.getValue());
		else
			this.pamHandle.setPointer(this.getValue());
		return pamHandle;
	}
}
