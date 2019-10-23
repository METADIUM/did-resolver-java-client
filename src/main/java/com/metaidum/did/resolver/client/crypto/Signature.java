package com.metaidum.did.resolver.client.crypto;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Arrays;

import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9IntegerConverter;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.sec.SecP256K1Curve;

import com.metaidum.did.resolver.client.util.Hex;

/**
 * ec-recover signature of Secp256k1.
 * copied Sign class of web3j.core
 * @author mansud
 *
 */
public class Signature {
    static final X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1");
    static final ECDomainParameters CURVE = new ECDomainParameters(CURVE_PARAMS.getCurve(), CURVE_PARAMS.getG(), CURVE_PARAMS.getN(), CURVE_PARAMS.getH());
    static final BigInteger HALF_CURVE_ORDER = CURVE_PARAMS.getN().shiftRight(1);

    private static void verifyPrecondition(boolean condition, String message) throws SignatureException {
    	if (!condition) {
    		throw new SignatureException(message);
    	}
    }

    /**
     * Compute public key from signature of secp256k1 
     * @param message to sign
     * @param signature secp256k1 signature. hex string of V+R+S
     * @return ec public key
     * @throws SignatureException
     */
	public static BigInteger publicKeyFromSignature(byte[] message, String signature) throws SignatureException {
		if (signature.startsWith("0x")) {
			signature = signature.substring(2);
		}
		byte[] vrs = Hex.hexStringToByteArray(signature);
		verifyPrecondition(vrs.length == 65, "signature must be 65 bytes");
		
		byte[] messageHash = sha3(message);
        byte[] r = Arrays.copyOfRange(vrs, 0, 32);
        byte[] s = Arrays.copyOfRange(vrs, 32, 64);
        byte v = vrs[64];
        
        int header = v & 0xFF;
        verifyPrecondition(header >= 27 && header <= 34, "Header byte out of range: " + header);
        int recId = header - 27;

        BigInteger key = recoverFromSignature(recId, new BigInteger(1, r), new BigInteger(1, s), messageHash);
        verifyPrecondition(key != null, "Could not recover public key from signature");
        return key;
	}
	
	/**
	 * Compute address of public key from signature of secp256k1
	 * @param message to sign
	 * @param signature secp256k1 signature. hex string of V+R+S
	 * @return
	 * @throws SignatureException
	 */
	public static String addressFromSignature(byte[] message, String signature) throws SignatureException {
		return toAddress(publicKeyFromSignature(message, signature));
	}
	
	private static byte[] sha3(byte[] message) {
        Keccak.DigestKeccak kecc = new Keccak.Digest256();
        kecc.update(message, 0, message.length);
		return kecc.digest();
	}
	
	public static String toAddress(BigInteger publicKey) {
		byte[] result = new byte[64];
		byte[] k = Hex.hexStringToByteArray(publicKey.toString(16));
        System.arraycopy(k, 0, result, result.length-k.length, k.length);

        String resultHash = Hex.toHexString(sha3(result));
        return resultHash.substring(resultHash.length()-40);
	}
	
    /**
     * Given the components of a signature and a selector value, recover and return the public key
     * that generated the signature according to the algorithm in SEC1v2 section 4.1.6.
     *
     * <p>The recId is an index from 0 to 3 which indicates which of the 4 possible keys is the
     * correct one. Because the key recovery operation yields multiple potential keys, the correct
     * key must either be stored alongside the signature, or you must be willing to try each recId
     * in turn until you find one that outputs the key you are expecting.
     *
     * <p>If this method returns null it means recovery was not possible and recId should be
     * iterated.
     *
     * <p>Given the above two points, a correct usage of this method is inside a for loop from 0 to
     * 3, and if the output is null OR a key that is not the one you expect, you try again with the
     * next recId.
     *
     * @param recId Which possible key to recover.
     * @param sig the R and S components of the signature, wrapped.
     * @param message Hash of the data that was signed.
     * @return An ECKey containing only the public part, or null if recovery wasn't possible.
     */
    public static BigInteger recoverFromSignature(int recId, BigInteger r, BigInteger s, byte[] message) throws SignatureException {
        verifyPrecondition(recId >= 0, "recId must be positive");
        verifyPrecondition(r.signum() >= 0, "r must be positive");
        verifyPrecondition(s.signum() >= 0, "s must be positive");
        verifyPrecondition(message != null, "message cannot be null");

        // 1.0 For j from 0 to h   (h == recId here and the loop is outside this function)
        //   1.1 Let x = r + jn
        BigInteger n = CURVE.getN();  // Curve order.
        BigInteger i = BigInteger.valueOf((long) recId / 2);
        BigInteger x = r.add(i.multiply(n));
        BigInteger prime = SecP256K1Curve.q;
        if (x.compareTo(prime) >= 0) {
            // Cannot have point co-ordinates larger than this as everything takes place modulo Q.
            return null;
        }
        // Compressed keys require you to know an extra bit of data about the y-coord as there are
        // two possibilities. So it's encoded in the recId.
        ECPoint R = decompressKey(x, (recId & 1) == 1);
        //   1.4. If nR != point at infinity, then do another iteration of Step 1 (callers
        //        responsibility).
        if (!R.multiply(n).isInfinity()) {
            return null;
        }
        //   1.5. Compute e from M using Steps 2 and 3 of ECDSA signature verification.
        BigInteger e = new BigInteger(1, message);
        //   1.6. For k from 1 to 2 do the following.   (loop is outside this function via
        //        iterating recId)
        //   1.6.1. Compute a candidate public key as:
        //               Q = mi(r) * (sR - eG)
        //
        // Where mi(x) is the modular multiplicative inverse. We transform this into the following:
        //               Q = (mi(r) * s ** R) + (mi(r) * -e ** G)
        // Where -e is the modular additive inverse of e, that is z such that z + e = 0 (mod n).
        // In the above equation ** is point multiplication and + is point addition (the EC group
        // operator).
        //
        // We can find the additive inverse by subtracting e from zero then taking the mod. For
        // example the additive inverse of 3 modulo 11 is 8 because 3 + 8 mod 11 = 0, and
        // -3 mod 11 = 8.
        BigInteger eInv = BigInteger.ZERO.subtract(e).mod(n);
        BigInteger rInv = r.modInverse(n);
        BigInteger srInv = rInv.multiply(s).mod(n);
        BigInteger eInvrInv = rInv.multiply(eInv).mod(n);
        ECPoint q = ECAlgorithms.sumOfTwoMultiplies(CURVE.getG(), eInvrInv, R, srInv);

        byte[] qBytes = q.getEncoded(false);
        // We remove the prefix
        return new BigInteger(1, Arrays.copyOfRange(qBytes, 1, qBytes.length));
    }
    
    
    /** Decompress a compressed public key (x co-ord and low-bit of y-coord). */
    private static ECPoint decompressKey(BigInteger xBN, boolean yBit) {
        X9IntegerConverter x9 = new X9IntegerConverter();
        byte[] compEnc = x9.integerToBytes(xBN, 1 + x9.getByteLength(CURVE.getCurve()));
        compEnc[0] = (byte)(yBit ? 0x03 : 0x02);
        return CURVE.getCurve().decodePoint(compEnc);
    }

}
