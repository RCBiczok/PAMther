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
package org.pamther.internal.nativelib;

/**
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public enum MsgStyle {

	PAM_PROMPT_ECHO_OFF(1),

	PAM_PROMPT_ECHO_ON (2),

	PAM_ERROR_MSG(3),

	PAM_TEXT_INFO(4);
	

	private int code;
	
	MsgStyle(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}
}
