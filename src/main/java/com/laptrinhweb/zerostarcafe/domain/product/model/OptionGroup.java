package com.laptrinhweb.zerostarcafe.domain.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents a group of options (e.g., Size, Sugar, Ice, Topping) that
 * can be selected for a product. Maps to {@code option_groups} table
 * and its relationship with {@code item_option_groups}.
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
public class OptionGroup {
    private long id;
    private String name;
    private String type;
    private boolean required;
    private int minSelect;
    private int maxSelect;
    private List<OptionValue> values;
}

