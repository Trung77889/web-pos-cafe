/**
 * ------------------------------------------------------------
 * Module: Web Constants
 * ------------------------------------------------------------
 * @description
 * Centralized endpoints and cookie names used by client modules
 * for store detection and session context.
 *
 * @example
 * import { StoreWebConstants } from './web-constants.js';
 * fetch(StoreWebConstants.Endpoint.STORE_DETECT)
 *
 * @version 1.0.1
 * @since 1.0.0
 * @lastModified 12/26/2025
 * @module web-constants
 * @author Dang Van Trung
 */
export const StoreWebConstants = {

    Endpoint: {
        STORE_DETECT: "/zero_star_cafe/store-detect",
        GET_PRODUCTS: "/zero_star_cafe/api/products"
    },

    Cookie: {
        LAST_STORE_ID: "store_id",
        LAST_TABLE_ID: "table_id",
    }
};

export const ProductWebConstants = {
    Endpoint: {
        PRODUCT_MODAL: "/zero_star_cafe/api/products",
        PRODUCTS_BY_CATEGORY: "/zero_star_cafe/api/products/category",
        PRODUCT_SEARCH: "/zero_star_cafe/api/products/search"
    }
};