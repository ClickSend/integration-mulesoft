package org.clicksend.internal;

public class ClickSendSmsException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ClickSendSmsException(String errorMessage) {
		super(errorMessage);
	}
}
