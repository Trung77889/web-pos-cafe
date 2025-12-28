package com.laptrinhweb.zerostarcafe.domain.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents store-specific availability information for a product.
 * Maps to data from {@code store_menu_items} table.
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
public class Availability {
    private boolean inMenu;
    private AvailabilityStatus status;
    private LocalDateTime soldOutUntil;
    private String soldOutNote;
}

