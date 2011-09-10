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
 * TODO
 * @author <a href="https://github.com/RCBiczok">Rudolf Biczok</a>
 */
public enum ReturnCode {

	PAM_SUCCESS(0),

	PAM_OPEN_ERR(1),

	PAM_SYMBOL_ERR(2),

	PAM_SERVICE_ERR(3),

	PAM_SYSTEM_ERR(4),

	PAM_BUF_ERR(5),

	PAM_CONV_ERR(6),

	PAM_PERM_DENIED(7),

	PAM_MAXTRIES(8),

	PAM_AUTH_ERR(9),

	PAM_NEW_AUTHTOK_REQD(10),

	PAM_CRED_INSUFFICIENT(11),

	PAM_AUTHINFO_UNAVAIL(12),

	PAM_USER_UNKNOWN(13),

	PAM_CRED_UNAVAIL(14),

	PAM_CRED_EXPIRED(15),

	PAM_CRED_ERR(16),

	PAM_ACCT_EXPIRED(17),

	PAM_AUTHTOK_EXPIRED(18),

	PAM_SESSION_ERR(19),

	PAM_AUTHTOK_ERR(20),

	PAM_AUTHTOK_RECOVERY_ERR(21),

	PAM_AUTHTOK_LOCK_BUSY(22),

	PAM_AUTHTOK_DISABLE_AGING(23),

	PAM_NO_MODULE_DATA(24),

	PAM_IGNORE(25),

	PAM_ABORT(26),

	PAM_TRY_AGAIN(27),

	PAM_MODULE_UNKNOWN(28),

	PAM_DOMAIN_UNKNOWN(29);
	
	/**
	 * @uml.property  name="code"
	 */
	private int code;
	
	ReturnCode(int code) {
		this.code = code;
	}
	
	/**
	 * @return
	 * @uml.property  name="code"
	 */
	public int getCode() {
		return this.code;
	}
}
