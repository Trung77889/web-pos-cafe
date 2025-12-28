/**
 * ------------------------------------------------------------
 * Module: Product Card
 * ------------------------------------------------------------
 * @description
 * Hydrates product cards from data-* attributes with price display
 * and promotional badge. Designed to work with SSR markup.
 *
 * @example
 * import { initProductCards } from './product-card.js';
 * initProductCards();
 *
 * @version 1.0.1
 * @since 1.0.0
 * @lastModified 12/26/2025
 * @module product-card
 * @author Dang Van Trung
 */
// Product Card Module - Handles dynamic pricing and discount calculation

/** Parse price from mixed string/number into integer. */
function parsePrice(priceString) {
  if (typeof priceString === "number") return priceString;
  const numeric = (priceString || "").toString().replace(/[^\d]/g, "");
  return parseInt(numeric, 10) || 0;
}

/** Calculate rounded discount percent. */
function calculateDiscountPercent(currentPrice, originalPrice) {
  if (!originalPrice || originalPrice <= currentPrice) return 0;
  const discount = ((originalPrice - currentPrice) / originalPrice) * 100;
  return Math.round(discount);
}

/** Format price to vi-VN with đ suffix. */
function formatPriceDisplay(price) {
  return new Intl.NumberFormat("vi-VN").format(price) + "đ";
}

/** Initialize product card hydration and badge calculation. */
function initProductCards() {
  const cards = document.querySelectorAll("[data-product-card]");

  cards.forEach((card) => {
    // Support both old and new attribute names for backward compatibility
    const currentPrice = parsePrice(
      card.dataset.productCurrentPrice ||
        card.dataset.productPrice?.replace(/[^\d]/g, "") ||
        "0"
    );
    const originalPrice = parsePrice(
      card.dataset.productOriginalPrice ||
        card.dataset.productOldPrice?.replace(/[^\d]/g, "") ||
        "0"
    );

    // Update price displays
    const currentPriceEl = card.querySelector(".product-card__price-current");
    const originalPriceEl =
      card.querySelector(".product-card__price-original") ||
      card.querySelector(".product-card__price-old");
    const badgeEl = card.querySelector(".product-card__badge");
    const badgeTextEl = badgeEl?.querySelector("p");

    if (currentPriceEl) {
      currentPriceEl.textContent = formatPriceDisplay(currentPrice);
    }

    if (originalPriceEl) {
      if (originalPrice > currentPrice && originalPrice > 0) {
        originalPriceEl.textContent = formatPriceDisplay(originalPrice);
        originalPriceEl.style.display = "inline";
      } else {
        originalPriceEl.style.display = "none";
      }
    }

    // Calculate and display discount badge or sold-out badge
    if (badgeEl && badgeTextEl) {
      const isSoldOut =
        card.dataset.soldOut === "true" ||
        card.dataset.availabilityStatus === "sold_out";

      if (isSoldOut) {
        badgeTextEl.textContent = "Tạm hết";
        badgeEl.style.display = "block";
      } else {
        const discountPercent = calculateDiscountPercent(
          currentPrice,
          originalPrice
        );
        if (discountPercent > 0) {
          badgeTextEl.textContent = `Giảm ${discountPercent}%`;
          badgeEl.style.display = "block";
        } else {
          badgeEl.style.display = "none";
        }
      }
    }

    // Update data attributes for product modal compatibility
    card.dataset.productPrice = formatPriceDisplay(currentPrice);
    card.dataset.productOldPrice =
      originalPrice > currentPrice ? formatPriceDisplay(originalPrice) : "";
  });
}

export { initProductCards };
