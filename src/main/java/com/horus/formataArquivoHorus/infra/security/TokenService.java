package com.horus.formataArquivoHorus.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.horus.formataArquivoHorus.model.entity.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user){
        try{

            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create().withIssuer("format-horus")
                    .withSubject(user.getLogin())
                    .withExpiresAt(this.genExpirantionDate())
                    .withClaim("roles", List.of(user.getRole().getRole()))
                    .sign(algorithm)
                    ;

            return token;

        } catch (JWTCreationException e){

            throw new RuntimeException("Error while generating token", e);

        }
    }

    public String validateToken(String token){
        try{

            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm).withIssuer("format-horus").build().verify(token).getSubject();

        } catch (JWTVerificationException e){

            throw new RuntimeException("INVALID TOKEN", e);

        }

    }

    private Instant genExpirantionDate(){

        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));

    }

    public DecodedJWT getDecodedJWT(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("format-horus")
                .build()
                .verify(token);

    }


}
