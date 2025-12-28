/**
 * ------------------------------------------------------------
 * Module: Page Loader Utility
 * ------------------------------------------------------------
 * @description
 * Reusable utility for showing/hiding full-page loading overlay.
 * Can be used across the application for any async operations.
 *
 * @example
 * import { showLoader, hideLoader } from './page-loader.js';
 * 
 * showLoader();
 * await fetchData();
 * hideLoader();
 *
 * @version 1.0.0
 * @since 1.0.0
 * @lastModified 28/12/2025
 * @module page-loader
 * @author Dang Van Trung
 */

const PAGE_LOADER = '[data-page-loader]';

/**
 * Shows the full-page loading overlay.
 */
export function showLoader() {
    const loader = document.querySelector(PAGE_LOADER);
    if (loader) {
        loader.classList.add('is-active');
        // Prevent body scrolling while loading
        document.body.style.overflow = 'hidden';
    }
}

/**
 * Hides the full-page loading overlay.
 */
export function hideLoader() {
    const loader = document.querySelector(PAGE_LOADER);
    if (loader) {
        loader.classList.remove('is-active');
        // Restore body scrolling
        document.body.style.overflow = '';
    }
}

/**
 * Shows loader, executes async function, then hides loader.
 * Automatically handles errors and ensures loader is hidden.
 * 
 * @param {Function} asyncFn - Async function to execute
 * @returns {Promise<any>} Result from asyncFn
 * 
 * @example
 * await withLoader(async () => {
 *   return await fetch('/api/data');
 * });
 */
export async function withLoader(asyncFn) {
    showLoader();
    try {
        return await asyncFn();
    } finally {
        hideLoader();
    }
}
