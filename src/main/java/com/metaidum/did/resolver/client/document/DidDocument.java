package com.metaidum.did.resolver.client.document;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.metaidum.did.resolver.client.crypto.Signature;
import com.metaidum.did.resolver.client.util.Hex;

/**
 * Did document class
 * @author mansud
 *
 */
public class DidDocument {
    @SerializedName("@context")
    @Expose
    private String context;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("publicKey")
    @Expose
    private List<PublicKey> publicKey = null;
    @SerializedName("authentication")
    @Expose
    private List<String> authentication = null;
    @SerializedName("service")
    @Expose
    private List<Service> service = null;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PublicKey> getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(List<PublicKey> publicKey) {
        this.publicKey = publicKey;
    }

    public List<String> getAuthentication() {
        return authentication;
    }

    public void setAuthentication(List<String> authentication) {
        this.authentication = authentication;
    }

    public List<Service> getService() {
        return service;
    }

    public void setService(List<Service> service) {
        this.service = service;
    }
    
    /**
     * Find public key of service
     * @param serviceId to find
     * @return find result
     */
    public boolean hasServicePublicKey(String serviceId) {
        List<PublicKey> publicKeyVoList = getPublicKey();
        for (PublicKey key : publicKeyVoList) {
            if (key.getId().contains(serviceId)) {
            	return true;
            }
        }
        
        return false;
    }
    
    /**
     * Get public key with key id
     * @param keyId
     * @return public key object
     */
    public PublicKey getPublicKey(String keyId) {
        List<PublicKey> publicKeyVoList = getPublicKey();
        for (PublicKey key : publicKeyVoList) {
            if (key.getId().equals(keyId)) {
            	return key;
            }
        }
        
        return null;
    }
    
    /**
     * Find public key hash with address
     * @param address
     * @return find result
     */
    public boolean hasPublicKeyWithAddress(String address) {
    	List<PublicKey> publicKeyVoList = getPublicKey();
    	if (address.startsWith("0x")) {
    		address = address.substring(2);
    	}
        for (PublicKey key : publicKeyVoList) {
            if (key.getId().endsWith(address)) {
            	// check publicKeyHash(is address)
            	if (key.getPublicKeyHash() != null && key.getPublicKeyHash().equals(address)) {
            		return true;
            	}
            	
            	// check publicKeyHex(is encoded public key)
            	if (key.getPublicKeyHex() != null) {
            		// non-compressed public key
            		byte[] publicKeyBytes = Hex.hexStringToByteArray(key.getPublicKeyHex());
                    BigInteger publicKeyValue = new BigInteger(1, Arrays.copyOfRange(publicKeyBytes, 1, publicKeyBytes.length));
                    if (Signature.toAddress(publicKeyValue).equals(address)) {
                    	return true;
                    }

            	}
            }
        }
        return false;
    }
    
    /**
     * Get public keys of authentication
     * @return public key list
     */
    public List<PublicKey> getPublicKeyOfAuthentication() {
    	List<PublicKey> ret = new ArrayList<>();
    	if (getAuthentication() != null) {
	    	for (String kid : getAuthentication()) {
	    		PublicKey publicKey = getPublicKey(kid);
	    		if (publicKey != null) {
	    			ret.add(publicKey);
	    		}
	    	}
    	}
    	return ret;
    }
    
    /**
     * verify signature and check if address is owned by did
     * @param message used when signing
     * @param signature signature
     * @return if verified and is address of did owner, return true 
     * @throws SignatureException
     */
    public boolean hasRecoverAddressFromSignature(byte[] message, String signature) throws SignatureException {
    	return hasPublicKeyWithAddress(Signature.addressFromSignature(message, signature));
    }
}
