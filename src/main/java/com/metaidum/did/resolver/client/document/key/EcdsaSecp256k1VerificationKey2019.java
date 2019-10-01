package com.metaidum.did.resolver.client.document.key;

import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.ECPointUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

import com.metaidum.did.resolver.client.util.Hex;

public class EcdsaSecp256k1VerificationKey2019 implements PublicKeyType<BCECPublicKey> {
	@Override
	public BCECPublicKey getPublicKey(String publicKeyHex) {
        ECNamedCurveParameterSpec params = ECNamedCurveTable.getParameterSpec("secp256k1");
        EllipticCurve ellipticCurve = EC5Util.convertCurve(params.getCurve(), params.getSeed());

        ECPoint ecPoint = ECPointUtil.decodePoint(ellipticCurve, Hex.hexStringToByteArray(publicKeyHex));
        ECPublicKeySpec publicKeySpec = new ECPublicKeySpec(ecPoint, EC5Util.convertSpec(ellipticCurve, params));
        return new BCECPublicKey("EC", publicKeySpec, BouncyCastleProvider.CONFIGURATION);
    }

}
