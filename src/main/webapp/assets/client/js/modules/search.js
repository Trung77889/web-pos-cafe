/**
 * ------------------------------------------------------------
 * Module: Product Search
 * ------------------------------------------------------------
 * @description
 * Manages product search functionality with debounced AJAX requests,
 * full-page loading indicators, and search history stored in localStorage.
 *
 * @example
 * import { initProductSearch } from './search.js';
 * initProductSearch();
 *
 * @version 1.3.0
 * @since 1.0.0
 * @lastModified 28/12/2025
 * @module search
 * @author Dang Van Trung
 */

import { ProductWebConstants } from './web-constants.js';
import { showLoader, hideLoader } from './page-loader.js';

const SEARCH_INPUT = '[data-search-input]';
const SEARCH_HISTORY = '[data-search-history]';
const HISTORY_LIST = '[data-history-list]';
const HISTORY_CLEAR = '[data-history-clear]';
const CATALOG_CONTAINER = '[data-product-grid]';
const CATEGORY_TITLE = '[data-category-title]';
const CATEGORY_META = '[data-category-meta]';
const CATEGORY_ITEM = '[data-category-item]';
const ACTIVE_CLASS = 'is-active';
const DEBOUNCE_DELAY = 600; // Increased to 600ms for better UX
const MAX_HISTORY_ITEMS = 5;
const STORAGE_KEY = 'product_search_history';

let searchTimeout = null;
let isSearching = false;

/**
 * Initializes product search functionality.
 */
export function initProductSearch() {
    const searchInput = document.querySelector(SEARCH_INPUT);
    if (!searchInput) return;

    const historyDropdown = document.querySelector(SEARCH_HISTORY);
    const historyList = document.querySelector(HISTORY_LIST);
    const historyClear = document.querySelector(HISTORY_CLEAR);

    // Focus: Show search history when input is empty
    searchInput.addEventListener('focus', () => {
        if (!searchInput.value.trim()) {
            showSearchHistory(historyDropdown, historyList, searchInput);
        }
    });

    // Click: Show search history when input is empty
    searchInput.addEventListener('click', () => {
        if (!searchInput.value.trim()) {
            showSearchHistory(historyDropdown, historyList, searchInput);
        }
    });

    // Blur: Hide history dropdown
    searchInput.addEventListener('blur', () => {
        // Delay to allow clicking on history items
        setTimeout(() => {
            if (historyDropdown) {
                historyDropdown.classList.remove('is-visible');
            }
        }, 200);
    });

    // Search input handler with debouncing
    searchInput.addEventListener('input', (e) => {
        const searchTerm = e.target.value.trim();

        // Show history when input is empty, hide when typing
        if (historyDropdown) {
            if (!searchTerm) {
                showSearchHistory(historyDropdown, historyList, searchInput);
            } else {
                historyDropdown.classList.remove('is-visible');
            }
        }

        // Clear previous timeout
        if (searchTimeout) {
            clearTimeout(searchTimeout);
        }

        // Debounce search
        searchTimeout = setTimeout(async () => {
            if (searchTerm.length > 0) {
                await performSearch(searchTerm);
                // Save to history after successful search
                saveSearchHistory(searchTerm);
            } else {
                await reloadDefaultCategory();
            }
        }, DEBOUNCE_DELAY);
    });

    // Clear all history
    if (historyClear) {
        historyClear.addEventListener('mousedown', (e) => {
            e.preventDefault();
        });

        historyClear.addEventListener('click', (e) => {
            e.stopPropagation();
            clearSearchHistory();
            showSearchHistory(historyDropdown, historyList, searchInput);
        });
    }

    // Prevent form submission on Enter key and blur
    searchInput.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') {
            e.preventDefault();
            searchInput.blur();
        }
    });

    // Clear search input when clicking on category
    document.querySelectorAll(CATEGORY_ITEM).forEach(categoryEl => {
        categoryEl.addEventListener('click', () => {
            searchInput.value = '';
            if (historyDropdown) {
                historyDropdown.classList.remove('is-visible');
            }
        });
    });

    // Hide history when clicking anywhere outside search field
    document.addEventListener('click', (e) => {
        const searchField = searchInput.closest('.search-field');
        if (searchField && !searchField.contains(e.target)) {
            if (historyDropdown) {
                historyDropdown.classList.remove('is-visible');
            }
        }
    });
}

/**
 * Shows search history dropdown.
 * @param {HTMLElement} dropdown - History dropdown element
 * @param {HTMLElement} list - History list element
 * @param {HTMLElement} input - Search input element
 */
function showSearchHistory(dropdown, list, input) {
    const history = getSearchHistory();
    
    if (!dropdown || !list) return;

    // Clear existing items
    list.innerHTML = '';

    if (history.length === 0) {
        list.innerHTML = `
            <li class="search-history__empty">
                <i class="fi fi-rr-time-past"></i>
                <span>Không có lịch sử tìm kiếm</span>
            </li>
        `;
        dropdown.classList.add('is-visible');
        return;
    }

    // Render history items
    history.forEach(term => {
        const li = document.createElement('li');
        li.className = 'search-history__item';
        li.innerHTML = `
            <i class="fi fi-rr-search"></i>
            <span class="search-history__text">${escapeHtml(term)}</span>
        `;
        
        li.addEventListener('mousedown', async (e) => {
            e.preventDefault(); // Prevent blur
            input.value = term;
            dropdown.classList.remove('is-visible');
            await performSearch(term);
        });
        
        list.appendChild(li);
    });

    dropdown.classList.add('is-visible');
}

/**
 * Saves search term to localStorage history.
 * @param {string} term - Search term to save
 */
function saveSearchHistory(term) {
    if (!term || term.length < 2) return;

    let history = getSearchHistory();
    
    history = history.filter(item => item !== term);
    history.unshift(term);
    history = history.slice(0, MAX_HISTORY_ITEMS);
    
    localStorage.setItem(STORAGE_KEY, JSON.stringify(history));
}

/**
 * Gets search history from localStorage.
 * @returns {string[]} Array of search terms
 */
function getSearchHistory() {
    try {
        const history = localStorage.getItem(STORAGE_KEY);
        return history ? JSON.parse(history) : [];
    } catch (e) {
        console.error('Failed to load search history:', e);
        return [];
    }
}

/**
 * Clears all search history.
 */
function clearSearchHistory() {
    localStorage.removeItem(STORAGE_KEY);
}

/**
 * Escapes HTML to prevent XSS.
 * @param {string} str - String to escape
 * @returns {string} Escaped string
 */
function escapeHtml(str) {
    const div = document.createElement('div');
    div.textContent = str;
    return div.innerHTML;
}

/**
 * Performs product search via AJAX with loading state.
 * @param {string} searchTerm - The search term
 */
async function performSearch(searchTerm) {
    if (isSearching) return;
    
    isSearching = true;
    showLoader();

    try {
        const url = `${ProductWebConstants.Endpoint.PRODUCT_SEARCH}?q=${encodeURIComponent(searchTerm)}`;
        const response = await fetch(url);
        
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }

        const html = await response.text();
        const container = document.querySelector(CATALOG_CONTAINER);
        
        // Smooth transition
        await new Promise(resolve => setTimeout(resolve, 150));
        
        container.innerHTML = html;

        // Update header
        updateSearchHeader(searchTerm);

        // Clear active category
        document.querySelectorAll(`${CATEGORY_ITEM}.${ACTIVE_CLASS}`)
            .forEach(c => c.classList.remove(ACTIVE_CLASS));

        // Re-attach event listeners
        await initProductModalTriggers();

    } catch (error) {
        console.error('Search failed:', error);
        showErrorMessage('Không thể tìm kiếm sản phẩm. Vui lòng thử lại.');
    } finally {
        hideLoader();
        isSearching = false;
    }
}

/**
 * Reloads the default category when search is cleared.
 */
async function reloadDefaultCategory() {
    const firstCategory = document.querySelector(CATEGORY_ITEM);
    if (!firstCategory) return;

    const categorySlug = firstCategory.dataset.categorySlug;
    if (!categorySlug) return;

    try {
        const container = document.querySelector(CATALOG_CONTAINER);
        container.style.opacity = '0.5';

        const url = `${ProductWebConstants.Endpoint.PRODUCTS_BY_CATEGORY}/${categorySlug}`;
        const response = await fetch(url);
        
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }

        const html = await response.text();
        
        await new Promise(resolve => setTimeout(resolve, 100));
        
        container.innerHTML = html;
        container.style.opacity = '1';

        // Restore first category as active
        document.querySelectorAll(`${CATEGORY_ITEM}.${ACTIVE_CLASS}`)
            .forEach(c => c.classList.remove(ACTIVE_CLASS));
        firstCategory.classList.add(ACTIVE_CLASS);

        // Update header
        updateCategoryHeader(firstCategory);

        // Re-attach event listeners
        await initProductModalTriggers();

    } catch (error) {
        console.error('Failed to reload category:', error);
    }
}

/**
 * Updates the category header for search results.
 * @param {string} searchTerm - The search term
 */
function updateSearchHeader(searchTerm) {
    const titleEl = document.querySelector(CATEGORY_TITLE);
    const metaEl = document.querySelector(CATEGORY_META);

    if (titleEl) {
        titleEl.textContent = `Kết quả tìm kiếm: "${searchTerm}"`;
    }

    if (metaEl) {
        const productCount = document.querySelectorAll('[data-product-card]').length;
        metaEl.textContent = productCount === 0 
            ? 'Không tìm thấy sản phẩm' 
            : productCount === 1 
                ? '1 sản phẩm' 
                : `${productCount} sản phẩm`;
    }
}

/**
 * Updates the category header with category name and product count.
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
