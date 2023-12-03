package com.nullers.restbookstore.rest.auth.services.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Implementación de JwtService
 *
 * @Author: Binwei Wang
 */
@Service
@Data
public class JwtServiceImp implements JwtService {
    /**
     * Atributos de configuración de JWT
     */
    @Value("${jwt.secret}")
    private String jwtSecretKey;
    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    /**
     * Extrae el nombre de usuario del token
     * @param token jwt token de autenticación
     * @return nombre de usuario
     */
    @Override
    public String extractUserName(String token) {
        return extractClaim(token,DecodedJWT::getSubject);
    }

    /**
     * Genera un token de autenticación
     * @param userDetails username y password
     * @return jwt token
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Genera un token de autenticación con claims extra
     * @param token jwt token
     * @param userDetails user details
     * @return jwt token
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Verifica si el token ha expirado
     * @param token jwt token
     * @return true si el token ha expirado
     */
    private boolean isTokenExpired(String token) {
        Date expirationDate = extractExpiration(token);
        return expirationDate.before(new Date());
    }

    /**
     * Extrae la fecha de expiración del token
     * @param token jwt token
     * @return fecha de expiración
     */
    private Date extractExpiration(String token) {
        return extractClaim(token,DecodedJWT::getExpiresAt);
    }

    /**
     * Extrae un claim del token
     * @param token jwt token
     * @param claimsResolver función que extrae el claim
     * @return claim extraído
     * @param <T> tipo de dato del claim
     */
    private <T> T extractClaim(String token, Function<DecodedJWT, T> claimsResolver) {
        final DecodedJWT jwt = JWT.decode(token);
        return claimsResolver.apply(jwt);
    }

    /**
     * Genera un token de autenticación
     * @param extraClaims claims extra
     * @param userDetails username y password
     * @return jwt token
     */
    private String generateToken(HashMap<String,Object> extraClaims, UserDetails userDetails) {
        Algorithm algorithm = Algorithm.HMAC512(getSigningKey());
        Date now = new Date();
        Date expiration = new Date(now.getTime() + (1000*jwtExpiration));
        return JWT.create()
                .withHeader(createHeader())
                .withSubject(userDetails.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .withClaim("extra",extraClaims)
                .sign(algorithm);
    }

    /**
     * Crea el header del token
     * @return header
     */

    private Map<String,Object> createHeader() {
        Map<String,Object> header = new HashMap<>();
        header.put("typ","JWT");
        return header;
    }

    /**
     * Obtiene la llave de firma
     * @return llave de firma
     */
    private byte[] getSigningKey() {
        return Base64.getEncoder().encode(jwtSecretKey.getBytes());
    }
}
