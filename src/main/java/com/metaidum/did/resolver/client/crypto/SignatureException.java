package com.metaidum.did.resolver.client.crypto;

/**
 * Signature Exception
 * @author mansud
 *
 */
public class SignatureException extends Exception {
	private static final long serialVersionUID = 2605438053670691386L;

	public SignatureException() {
		super();
	}

	public SignatureException(String message, Throwable cause) {
		super(message, cause);
	}

	public SignatureException(String message) {
		super(message);
	}

}
