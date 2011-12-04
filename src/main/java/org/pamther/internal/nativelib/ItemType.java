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
 * Represents the item
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public enum ItemType {

	PAM_SERVICE(1),

	PAM_USER(2),

	PAM_TTY(3),

	PAM_RHOST(4),

	PAM_CONV(5),

	PAM_AUTHTOK(6),

	PAM_OLDAUTHTOK(7),

	PAM_RUSER(8),

	PAM_USER_PROMPT(9),

	PAM_REPOSITORY(10);

	private int code;

	ItemType(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}
}
