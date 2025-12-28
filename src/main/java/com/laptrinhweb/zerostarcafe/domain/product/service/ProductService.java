package com.laptrinhweb.zerostarcafe.domain.product.service;

import com.laptrinhweb.zerostarcafe.core.database.DBConnection;
import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.domain.product.dao.ProductDAO;
import com.laptrinhweb.zerostarcafe.domain.product.dao.ProductDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.product.model.CatalogItem;
import com.laptrinhweb.zerostarcafe.domain.product.model.OptionGroup;
import com.laptrinhweb.zerostarcafe.domain.product.model.Product;
import com.laptrinhweb.zerostarcafe.domain.product.model.ProductDetail;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Provides methods to retrieve products, catalog items, and product details
 * with proper error handling and logging.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * ProductService productService = new ProductService();
 *
 * // Load all products for a store
 * List<CatalogItem> items = productService.getCatalogItemsByStoreId(1L);
 *
 * // Get product detail for modal
 * ProductDetail detail = productService.getProductDetailByIdAndStoreId(1L, 1L);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 26/12/2025
 * @since 1.0.0
 */
public final class ProductService {

    /**
     * Retrieves all active products from the database.
     *
     * @return list of active products, or empty list if error occurs
     */
    public List<Product> getAllActiveProducts() {
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO productDAO = new ProductDAOImpl(conn);
            return productDAO.findAllActive();
        } catch (AppException | SQLException e) {
            LoggerUtil.error(ProductService.class, "Failed to get all active products", e);
            return List.of();
        }
    }

    /**
     * Retrieves all active products in a specific category.
     *
     * @param categoryId the category ID
     * @return list of active products in the category, or empty list if error occurs
     */
    public List<Product> getActiveProductsByCategoryId(@NonNull Long categoryId) {
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO productDAO = new ProductDAOImpl(conn);
            return productDAO.findAllActiveByCategoryId(categoryId);
        } catch (AppException | SQLException e) {
            LoggerUtil.error(ProductService.class,
                    "Failed to get active products for categoryId=" + categoryId, e);
            return List.of();
        }
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param productId the product ID
     * @return the product, or null if not found or error occurs
     */
    public Product getProductById(@NonNull Long productId) {
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO productDAO = new ProductDAOImpl(conn);
            Optional<Product> productOpt = productDAO.findById(productId);
            return productOpt.orElse(null);
        } catch (AppException | SQLException e) {
            LoggerUtil.error(ProductService.class,
                    "Failed to get product by productId=" + productId, e);
            return null;
        }
    }

    /**
     * Retrieves all catalog items (products with pricing and availability) for a specific store.
     * This method is used for rendering the store's menu on the homepage.
     *
     * @param storeId the store ID
     * @return list of catalog items for the store, or empty list if error occurs
     */
    public List<CatalogItem> getCatalogItemsByStoreId(@NonNull Long storeId) {
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO productDAO = new ProductDAOImpl(conn);
            return productDAO.findCatalogItemsByStoreId(storeId);
        } catch (AppException | SQLException e) {
            LoggerUtil.error(ProductService.class,
                    "Failed to get catalog items for storeId=" + storeId, e);
            return List.of();
        }
    }

    /**
     * Retrieves catalog items filtered by category for a specific store.
     *
     * @param storeId    the store ID
     * @param categoryId the category ID
     * @return list of catalog items for the store and category, or empty list if error occurs
     */
    public List<CatalogItem> getCatalogItemsByStoreIdAndCategoryId(@NonNull Long storeId, @NonNull Long categoryId) {
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO productDAO = new ProductDAOImpl(conn);
            return productDAO.findCatalogItemsByStoreIdAndCategoryId(storeId, categoryId);
        } catch (AppException | SQLException e) {
            LoggerUtil.error(ProductService.class,
                    "Failed to get catalog items for storeId=" + storeId + ", categoryId=" + categoryId, e);
            return List.of();
        }
    }

    /**
     * Retrieves a single catalog item by product ID and store ID.
     *
     * @param productId the product ID
     * @param storeId   the store ID
     * @return the catalog item, or null if not found or error occurs
     */
    public CatalogItem getCatalogItemByIdAndStoreId(@NonNull Long productId, @NonNull Long storeId) {
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO productDAO = new ProductDAOImpl(conn);
            Optional<CatalogItem> itemOpt = productDAO.findCatalogItemByIdAndStoreId(productId, storeId);
            return itemOpt.orElse(null);
        } catch (AppException | SQLException e) {
            LoggerUtil.error(ProductService.class,
                    "Failed to get catalog item for productId=" + productId + ", storeId=" + storeId, e);
            return null;
        }
    }

    /**
     * Retrieves complete product detail including options and pricing for a specific store.
     * This method is used for product detail modals where users configure their order.
     *
     * @param productId the product ID
     * @param storeId   the store ID
     * @return the product detail, or null if not found or error occurs
     */
    public ProductDetail getProductDetailByIdAndStoreId(@NonNull Long productId, @NonNull Long storeId) {
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO productDAO = new ProductDAOImpl(conn);
            Optional<ProductDetail> detailOpt = productDAO.findProductDetailByIdAndStoreId(productId, storeId);
            return detailOpt.orElse(null);
        } catch (AppException | SQLException e) {
            LoggerUtil.error(ProductService.class,
                    "Failed to get product detail for productId=" + productId + ", storeId=" + storeId, e);
            return null;
        }
    }

    /**
     * Retrieves option groups for a product at a specific store.
     *
     * @param productId the product ID
     * @param storeId   the store ID
     * @return list of option groups with their values, or empty list if error occurs
     */
    public List<OptionGroup> getOptionGroupsByProductIdAndStoreId(@NonNull Long productId, @NonNull Long storeId) {
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO productDAO = new ProductDAOImpl(conn);
            return productDAO.findOptionGroupsByProductIdAndStoreId(productId, storeId);
        } catch (AppException | SQLException e) {
            LoggerUtil.error(ProductService.class,
                    "Failed to get option groups for productId=" + productId + ", storeId=" + storeId, e);
            return List.of();
        }
    }

    public ProductDetail getProductDetailBySlugAndStoreId(@NonNull String productSlug, @NonNull Long storeId) {
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO productDAO = new ProductDAOImpl(conn);
            Optional<ProductDetail> detailOpt = productDAO.findProductDetailBySlugAndStoreId(productSlug, storeId);
            return detailOpt.orElse(null);
        } catch (SQLException e) {
            LoggerUtil.error(ProductService.class,
                    "Failed to get product detail by slug: " + productSlug + " - " + e.getMessage(), e);
            return null;
        }
    }
}

