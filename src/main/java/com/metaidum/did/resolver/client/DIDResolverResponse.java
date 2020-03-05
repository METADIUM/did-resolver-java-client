package com.metaidum.did.resolver.client;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.metaidum.did.resolver.client.document.DidDocument;
import com.metaidum.did.resolver.client.document.MethodMetadata;
import com.metaidum.did.resolver.client.document.ResolverMetadata;

/**
 * DID resolver response data
 * <p>
 * 
 * @author ybjeon
 *
 */
public class DIDResolverResponse {
    @SerializedName("redirect")
    @Expose
    private Object redirect;
    @SerializedName("didDocument")
    @Expose
    private DidDocument didDocument;
    @SerializedName("resolverMetadata")
    @Expose
    private ResolverMetadata resolverMetadata;
    @SerializedName("methodMetadata")
    @Expose
    private MethodMetadata methodMetadata;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private String message;


    public Object getRedirect() {
        return redirect;
    }

    public void setRedirect(Object redirect) {
        this.redirect = redirect;
    }

    public DidDocument getDidDocument() {
        return didDocument;
    }

    public void setDidDocument(DidDocument didDocument) {
        this.didDocument = didDocument;
    }

    public ResolverMetadata getResolverMetadata() {
        return resolverMetadata;
    }

    public void setResolverMetadata(ResolverMetadata resolverMetadata) {
        this.resolverMetadata = resolverMetadata;
    }

    public MethodMetadata getMethodMetadata() {
        return methodMetadata;
    }

    public void setMethodMetadata(MethodMetadata methodMetadata) {
        this.methodMetadata = methodMetadata;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
