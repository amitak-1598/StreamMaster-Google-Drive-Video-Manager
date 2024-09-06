package com.videostreaming.VideoStreamingApplication.Services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {
    private Set<String> blacklistedTokens = new HashSet<>();

    public boolean blacklistToken(String token) {
        return blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
