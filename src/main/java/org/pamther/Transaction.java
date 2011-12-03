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

import org.pamther.internal.nativelib.ItemType;
import org.pamther.internal.nativelib.PamLibrary;
import org.pamther.internal.nativelib.ReturnCode;
import org.pamther.internal.nativelib.types.Conversation;
import org.pamther.internal.nativelib.types.HandleByReference;
import org.pamther.internal.nativelib.types.NativeCallbackHandlerImp;

/**
 * The {@link Transaction} encapsulates a PAM handle returned by pam_start and
 * is the first place for executing common PAM functions like <a
 * href="http://linux.die.net/man/3/pam_authenticate">pam_authenticate</a> or <a
 * href="http://linux.die.net/man/3/pam_acct_mgmt">pam_acct_mgmt</a>.
 * 
 * @author <a href="https://github.com/RCBiczok">Rudolf Biczok</a>
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

	// TODO REMOV THIS !!!!
	private String password;

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
		return this.getStringItem(ItemType.PAM_SERVICE);
	}

	/**
	 * Sets the name of the used service configuration.
	 * 
	 * @param service
	 *            the name of the service configuration.
	 * 
	 * @see <a href="http://linux.die.net/man/8/pam.d">PAM configuration
	 *      files</a>
	 */
	public void setService(final String service) {
		this.setStringItem(ItemType.PAM_SERVICE, service);
	}

	// TODO REMOVE THIS !!!!!
	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getUser() {
		return this.getStringItem(ItemType.PAM_USER);
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
	public Transaction() throws PAMException {
		this(DEFAULT_SERVICE);
	}

	public Transaction(String serviceName) throws PAMException {
		this(serviceName, null, null);
	}

	public Transaction(final String service, final String user,
			final CallbackHandler handler) throws PAMException {

		if (service == null) {
			throw new NullPointerException("service");
		} else if (service.length() == 0) {
			throw new IllegalArgumentException("service can not be empty");
		}

		if (handler == null && this.pamConverse.conv.getCallbackHandler() == null) {
			this.pamConverse.conv.setCallbackHandler(new DefaultCallbackHandler(this));
		} else {
			this.pamConverse.conv.setCallbackHandler(handler);
		}

		this.dispatchReturnValue(PamLibrary.INSTANCE.pam_start(service, user,
				this.pamConverse, this.pamHandlePointer));
	}

	public void authenticate() throws PAMException {
		this.authenticate(0);
	}

	public void authenticate(int flags) throws PAMException {
		this.dispatchReturnValue(PamLibrary.INSTANCE.pam_authenticate(
				this.pamHandlePointer.getPamHandle(), flags));
	}

	public void verify() throws PAMException {
		this.verify(0);
	}

	private void verify(int flags) throws PAMException {
		this.dispatchReturnValue(PamLibrary.INSTANCE.pam_acct_mgmt(
				this.pamHandlePointer.getPamHandle(), flags));
	}

	public void chauthtok() throws PAMException {
		this.chauthtok(0);
	}

	private void chauthtok(int flags) throws PAMException {
		this.dispatchReturnValue(PamLibrary.INSTANCE.pam_chauthtok(
				this.pamHandlePointer.getPamHandle(), flags));
	}

	public void close() throws PAMException {
		if (this.open) {
			this.dispatchReturnValue(PamLibrary.INSTANCE.pam_end(
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

	private void dispatchReturnValue(final int retVal) throws PAMException {
		this.state = retVal;
		if (this.state != ReturnCode.PAM_SUCCESS.getCode()) {
			if (this.state == ReturnCode.PAM_USER_UNKNOWN.getCode()) {
				throw new PAMException("Failed to find user" + this.getUser()
						+ " with error: " + this.state);
			} else {
				throw new PAMException(
						"Failed to authenticate for an unknown error: "
								+ PamLibrary.INSTANCE.pam_strerror(
										pamHandlePointer.getPamHandle(),
										this.state));
			}
		}
	}

	private String getStringItem(final ItemType itemType) {
		String[] item = new String[1];
		PamLibrary.INSTANCE.pam_get_item(this.pamHandlePointer.getPamHandle(),
				itemType.getCode(), item);
		return item[0];
	}

	private void setStringItem(final ItemType itemType, final String value) {
		PamLibrary.INSTANCE.pam_set_item(this.pamHandlePointer.getPamHandle(),
				itemType.getCode(), value);
	}

}
