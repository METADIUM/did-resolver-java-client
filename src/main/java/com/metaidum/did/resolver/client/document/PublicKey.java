package com.metaidum.did.resolver.client.document;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Public key Object in did document
 * @author mansud
 *
 */
public class PublicKey {
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
