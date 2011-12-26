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

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.CredentialNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.pamther.internal.nativelib.ItemType;
import org.pamther.internal.nativelib.PAMLibrary;
import org.pamther.internal.nativelib.ReturnCode;
import org.pamther.internal.nativelib.types.Conversation;
import org.pamther.internal.nativelib.types.HandleByReference;
import org.pamther.internal.nativelib.types.NativeCallbackHandlerImp;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * The {@link Transaction} encapsulates a PAM handle returned by pam_start and
 * is the first place for executing common PAM functions like <a
 * href="http://linux.die.net/man/3/pam_authenticate">pam_authenticate</a> or <a
 * href="http://linux.die.net/man/3/pam_acct_mgmt">pam_acct_mgmt</a>.
 * 
 * @author <a href="https://bitbucket.org/RCBiczok">Rudolf Biczok</a>
 */
public final class Transaction {

	/**
	 * The name of the service configuration used if no other service name was
	 * given during instantiation.
	 * 
	 * @see <a href="http://linux.die.net/man/8/pam.d">PAM configuration
	 *      files</a>
	 */
	public static final String DEFAULT_SERVICE = "login";

	/**
	 * Holds the value returned from the recently executed PAM function.
	 * 
	 * It will be used later when the {@link Transaction#close()} method is
	 * called.
	 */
	private int state;

	private boolean open = true;

	private final Conversation pamConverse = new Conversation(
			new NativeCallbackHandlerImp());

	/**
	 * Points the internal PAM handle.
	 */
	private HandleByReference pamHandlePointer = new HandleByReference();

	/**
	 * Returns the name of the service configuration located in the
	 * <code>/etc/pam.d</code> folder.
	 * 
	 * @return the name of the service configuration located in the
	 *         <code>/etc/pam.d</code> folder.
	 * 
	 * @see <a href="http://linux.die.net/man/8/pam.d">PAM configuration
	 *      files</a>
	 */
	public String getService() {
		return this.getItem(ItemType.PAM_SERVICE).getString(0);
	}

	/**
	 * Sets the name of the used service configuration.
	 * 
	 * @param service
	 *            the name of the service configuration
	 * 
	 * @see <a href="http://linux.die.net/man/8/pam.d">PAM configuration
	 *      files</a>
	 */
	public void setService(final String service) {
		this.setItem(ItemType.PAM_SERVICE, service);
	}

	public String getUser() {
		return this.getItem(ItemType.PAM_USER).getString(0);
	}

	public void setUser(String user) {
		this.setItem(ItemType.PAM_SERVICE, user);
	}

	/**
	 * Constructs a new PAM transaction with {@link Transaction#DEFAULT_SERVICE}
	 * as name for the service configuration.
	 * 
	 * @throws PAMException
	 *             if an error accrued during the call of <a
	 *             href="http://linux.die.net/man/3/pam_start">pam_start</a>.
	 * @see {@link Transaction#Transaction(String)}
	 */
	public Transaction(final String service, final String user,
			final CallbackHandler handler) throws LoginException {

		if (service == null) {
			throw new NullPointerException("service");
		} else if (service.length() == 0) {
			throw new IllegalArgumentException("service can not be empty");
		}
		if (handler == null) {
			throw new NullPointerException("handler");
		}

		this.pamConverse.conv.setCallbackHandler(handler);

		this.dispatchReturnValue(PAMLibrary.pam_start(service, user,
				this.pamConverse, this.pamHandlePointer));
	}

	public void authenticate() throws LoginException {
		this.authenticate(0);
	}

	private void authenticate(int flags) throws LoginException {
		this.dispatchReturnValue(PAMLibrary.pam_authenticate(
				this.pamHandlePointer.getPamHandle(), flags));
	}

	public void verify() throws LoginException {
		this.verify(0);
	}

	private void verify(int flags) throws LoginException {
		this.dispatchReturnValue(PAMLibrary.pam_acct_mgmt(
				this.pamHandlePointer.getPamHandle(), flags));
	}

	public void chauthtok() throws LoginException {
		this.chauthtok(0);
	}

	private void chauthtok(int flags) throws LoginException {
		this.dispatchReturnValue(PAMLibrary.pam_chauthtok(
				this.pamHandlePointer.getPamHandle(), flags));
	}

	public void close() throws LoginException {
		if (this.open) {
			this.dispatchReturnValue(PAMLibrary.pam_end(
					pamHandlePointer.getPamHandle(), this.state));
			this.open = false;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			this.close();
		} finally {
			super.finalize();
		}
	}

	private void dispatchReturnValue(final int retVal) throws LoginException {
		this.state = retVal;
		if (this.state != ReturnCode.PAM_SUCCESS.getCode()) {
			final String message = String.format(
					"PAM message: %s [Return code: %d]", PAMLibrary
							.pam_strerror(pamHandlePointer.getPamHandle(),
									this.state), this.state);
			LoginException exception;
			switch (ReturnCode.dispatch(retVal)) {
			case PAM_USER_UNKNOWN:
				exception = new AccountNotFoundException(message);
			case PAM_ACCT_EXPIRED:
				exception = new AccountExpiredException(message);
			case PAM_CRED_EXPIRED:
				exception = new CredentialExpiredException(message);
			case PAM_CRED_UNAVAIL:
				exception = new CredentialNotFoundException(message);
			case PAM_CONV_ERR:
				exception = new FailedLoginException(message);
				exception.initCause(this.pamConverse.conv.getLastException());
			default:
				exception = new FailedLoginException(message);
			}

			throw exception;
		}
	}

	private Pointer getItem(final ItemType itemType) {
		PointerByReference item = new PointerByReference();
		PAMLibrary.pam_get_item(this.pamHandlePointer.getPamHandle(),
				itemType.getCode(), item);
		return item.getValue();
	}

	private void setItem(final ItemType itemType, final String value) {
		PAMLibrary.pam_set_item(this.pamHandlePointer.getPamHandle(),
				itemType.getCode(), value);
	}

}
