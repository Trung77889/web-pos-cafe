package com.laptrinhweb.zerostarcafe.domain.product.model;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents the availability status of a product or option in a store.
 * Maps to the {@code availability_status} enum in database tables
 * {@code store_menu_items} and {@code store_option_values}.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 26/12/2025
 * @since 1.0.0
 */
public enum AvailabilityStatus {
    AVAILABLE,
    SOLD_OUT
}



