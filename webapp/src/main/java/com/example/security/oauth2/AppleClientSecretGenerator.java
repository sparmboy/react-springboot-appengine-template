package com.example.security.oauth2;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class AppleClientSecretGenerator implements ClientSecretGenerator {

    @Value("${spring.security.oauth2.client.registration.apple.keyId}")
    private String appleKeyId;

    @Value("${spring.security.oauth2.client.registration.apple.teamId}")
    private String appleTeamId;

    @Value("${spring.security.oauth2.client.registration.apple.clientId}")
    private String clientId;

    public final static int EXPIRY_TIME_IN_MINS = 5;


    // Get the private key from .p8 file (assume that it is located inside the resource dir (apple/ folder will be same level with application.yml file)
    private PrivateKey getPrivateKey() throws Exception {
        InputStream is = new ClassPathResource("security/oauth/apple/AuthKey_" + appleKeyId + ".p8").getInputStream();
        File file = File.createTempFile("TEMPFILE_P8", new Date().getTime() + ".p8");
        try (OutputStream outputStream = new FileOutputStream(file)) {
            IOUtils.copy(is, outputStream);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred writing key to temp file:" + e, e);
        }
        final PEMParser pemParser = new PEMParser(new FileReader(file));
        final JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        final PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        final PrivateKey pKey = converter.getPrivateKey(object);
        pemParser.close();
        return pKey;
    }

    // From the private key, we sign a JWT as AppleDeveloper guides
    @Override
    public Map.Entry<String, LocalDateTime> generateClientSecret() throws Exception {
        final PrivateKey key = getPrivateKey();
        long nowSeconds = System.currentTimeMillis();
        Date now = new Date(nowSeconds);
        long expMillis = nowSeconds + (EXPIRY_TIME_IN_MINS * 60 * 1000); // Change the time as you wish
        Date exp = new Date(expMillis);

        log.info("Generating new Apple secret key with expiry {}", exp);

        return new AbstractMap.SimpleEntry<>(
            Jwts.builder()
                .setHeaderParam("alg", "ES256")
                .setHeaderParam("kid", appleKeyId)
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(now)
                .setSubject(clientId)
                .setIssuer(appleTeamId)
                .setAudience("https://appleid.apple.com")
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.ES256)
                .compact(),
            LocalDateTime.now().plus(Duration.ofMillis(expMillis))
        );
    }
}
