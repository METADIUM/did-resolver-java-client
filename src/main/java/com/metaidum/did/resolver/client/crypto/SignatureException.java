package com.metaidum.did.resolver.client.crypto;

/**
 * Signature Exception
 * @author mansud
 *
 */
public class SignatureException extends Exception {

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
