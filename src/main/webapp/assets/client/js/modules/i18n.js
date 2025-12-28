/**
 * ------------------------------------------------------------
 * Module: i18n - Internationalization for Client-Side Validation
 * ------------------------------------------------------------
 * @description
 * Provides simple internationalization support for client-side
 * form validation messages. Supports English and Vietnamese.
 *
 * @example
 * import { getValidationMessages } from './modules/i18n.js';
 * const messages = getValidationMessages('en');
 * console.log(messages.emailRequired);
 *
 * @version 1.0.0
 * @since 1.0.0
 * @lastModified 28/12/2025
 * @module i18n
 * @author Dang Van Trung
 */

/**
 * Validation messages in English
 */
const validationMessages_en = {
  emailRequired: "Email is required",
  emailInvalid: "Please enter a valid email address",
  passwordRequired: "Password is required",
  passwordMinLength: "Password must be at least 6 characters",
  confirmPasswordRequired: "Please confirm your password",
  passwordMismatch: "Passwords do not match",
  termsRequired: "You must agree to the terms of use",
};

/**
 * Validation messages in Vietnamese
 */
const validationMessages_vi = {
  emailRequired: "Email là bắt buộc",
  emailInvalid: "Vui lòng nhập địa chỉ email hợp lệ",
  passwordRequired: "Mật khẩu là bắt buộc",
  passwordMinLength: "Mật khẩu phải có ít nhất 6 ký tự",
  confirmPasswordRequired: "Vui lòng xác nhận mật khẩu của bạn",
  passwordMismatch: "Mật khẩu không khớp",
  termsRequired: "Bạn phải đồng ý với điều khoản sử dụng",
};

/**
 * Get validation messages for specified locale
 * @param {string} locale - The locale code ('en' or 'vi')
 * @returns {object} Validation messages object
 */
export function getValidationMessages(locale = "vi") {
  switch (locale) {
    case "en":
      return validationMessages_en;
    case "vi":
      return validationMessages_vi;
    default:
      return validationMessages_vi; // Default to Vietnamese
  }
}

/**
 * Get all available locales
 * @returns {string[]} Array of available locale codes
 */
export function getAvailableLocales() {
  return ["en", "vi"];
}
