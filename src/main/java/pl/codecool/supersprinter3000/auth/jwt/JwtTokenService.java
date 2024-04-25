package pl.codecool.supersprinter3000.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import pl.codecool.supersprinter3000.auth.config.AuthConfigProperties;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JwtTokenService {

    private final AuthConfigProperties authProperties;

    public JwtTokenService(AuthConfigProperties authProperties) {
        this.authProperties = authProperties;
    }

    public String generateToken(String username) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plus(authProperties.validity());

        return Jwts.builder()
                .subject(username)
                .issuedAt(transformLDTToDate(now))
                .expiration(transformLDTToDate(expiration))
                .signWith(getKey())
                .compact();
    }

    public boolean validateToken(String jwtToken, String springUserName) {
        String jwtUserName = getUserNameFromToken(jwtToken);
        boolean isExpired = getExpirationFromToken(jwtToken).before(new Date());
        return !isExpired && jwtUserName.equals(springUserName);
    }

    public String getUserNameFromToken(String jwtToken) {
        return getClaims(jwtToken).getSubject();
    }

    public Date getExpirationFromToken(String jwtToken) {
        return getClaims(jwtToken).getExpiration();
    }

    private Claims getClaims(String jwtToken) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(authProperties.secret().getBytes());
    }

    // TODO: extract to separate class as it breaks SRP, or use Adapter Pattern
    private Date transformLDTToDate(LocalDateTime now) {
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }
}
