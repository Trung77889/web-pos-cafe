import {Modal} from './modules/modal.js';
import {setupLogging} from './modules/utils.js';
import {Toast} from "./modules/toast.js";

setupLogging();

await Modal.init({
    preloadList: ['login', 'register'],
    onReady() {
        // Flash Toast
        if (window.__FLASH__) {
            Toast[window.__FLASH__.type](window.__FLASH__.msg);
        }

        // Modal auto-open
        if (window.__OPEN_MODAL__) {
            Modal.open(window.__OPEN_MODAL__);
        }
    }
});