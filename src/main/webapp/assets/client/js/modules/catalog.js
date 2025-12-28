/**
 * ------------------------------------------------------------
 * Module: Catalog Manager
 * ------------------------------------------------------------
 * @description
 * Manages category navigation and dynamic product catalog loading.
 * Fetches products by category via AJAX and updates the grid without
 * page reload. Integrates with product modal system.
 *
 * @example
 * import { initCatalogSwitcher } from './catalog.js';
 * initCatalogSwitcher();
 *
 * @version 1.0.0
 * @since 1.0.0
 * @lastModified 28/12/2025
 * @module catalog
 * @author Dang Van Trung
 */

import { ProductWebConstants } from './web-constants.js';

const CATALOG_CONTAINER = '[data-product-grid]';
const CATEGORY_ITEM = '[data-category-item]';
const CATEGORY_TITLE = '[data-category-title]';
const CATEGORY_META = '[data-category-meta]';
const ACTIVE_CLASS = 'is-active';

/**
 * Initializes category switcher and handles category navigation.
 */
export function initCatalogSwitcher() {
    const productGrid = document.querySelector(CATALOG_CONTAINER);
    if (!productGrid) return;

    // Auto-detect default category from first product
    const firstProduct = productGrid.querySelector('[data-product-id]');
    const defaultCategorySlug = firstProduct?.dataset.categorySlug; // Use slug

    if (defaultCategorySlug) {
        const defaultCategory = document.querySelector(
            `${CATEGORY_ITEM}[data-category-slug="${defaultCategorySlug}"]`
        );
        if (defaultCategory) {
            defaultCategory.classList.add(ACTIVE_CLASS);
            updateCategoryHeader(defaultCategory);
            
            // Scroll active category into view (Task 3)
            scrollCategoryIntoView(defaultCategory);
        }
    }

    // Category click handler
    const categoryItems = document.querySelectorAll(CATEGORY_ITEM);
    categoryItems.forEach(cat => {
        cat.addEventListener('click', async (e) => {
            e.preventDefault();

            // Don't trigger if clicking product card
            if (e.target.closest('[data-product-card]')) return;

            const categorySlug = cat.dataset.categorySlug; // Use slug instead of ID
            const categoryName = cat.dataset.categoryName;

            if (!categorySlug) return;

            // Update active state
            document.querySelectorAll(`${CATEGORY_ITEM}.${ACTIVE_CLASS}`)
                .forEach(c => c.classList.remove(ACTIVE_CLASS));
            cat.classList.add(ACTIVE_CLASS);

            // Update header
            updateCategoryHeader(cat);
            
            // Scroll into view
            scrollCategoryIntoView(cat);

            // Fetch products for this category (using slug)
            try {
                const url = `${ProductWebConstants.Endpoint.PRODUCTS_BY_CATEGORY}/${categorySlug}`;
                const response = await fetch(url);
                if (!response.ok) {
                    throw new Error(`HTTP ${response.status}`);
                }

                const html = await response.text();
                const container = document.querySelector(CATALOG_CONTAINER);
                container.innerHTML = html;

                // Update product count after loading new products
                updateCategoryHeader(cat);

                // Re-attach event listeners to new product cards
                await initProductModalTriggers();
            } catch (error) {
                console.error('Failed to load category:', error);
                showErrorMessage('Không thể tải sản phẩm. Vui lòng thử lại.');
            }
        });
    });
}

/**
 * Scrolls the category item into view in the horizontal slider.
 * Ensures the selected category is always visible.
 * 
 * @param {HTMLElement} categoryEl - The category element to scroll to
 */
function scrollCategoryIntoView(categoryEl) {
    if (!categoryEl) return;
    
    const track = document.querySelector('[data-category-track]');
    if (!track) return;
    
    // Use smooth scrolling with center alignment
    categoryEl.scrollIntoView({
        behavior: 'smooth',
        block: 'nearest',
        inline: 'center'
    });
}

/**
 * Updates the category header with active category name and product count.
 * @param {HTMLElement} categoryEl - The active category element
 */
function updateCategoryHeader(categoryEl) {
    const categoryName = categoryEl.dataset.categoryName;
    if (!categoryName) return;

    const titleEl = document.querySelector(CATEGORY_TITLE);
    const metaEl = document.querySelector(CATEGORY_META);

    if (titleEl) {
        titleEl.textContent = categoryName;
    }

    // Count products in the grid (will be updated after AJAX load)
    if (metaEl) {
        const productCount = document.querySelectorAll('[data-product-card]').length;
        metaEl.textContent = productCount === 1 ? '1 sản phẩm' : `${productCount} sản phẩm`;
    }
}

/**
 * Re-initializes product modal triggers for dynamically loaded cards.
 */
async function initProductModalTriggers() {
    try {
        const { initProductModal } = await import('./product-modal.js');
        initProductModal();
    } catch (error) {
        console.error('Failed to init product modal:', error);
    }
}

/**
 * Shows error toast message.
 * @param {string} message - Error message to display
 */
async function showErrorMessage(message) {
    try {
        const { Toast } = await import('../../../shared/js/modules/toast.js');
        Toast.error(message);
    } catch (error) {
        console.error('Toast not available:', error);
        alert(message);
    }
}
