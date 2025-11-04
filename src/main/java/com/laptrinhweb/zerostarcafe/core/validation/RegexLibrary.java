package com.laptrinhweb.zerostarcafe.core.validation;

import java.util.regex.Pattern;

/**
 * <h2>Description:</h2>
 * <p>
 * Precompiled and Unicode-safe regex patterns for validation across the project.
 * Based on OWASP Validation Regex Recommendations where applicable.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * if (RegexPatterns.matches(RegexPatterns.EMAIL, email)) {
 *     // valid email
 * }
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 28/10/2025
 * @since 1.0.0
 */
public final class RegexLibrary {

    /**
     * Lowercase slug (a-z0-9 and hyphen)
     */
    public static final Pattern SLUG = Pattern.compile(
            "^[a-z0-9]+(?:-[a-z0-9]+)*$"
    );
    /**
     * Uppercase code (letters, digits, underscore)
     */
    public static final Pattern CODE = Pattern.compile(
            "^[A-Z0-9_]{3,32}$"
    );

    // ============================================================
    // TEXT
    // ============================================================
    /**
     * Person name with Unicode letters and simple punctuation
     */
    public static final Pattern PERSON_NAME = Pattern.compile(
            "^[\\p{L}][\\p{L}\\p{Z}.'-]{1,48}$",
            Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS
    );
    /**
     * Email (OWASP recommended)
     */
    public static final Pattern EMAIL = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,63}$"
    );
    /**
     * Username: start with a letter, 3–32 chars, letters/numbers/._-
     */
    public static final Pattern USERNAME = Pattern.compile(
            "^[A-Za-z][A-Za-z0-9._-]{2,31}$"
    );

    // ============================================================
    // IDENTITY
    // ============================================================
    /**
     * Basic password: 6–32 chars, at least 1 letter and 1 digit.
     */
    public static final Pattern PASSWORD_BASIC = Pattern.compile(
            "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&._-]{6,32}$"
    );
    /**
     * Strong password (OWASP): 8–32 chars, at least 1 upper, lower, digit, special
     */
    public static final Pattern PASSWORD_STRONG = Pattern.compile(
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,32}$"
    );
    /**
     * Vietnam phone number (+84 or 0)
     */
    public static final Pattern VN_PHONE = Pattern.compile(
            "^(\\+84|0)(3|5|7|8|9)\\d{8}$"
    );
    /**
     * UUID (RFC 4122 v1–v5)
     */
    public static final Pattern UUID = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$"
    );
    /**
     * Integer (optional + or -)
     */
    public static final Pattern INTEGER = Pattern.compile(
            "^[+-]?\\d+$"
    );
    /**
     * Decimal number with optional fraction
     */
    public static final Pattern DECIMAL = Pattern.compile(
            "^[+-]?\\d+(\\.\\d+)?$"
    );

    // ============================================================
    // NUMBER
    // ============================================================
    /**
     * Price with max 2 decimal places
     */
    public static final Pattern PRICE = Pattern.compile(
            "^\\d+(\\.\\d{1,2})?$"
    );
    /**
     * Domain name (OWASP safe version)
     */
    public static final Pattern DOMAIN = Pattern.compile(
            "^(?=.{1,255}$)[A-Za-z0-9]([A-Za-z0-9-]{0,61}[A-Za-z0-9])?(\\.[A-Za-z]{2,})+$"
    );
    /**
     * IPv4 quick check
     */
    public static final Pattern IPV4 = Pattern.compile(
            "^(?:\\d{1,3}\\.){3}\\d{1,3}$"
    );

    // ============================================================
    // NETWORK
    // ============================================================
    /**
     * URL (OWASP safe simplified)
     */
    public static final Pattern URL = Pattern.compile(
            "^(?i)(https?|ftp)://[\\w.-]+(:\\d{2,5})?(/[\\w./?%&=+#-]*)?$"
    );
    /**
     * Date ISO: YYYY-MM-DD
     */
    public static final Pattern ISO_DATE = Pattern.compile(
            "^\\d{4}-\\d{2}-\\d{2}$"
    );
    /**
     * Time 24h: HH:mm[:ss]
     */
    public static final Pattern TIME_24H = Pattern.compile(
            "^(?:[01]\\d|2[0-3]):[0-5]\\d(?::[0-5]\\d)?$"
    );

    // ============================================================
    // DATE & TIME
    // ============================================================
    /**
     * SKU: uppercase letters, digits, dash, dot, underscore
     */
    public static final Pattern SKU = Pattern.compile(
            "^[A-Z0-9][A-Z0-9._-]{1,38}[A-Z0-9]$"
    );
    /**
     * EAN-13 barcode (13 digits)
     */
    public static final Pattern EAN13 = Pattern.compile(
            "^\\d{13}$"
    );

    // ============================================================
    // PRODUCT / CODE
    // ============================================================

    private RegexLibrary() {
    } // prevent instantiation

    /**
     * Safe helper for null check + pattern match
     */
    public static boolean matches(Pattern pattern, CharSequence input) {
        return input != null && pattern.matcher(input).matches();
    }
}