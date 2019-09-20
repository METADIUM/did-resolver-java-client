package com.metaidum.did.resolver.client.document;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Resolver meta data of did document
 * @author mansud
 *
 */
public class ResolverMetadata {
    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("driver")
    @Expose
    private String driver;
    @SerializedName("retrieved")
    @Expose
    private String retrieved;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("cached")
    @Expose
    private Boolean cached;
    @SerializedName("ttl")
    @Expose
    private String ttl;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getRetrieved() {
        return retrieved;
    }

    public void setRetrieved(String retrieved) {
        this.retrieved = retrieved;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Boolean getCached() {
        return cached;
    }

    public void setCached(Boolean cached) {
        this.cached = cached;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }
}
