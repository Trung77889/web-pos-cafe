// ==== Initialize APP_MODE before anything else ====
window.__APP_MODE__ = "${initParam.APP_MODE}";

import {Modal} from "./modules/modal.js";
import {getFlashMessages, getReopenModal, initResponseHandler, refillFormData, setupLogging,} from "./modules/utils.js";

// ==== Config logging system for client side ====
setupLogging();

// ==== Initialize response handler for modal reopening ====
initResponseHandler();

// ==== Flash from SSR ====
getFlashMessages();

// ==== Modal System ====
await Modal.init({
    CONTAINER_SELECTOR: "#modal-container",
    TRIGGER_SELECTOR: ".btn-open-modal",
    preloadList: ["login", "register", "forgot-password", "reset-password"],
});

// ==== Reopen modal if required (from cache or server response) ====
const reopen = getReopenModal();
if (reopen) {
    Modal.open(reopen);
    // Refill form data AFTER modal is opened (when DOM elements exist)
    refillFormData();
}