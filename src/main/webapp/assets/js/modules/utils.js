/**
 * ------------------------------------------------------------
 * Module: Utility Functions
 * ------------------------------------------------------------
 * @description
 * Provides helper utilities for DOM manipulation and console logging.
 * Includes:
 * - `getOrCreateContainer()` to find or create a DOM container by selector.
 * - `setupLogging()` to enhance or disable console output depending on the environment.
 * When in development mode, console logs are colorized and mirrored as on-screen toasts.
 *
 * @example
 * setupLogging();
 * const container = getOrCreateContainer('.toast-container');
 *
 * @version 1.0.0
 * @since 1.0.0
 * @lastModified 25/10/2025
 * @module utils
 * @author Dang Van Trung
 */

import {Toast} from './toast.js';

/**
 * Finds an existing DOM element by selector or creates it if not found.
 * Automatically sets the element’s ID and class names based on the selector.
 *
 * @param {string} selector - CSS selector (e.g., '#container', '.toast-container').
 * @returns {HTMLElement} The found or newly created DOM element.
 */
export function getOrCreateContainer(selector) {
    let el = document.querySelector(selector);
    if (el) return el;

    el = document.createElement('div');
    const idMatch = selector.match(/#([\w-]+)/);
    if (idMatch && idMatch[1]) {
        el.id = idMatch[1];
    }
    const classMatches = selector.match(/\.([\w-]+)/g);
    if (classMatches) {
        const classes = classMatches.map(c => c.substring(1));
        el.classList.add(...classes);
    }
    document.body.appendChild(el);
    return el;
}

/**
 * Sets up enhanced console logging based on the environment mode.
 * In development mode, logs are colorized and displayed as toast messages.
 * In production mode, all console outputs are disabled for cleaner UX.
 */
export function setupLogging() {
    const MODE = window.__APP_MODE__ || 'production';
    const isDev = MODE === 'development';

    // Copy original console methods to prevent recursion.
    const original = Object.assign({}, console);

    if (!isDev) {
        // Production mode → disable all console outputs.
        console.log = console.info = console.warn = console.debug = () => {
        };
        console.error = () => {
        };
        return;
    }

    /** Prints messages with colored prefix in the browser console. */
    function printWithStyle(type, color, ...args) {
        const prefix = `[${type.toUpperCase()}]`;
        const style = `color:${color};font-weight:bold`;
        original[type](`%c${prefix}`, style, ...args);
    }

    /** Converts any log arguments into a readable multiline string. */
    function buildMessage(args) {
        return args
            .map(a => (a instanceof Error ? a.message : String(a)))
            .join('\n');
    }

    // Override console methods in development mode.
    console.log = (...args) => {
        printWithStyle('log', '#00bcd4', ...args);
        Toast.normal('', buildMessage(args));
    };

    console.info = (...args) => {
        printWithStyle('info', '#4caf50', ...args);
        Toast.info(buildMessage(args));
    };

    console.warn = (...args) => {
        printWithStyle('warn', '#ff9800', ...args);
        Toast.warn(buildMessage(args));
    };

    console.error = (...args) => {
        const error = args.find(a => a instanceof Error) || args[0];
        printWithStyle('error', '#f44336', error);
        Toast.error(buildMessage(args));
    };
}