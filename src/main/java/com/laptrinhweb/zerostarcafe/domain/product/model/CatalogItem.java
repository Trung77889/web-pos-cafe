package com.laptrinhweb.zerostarcafe.domain.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents a product summary item displayed in the catalog/menu list.
 * Combines data from {@code menu_items}, {@code store_menu_items}, and
 * {@code store_item_price_schedules} tables.
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
public class CatalogItem {
    private long id;
    private long categoryId;
    private String categorySlug;
    private String name;
    private String slug;
    private String imageUrl;
    private String description;
    private int basePrice;
    private String unit;
    private boolean active;
    private Availability availability;
    private int resolvedPrice;
    private boolean hasOptions;
}

