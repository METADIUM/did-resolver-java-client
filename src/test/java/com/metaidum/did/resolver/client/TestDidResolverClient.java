package com.metaidum.did.resolver.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.junit.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import com.metaidum.did.resolver.client.document.DidDocument;
import com.metaidum.did.resolver.client.document.PublicKey;

public class TestDidResolverClient {
	private static final String TEST_DID = "did:meta:testnet:000000000000000000000000000000000000000000000000000000000000054b";
	private static final String TEST_PRIVATE_OF_DID = "86975dca6a36062768cf4b648b5b3f712caa2d1d61fa42520624a8e574788822";
	
	static {
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");
	}
	
	@Test
	public void requestResolver() {
		DidDocument document = DIDResolverAPI.getInstance().getDocument(TEST_DID);
		assertNotNull(document);
		
		// invalid did
		document = DIDResolverAPI.getInstance().getDocument("did:meta:testnet000000000000000000000000000000000000000000000000000000000000054b");
		assertNull(document);
		document = DIDResolverAPI.getInstance().getDocument("did:meta:testnet:00000000000000000000000000000000000000000000000000000000000054b");
		assertNull(document);
		document = DIDResolverAPI.getInstance().getDocument("did:met:000000000000000000000000000000000000000000000000000000000000054b");
		assertNull(document);
		document = DIDResolverAPI.getInstance().getDocument("000000000000000000000000000000000000000000000000000000000000054b");
		assertNull(document);
		document = DIDResolverAPI.getInstance().getDocument("did:meta:testnet:0x000000000000000000000000000000000000000000000000000000000000054b");
		assertNull(document);

		// not found
		document = DIDResolverAPI.getInstance().getDocument("did:meta:testnet:000000000000000000000000000000000000000000000000000000000001154b");
		assertNull(document);
	}
	
	@Test
	public void didDocument() {
		DidDocument document = DIDResolverAPI.getInstance().getDocument(TEST_DID);
		assertNotNull(document);

		// require
		assertNotNull(document.getContext());
		assertEquals(TEST_DID, document.getId());
		assertTrue(document.getPublicKey().size() > 0);
		
		// public key
		assertNotNull(document.getPublicKey("did:meta:testnet:000000000000000000000000000000000000000000000000000000000000054b#MetaManagementKey#cfd31afff25b2260ea15ef59f2d5d7dfe8c13511"));
		assertNull(document.getPublicKey("did:meta:testnet:000000000000000000000000000000000000000000000000000000000000054b#MetaManagementKey#0xcfd31afff25b2260ea15ef59f2d5d7dfe8c13511"));
		assertNull(document.getPublicKey("did:meta:testnet:000000000000000000000000000000000000000000000000000000000000054b#MetaManagementKey#11223344f25b2260ea15ef59f2d5d7dfe8c13511"));
		assertTrue(document.hasServicePublicKey("a0ad73f907ab3ce870db6671afb4a417a3315f03e5eca6a2b44cb1fac08d5d60"));
		assertFalse(document.hasServicePublicKey("1111111111111111111111111111111113315f03e5eca6a2b44cb1fac08d5d60"));
		assertTrue(document.hasPublicKeyWithAddress("0eba7b2b80419023e92a23716f87341c62c5474f"));
		assertTrue(document.hasPublicKeyWithAddress("0x0eba7b2b80419023e92a23716f87341c62c5474f"));
		assertFalse(document.hasPublicKeyWithAddress("1111111111119023e92a23716f87341c62c5474f"));
		for (String kid : document.getAuthentication()) {
			assertNotNull(document.getPublicKey(kid));
		}
		assertEquals(2, document.getPublicKeyOfAuthentication().size());
	}
	
	@Test
	public void pubicKey() {
		DidDocument document = DIDResolverAPI.getInstance().getDocument(TEST_DID);
		assertNotNull(document);

		PublicKey pk = document.getPublicKey("did:meta:testnet:000000000000000000000000000000000000000000000000000000000000054b#MetaManagementKey#cfd31afff25b2260ea15ef59f2d5d7dfe8c13511");
		java.security.PublicKey publicKey = pk.getPublicKey();
		assertNotNull(publicKey);
		assertTrue(publicKey instanceof BCECPublicKey);
	}
	
	@Test
	public void changeResolverUrl() {
		DIDResolverAPI.getInstance().setResolverUrl("https://naver.com");
		DidDocument document = DIDResolverAPI.getInstance().getDocument(TEST_DID);
		assertNull(document);
		DIDResolverAPI.getInstance().setResolverUrl(null);
		document = DIDResolverAPI.getInstance().getDocument(TEST_DID);
		assertNotNull(document);
	}
	
	@Test
	public void signatureTest() throws SignatureException {
		// signing
		byte[] message = "test message".getBytes(StandardCharsets.UTF_8);
		BigInteger privateKey = Numeric.toBigIntNoPrefix(TEST_PRIVATE_OF_DID);
		Sign.SignatureData signatureData = Sign.signMessage(message, ECKeyPair.create(privateKey));
		
		ByteBuffer buffer = ByteBuffer.allocate(65);
        buffer.put(signatureData.getR());
        buffer.put(signatureData.getS());
        buffer.put(signatureData.getV());
        String signature = Numeric.toHexString(buffer.array());
		
		DidDocument document = DIDResolverAPI.getInstance().getDocument(TEST_DID);
		assertTrue(document.hasRecoverAddressFromSignature(message, signature));
		
	}
}
