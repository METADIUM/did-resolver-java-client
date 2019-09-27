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
	private final static String TESTNET_RESOLVER_URL = "https://testnetresolver.metadium.com/1.0/";
	private final static String MAINNET_RESOLVER_URL = "https://resolver.metadium.com/1.0/";
	
	private static boolean bDebug = false;
	
	public static void setDebug(boolean debug) {
		bDebug = debug;
	}
	
    private final OkHttpClient okHttpClient;
    
    private static DIDResolverAPI instance;
    
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
     * @return response
     * @throws IOException
     */
    public DIDResolverResponse requestDocument(String did) throws IOException {
    	boolean testnet = did.startsWith("did:meta:testnet");
    	
    	Request request = new Request.Builder()
    			.url((testnet ? TESTNET_RESOLVER_URL : MAINNET_RESOLVER_URL)+"identifiers/"+did)
    			.build();
    	
		Response response = okHttpClient.newCall(request).execute();
		return new Gson().fromJson(response.body().charStream(), DIDResolverResponse.class);
    }
    
    /**
     * Request did document
     * @param did to search. did:meta:(testnet|mainnet):{meta_id}
     * @return Did document. if not exists did or occur io error, return null
     */
    public DidDocument getDocument(String did) {
    	try {
    		return requestDocument(did).getDidDocument();
    	}
    	catch (IOException e) {
    		// 통신에러
    		return null;
    	}
    }
}
