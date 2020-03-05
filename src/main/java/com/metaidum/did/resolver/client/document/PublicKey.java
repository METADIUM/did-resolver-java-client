package com.metaidum.did.resolver.client.document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.metaidum.did.resolver.client.DIDResolverAPI;
import com.metaidum.did.resolver.client.document.key.EcdsaSecp256k1VerificationKey2019;
import com.metaidum.did.resolver.client.document.key.PublicKeyType;

/**
 * Public key Object in did document
 * @author mansud
 *
 */
public class PublicKey {
	private static Logger logger = LoggerFactory.getLogger(DIDResolverAPI.class);
	
	enum Type {
		EcdsaSecp256k1VerificationKey2019(new EcdsaSecp256k1VerificationKey2019())
		;
		
		private PublicKeyType<?> keyType;
		
		private Type(PublicKeyType<?> keyType) {
			this.keyType = keyType;
		}
		
		public PublicKeyType<?> getKeyType() {
			return keyType;
		}
	}
	
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("controller")
    @Expose
    private String controller;
    @SerializedName("publicKeyHex")
    @Expose
    private String publicKeyHex;
    @SerializedName("publicKeyHash")
    @Expose
    private String publicKeyHash;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Get decoded public key from publickeyhex
     * @return decoded public key
     */
    public java.security.PublicKey getPublicKey() {
    	if (publicKeyHex == null) {
    		return null;
    	}
    	
    	PublicKeyType<?> publicKeyType = null; 
    	try {
    		publicKeyType = Type.valueOf(type).getKeyType();
    	}
    	catch (IllegalArgumentException e) {
    	}

    	if (publicKeyType == null) {
    		if (logger.isWarnEnabled()) {
    			logger.warn("Unknown public key type: "+type);
    		}
    		return null;
    	}
    	
    	return publicKeyType.getPublicKey(publicKeyHex);
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getPublicKeyHex() {
        return publicKeyHex;
    }

    public void setPublicKeyHex(String publicKeyHex) {
        this.publicKeyHex = publicKeyHex;
    }

    public String getPublicKeyHash() {
        return publicKeyHash;
    }

    public void setPublicKeyHash(String publicKeyHash) {
        this.publicKeyHash = publicKeyHash;
    }
    
}
