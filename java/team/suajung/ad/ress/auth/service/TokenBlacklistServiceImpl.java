package team.suajung.ad.ress.auth.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.suajung.ad.ress.auth.util.JwtUtils;
import team.suajung.ad.ress.auth.model.BlacklistedToken;
import team.suajung.ad.ress.repository1.BlacklistedTokenRepository;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final JwtUtils jwtUtils;
    private static final String BLACKLIST_PREFIX = "blacklist:";

    public void addBlacklist(String token) {
        String key = BLACKLIST_PREFIX + token;
        Claims claims = jwtUtils.parseClaims(token);
        Date expiration = claims.getExpiration();
        long now = new Date().getTime();
        long remainingExpiration = expiration.getTime() - now;

        if (remainingExpiration > 0) {
            BlacklistedToken blacklistedToken = new BlacklistedToken();
            blacklistedToken.setToken(key);
            blacklistedToken.setExpirationDate(new Date(now + remainingExpiration));

            blacklistedTokenRepository.save(blacklistedToken);
        }
    }

    public boolean isBlacklisted(String token) {
        String key = BLACKLIST_PREFIX + token;
        return blacklistedTokenRepository.findByToken(key) != null;
    }
}


