package com.metaidum.did.resolver.client.document;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
            if (key.getId().endsWith(address) && key.getPublicKeyHash() != null && key.getPublicKeyHash().equals(address)) {
            	return true;
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
}
