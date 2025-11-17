package com.laptrinhweb.zerostarcafe.core.utils;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * Utility to calculate Time-To-Live (TTL) durations.
 * Primarily converts future timestamps into remaining seconds.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * LocalDateTime expiredAt = authSession.getExpiredAt();
 * int ttlSeconds = TimeUtil.ttlFromNow(expiredAt);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 16/11/2025
 * @since 1.0.0
 */
public final class TimeUtil {

    private TimeUtil() {
    }

    /**
     * Calculates the TTL (time-to-live) in seconds from the current time
     *
     * @param expiredAt the future expiration time
     * @return the TTL in seconds (>= 0) for Cookie max-age
     */
    public static int ttlFromNow(LocalDateTime expiredAt) {
        if (expiredAt == null) {
            return 0;
        }

        long seconds = Duration.between(LocalDateTime.now(), expiredAt).getSeconds();
        if (seconds <= 0) {
            return 0;
        }

        if (seconds > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }

        return (int) seconds;
    }
}
