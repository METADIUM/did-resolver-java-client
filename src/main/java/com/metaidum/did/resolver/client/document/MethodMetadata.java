package com.metaidum.did.resolver.client.document;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Method meta data in did document
 * @author mansud
 *
 */
public class MethodMetadata {
    @SerializedName("network")
    @Expose
    private String network;
    @SerializedName("registryAddress")
    @Expose
    private String registryAddress;

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

}
