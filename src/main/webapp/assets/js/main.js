document.addEventListener("DOMContentLoaded", function () {
  const list = document.querySelector(".category-list");
  const prevBtn = document.getElementById("prevCategory");
  const nextBtn = document.getElementById("nextCategory");

  if (!list || !prevBtn || !nextBtn) return;

  let position = 0;
  const chipWidth = list.querySelector(".category-chip")?.offsetWidth || 0;
  const visibleChips = 8;
  const totalChips = list.children.length;
  const maxPosition = -(totalChips - visibleChips) * (chipWidth + 19.2); // 19.2px is gap

  function updatePosition() {
    list.style.transform = `translateX(${position}px)`;
    prevBtn.disabled = position >= 0;
    nextBtn.disabled = position <= maxPosition;
  }

  prevBtn.addEventListener("click", () => {
    position = Math.min(position + chipWidth + 19.2, 0);
    updatePosition();
  });

  nextBtn.addEventListener("click", () => {
    position = Math.max(position - chipWidth - 19.2, maxPosition);
    updatePosition();
  });

  // Initialize
  updatePosition();
});

document.addEventListener("DOMContentLoaded", function () {
  const productDetailModal = document.getElementById("productDetailModal");

  if (productDetailModal) {
    // 1. Xử lý tăng giảm số lượng
    const quantityInput = productDetailModal.querySelector(".product-qty");
    const minusBtn = productDetailModal.querySelector(".product-qty-btn.minus");
    const plusBtn = productDetailModal.querySelector(".product-qty-btn.plus");

    minusBtn.addEventListener("click", function () {
      let currentValue = parseInt(quantityInput.value);
      if (currentValue > 1) {
        quantityInput.value = currentValue - 1;
      }
    });

    plusBtn.addEventListener("click", function () {
      let currentValue = parseInt(quantityInput.value);
      quantityInput.value = currentValue + 1;
    });

    // 2. Xử lý radio button selection
    const radioInputs = productDetailModal.querySelectorAll('.form-check-input[type="radio"]');

    radioInputs.forEach((radio) => {
      radio.addEventListener("change", function () {
        // Remove checked class from all form-checks in the same group
        const name = this.getAttribute("name");
        const groupFormChecks = productDetailModal
          .querySelectorAll(`.form-check-input[name="${name}"]`)
          .forEach((input) => input.closest(".form-check").classList.remove("checked"));

        // Add checked class to selected form-check
        if (this.checked) {
          this.closest(".form-check").classList.add("checked");
        }
      });
    });

    // Initialize checked class for pre-selected radio buttons
    radioInputs.forEach((radio) => {
      if (radio.checked) {
        radio.closest(".form-check").classList.add("checked");
      }
    });
  }
});
