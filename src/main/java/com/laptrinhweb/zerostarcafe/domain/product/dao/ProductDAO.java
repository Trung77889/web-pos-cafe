package com.laptrinhweb.zerostarcafe.domain.product.dao;

import com.laptrinhweb.zerostarcafe.domain.product.model.CatalogItem;
import com.laptrinhweb.zerostarcafe.domain.product.model.OptionGroup;
import com.laptrinhweb.zerostarcafe.domain.product.model.Product;
import com.laptrinhweb.zerostarcafe.domain.product.model.ProductDetail;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Provides database access operations for product-related entities.
 * This DAO handles queries for products, catalog items, options, and
 * store-specific availability information.
 * </p>
 *
 * <h2>Responsibilities:</h2>
 * <ul>
 *     <li>Find products by ID or category</li>
 *     <li>Load catalog items for a specific store (with pricing and availability)</li>
 *     <li>Load product details including options and price schedules</li>
 *     <li>Query option groups and values for products</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * ProductDAO productDAO = new ProductDAOImpl(connection);
 *
 * // Load all active products for a store's menu
 * List<CatalogItem> items = productDAO.findCatalogItemsByStoreId(1L);
 *
 * // Get full product details for a specific product by slug
 * Optional<ProductDetail> detail = productDAO.findProductDetailBySlugAndStoreId("ca-phe-sua", 1L);
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 26/12/2025
 * @since 1.0.0
 */
public interface ProductDAO {

    /**
     * Loads catalog items (products with availability and pricing) for a specific store.
     * Only returns products that are active, in the store's menu, and available.
     *
     * @param storeId the store ID
     * @return list of catalog items for the store
     * @throws SQLException if a database access error occurs
     */
    List<CatalogItem> findCatalogItemsByStoreId(long storeId) throws SQLException;

    /**
     * Loads catalog items filtered by category for a specific store.
     *
     * @param storeId    the store ID
     * @param categoryId the category ID
     * @return list of catalog items for the store and category
     * @throws SQLException if a database access error occurs
     */
    List<CatalogItem> findCatalogItemsByStoreIdAndCategoryId(long storeId, long categoryId) throws SQLException;

    /**
     * Finds a catalog item by slug and store ID.
     */
    Optional<CatalogItem> findCatalogItemBySlugAndStoreId(String productSlug, long storeId) throws SQLException;

    /**
     * Finds complete product details by slug and store.
     */
    Optional<ProductDetail> findProductDetailBySlugAndStoreId(String productSlug, long storeId) throws SQLException;

    /**
     * Finds all option groups associated with a specific product.
     *
     * @param productId the product ID
     * @param storeId   the store ID (for store-specific option availability)
     * @return list of option groups with their values
     * @throws SQLException if a database access error occurs
     */
    List<OptionGroup> findOptionGroupsByProductIdAndStoreId(long productId, long storeId) throws SQLException;

    /**
     * Searches catalog items by product name for a specific store.
     *
     * @param storeId the store ID
     * @param searchTerm the search term to match against product names
     * @return list of catalog items matching the search term
     * @throws SQLException if a database access error occurs
     */
    List<CatalogItem> searchCatalogItemsByNameAndStoreId(long storeId, String searchTerm) throws SQLException;
}

