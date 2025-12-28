/**
 * ------------------------------------------------------------
 * Module: Cart Interactions
 * ------------------------------------------------------------
 * @description
 * Manages cart UI and persistence: load, edit/remove items,
 * loyalty toggle, payment logging, and summary rendering.
 *
 * @example
 * import { initCartInteractions } from './cart.js';
 * initCartInteractions();
 *
 * @version 1.0.1
 * @since 1.0.0
 * @lastModified 12/26/2025
 * @module cart
 * @author Dang Van Trung
 */
import { Modal } from "../../../shared/js/modules/modal.js";

const CART_KEY = "pos_cart";

/** Format value as Vietnamese currency string. */
const formatPrice = (value) => {
  if (typeof value === "number") {
    return `${value.toLocaleString("vi-VN")} Đ`;
  }
  return value;
};

/** Bind quantity stepper interactions for a cart item. */
export function bindStepper(stepper, cartItemIndex) {
  const minus = stepper.querySelector("[data-stepper-minus]");
  const plus = stepper.querySelector("[data-stepper-plus]");
  const value = stepper.querySelector("[data-stepper-value]");

  if (!minus || !plus || !value) return;

  const updateValue = (newValue) => {
    const qty = Math.max(1, Math.min(99, Math.floor(newValue) || 1));

    // If quantity reaches 0 or below, remove the item
    if (qty <= 0) {
      if (cartItemIndex !== undefined) {
        removeCartItem(cartItemIndex);
      }
      return;
    }

    value.textContent = qty.toString();

    // Update cart in localStorage
    if (cartItemIndex !== undefined) {
      const cart = JSON.parse(localStorage.getItem(CART_KEY) || "[]");
      if (cart[cartItemIndex]) {
        cart[cartItemIndex].qty = qty;
        // Recalculate total if needed
        const item = cart[cartItemIndex];
        const basePrice = parseFloat(item.basePrice || 0);
        const optionsPrice = parseFloat(item.optionsPrice || 0);
        item.total = (basePrice + optionsPrice) * qty;
        localStorage.setItem(CART_KEY, JSON.stringify(cart));

        // Update price display
        const cartItem = stepper.closest(".cart-item");
        const priceEl = cartItem?.querySelector(".cart-price");
        if (priceEl) {
          priceEl.textContent = formatPrice(item.total);
        }

        // Update cart summary
        updateCartSummary();
      }
    }
  };

  // Handle button clicks
  minus.addEventListener("click", () => {
    const current = parseInt(value.textContent, 10) || 1;
    updateValue(current - 1);
  });

  plus.addEventListener("click", () => {
    const current = parseInt(value.textContent, 10) || 1;
    updateValue(current + 1);
  });

  // Handle direct keyboard input
  value.addEventListener("blur", () => {
    const inputValue = parseInt(value.textContent, 10);
    if (isNaN(inputValue) || inputValue < 1) {
      updateValue(1);
    } else if (inputValue > 99) {
      updateValue(99);
    } else {
      updateValue(inputValue);
    }
  });

  value.addEventListener("keydown", (e) => {
    // Allow Enter to blur and save
    if (e.key === "Enter") {
      e.preventDefault();
      value.blur();
    }
    // Allow Escape to cancel and revert
    if (e.key === "Escape") {
      e.preventDefault();
      const cart = JSON.parse(localStorage.getItem(CART_KEY) || "[]");
      if (cart[cartItemIndex]) {
        value.textContent = cart[cartItemIndex].qty.toString();
      }
      value.blur();
    }
  });
}

/** Render a cart item row (HTML string) */
function renderCartItem(item, index) {
  // Format options with labels, each group on separate line
  const optionsRows = [];

  // Helper to capitalize first letter
  const capitalize = (str) => str.charAt(0).toUpperCase() + str.slice(1);

  // Handle new flexible format
  if (item.options) {
    // Display all single-choice options
    if (item.options.single) {
      Object.keys(item.options.single).forEach((group) => {
        const value = item.options.single[group];
        if (value) {
          const label = capitalize(group);
          optionsRows.push(
            `<p class="mb-0"><span class="cart-meta__label">${label}:</span> ${value}</p>`
          );
        }
      });
    }

    // Display all multi-choice options
    if (item.options.multi) {
      Object.keys(item.options.multi).forEach((group) => {
        const values = item.options.multi[group];
        if (Array.isArray(values) && values.length > 0) {
          const label = capitalize(group);
          const valuesText = values.join(", ");
          optionsRows.push(
            `<p class="mb-0"><span class="cart-meta__label">${label}:</span> ${valuesText}</p>`
          );
        }
      });
    }
  } else {
    // Fallback for old format (backward compatibility)
    if (item.size) {
      optionsRows.push(
        `<p class="mb-0"><span class="cart-meta__label">Size:</span> ${item.size}</p>`
      );
    }

    if (item.ice) {
      optionsRows.push(
        `<p class="mb-0"><span class="cart-meta__label">Đá:</span> ${item.ice}</p>`
      );
    }

    if (item.sweetness) {
      optionsRows.push(
        `<p class="mb-0"><span class="cart-meta__label">Độ ngọt:</span> ${item.sweetness}</p>`
      );
    }

    if (item.sugar) {
      optionsRows.push(
        `<p class="mb-0"><span class="cart-meta__label">Đường:</span> ${item.sugar}</p>`
      );
    }

    if (item.toppings && item.toppings.length > 0) {
      const toppingsText = item.toppings.join(", ");
      optionsRows.push(
        `<p class="mb-0"><span class="cart-meta__label">Topping:</span> ${toppingsText}</p>`
      );
    }
  }

  if (item.note) {
    optionsRows.push(
      `<p class="mb-0 cart-note"><span class="cart-meta__label">Ghi chú:</span> ${item.note}</p>`
    );
  }

  return `
    <div class="cart-item" data-cart-item-index="${index}">
      <img src="${item.image}" class="cart-thumb" alt="${item.name}" />
      <div class="flex-grow-1 d-flex gap-4">
        <div class="flex-grow-1">
          <div class="d-flex gap-1 align-items-start mb-0">
            <p class="cart-name">${item.name}</p>
          </div>
          <div class="cart-meta">
            ${optionsRows.join("")}
          </div>
          <div class="cart-item__actions">
            <button class="cart-item__action-btn" type="button" data-cart-edit data-cart-index="${index}">
              <i class="fi fi-rr-pencil icon-base"></i>
              <span>Sửa</span>
            </button>
            <button class="cart-item__action-btn" type="button" data-cart-remove data-cart-index="${index}">
              <i class="fi fi-rr-trash icon-base"></i>
              <span>Xóa</span>
            </button>
          </div>
        </div>
        <div class="text-end">
          <div class="cart-price">${formatPrice(item.total)}</div>
          <div class="cart-stepper" data-stepper>
            <button class="cart-stepper__btn" type="button" data-stepper-minus>
              <span class="icon-base"><i class="fi fi-rr-minus-small"></i></span>
            </button>
            <span class="cart-stepper__value" data-stepper-value contenteditable="true">${
              item.qty || 1
            }</span>
            <button class="cart-stepper__btn" type="button" data-stepper-plus>
              <span class="icon-base"><i class="fi fi-rr-plus-small"></i></span>
            </button>
          </div>
        </div>
      </div>
    </div>
  `;
}

/** Load cart items from localStorage into desktop & mobile lists. */
export function loadCartFromStorage() {
  // Load for both desktop and mobile views
  const desktopList = document.querySelector(".cart-panel [data-cart-list]");
  const mobileList = document.querySelector(
    ".mobile-cart-drawer [data-cart-list]"
  );

  const cart = JSON.parse(localStorage.getItem(CART_KEY) || "[]");
  const isEmpty = cart.length === 0;

  // Update both lists
  [desktopList, mobileList].forEach((list) => {
    if (!list) return;

    list.innerHTML = "";

    if (isEmpty) {
      list.innerHTML = `
        <div class="cart-empty">
          <div class="cart-empty__icon">
            <i class="fi fi-rr-shopping-cart icon-base"></i>
          </div>
          <p class="cart-empty__text">Giỏ hàng trống, đặt gì uống giải khát nha bạn ơi</p>
        </div>
      `;
    } else {
      cart.forEach((item, index) => {
        list.insertAdjacentHTML("beforeend", renderCartItem(item, index));
        const cartItem = list.lastElementChild;
        const stepper = cartItem?.querySelector("[data-stepper]");
        if (stepper) bindStepper(stepper, index);
      });
    }
  });

  // Show/hide View Cart buttons based on cart state
  document.querySelectorAll("[data-view-cart]").forEach((btn) => {
    if (btn) {
      btn.style.display = isEmpty ? "none" : "";
    }
  });

  // Show/hide cart summary based on cart state
  document.querySelectorAll(".cart-summary").forEach((summary) => {
    if (summary) {
      summary.style.display = isEmpty ? "none" : "";
    }
  });

  // Update cart count badges
  updateCartBadges(cart);
  updateCartSummary();
}

/** Recompute subtotal, discount and total; update summary UI. */
export function updateCartSummary() {
  const cart = JSON.parse(localStorage.getItem(CART_KEY) || "[]");
  const loyaltyPoints = 300; // This will be computed later
  const useLoyaltyPoints =
    document.querySelector("[data-loyalty-toggle]")?.checked || false;

  // Calculate subtotal
  const subtotal = cart.reduce((sum, item) => sum + (item.total || 0), 0);
  const totalItems = cart.reduce((sum, item) => sum + (item.qty || 0), 0);

  // Calculate discount from loyalty points
  const loyaltyDiscount = useLoyaltyPoints ? loyaltyPoints : 0;
  const total = Math.max(0, subtotal - loyaltyDiscount);

  // Update summary display (for both desktop and mobile)
  const totalRows = document.querySelectorAll("[data-cart-total]");

  totalRows.forEach((totalRow) => {
    if (totalRow) {
      totalRow.textContent = formatPrice(total);
    }
  });

  // Mobile-specific summary rows
  const subtotalRow = document.querySelector(
    ".mobile-cart-drawer [data-cart-subtotal]"
  );
  const discountRow = document.querySelector(
    ".mobile-cart-drawer [data-cart-discount]"
  );

  if (subtotalRow) {
    const countText =
      totalItems > 0
        ? ` (${totalItems} ${totalItems === 1 ? "phần" : "phần"})`
        : "";
    subtotalRow.textContent = formatPrice(subtotal);
    const label = subtotalRow
      .closest(".cart-summary__row")
      ?.querySelector("span:first-child");
    if (label) label.textContent = `Tạm tính${countText}`;
  }

  if (discountRow) {
    if (loyaltyDiscount > 0) {
      discountRow.textContent = `- ${formatPrice(loyaltyDiscount)}`;
      discountRow.closest(".cart-summary__row")?.classList.remove("d-none");
    } else {
      discountRow.closest(".cart-summary__row")?.classList.add("d-none");
    }
  }

  // Update cart count badges
  updateCartBadges(cart);
}

// New function to update all cart count badges
/** Update cart count badges (panel + mobile). */
function updateCartBadges(cart) {
  const totalItems = cart.reduce((sum, item) => sum + (item.qty || 0), 0);

  // Update all cart count displays (desktop panel + mobile drawer)
  const cartCounts = document.querySelectorAll(".cart-count");
  cartCounts.forEach((countEl) => {
    if (countEl) {
      countEl.textContent = totalItems > 0 ? `(${totalItems})` : "";
    }
  });

  // Update mobile bottom nav badge
  const mobileBadge = document.querySelector("[data-cart-badge]");
  if (mobileBadge) {
    if (totalItems > 0) {
      mobileBadge.textContent = totalItems > 99 ? "99+" : totalItems;
      mobileBadge.style.display = "flex";
    } else {
      mobileBadge.style.display = "none";
    }
  }
}

/** Remove a cart item by index and refresh UI */
export function removeCartItem(index) {
  const cart = JSON.parse(localStorage.getItem(CART_KEY) || "[]");
  if (index >= 0 && index < cart.length) {
    cart.splice(index, 1);
    localStorage.setItem(CART_KEY, JSON.stringify(cart));
    loadCartFromStorage();
    updateCartSummary();
  }
}

/** Initialize cart UI bindings and edit/remove handlers. */
export function initCartInteractions() {
  // Load cart from localStorage (this will also update summary)
  loadCartFromStorage();

  // Handle edit and remove button clicks
  document.addEventListener("click", async (e) => {
    // Handle remove button clicks
    const removeBtn = e.target.closest("[data-cart-remove]");
    if (removeBtn) {
      e.preventDefault();
      e.stopPropagation();
      const index = parseInt(removeBtn.dataset.cartIndex, 10);
      if (isNaN(index)) return;

      removeCartItem(index);
      return;
    }

    // Prevent edit when clicking on quantity stepper
    if (e.target.closest("[data-stepper]")) {
      return;
    }

    // Handle edit button clicks
    const editBtn = e.target.closest("[data-cart-edit]");
    if (editBtn) {
      e.preventDefault();
      e.stopPropagation();
      const index = parseInt(editBtn.dataset.cartIndex, 10);
      if (isNaN(index)) return;

      const cart = JSON.parse(localStorage.getItem(CART_KEY) || "[]");
      const item = cart[index];
      if (!item) return;

      // Open product modal for editing
      const { openProductModalForEdit } = await import("./product-modal.js");
      await openProductModalForEdit(item, index);
      return;
    }
  });

  // Handle loyalty points toggle
  const loyaltyToggle = document.querySelector("[data-loyalty-toggle]");
  if (loyaltyToggle) {
    loyaltyToggle.addEventListener("change", () => {
      updateCartSummary();
    });
  }

  // Handle payment button click
  const paymentBtn = document.querySelector("[data-cart-payment]");
  if (paymentBtn) {
    paymentBtn.addEventListener("click", () => {
      const cart = JSON.parse(localStorage.getItem(CART_KEY) || "[]");
      const loyaltyPoints = 300;
      const useLoyaltyPoints =
        document.querySelector("[data-loyalty-toggle]")?.checked || false;
      const subtotal = cart.reduce((sum, item) => sum + (item.total || 0), 0);
      const loyaltyDiscount = useLoyaltyPoints ? loyaltyPoints : 0;
      const total = Math.max(0, subtotal - loyaltyDiscount);

      const paymentInfo = {
        cart: cart,
        subtotal: subtotal,
        loyaltyPointsUsed: useLoyaltyPoints ? loyaltyPoints : 0,
        discount: loyaltyDiscount,
        total: total,
        timestamp: new Date().toISOString(),
      };

      console.log(JSON.stringify(paymentInfo, null, 2));
    });
  }
}

export { CART_KEY, formatPrice, renderCartItem };
