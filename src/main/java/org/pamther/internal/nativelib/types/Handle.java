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

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

/**
 * Represents a native handle returned by the <a
 * href="http://linux.die.net/man/3/pam_start">pam_start()</a> method.
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public class Handle extends PointerType {

	/**
	 * Constructs a new {@link Handle} instance.
	 * 
	 * @param p
	 *            the native pointer itself.
	 */
	public Handle(final Pointer p) {
		super(p);
	}

	/**
	 * Constructs a new {@link Handle} instance. This class is only called by
	 * JNA itself.
	 */
	public Handle() {
		super();
	}
}
