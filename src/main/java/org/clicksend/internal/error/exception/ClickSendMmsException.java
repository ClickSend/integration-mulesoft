/**
 * The usage of this connector is governed by the terms in the LICENSE.md file.
 */
package org.clicksend.internal.error.exception;

import org.mule.runtime.extension.api.exception.ModuleException;

public class ClickSendMmsException extends ModuleException {

	private static final long serialVersionUID = 1L;

	public ClickSendMmsException(String errorMessage) {  
	    super(errorMessage, null);  
	    }  
}
