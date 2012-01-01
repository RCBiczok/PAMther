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
package org.pamther.internal.nativelib;

/**
 * Represents the style of an received message.
 * 
 * @see <a href="http://linux.die.net/man/3/pam_conv">pam_conv()</a>
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public enum MsgStyle {

	/**
	 * Obtain a string without echoing any text.
	 */
	PAM_PROMPT_ECHO_OFF(1),

	/**
	 * Obtain a string whilst echoing text.
	 */
	PAM_PROMPT_ECHO_ON(2),

	/**
	 * Display an error message.
	 */
	PAM_ERROR_MSG(3),

	/**
	 * Display some text.
	 */
	PAM_TEXT_INFO(4);

	/**
	 * Holds the native message stype type.
	 */
	private int code;

	/**
	 * Constructs a new {@link MsgStyle} instance.
	 * 
	 * @param code
	 *            the native message style type code.
	 */
	MsgStyle(final int code) {
		this.code = code;
	}

	/**
	 * Returns the corresponding native message style type.
	 * 
	 * @return the corresponding native message style type.
	 */
	public int getCode() {
		return this.code;
	}
}
