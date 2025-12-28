package com.laptrinhweb.zerostarcafe.domain.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * Domain model representing a menu item/product in the system.
 * Maps directly to the {@code menu_items} table.
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
public class Product {
    private long id;
    private long categoryId;
    private String name;
    private String imageUrl;
    private String description;
    private int basePrice;
    private String unit;
    private boolean active;
    private LocalDateTime createdAt;
}

