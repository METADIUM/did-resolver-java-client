# did-resolver-java-client

## Get it
### Maven
Add the JitPack repository to build file

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Add dependency

```xml
<dependency>
    <groupId>com.github.METADIUM</groupId>
    <artifactId>did-resolver-java-client</artifactId>
    <version>0.1.3</version>
</dependency>
```
### Gradle
Add root build.gradle

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
Add dependency

```gradle
dependencies {
    implementation 'com.github.METADIUM:did-resolver-java-client:0.1.3'
}
```


## Use it

### Get DID Document
```java
DidDocument didDocument = DIDResolverAPI.getInstance().getDocument("did:meta:testnet000000000000000000000000000000000000000000000000000000000000054b");
```

### Get public key in document
```java
DidDocument didDocument = DIDResolverAPI.getInstance().getDocument("did:meta:testnet000000000000000000000000000000000000000000000000000000000000054b");

// retrieve public key
for (PublicKey publicKey : didDocument.getPublicKey()) {
	// getPublicKeyHex() or getPublicKeyHash()
}

// Get public key with key id
String keyId = "did:meta:testnet:000000000000000000000000000000000000000000000000000000000000054b#MetaManagementKey#cfd31afff25b2260ea15ef59f2d5d7dfe8c13511"
document.getPublicKey(keyId);

// Get decoded public key
ECPublicKey publicKey = (ECPublicKey)document.getPublicKey(keyId).getPublicKey();

// Check public key from signature
byte[] message = (to sign message);
String signature = (hex string of V+R+S to sign secp256k1);
boolean bContainsPublicKey = document.hasRecoverAddressFromSignature(message, signature);
```


