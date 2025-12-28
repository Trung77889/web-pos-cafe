package com.laptrinhweb.zerostarcafe.domain.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents a specific option value (e.g., "Size M", "50% sugar") within an option group.
 * Maps to {@code option_values} table with store-specific overrides from
 * {@code store_option_values}.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 26/12/2025
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OptionValue {
    private long id;
    private long groupId;
    private String name;
    private int priceDelta;
    private boolean active;
    private OptionAvailability storeAvailability;
}

