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
import org.pamther.internal.nativelib.types.PermanentMemory;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * The {@link Transaction} encapsulates a PAM handle returned by pam_start and
 * is the first place for executing common PAM functions like <a
 * href="http://linux.die.net/man/3/pam_authenticate">pam_authenticate()</a> or
 * <a href="http://linux.die.net/man/3/pam_acct_mgmt">pam_acct_mgmt()</a>.
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

	/**
	 * Holds information about the availability of this {@link Transaction}
	 * instance.
	 */
	private boolean open = true;

	/**
	 * Holds the {@link Conversation} form the communication between module and
	 * application.
	 */
	private final Conversation pamConverse = new Conversation(
			new PermanentMemory(Conversation.SIZE),
			new NativeCallbackHandlerImp());

	/**
	 * Holds the internal PAM handle.
	 */
	private HandleByReference pamHandlePointer = new HandleByReference();

	/**
	 * Returns the name of the service configuration located in the
	 * <code>/etc/pam.d</code> folder.
	 * 
	 * @return the name of the service configuration located in the
	 *         <code>/etc/pam.d</code> folder.
	 * @throws LoginException
	 *             if an error accrued during the execution of <a
	 *             href="http://linux.die.net/man/3/pam_get_item"
	 *             >pam_get_item()</a>.
	 * 
	 * @see <a href="http://linux.die.net/man/8/pam.d">PAM configuration
	 *      files</a>
	 */
	public String getService() throws LoginException {
		return this.getItem(ItemType.PAM_SERVICE).getString(0);
	}

	/**
	 * Sets the name of the used service configuration.
	 * 
	 * @param service
	 *            the name of the service configuration
	 * @throws LoginException
	 *             if an error accrued during the execution of <a
	 *             href="http://linux.die.net/man/3/pam_set_item"
	 *             >pam_set_item()</a>.
	 * 
	 * @see <a href="http://linux.die.net/man/8/pam.d">PAM configuration
	 *      files</a>
	 */
	public void setService(final String service) throws LoginException {
		this.setItem(ItemType.PAM_SERVICE, service);
	}

	/**
	 * Returns the user name item.
	 * 
	 * @return the user name item.
	 * @throws LoginException
	 *             if an error accrued during the execution of <a
	 *             href="http://linux.die.net/man/3/pam_get_item"
	 *             >pam_get_item()</a>.
	 * @see <a href="http://linux.die.net/man/3/pam_get_item">pam_get_item()</a>
	 */
	public String getUser() throws LoginException {
		return this.getItem(ItemType.PAM_USER).getString(0);
	}

	/**
	 * Sets the user name item.
	 * 
	 * @param user
	 *            the name of the user.
	 * @throws LoginException
	 *             if an error accrued during the execution of <a
	 *             href="http://linux.die.net/man/3/pam_set_item"
	 *             >pam_set_item()</a>.
	 * @see <a href="http://linux.die.net/man/3/pam_set_item">pam_set_item()</a>
	 */
	public void setUser(final String user) throws LoginException {
		this.setItem(ItemType.PAM_SERVICE, user);
	}

	/**
	 * Constructs a new PAM transaction with {@link Transaction#DEFAULT_SERVICE}
	 * as name for the service configuration.
	 * 
	 * @param service
	 *            the name of the user service configuration.
	 * @param user
	 *            the name of the user.
	 * @param handler
	 *            the used {@link CallbackHandler} instance form handling
	 *            {@link javax.security.auth.callback.Callback JAAS Callbacks}
	 * @throws LoginException
	 *             if an error accrued during the execution of <a
	 *             href="http://linux.die.net/man/3/pam_start">pam_start()</a>
	 *             or callback routines.
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

	/**
	 * Authenticates the user.
	 * 
	 * @throws LoginException
	 *             if an error accrued during the execution of <a
	 *             href="http://linux.die.net/man/3/pam_authenticate"
	 *             >pam_authenticate()</a> or callback routines.
	 * @see <a
	 *      href="http://linux.die.net/man/3/pam_authenticate">pam_authenticate()</a>
	 */
	public void authenticate() throws LoginException {
		this.authenticate(0);
	}

	/**
	 * Authenticates the user.
	 * 
	 * @param flags
	 *            for passing additional options the routine.
	 * @throws LoginException
	 *             if an error accrued during the execution of <a
	 *             href="http://linux.die.net/man/3/pam_authenticate"
	 *             >pam_authenticate()</a> or callback routines.
	 * @see <a
	 *      href="http://linux.die.net/man/3/pam_authenticate">pam_authenticate()</a>
	 */
	private void authenticate(final int flags) throws LoginException {
		this.dispatchReturnValue(PAMLibrary.pam_authenticate(
				this.pamHandlePointer.getHandle(), flags));
	}

	/**
	 * Validates the users account.
	 * 
	 * @throws LoginException
	 *             if an error accrued during the execution of <a
	 *             href="http://linux.die.net/man/3/pam_acct_mgmt"
	 *             >pam_acct_mgmt()</a> or callback routines.
	 * @see <a
	 *      href="http://linux.die.net/man/3/pam_acct_mgmt">pam_acct_mgmt()</a>
	 */
	public void validate() throws LoginException {
		this.validate(0);
	}

	/**
	 * Validates the users account.
	 * 
	 * @param flags
	 *            for passing additional options the routine.
	 * @throws LoginException
	 *             if an error accrued during the execution of <a
	 *             href="http://linux.die.net/man/3/pam_acct_mgmt"
	 *             >pam_acct_mgmt()</a> or callback routines.
	 * @see <a
	 *      href="http://linux.die.net/man/3/pam_acct_mgmt">pam_acct_mgmt()</a>
	 */
	private void validate(final int flags) throws LoginException {
		this.dispatchReturnValue(PAMLibrary.pam_acct_mgmt(
				this.pamHandlePointer.getHandle(), flags));
	}

	/**
	 * Changes the user's authentication token.
	 * 
	 * @throws LoginException
	 *             if an error accrued during the execution of <a
	 *             href="http://linux.die.net/man/3/pam_chauthtok"
	 *             >pam_chauthtok()</a> or callback routines.
	 * @see <a
	 *      href="http://linux.die.net/man/3/pam_chauthtok">pam_chauthtok()</a>
	 */
	public void changeAuthTok() throws LoginException {
		this.changeAuthTok(0);
	}

	/**
	 * Changes the user's authentication token.
	 * 
	 * @param flags
	 *            for passing additional options the routine.
	 * @throws LoginException
	 *             if an error accrued during the execution of <a
	 *             href="http://linux.die.net/man/3/pam_chauthtok"
	 *             >pam_chauthtok()</a> or callback routines.
	 * @see <a
	 *      href="http://linux.die.net/man/3/pam_chauthtok">pam_chauthtok()</a>
	 */
	private void changeAuthTok(final int flags) throws LoginException {
		this.dispatchReturnValue(PAMLibrary.pam_chauthtok(
				this.pamHandlePointer.getHandle(), flags));
	}

	/**
	 * Closes this transaction and cleans up native resources.
	 * 
	 * @throws LoginException
	 *             if an error accrued during the execution of <a
	 *             href="http://linux.die.net/man/3/pam_end" >pam_end()</a> or
	 *             callback routines.
	 * @see <a href="http://linux.die.net/man/3/pam_end">pam_end()</a>
	 */
	public void close() throws LoginException {
		if (this.open) {
			this.dispatchReturnValue(PAMLibrary.pam_end(
					pamHandlePointer.getHandle(), this.state));
			this.open = false;
		}
	}

	/**
	 * Tries to close the {@link Transaction} before it gets garbage collected.
	 * 
	 * @throws Throwable
	 *             if closure was not successful.
	 */
	@Override
	protected void finalize() throws Throwable {
		try {
			this.close();
		} finally {
			super.finalize();
		}
	}

	/**
	 * Returns a transaction item.
	 * 
	 * @param itemType
	 *            identifies the item we want to fetch from the transaction.
	 * @return the transaction item corresponding to the specified
	 *         <code>itemType</code>.
	 * @throws LoginException
	 *             if an error accrued during the execution of <a
	 *             href="http://linux.die.net/man/3/pam_get_item"
	 *             >pam_get_item()</a> or callback routines.
	 */
	private Pointer getItem(final ItemType itemType) throws LoginException {
		PointerByReference item = new PointerByReference();
		this.dispatchReturnValue(PAMLibrary.pam_get_item(
				this.pamHandlePointer.getHandle(), itemType.getCode(), item));
		return item.getValue();
	}

	/**
	 * Overwrites a transaction item with a string.
	 * 
	 * @param itemType
	 *            identifies the item we want to fetch from the transaction.
	 * @param value
	 *            the new value for the item.
	 * @throws LoginException
	 *             if an error accrued during the execution of <a
	 *             href="http://linux.die.net/man/3/pam_set_item"
	 *             >pam_set_item()</a> or callback routines.
	 */
	private void setItem(final ItemType itemType, final String value)
			throws LoginException {
		this.dispatchReturnValue(PAMLibrary.pam_set_item(
				this.pamHandlePointer.getHandle(), itemType.getCode(), value));
	}

	/**
	 * Dispatches the return value of an executed PAM function in
	 * {@link PAMLibrary}. It either does nothing or throws an exception
	 * depending on the given return value. A {@link Transaction} instance will
	 * always save the last return code.
	 * 
	 * @param retVal
	 *            the return value got from a native PAM function.
	 * @throws LoginException
	 *             whenever the return value is not equals to
	 *             {@link ReturnCode#PAM_SUCCESS}.
	 * @see <a href="http://linux.die.net/man/3/pam_strerror">pam_strerror()</a>
	 *      used to fetch a string representation which describes the error.
	 */
	private void dispatchReturnValue(final int retVal) throws LoginException {
		this.state = retVal;
		if (this.state != ReturnCode.PAM_SUCCESS.getCode()) {
			final String message = String.format(
					"PAM message: %s [Return code: %d]", PAMLibrary
							.pam_strerror(pamHandlePointer.getHandle(),
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

}
