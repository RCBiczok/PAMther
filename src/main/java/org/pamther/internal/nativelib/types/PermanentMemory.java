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

import com.sun.jna.Memory;

/**
 * The {@link PermanentMemory} represents a {@link com.sun.jna.Pointer Pointer}
 * to a block of memory. Instances of this class won't delete any allocated
 * memory, so its up to the native code for cleaning up the memory block.
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public class PermanentMemory extends Memory {

	/**
	 * Allocates some memory.
	 * 
	 * @param size
	 *            number of <em>bytes</em> to allocate
	 * @see com.sun.jna.Memory#Memory(long)
	 */
	public PermanentMemory(final long size) {
		super(size);
	}

	/**
	 * Prevents the JNA environment for freeing the allocated memory.
	 * @see com.sun.jna.Memory#finalize()
	 */
	@Override
	protected void finalize() {
	}
}
