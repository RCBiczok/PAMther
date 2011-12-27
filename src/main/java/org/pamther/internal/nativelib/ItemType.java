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
 * Used to identify an item inside an PAM transaction.
 * 
 * @see <a href="http://linux.die.net/man/3/pam_set_item">pam_set_item()</a> or
 *      <a href="http://linux.die.net/man/3/pam_get_item">pam_get_item()</a>
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public enum ItemType {

	/**
	 * The service name (which identifies that PAM stack that the PAM functions
	 * will use to authenticate the program).
	 */
	PAM_SERVICE(1),

	/**
	 * The username of the entity under whose identity service will be given.
	 * That is, following authentication, PAM_USER identifies the local entity
	 * that gets to use the service. Note, this value can be mapped from
	 * something (eg., "anonymous") to something else (eg. "guest119") by any
	 * module in the PAM stack. As such an application should consult the value
	 * of PAM_USER after each call to a PAM function.
	 */
	PAM_USER(2),

	/**
	 * The terminal name: prefixed by /dev/ if it is a device file; for
	 * graphical, X-based, applications the value for this item should be the
	 * $DISPLAY variable.
	 */
	PAM_TTY(3),

	/**
	 * 
	 * The requesting hostname (the hostname of the machine from which the
	 * PAM_RUSER entity is requesting service). That is PAM_RUSER@PAM_RHOST does
	 * identify the requesting user. In some applications, PAM_RHOST may be
	 * NULL. In such situations, it is unclear where the authentication request
	 * is originating from.
	 */
	PAM_RHOST(4),

	/**
	 * The pam_conv structure. See pam_conv(3).
	 */
	PAM_CONV(5),

	/**
	 * The authentication token (often a password). This token should be ignored
	 * by all module functions besides <a
	 * href="http://linux.die.net/man/3/pam_sm_authenticate"
	 * >pam_sm_authenticate()</a> and <a
	 * href="http://linux.die.net/man/3/pam_sm_chauthtok"
	 * >pam_sm_chauthtok()</a>. In the former function it is used to pass the
	 * most recent authentication token from one stacked module to another. In
	 * the latter function the token is used for another purpose. It contains
	 * the currently active authentication token.
	 */
	PAM_AUTHTOK(6),

	/**
	 * The old authentication token. This token should be ignored by all module
	 * functions except <a href="http://linux.die.net/man/3/pam_sm_chauthtok"
	 * >pam_sm_chauthtok()</a>.
	 */
	PAM_OLDAUTHTOK(7),

	/**
	 * The requesting user name: local name for a locally requesting user or a
	 * remote user name for a remote requesting user. <br />
	 * <br />
	 * Generally an application or module will attempt to supply the value that
	 * is most strongly authenticated (a local account before a remote one. The
	 * level of trust in this value is embodied in the actual authentication
	 * stack associated with the application, so it is ultimately at the
	 * discretion of the system administrator. <br />
	 * <br />
	 * PAM_RUSER@PAM_RHOST should always identify the requesting user. In some
	 * cases, PAM_RUSER may be <code>NULL</code>. In such situations, it is unclear who the
	 * requesting entity is.
	 */
	PAM_RUSER(8),

	/**
	 * The string used when prompting for a user's name. The default value for
	 * this string is a localized version of "login: ".
	 */
	PAM_USER_PROMPT(9);

	/**
	 * Holds the native return code.
	 */
	private int code;

	/**
	 * Constructs a new {@link ItemType} instance.
	 * 
	 * @param code
	 *            the native item type code.
	 */
	ItemType(final int code) {
		this.code = code;
	}

	/**
	 * Returns the corresponding native item type.
	 * 
	 * @return the corresponding native item type.
	 */
	public int getCode() {
		return this.code;
	}
}
