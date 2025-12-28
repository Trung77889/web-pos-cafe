package com.laptrinhweb.zerostarcafe.domain.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents complete product information including all options and pricing schedules.
 * Used for product detail pages where users configure their order.
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
public class ProductDetail {
    private CatalogItem item;
    private List<OptionGroup> optionGroups;
}

