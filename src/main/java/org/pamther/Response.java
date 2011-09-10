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

import org.pamther.internal.nativelib.types.NativeResponse;

/**
 * @author <a href="https://github.com/RCBiczok">Rudolf Biczok</a>
 */
public final class Response {

	private NativeResponse nativeResponse;
	
	public Response(NativeResponse nativeResponse) {
		this.nativeResponse = nativeResponse;
	}
	
	public String getResponse() {
		return this.nativeResponse.resp;
	}
	
	public void setResponse(String response) {
		this.nativeResponse.resp = response;
	}
	
}
