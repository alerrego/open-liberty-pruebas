package com.alito.rest.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Date;

import com.alito.rest.model.User;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtProvider {

    private KeyPair rsaKey;

    private final String KID = "tickets-key-id"; // Un ID fijo para que coincidan

    public JwtProvider(){
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(2048);
            rsaKey = gen.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("ERROR creating key RSA",e);
        }
    }

    //ESTE MÉTODO ES SIMILAR A LA IMPLEMENTACION DE SPRING, EXPONGO LA CLAVE PUBLICA PQ ESTA SOLO LA USAMOS PARA VERIFICAR EL TOKEN
    //LA CLAVE PRIVADA SOLO LA TIENE EL QUE CREA EL TOKEN
    public String getPublicKeysJson() {
    try {
        RSAPublicKey pub = (RSAPublicKey) rsaKey.getPublic();
        String n = Base64.getUrlEncoder().withoutPadding().encodeToString(pub.getModulus().toByteArray());
        String e = Base64.getUrlEncoder().withoutPadding().encodeToString(pub.getPublicExponent().toByteArray());

        return "{"
            + "\"keys\": ["
            + "  {"
            + "    \"kty\": \"RSA\","
            + "    \"e\": \"" + e + "\","
            + "    \"kid\": \"" + KID + "\","
            + "    \"alg\": \"RS256\","
            + "    \"n\": \"" + n + "\""
            + "  }"
            + "]"
            + "}";
    } catch (Exception ex) {
        throw new RuntimeException("Error al generar JSON JWKS", ex);
    }
    }

    public String generateToken(User u) throws Exception{
        JWSSigner signer = new RSASSASigner(rsaKey.getPrivate());

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .subject(u.getId().toString())
            .issuer("https://auth.tickets.com")
            .claim("upn", u.getEmail())
            .claim("groups",new String[]{u.getRol()})
            .expirationTime(new Date(new Date().getTime() + 3600*1000))
            .build();

        SignedJWT signedJWT = new SignedJWT(
            new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(KID).build(),
            claimsSet);
        
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

}
