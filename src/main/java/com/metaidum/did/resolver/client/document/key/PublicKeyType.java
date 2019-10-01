package com.metaidum.did.resolver.client.document.key;

import java.security.PublicKey;

public interface PublicKeyType<T extends PublicKey> {
	public T getPublicKey(String publicKeyHex);
}
