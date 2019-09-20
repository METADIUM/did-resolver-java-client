package com.metaidum.did.resolver.client.document;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Service of did document
 * @author mansud
 *
 */
public class Service {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("publicKey")
    @Expose
    private String publicKey;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("serviceEndpoint")
    @Expose
    private String serviceEndpoint;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServiceEndpoint() {
        return serviceEndpoint;
    }

    public void setServiceEndpoint(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }
}
