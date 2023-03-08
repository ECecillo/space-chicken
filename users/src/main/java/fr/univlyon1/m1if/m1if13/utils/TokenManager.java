package fr.univlyon1.m1if.m1if13.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Classe qui centralise les opérations de validation et de génération d'un token "métier", c'est-à-dire dédié
 * à cette application.
 *
 * @author Lionel Médini
 */
@Component
public final class TokenManager {
    private static final String SECRET = "monsecret2023";
    private static final String ISSUER = "SpaceChicken M1IF03";
    private static final long LIFETIME = 1800000; // Durée de vie d'un token : 30 minutes ;
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);



    /**
     * Vérifie l'authentification d'un utilisateur grâce à un token JWT.
     *
     * @param token le token à vérifier.
     * @return un booléen qui indique si le token est bien formé et valide (pas expiré) et si l'utilisateur est
     * authentifié
     */
    public String verifyToken(final String token) throws NullPointerException, JWTVerificationException {
        JWTVerifier authenticationVerifier = JWT.require(ALGORITHM)
                .withIssuer(ISSUER)
                .build();
        // Lève une NullPointerException si le token n'existe pas, et une JWTVerificationException s'il est invalide
        authenticationVerifier.verify(token);
        // Pourrait lever une JWTDecodeException mais comme le token est vérifié avant, cela ne devrait pas arriver
        DecodedJWT jwt = JWT.decode(token);

        return jwt.getClaim("sub").asString();
    }

    /**
     * Vérifie dans le token si un user est membre d'un salon.
     *
     * @param token le token à vérifier
     * @param salonId l'id du salon dont on veut savoir si l'utilisateur est membre
     * @return un booléen indiquant si le token contient un booléen admin à true
     */
    public static boolean getMember(final String token, final String salonId) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM).withClaimPresence("member").build();
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("member").asString().contains(salonId);
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * Crée un token avec les caractéristiques de l'utilisateur.
     *
     * @param subject le login de l'utilisateur
     * @return le token signé
     * @throws JWTCreationException si les paramètres ne permettent pas de créer un token
     */
    public String generateToken(final String subject) throws JWTCreationException {
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(subject)
                .withExpiresAt(new Date(new Date().getTime() + LIFETIME))
                .sign(ALGORITHM);
    }

    /**
     * Renvoie l'URL d'origine du client, en fonction des headers de proxy (si existants) ou de l'URL de la requête.
     *
     * @param request la requête HTTP
     * @return une String qui sera passée aux éléments de l'application pour générer les URL absolues.
     */
    private static String getOrigin(final HttpServletRequest request) {
        String origin = String.valueOf(request.getRequestURL())
                .substring(0, request.getRequestURL().lastIndexOf(request.getRequestURI()));
        if (request.getHeader("X-Forwarded-Host") != null
                && request.getHeader("X-Forwarded-Proto") != null
                && request.getHeader("X-Forwarded-Path") != null) {
            switch (request.getHeader("X-Forwarded-Proto")) {
                case "http":
                    origin = request.getHeader("X-Forwarded-Proto") + "://"
                            + (request.getHeader("X-Forwarded-Host")
                                    .endsWith(":80") ? request.getHeader("X-Forwarded-Host")
                                    .replace(":80", "")
                                    : request.getHeader("X-Forwarded-Host"));
                    break;
                case "https":
                    origin = request.getHeader("X-Forwarded-Proto")
                            + "://"
                            + (request.getHeader("X-Forwarded-Host")
                            .endsWith(":443") ? request.getHeader("X-Forwarded-Host")
                            .replace(":443", "")
                            : request.getHeader("X-Forwarded-Host"));
                default:
            }
            origin = origin + request.getHeader("X-Forwarded-Path");
        }
        return origin + request.getContextPath();
    }
}
