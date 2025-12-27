/*
HÀM 1: TÌM KIẾM ĐỘNG (CHUNG CHO CÁC BẢNG)
Hàm này có thể tái sử dụng cho bất kỳ thanh tìm kiếm và bảng nào.
@param {string} inputSelector - ID của ô input, ví dụ: '#product-search-input'
@param {string} tableBodySelector - ID (hoặc class) của tbody, ví dụ: '#product-table-body'
@param {number[]} columnsToSearch - Mảng các chỉ số cột (index) để tìm, ví dụ: [0, 2]
 */
function setupDynamicSearch(inputSelector, tableBodySelector, columnsToSearch) {
    // Gắn sự kiện 'input' vào ô tìm kiếm được chỉ định
    $(inputSelector).on('input', function () {

        // 1. Lấy từ khóa tìm kiếm
        const searchTerm = $(this).val().trim().toLowerCase();

        // 2. Lấy tất cả các hàng <tr> bên trong tbody được chỉ định
        const $rows = $(tableBodySelector).find('tr');

        // 3. Lặp qua từng hàng
        $rows.each(function () {
            const $row = $(this);
            let isMatch = false; // Cờ để kiểm tra xem hàng này có khớp không

            // 4. Lặp qua các cột CẦN TÌM (ví dụ: [0, 2])
            for (const colIndex of columnsToSearch) {
                // Lấy nội dung của ô <td> ở vị trí (index) tương ứng
                const cellText = $row.find('td').eq(colIndex).text().toLowerCase();

                // 5. Nếu nội dung ô chứa từ khóa
                if (cellText.includes(searchTerm)) {
                    isMatch = true;
                    break; // Thoát vòng lặp, vì chỉ cần 1 ô khớp là đủ
                }
            }

            // 6. Ẩn hoặc hiện hàng dựa trên kết quả
            $row.toggle(isMatch);
        });
    });
}

// HÀM 3: VẼ BIỂU ĐỒ DOANH THU
function setupRevenueChart() {
    const $ctx = $('#myRevenueChart');
    if ($ctx.length === 0) {
        return;
    }
    new Chart($ctx[0], {
        type: 'line', data: {
            labels: ['Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10'], datasets: [{
                label: 'Doanh Thu (triệu VNĐ)',
                data: [210, 350, 420, 380, 510, 600],
                borderColor: '#4a69bd',
                backgroundColor: 'rgba(74, 105, 189, 0.1)',
                fill: true,
                tension: 0.4
            }]
        }, options: {
            responsive: true, maintainAspectRatio: false, scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

// HÀM 4: LOGIC MỞ MODAL
function setupModal(openButton, modal) {
    const $modal = $(modal);
    const $openBtn = $(openButton);
    const $closeBtn = $modal.find('.close-btn');

    if ($modal.length === 0) {
        return;
    }

    $openBtn.on('click', function () {
        $modal.show();
    });

    $closeBtn.on('click', function () {
        $modal.hide();
    });

    $(window).on('click', function (event) {
        if (event.target === $modal[0]) {
            $modal.hide();
        }
    });
}

// HÀM 5: HÀM KHỞI TẠO CHỨC NĂNG LỌC NGÀY
function initDateFilter(date, text) {
    const dateBtn = $(date);
    if (!dateBtn.length) return;
    const dateSpan = $(text)
    if (!dateSpan.length) {
        console.error("Không tìm thấy span #date-filter-text bên trong button");
        return;
    }
    const picker = new Litepicker({
        element: dateBtn[0],
        singleMode: true,
        allowRepick: true,
        numberOfMonths: 1,
        format: 'DD/MM/YYYY',
        footer: true,
        startDate: new Date(),
        buttonText: {
            previousMonth: '<i class="fas fa-chevron-left"></i>',
            nextMonth: '<i class="fas fa-chevron-right"></i>',
            reset: 'Xóa',
            apply: 'Chọn',
            today: 'Hôm nay'
        },

    });
    picker.on('selected', (date) => {
        dateSpan.text(date.format('DD/MM/YYYY'));
    });
    const startDate = picker.getStartDate();
    if (startDate) {
        dateSpan.text(startDate.format('DD/MM/YYYY'));
    }
}

function initDateStaffWork(input) {
    // Đặt code này trong $(document).ready()
    $(document).ready(function () {
        const dateInput = $(input);
        // Kích hoạt Litepicker cho input "Ngày Vào Làm"
        const picker = new Litepicker({
            element: dateInput[0],

            singleMode: true,
            allowInput: true,
            format: 'DD/MM/YYYY',

            startDate: new Date(),

            footer: true,
            lang: 'vi-VN',
            buttonText: {
                previousMonth: '<i class="fas fa-chevron-left"></i>',
                nextMonth: '<i class="fas fa-chevron-right"></i>',
                reset: 'Xóa',
                apply: 'Chọn',
                today: 'Hôm nay'
            },
        });

    });
}

// HÀM 6: SORT CÁC SẢN PHẨM BÁN CHẠY
function sortBestsellerItems() {
    const $sortSelect = $('#bestseller-sort');
    const $bestsellerList = $('#bestseller-list');

    const sortOrder = $sortSelect.val();

    const items = $bestsellerList.find('.product-item').get();

    items.sort((a, b) => {
        const salesA = parseInt($(a).data('sales'), 10);
        const salesB = parseInt($(b).data('sales'), 10);
        return (sortOrder === 'desc') ? (salesB - salesA) : (salesA - salesB);
    });

    $bestsellerList.html('').append(items);
}

// HÀM 7: KHỞI TẠO HÀM SORT SẢN PHẨM BÁN CHẠY
function initBestsellerSort() {
    const $sortSelect = $('#bestseller-sort');
    if (!$sortSelect.length || !$('#bestseller-list').length) return;

    sortBestsellerItems();

    $sortSelect.on('change', sortBestsellerItems);
}

// HÀM 8: HÀM KHỞI TẠO XUẤT/NHẬP KHO
function initInventoryActions() {
    const $inventoryForm = $('#inventory-form');
    if (!$inventoryForm.length) return;

    $inventoryForm.find('.btn-import').on('click', () => {
        const product = $('#product-select').val() || "Sản phẩm";
        const quantity = $('#product-quantity').val();
        alert(`Đã nhập ${quantity} ${product} vào kho!`);
    });

    $inventoryForm.find('.btn-export').on('click', () => {
        const product = $('#product-select').val() || "Sản phẩm";
        const quantity = $('#product-quantity').val();
        alert(`Đã xuất ${quantity} ${product} khỏi kho!`);
    });
}

// HÀM 9.1: HÀM XỬ LÝ MODAL DÙNG CHUNG CHO CÁC ACTION
function setupModalTrigger(containerSelector, btnSelector) {

    $(containerSelector).on('click', btnSelector, function () {
        const $button = $(this);
        const $row = $button.closest('tr'); // Dữ liệu nằm trên <tr>

        // 1. Lấy ID modal từ 'data-target' của nút
        const modalSelector = $button.data('target');
        const $modal = $(modalSelector);

        if ($modal.length === 0) {
            console.error('Modal not found:', modalSelector);
            return;
        }
        // 2. Lấy tất cả 'data-*' từ <tr>
        const rowData = $row.data();
        // 3. Bắn sự kiện tùy chỉnh LÊN MODAL, gửi kèm dữ liệu
        $modal.trigger('modal:fillData', [rowData]);
        // 4. Hiển thị modal
        $modal.show();
    });
}

// HÀM 9.2: HÀM XỬ LÝ LẤY THÔNG TIN FILL VÀO MODAL
function setupHandleModal() {
    $(document).on('modal:fillData', '.modal', function (event, data) {
        const $modal = $(this);

        // Dùng vòng lặp để duyệt qua mọi data (name, price, id...)
        for (var key in data) {
            if (data.hasOwnProperty(key)) {
                const value = data[key];

                // 1. Tìm input/textarea [data-fill="key"] để set .val()
                const $input = $modal.find('[data-fill="' + key + '"]');
                if ($input.length) {
                    $input.val(value);
                }

                // 2. Tìm span/p/strong [data-fill-text="key"] để set .text()
                const $text = $modal.find('[data-fill-text="' + key + '"]');
                if ($text.length) {
                    $text.text(value);
                }

                const $img = $modal.find('[data-fill-src="' + key + '"]');
                if ($img.length) {
                    if (value) {
                        $img.attr('src', value).show();
                    } else {
                        $img.attr('src', 'https://via.placeholder.com/200x200?text=No+Image').show();
                    }
                }
                // 3. Xử lý hiển thị ẩn/hiện
                if (key === 'hide') {
                    // Nếu hide=true (đang ẩn) -> checkbox bỏ tick.
                    // Nếu hide=false (đang hiện) -> checkbox tick.
                    const isActive = (value === false);
                    $modal.find('input[name="active"]').prop('checked', isActive);
                }
            }
        }
    });
}

// HÀM 10: HÀM XỬ LÝ ĐÓNG MODAL
function closeModal() {
    $('.modal .close-btn, .modal [data-dismiss="modal"]').on('click', function () {
        $(this).closest('.modal').hide();
    });
}

// HÀM 11: HÀM XỬ LÝ XUẤT FILE EXCEL
function excelExport() {
    $('#export-report-btn').on('click', function () {
        let wb = XLSX.utils.book_new();
        let summaryData = [];
        summaryData.push(["Tiêu Đề", "Giá Trị", "So Sánh"]);
        $('.revenue-summary .summary-card').each(function () {
            var $card = $(this);
            var title = $card.find('.summary-title').text();
            var value = $card.find('.summary-value').text();
            var comparison = $card.find('.summary-comparison').text().trim();

            summaryData.push([title, value, comparison]);
        });
        const ws_summary = XLSX.utils.aoa_to_sheet(summaryData);
        XLSX.utils.book_append_sheet(wb, ws_summary, "Tóm Tắt Doanh Thu");
        const table = $('#revenue .data-table')[0];
        const ws_details = XLSX.utils.table_to_sheet(table);
        XLSX.utils.book_append_sheet(wb, ws_details, "Chi Tiết Theo Tháng");
        XLSX.writeFile(wb, "BaoCaoDoanhThu.xlsx");
    })
}

// HÀM 12: HÀM XỬ LÝ ĐỒNG BỘ ALERT
function showNotification(message, type) {
    $('.alert').remove();

    const alertType = type === 'success' ? 'alert-success' : 'alert-danger';
    const title = type === 'success' ? 'Thành công!' : 'Thông báo:';

    const alertHtml = `
        <div class="alert ${alertType}" role="alert">
            <strong>${title}</strong> ${message}
        </div>
    `;

    $('body').append(alertHtml);
    if ($(".alert").length) {
        window.setTimeout(function () {
            $(".alert").fadeTo(500, 0).slideUp(500, function () {
                $(this).remove();
            });

        }, 3000);
    }
}

// HÀM 13: HÀM XỬ LÝ ẨN HIỆN
function hideItem() {
    $(document).on('click', '.btn-hide', function (e) {
        e.preventDefault();
        const $btn = $(this);
        const $row = $btn.closest("tr");

        const id = $row.data('id');
        const isCurrentlyHidden = $row.attr("data-hide") === "true";

        const newActiveStatus = isCurrentlyHidden;

        $.ajax({
            url: 'admin/api/product-status',
            method: 'POST',
            data: {
                id: id,
                active: newActiveStatus
            },
            success: function () {
                showNotification("Cập nhật trạng thái thành công!", "success");

                const newHiddenState = !isCurrentlyHidden;
                $row.attr("data-hide", newHiddenState);

                if (newHiddenState) {
                    $row.addClass("hidden-row");
                    $btn.html('<i class="fa-regular fa-eye-slash"></i>');
                } else {
                    $row.removeClass("hidden-row");
                    $btn.html('<i class="fa-regular fa-eye"></i>');
                }
            },
            error: function () {
                showNotification("Lỗi kết nối! Không thể cập nhật trạng thái.", "error");
            }
        });
    });
}

// Plugin jQuery để thiết lập xem trước hình ảnh và nút xóa.
$.fn.setupImagePreview = function (previewSelector, removeSelector) {
    return this.each(function () {

        const $input = $(this); // Input file (ví dụ: $('#newPic'))
        const $preview = $(previewSelector); // Thẻ img (ví dụ: $('#imagePreview'))
        const $removeBtn = $(removeSelector); // Nút xóa (ví dụ: $('#removeImageBtn'))

        // Gắn sự kiện 'change' (khi người dùng chọn file)
        $input.on('change', function () {
            // 'this.files' là API của JavaScript gốc
            const file = this.files[0];

            if (file) {
                const reader = new FileReader();

                reader.onload = function (e) {
                    // Dùng .attr() để set src và .show() để hiển thị
                    $preview.attr('src', e.target.result).show();
                    $removeBtn.show(); // Hiển thị nút "Xóa"
                }

                reader.readAsDataURL(file);
            }
        });

        // Gắn sự kiện 'click' cho nút "Xóa"
        $removeBtn.on('click', function () {
            $preview.attr('src', '').hide(); // Xóa src và ẩn ảnh
            $removeBtn.hide(); // Ẩn nút "Xóa"

            // Reset lại input file để có thể chọn lại file cũ
            $input.val(null);
        });
    });
};

// ĐIỂM KHỞI ĐỘNG của JQuery
$(function () {

    if ($('#product').length) {
        setupDynamicSearch(
            '#product-search-input',
            '#product .data-table tbody',
            [0, 2]
        );
        setupModal('#create-product-btn', '#create-product-modal');
        setupModalTrigger('#product', '.btn-edit');
        setupModalTrigger('#product', '.btn-delete');
        if ($('#newPic').length) {
            $('#newPic').setupImagePreview('#imagePreview', '#removeImageBtn');
        }
        hideItem();

        $('#edit-product-image').setupImagePreview('#edit-image-preview', '#fake-remove-btn');
        // Xử lý Preview ảnh khi chọn file
        $('#edit-product-image').on('change', function () {
            const file = this.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    $('#edit-image-preview').attr('src', e.target.result).show();
                }
                reader.readAsDataURL(file);
            }
        });

        $('#edit-image-preview').on('click', function () {
            const currentSrc = $(this).attr('src');
            if (!currentSrc || currentSrc.includes('via.placeholder.com')) {
                return;
            }

            $('#full-size-image').attr('src', currentSrc);

            $('#image-viewer-modal').css('display', 'flex').hide().fadeIn(200);
        });

        $('#image-viewer-modal .close-btn-img, #image-viewer-modal').on('click', function (e) {
            if (e.target.id === 'full-size-image') {
                return;
            }
            $('#image-viewer-modal').fadeOut(200);
        });
    }

    if ($('#order').length) {
        setupDynamicSearch(
            '#order-search-input',
            '#order .data-table tbody',
            [0, 1]
        );
    }

    if ($('#account').length) {
        setupModalTrigger('#account', '.btn-edit');
        setupModalTrigger('#account', '.btn-delete');
    }

    if ($('#staff').length) {
        setupModalTrigger('#staff', '.btn-edit');
        setupModalTrigger('#staff', '.btn-delete');
        initDateStaffWork('#createStaffJoinDate');
    }

    if ($(".alert").length) {
        window.setTimeout(function () {
            $(".alert").fadeTo(500, 0).slideUp(500, function () {
                $(this).remove();
            });

        }, 3000);
    }
    setupHandleModal();
    closeModal();
    excelExport();
});
