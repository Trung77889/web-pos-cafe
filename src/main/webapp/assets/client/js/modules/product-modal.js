/**
 * ------------------------------------------------------------
 * Module: Product Modal
 * ------------------------------------------------------------
 * @description
 * Manages product detail modal with options, quantity, and add-to-cart.
 * Uses shared Modal system for loading and display.
 *
 * @version 2.0.0
 * @since 1.0.0
 * @lastModified 28/12/2025
 * @module product-modal
 * @author Dang Van Trung
 */

import { ProductWebConstants } from "./web-constants.js";
import { CART_KEY, formatPrice } from "./cart.js";

const parsePrice = (value) => {
  const numeric = (value || "").toString().replace(/[^\d]/g, "");
  return parseInt(numeric, 10) || 0;
};

/**
 * Custom URL generator for product modal (SEO-friendly path param)
 */
const productModalUrl = (productSlug) => {
  return `${ProductWebConstants.Endpoint.PRODUCT_MODAL}/${productSlug}`;
};

/**
 * Initializes product cards to open modal on click
 */
export function initProductModal() {
  const cards = document.querySelectorAll("[data-product-card]");

  cards.forEach((card) => {
    card.addEventListener("click", async () => {
      const productSlug = card.dataset.productSlug;
      if (!productSlug) return;

      // Silently ignore sold-out products (no toast)
      if (card.dataset.soldOut === "true") {
        return; // Do nothing - gray overlay already visible
      }

      try {
        // Fetch modal HTML from server
        const response = await fetch(productModalUrl(productSlug), {
          headers: { "Accept": "text/html" }
        });

        if (!response.ok) {
          throw new Error(`HTTP ${response.status}`);
        }

        const modalHTML = await response.text();
        
        // Inject modal into container
        const container = document.querySelector("#modal-container");
        if (!container) {
          console.error("Modal container not found");
          return;
        }

        container.innerHTML = modalHTML;
        const modalEl = container.querySelector(".modal");
        if (!modalEl) {
          console.error("Modal element not found in response");
          return;
        }

        // Get base price
        const priceEl = modalEl.querySelector("[data-modal-price]");
        const basePrice = parsePrice(priceEl?.textContent || "0");

        // Setup modal interactions
        setupModalHandlers(modalEl, basePrice);

        // Show modal
        const bsModal = bootstrap.Modal.getOrCreateInstance(modalEl);
        bsModal.show();

        // Cleanup on hide
        modalEl.addEventListener("hidden.bs.modal", () => {
          resetModalState(modalEl);
          modalEl.remove();
        }, { once: true });

      } catch (error) {
        console.error("Failed to load product modal:", error);
        showError("Không thể tải thông tin sản phẩm");
      }
    });
  });
}

/**
 * Sets up all modal event handlers
 */
function setupModalHandlers(modalEl, basePrice) {
  initOptionHandlers(modalEl);
  initQuantityStepper(modalEl);
  initAddToCartButton(modalEl, basePrice);
  updateTotals(modalEl, basePrice);
}

/**
 * Handles option selection (single/multi)
 */
function initOptionHandlers(modalEl) {
  const items = modalEl.querySelectorAll("[data-option-item]");

  items.forEach((item) => {
    item.addEventListener("click", () => {
      const group = item.dataset.optionGroup;
      const type = item.dataset.optionType;
      const section = item.closest(".product-modal__section");

      if (type === "single") {
        // Deselect all in group
        modalEl.querySelectorAll(`[data-option-group="${group}"]`)
          .forEach(el => {
            el.classList.remove("is-active");
            el.querySelector(".option-row__check")?.classList.remove("is-active");
          });
        // Select clicked
        item.classList.add("is-active");
      } else if (type === "multi") {
        // Toggle multi-select with max check
        const maxSelect = parseInt(section.querySelector(".product-modal__section-sub")
          ?.textContent.match(/\d+/)?.[0] || "999");
        const groupItems = modalEl.querySelectorAll(`[data-option-group="${group}"]`);
        const currentActive = Array.from(groupItems).filter(el => el.classList.contains("is-active")).length;
        
        // Clicking on already selected item (deselect)
        if (item.classList.contains("is-active")) {
          item.classList.remove("is-active");
          item.classList.remove("is-disabled");
          
          // Re-enable other disabled items
          groupItems.forEach(el => {
            if (!el.classList.contains("is-active")) {
              el.classList.remove("is-disabled");
            }
          });
        } 
        // Clicking on unselected item
        else {
          if (currentActive >= maxSelect) {
            return; // Max reached, do nothing
          }
          
          item.classList.add("is-active");
          
          // Check if max reached after this selection
          const newActive = currentActive + 1;
          if (newActive >= maxSelect) {
            // Disable unselected items
            groupItems.forEach(el => {
              if (!el.classList.contains("is-active")) {
                el.classList.add("is-disabled");
              }
            });
          }
        }
      }

      // Update check icon
      const check = item.querySelector(".option-row__check");
      if (check) {
        check.classList.toggle("is-active", item.classList.contains("is-active"));
      }

      updateSectionBadge(section);
      updateTotals(modalEl, getBasePrice(modalEl));
    });
  });
}

/**
 * Handles quantity +/- buttons
 */
function initQuantityStepper(modalEl) {
  const minus = modalEl.querySelector("[data-modal-minus]");
  const plus = modalEl.querySelector("[data-modal-plus]");
  const qtyEl = modalEl.querySelector("[data-modal-qty]");

  if (!qtyEl) return;

  const updateQty = (delta) => {
    const current = parseInt(qtyEl.textContent) || 1;
    const newQty = Math.max(1, Math.min(99, current + delta));
    qtyEl.textContent = newQty;
    updateTotals(modalEl, getBasePrice(modalEl));
  };

  minus?.addEventListener("click", () => updateQty(-1));
  plus?.addEventListener("click", () => updateQty(1));
}

/**
 * Handles add to cart button click
 */
function initAddToCartButton(modalEl, basePrice) {
  const actionBtn = modalEl.querySelector("[data-modal-action]");
  if (!actionBtn) return;

  actionBtn.addEventListener("click", async () => {
    // Validate required options
    const validation = validateRequired(modalEl);
    if (!validation.valid) {
      showError(`Vui lòng chọn ${validation.missing}`);
      highlightSection(modalEl, validation.missing);
      return;
    }

    // Build cart item
    const item = buildCartItem(modalEl, basePrice);
    
    // Save to cart
    await saveToCart(item);

    // Close modal
    bootstrap.Modal.getInstance(modalEl)?.hide();
  });
}

/**
 * Validates required option sections
 */
function validateRequired(modalEl) {
  const sections = modalEl.querySelectorAll(".product-modal__section");
  
  for (const section of sections) {
    const subEl = section.querySelector(".product-modal__section-sub");
    const isRequired = subEl?.textContent.includes("Bắt buộc") || subEl?.textContent.includes("Required");
    
    if (!isRequired) continue;

    const firstItem = section.querySelector("[data-option-item]");
    const group = firstItem?.dataset.optionGroup;
    const selected = section.querySelectorAll(`[data-option-group="${group}"].is-active`).length;

    if (selected === 0) {
      const headerText = section.querySelector(".product-modal__section-header span")?.textContent;
      return { valid: false, missing: headerText || "tùy chọn" };
    }
  }

  return { valid: true };
}

/**
 * Builds cart item object from modal state
 */
function buildCartItem(modalEl, basePrice) {
  const nameEl = modalEl.querySelector("[data-modal-name]");
  const imageEl = modalEl.querySelector("[data-modal-image]");
  const qtyEl = modalEl.querySelector("[data-modal-qty]");
  const noteEl = modalEl.querySelector("[data-modal-note]");

  const qty = parseInt(qtyEl?.textContent || "1");
  const options = getSelectedOptions(modalEl);
  const optionsPrice = sumOptionPrices(options);
  const total = (basePrice + optionsPrice) * qty;

  return {
    name: nameEl?.textContent || "",
    image: imageEl?.src || "",
    qty,
    note: noteEl?.value || "",
    basePrice,
    optionsPrice,
    total,
    options
  };
}

/**
 * Gets selected options from modal
 */
function getSelectedOptions(modalEl) {
  const options = { single: {}, multi: {} };
  const items = modalEl.querySelectorAll("[data-option-item].is-active");

  items.forEach((item) => {
    const group = item.dataset.optionGroup;
    const type = item.dataset.optionType;
    const value = item.dataset.optionValue;
    const price = parsePrice(item.dataset.optionPrice);

    if (type === "single") {
      options.single[group] = { label: value, price };
    } else if (type === "multi") {
      if (!options.multi[group]) options.multi[group] = [];
      options.multi[group].push({ label: value, price });
    }
  });

  return options;
}

/**
 * Sums all option prices
 */
function sumOptionPrices(options) {
  let total = 0;
  Object.values(options.single).forEach(opt => total += opt.price || 0);
  Object.values(options.multi).forEach(arr => 
    arr.forEach(opt => total += opt.price || 0)
  );
  return total;
}

/**
 * Updates total price display
 */
function updateTotals(modalEl, basePrice) {
  const qtyEl = modalEl.querySelector("[data-modal-qty]");
  const totalEl = modalEl.querySelector("[data-modal-total]");

  const qty = parseInt(qtyEl?.textContent || "1");
  const options = getSelectedOptions(modalEl);
  const total = (basePrice + sumOptionPrices(options)) * qty;

  if (totalEl) {
    totalEl.textContent = formatPrice(total);
  }
}

/**
 * Updates section badge state (valid/invalid)
 */
function updateSectionBadge(section) {
  const subEl = section.querySelector(".product-modal__section-sub");
  if (!subEl) return;

  const firstItem = section.querySelector("[data-option-item]");
  const type = firstItem?.dataset.optionType;
  const group = firstItem?.dataset.optionGroup;
  const selected = section.querySelectorAll(`[data-option-group="${group}"].is-active`).length;

  if (selected > 0) {
    subEl.classList.add("is-dirty", "is-valid");
    subEl.classList.remove("is-invalid");
  } else {
    subEl.classList.remove("is-dirty", "is-valid", "is-invalid");
  }
}

/**
 * Gets base price from modal
 */
function getBasePrice(modalEl) {
  const priceEl = modalEl.querySelector("[data-modal-price]");
  return parsePrice(priceEl?.textContent || "0");
}

/**
 * Resets modal to default state
 */
function resetModalState(modalEl) {
  const qtyEl = modalEl.querySelector("[data-modal-qty]");
  const noteEl = modalEl.querySelector("[data-modal-note]");

  if (qtyEl) qtyEl.textContent = "1";
  if (noteEl) noteEl.value = "";

  modalEl.querySelectorAll("[data-option-item]").forEach(item => {
    item.classList.remove("is-active");
    item.querySelector(".option-row__check")?.classList.remove("is-active");
  });
}

/**
 * Highlights a section (for validation feedback)
 */
function highlightSection(modalEl, sectionName) {
  const sections = modalEl.querySelectorAll(".product-modal__section");
  for (const section of sections) {
    const headerText = section.querySelector(".product-modal__section-header span")?.textContent;
    if (headerText?.includes(sectionName)) {
      section.scrollIntoView({ behavior: "smooth", block: "center" });
      section.classList.add("flash-highlight");
      setTimeout(() => section.classList.remove("flash-highlight"), 1800);
      break;
    }
  }
}

/**
 * Saves item to localStorage cart
 */
async function saveToCart(item) {
  const cart = JSON.parse(localStorage.getItem(CART_KEY) || "[]");
  cart.push(item);
  localStorage.setItem(CART_KEY, JSON.stringify(cart));

  // Reload cart display
  const { loadCartFromStorage } = await import("./cart.js");
  loadCartFromStorage?.();

  // Show success toast
  const { Toast } = await import("../../../shared/js/modules/toast.js");
  Toast.success("Đã thêm sản phẩm vào giỏ hàng");
}

/**
 * Shows error toast
 */
async function showError(message) {
  try {
    const { Toast } = await import("../../../shared/js/modules/toast.js");
    Toast.error(message);
  } catch (e) {
    alert(message);
  }
}
