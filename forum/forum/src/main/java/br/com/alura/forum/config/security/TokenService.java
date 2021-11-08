package br.com.alura.forum.config.security;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    String expiration = "36000";

    String secret = "ashf91284hfpqw@*(#MFJBOÇUuhqw8r2hfnwç*¨@nG@";

    public String gerarToken(Authentication authentication) {

        Usuario usuario = (Usuario) authentication.getPrincipal();
        Date date = new Date();
        Date expirationDate = new Date(date.getTime() + Long.parseLong(expiration));

        return Jwts.builder().
                setIssuer("API do Forúm da Alura").
                setSubject(usuario.getId().toString()).
                setIssuedAt(date).
                setExpiration(expirationDate).
                signWith(SignatureAlgorithm.HS256, secret).
                compact();
    }

    public boolean isTokenValido(String token) {

        try {
            Jwts.parser().
                    setSigningKey(this.secret).
                    parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getIdUsuario(String token) {
        Claims claims = Jwts.parser().
                            setSigningKey(this.secret).
                            parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }
}
