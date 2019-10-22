package com.metaidum.did.resolver.client;

import java.io.IOException;

import com.google.gson.Gson;
import com.metaidum.did.resolver.client.document.DidDocument;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


/**
 * DID Resolver API
 * @author mansud
 *
 */
public class DIDResolverAPI {
	private final static String DEFAULT_TESTNET_RESOLVER_URL = "https://testnetresolver.metadium.com/1.0/";
	private final static String MADEFAULT_INNET_RESOLVER_URL = "https://resolver.metadium.com/1.0/";
	
	private static boolean bDebug = false;
	
	public static void setDebug(boolean debug) {
		bDebug = debug;
	}
	
    private final OkHttpClient okHttpClient;
    
    private static DIDResolverAPI instance;
    
    private String resovlerUrl;
    
    /**
     * Get singleton instance
     * @return
     */
    public static synchronized DIDResolverAPI getInstance() {
    	if (instance == null) {
    		instance = new DIDResolverAPI();
    	}
    	return instance;
    }
    
    /**
     * Set resovler url to request
     * @param resovlerUrl
     */
    public void setResolverUrl(String resovlerUrl) {
    	this.resovlerUrl = resovlerUrl;
    }

    /**
     * Create did resolver API
     */
    private DIDResolverAPI() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (bDebug) {
	        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
	        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
	        httpClient.interceptors().add(logging);
        }

        okHttpClient = httpClient.build();
    }
    
    /**
     * Request did document.
     * reponse raw data
     * @param did did to search
     * @param noCache if true, did resolver does not cache did document
     * @return response
     * @throws IOException
     */
    public DIDResolverResponse requestDocument(String did, boolean noCache) throws IOException {
    	String url = resovlerUrl != null ? resovlerUrl : (did.startsWith("did:meta:testnet") ? DEFAULT_TESTNET_RESOLVER_URL : MADEFAULT_INNET_RESOLVER_URL);
    	
    	Request request = new Request.Builder()
    			.url(url+"identifiers/"+did)
    			.addHeader("no-cache", "true")
    			.build();
    	
		Response response = okHttpClient.newCall(request).execute();
		return new Gson().fromJson(response.body().charStream(), DIDResolverResponse.class);
    }
    
    /**
     * Request did document
     * @param did to search. did:meta:(testnet|mainnet):{meta_id}
     * @param noCache if true, did resolver does not cache did document
     * @return Did document. if not exists did or occur io error, return null
     */
    public DidDocument getDocument(String did, boolean noCache) {
    	try {
    		return requestDocument(did, noCache).getDidDocument();
    	}
    	catch (IOException e) {
    		// 통신에러
    		return null;
    	}
    }
    
    
    /**
     * Request did document. no-cache is false
     * @param did to search. did:meta:(testnet|mainnet):{meta_id}
     * @return Did document. if not exists did or occur io error, return null
     */
    public DidDocument getDocument(String did) {
    	return getDocument(did, false);
    }
}
