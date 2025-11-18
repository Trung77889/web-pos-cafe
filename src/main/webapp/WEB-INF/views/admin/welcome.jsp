<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<body>
<!-- Thanh sidebar -->
<div class="sidebar">
    <div class="user-profile">
        <div class="user-avatar"><i class="fas fa-user-shield"></i></div>
        <div class="user-details">
            <p class="user-name">Nguyễn Văn An</p>
            <p class="user-role">Quản Trị Viên</p>
        </div>
    </div>
    <!-- Phần điều hướng -->
    <nav class="navigation">
        <ul>
            <li class="active"><a href="#dashboard"><i class="fas fa-tachometer-alt"></i><span>Dashboard</span></a></li>
            <li><a href="#product"><i class="fas fa-box-open"></i><span>Sản phẩm</span></a></li>
            <li><a href="#order"><i class="fas fa-shopping-cart"></i><span>Đơn hàng</span></a></li>
            <li><a href="#revenue"><i class="fas fa-chart-line"></i><span>Doanh thu</span></a></li>
            <li><a href="#account"><i class="fas fa-user-circle"></i><span>Tài khoản</span></a></li>
            <li><a href="#staff"><i class="fas fa-users-cog"></i><span>Nhân viên</span></a></li>
            <li><a href="#log"><i class="fas fa-clipboard-list"></i><span>Nhật ký</span></a></li>
        </ul>
    </nav>

    <div class="sidebar-footer">
        <a href="#logout"><i class="fas fa-sign-out-alt"></i><span>Đăng xuất</span></a>
    </div>
</div>

<%--resolve conflict--%>
<div class="main-content">
    <!-- Dashboard (Chứa Tổng quan doanh thu, đơn hàng, sản phẩm đã bán,)-->
    <div id="dashboard" class="page-content active">
        <header>
            <h1>Tổng Quan</h1>
            <button id="date-filter-button" class="date-filter">
                <i class="fas fa-calendar-alt"></i>
                <span id="date-filter-text"></span>
            </button>
        </header>

        <div class="card-container">
            <div class="card revenue">
                <div class="card-icon"><i class="fas fa-dollar-sign"></i></div>
                <div class="card-info">
                    <p class="card-title">Tổng Doanh Thu</p>
                    <p class="card-value">1,250,000,000 ₫</p>
                </div>
            </div>
            <div class="card orders">
                <div class="card-icon"><i class="fas fa-shopping-bag"></i></div>
                <div class="card-info">
                    <p class="card-title">Tổng Đơn Hàng</p>
                    <p class="card-value">1,480</p>
                </div>
            </div>
            <div class="card products">
                <div class="card-icon"><i class="fas fa-box"></i></div>
                <div class="card-info">
                    <p class="card-title">Sản Phẩm Đã Bán</p>
                    <p class="card-value">3,250</p>
                </div>
            </div>
            <div class="card users">
                <div class="card-icon"><i class="fas fa-users"></i></div>
                <div class="card-info">
                    <p class="card-title">Khách Hàng Mới</p>
                    <p class="card-value">85</p>
                </div>
            </div>
        </div>

        <div class="details-container">

            <div class="detail-card revenue-chart">
                <h2>Phân Tích Doanh Thu</h2>
                <canvas id="myRevenueChart"></canvas>
            </div>
            <div class="detail-card live-orders">
                <h2>Đơn Hàng Mới (Đang Chờ)</h2>
                <ul class="order-list">

                    <li class="order-item status-new">
                        <div class="order-table">
                            <i class="fas fa-user-circle"></i>
                            <span>Bàn 12</span>
                        </div>
                        <div class="order-details">
                            <p class="order-id">#DH-10583 <span class="order-time">1 phút trước</span></p>
                            <p class="order-summary">2x Coca-Cola, 1x Bò Bít Tết (Medium)</p>
                        </div>
                        <p class="order-total">350,000 ₫</p>
                        <div class="order-actions">
                            <button class="btn btn-confirm"><i class="fas fa-check"></i> Xác nhận</button>
                            <button class="btn btn-cancel"><i class="fas fa-times"></i> Hủy</button>
                        </div>
                    </li>

                    <li class="order-item status-processing">
                        <div class="order-table">
                            <i class="fas fa-user-circle"></i>
                            <span>Bàn 05</span>
                        </div>
                        <div class="order-details">
                            <p class="order-id">#DH-10582 <span class="order-time">10 phút trước</span></p>
                            <p class="order-summary">1x Set Lẩu Thái, 2x Bia Tiger</p>
                        </div>
                        <p class="order-total">580,000 ₫</p>
                        <div class="order-actions">
                            <button class="btn btn-payment"><i class="fa-solid fa-money-check"></i> Thanh toán</button>
                        </div>
                    </li>

                    <li class="order-item status-new">
                        <div class="order-table">
                            <i class="fas fa-user-circle"></i>
                            <span>Bàn 02</span>
                        </div>
                        <div class="order-details">
                            <p class="order-id">#DH-10584 <span class="order-time">15 giây trước</span></p>
                            <p class="order-summary">1x Nước cam ép</p>
                        </div>
                        <p class="order-total">55,000 ₫</p>
                        <div class="order-actions">
                            <button class="btn btn-confirm"><i class="fas fa-check"></i> Xác nhận</button>
                            <button class="btn btn-cancel"><i class="fas fa-times"></i> Hủy</button>
                        </div>
                    </li>

                </ul>
            </div>
            <div class="detail-card inventory-status">
                <h2>Cảnh Báo Tồn Kho</h2>
                <ul class="inventory-list">
                    <li class="inventory-item critical">
                        <img src="https://picsum.photos/id/40/50/50" alt="Product">
                        <div class="product-details">
                            <p class="product-name">Quần Jeans Rách Gối</p>
                            <p class="product-sku">SKU: QJ-002</p>
                        </div>
                        <p class="product-stock">Còn 3 sp</p>
                    </li>
                    <li class="inventory-item low">
                        <img src="https://picsum.photos/id/1025/50/50" alt="Product">
                        <div class="product-details">
                            <p class="product-name">Áo Hoodie Nỉ Bông</p>
                            <p class="product-sku">SKU: HD-001</p>
                        </div>
                        <p class="product-stock">Còn 8 sp</p>
                    </li>
                    <li class="inventory-item low">
                        <img src="https://picsum.photos/id/305/50/50" alt="Product">
                        <div class="product-details">
                            <p class="product-name">Tai Nghe Bluetooth Pro</p>
                            <p class="product-sku">SKU: BT-003</p>
                        </div>
                        <p class="product-stock">Còn 12 sp</p>
                    </li>
                </ul>
            </div>

            <div class="detail-card best-sellers">
                <div class="card-header">
                    <h2>Sản Phẩm Bán Chạy</h2>
                    <div class="sort-controls">
                        <select id="bestseller-sort">
                            <option value="desc">Bán chạy nhất</option>
                            <option value="asc">Bán ít nhất</option>
                        </select>
                    </div>
                </div>
                <ul id="bestseller-list">
                    <li class="product-item" data-sales="1200">
                        <img src="https://picsum.photos/id/1025/50/50" alt="Product">
                        <div class="product-details">
                            <p class="product-name">Áo Hoodie Nỉ Bông</p>
                            <p class="product-category">Thời trang Nam</p>
                        </div>
                        <p class="product-sales">1,200 sp</p>
                    </li>
                    <li class="product-item" data-sales="750">
                        <img src="https://picsum.photos/id/305/50/50" alt="Product">
                        <div class="product-details">
                            <p class="product-name">Tai Nghe Bluetooth Pro</p>
                            <p class="product-category">Phụ kiện điện tử</p>
                        </div>
                        <p class="product-sales">750 sp</p>
                    </li>
                    <li class="product-item" data-sales="980">
                        <img src="https://picsum.photos/id/201/50/50" alt="Product">
                        <div class="product-details">
                            <p class="product-name">Giày Sneaker Trắng</p>
                            <p class="product-category">Giày dép</p>
                        </div>
                        <p class="product-sales">980 sp</p>
                    </li>
                    <li class="product-item" data-sales="420">
                        <img src="https://picsum.photos/id/500/50/50" alt="Product">
                        <div class="product-details">
                            <p class="product-name">Bình Giữ Nhiệt Inox</p>
                            <p class="product-category">Đồ gia dụng</p>
                        </div>
                        <p class="product-sales">420 sp</p>
                    </li>
                </ul>
            </div>

            <div class="detail-card today-activity">
                <h2>Hoạt Động Hôm Nay</h2>
                <ul class="activity-feed">
                    <li class="activity-item">
                        <div class="activity-icon order">
                            <i class="fas fa-receipt"></i>
                        </div>
                        <div class="activity-details">
                            <p>Đơn hàng mới <strong>#DH-10582</strong> vừa được đặt.</p>
                            <span class="activity-time">5 phút trước</span>
                        </div>
                    </li>
                    <li class="activity-item">
                        <div class="activity-icon user">
                            <i class="fas fa-user-plus"></i>
                        </div>
                        <div class="activity-details">
                            <p><strong>nguyenvana@gmail.com</strong> vừa đăng ký tài khoản.</p>
                            <span class="activity-time">20 phút trước</span>
                        </div>
                    </li>
                    <li class="activity-item">
                        <div class="activity-icon inventory">
                            <i class="fas fa-boxes"></i>
                        </div>
                        <div class="activity-details">
                            <p>Đã cập nhật tồn kho cho <strong>Áo Hoodie Nỉ Bông</strong> (Còn 8 sp).</p>
                            <span class="activity-time">1 giờ trước</span>
                        </div>
                    </li>
                    <li class="activity-item">
                        <div class="activity-icon order">
                            <i class="fas fa-receipt"></i>
                        </div>
                        <div class="activity-details">
                            <p>Đơn hàng <strong>#DH-10581</strong> đã được giao thành công.</p>
                            <span class="activity-time">2 giờ trước</span>
                        </div>
                    </li>
                </ul>
            </div>

            <div class="detail-card inventory-actions">
                <h2>Xuất/Nhập Kho Nhanh</h2>
                <form id="inventory-form">
                    <div class="form-group">
                        <label for="product-select">Chọn Sản Phẩm</label>
                        <input type="text" id="product-select" placeholder="Tìm SKU hoặc Tên SP...">
                    </div>
                    <div class="form-group">
                        <label for="product-quantity">Số Lượng</label>
                        <input type="number" id="product-quantity" value="1" min="1">
                    </div>
                    <div class="form-buttons">
                        <button type="button" class="btn btn-import">
                            <i class="fas fa-plus-circle"></i> Nhập Kho
                        </button>
                        <button type="button" class="btn btn-export">
                            <i class="fas fa-minus-circle"></i> Xuất Kho
                        </button>
                    </div>
                </form>
            </div>

        </div>
    </div>
    <!-- Product (Các sản phẩm mặt hàng,chi tiết của sản phẩm, nhập/xuất hàng hóa)-->
    <div id="product" class="page-content">
        <header>
            <h1>Quản Lý Sản Phẩm</h1>
            <!-- Nút tạo sản phẩm mới -->
            <button class="btn btn-primary" id="create-product-btn"><i class="fas fa-plus"></i> Thêm Sản Phẩm Mới
            </button>
        </header>

        <div class="page-controls" id="found-bar">
            <input type="text" class="search-bar" id="product-search-input"
                   placeholder="Tìm kiếm sản phẩm theo tên, ID...">
        </div>
        <div class="data-table-container">
            <table class="data-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Hình Ảnh</th>
                    <th>Tên Sản Phẩm</th>
                    <th>Giá</th>
                    <th>Tồn Kho</th>
                    <th>Hành Động</th>
                </tr>
                </thead>
                <tbody>
                <tr data-id="SP001"
                    data-name="Áo Hoodie Nỉ Bông"
                    data-price="450000"
                    data-inventory="120"
                    data-image-src="https://picsum.photos/id/1025/50/50">

                    <td>#SP001</td>
                    <td><img src="https://picsum.photos/id/1025/50/50" alt="Product"></td>
                    <td>Áo Hoodie Nỉ Bông</td>
                    <td>450,000 ₫</td>
                    <td>120</td>
                    <td>
                        <button class="btn-action btn-edit" data-target="#edit-product-modal"><i
                                class="fas fa-edit"></i></button>
                        <button class="btn-action btn-delete" data-target="#delete-product-modal"><i
                                class="fas fa-trash"></i></button>
                    </td>
                </tr>
                <tr data-id="SP002"
                    data-name="Giày Sneaker Trắng"
                    data-price="790000"
                    data-inventory="85"
                    data-image-src="https://picsum.photos/id/201/50/50">

                    <td>#SP002</td>
                    <td><img src="https://picsum.photos/id/201/50/50" alt="Product"></td>
                    <td>Giày Sneaker Trắng</td>
                    <td>790,000 ₫</td>
                    <td>85</td>
                    <td>
                        <button class="btn-action btn-edit" data-target="#edit-product-modal"><i
                                class="fas fa-edit"></i></button>
                        <button class="btn-action btn-delete" data-target="#delete-product-modal"><i
                                class="fas fa-trash"></i></button>
                    </td>
                </tr>
                <tr data-id="SP003"
                    data-name="Tai Nghe Bluetooth Pro"
                    data-price="1200000"
                    data-inventory="50"
                    data-image-src="https://picsum.photos/id/305/50/50">

                    <td>#SP003</td>
                    <td><img src="https://picsum.photos/id/305/50/50" alt="Product"></td>
                    <td>Tai Nghe Bluetooth Pro</td>
                    <td>1,200,000 ₫</td>
                    <td>50</td>
                    <td>
                        <button class="btn-action btn-edit" data-target="#edit-product-modal"><i
                                class="fas fa-edit"></i></button>
                        <button class="btn-action btn-delete" data-target="#delete-product-modal"><i
                                class="fas fa-trash"></i></button>
                    </td>
                </tr>
                <tr data-id="SP004"
                    data-name="Đồng Hồ Thông Minh Gen 5"
                    data-price="2500000"
                    data-inventory="30"
                    data-image-src="https://picsum.photos/id/412/50/50">

                    <td>#SP004</td>
                    <td><img src="https://picsum.photos/id/412/50/50" alt="Product"></td>
                    <td>Đồng Hồ Thông Minh Gen 5</td>
                    <td>2,500,000 ₫</td>
                    <td>30</td>
                    <td>
                        <button class="btn-action btn-edit" data-target="#edit-product-modal"><i
                                class="fas fa-edit"></i></button>
                        <button class="btn-action btn-delete" data-target="#delete-product-modal"><i
                                class="fas fa-trash"></i></button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <!-- Form modal create product -->
    <div id="create-product-modal" class="modal">
        <div class="modal-content">
            <header class="modal-header">
                <h2 class="modal-title">Tạo Sản Phẩm Mới</h2>
                <button class="close-btn">&times;</button>
            </header>
            <div class="modal-body">
                <form id="create-product-form" class="product-form">
                    <div class="form-group">
                        <label for="newFullProductName">Tên</label>
                        <input type="text" id="newFullProductName" placeholder="Nhập tên sản phẩm">
                    </div>
                    <div class="form-group">
                        <label for="newPrice">Giá(₫)</label>
                        <input type="number" id="newPrice" placeholder="Nhập giá sản phẩm">
                    </div>
                    <div class="form-group">
                        <label for="newInventory">Số lượng Tồn kho hiện tại</label>
                        <input type="number" id="newInventory" placeholder="Nhập số tồn kho">
                    </div>
                    <div class="form-group">
                        <label for="newPic" class="upload-label">Thêm hình ảnh</label>
                        <input type="file" id="newPic" name="newPic" class="file-input" accept="image/*">
                        <div class="preview-container">
                            <img id="imagePreview" src="" alt="Xem trước ảnh" class="image-preview">
                            <button type="button" id="removeImageBtn" class="remove-btn">Xóa</button>
                        </div>
                    </div>
                </form>
            </div>
            <footer class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                <button type="button" form="create-account-form" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Tạo Sản Phẩm
                </button>
            </footer>
        </div>
    </div>
    <!-- Form modal edit product -->
    <div id="edit-product-modal" class="modal">
        <div class="modal-content">
            <header class="modal-header">
                <h2 class="modal-title">Chỉnh Sửa Sản Phẩm</h2>
                <button class="close-btn">&times;</button>
            </header>

            <div class="modal-body">
                <form class="product-form">
                    <div class="form-group">
                        <label>Tên</label>
                        <input type="text" data-fill="name" placeholder="Nhập tên sản phẩm">
                    </div>
                    <div class="form-group">
                        <label>Giá(₫)</label>
                        <input type="number" data-fill="price" placeholder="Nhập giá sản phẩm">
                    </div>
                    <div class="form-group">
                        <label>Tồn kho</label>
                        <input type="number" data-fill="inventory" placeholder="Nhập số tồn kho">
                    </div>
                    <div class="form-group">
                        <label>Hình ảnh xem trước</label>
                        <img data-fill-src="imageSrc" alt="Xem trước ảnh" class="image-preview">
                    </div>

                    <input type="hidden" data-fill="id" name="product_id">
                </form>
            </div>

            <footer class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-primary">Lưu Thay Đổi</button>
            </footer>
        </div>
    </div>
    <!-- Form modal delete product -->
    <div id="delete-product-modal" class="modal">
        <div class="modal-content">
            <header class="modal-header">
                <h2 class="modal-title">Xác nhận Xóa</h2>
                <button class="close-btn">&times;</button>
            </header>

            <div class="modal-body">
                <p>Bạn có chắc muốn xóa <strong data-fill-text="name">Tên sản phẩm...</strong> không?</p>
                <input type="hidden" data-fill="id" id="id-to-delete">
            </div>

            <footer class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-danger">Xóa</button>
            </footer>
        </div>
    </div>
    <!-- Order (Quản lý các đơn hàng được đặt, xử lý các thao tác gồm Xác nhận, Hủy)-->
    <div id="order" class="page-content">
        <header>
            <h1>Quản Lý Đơn Hàng</h1>
            <input type="text" class="search-bar" id="order-search-input" placeholder="Tìm kiếm theo mã đơn, tên KH...">
        </header>
        <div class="data-table-container">
            <table class="data-table" id="table-heading">
                <thead>
                <tr>
                    <th>Mã Đơn</th>
                    <th>Khách Hàng</th>
                    <th>Ngày Đặt</th>
                    <th>Tổng Tiền</th>
                    <th>Trạng Thái</th>
                    <th>Thời gian</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>#DH-1710</td>
                    <td>Trần Minh Tuấn</td>
                    <td>16/10/2025</td>
                    <td>1,240,000 ₫</td>
                    <td><span class="status-badge status-completed">Đã hoàn thành</span></td>
                    <td>13:40 , 16/10/2025</td>
                </tr>
                <tr>
                    <td>#DH-1709</td>
                    <td>Lê Thị Hoa</td>
                    <td>16/10/2025</td>
                    <td>790,000 ₫</td>
                    <td><span class="status-badge status-completed">Đã hoàn thành</span></td>
                    <td>13:40 , 16/10/2025</td>
                </tr>
                <tr>
                    <td>#DH-1708</td>
                    <td>Phạm Văn Dũng</td>
                    <td>15/10/2025</td>
                    <td>450,000 ₫</td>
                    <td><span class="status-badge status-cancelled">Đã hủy</span></td>
                    <td>13:40 , 16/10/2025</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <!--Revenue-->
    <div id="revenue" class="page-content">
        <header>
            <h1>Báo Cáo Doanh Thu</h1>
            <button class="btn btn-primary" id="export-report-btn"><i class="fas fa-file-export"></i> Xuất Báo Cáo
            </button>
        </header>

        <div class="revenue-summary">
            <div class="summary-card">
                <p class="summary-title">Doanh Thu Thuần</p>
                <p class="summary-value">980,500,000 ₫</p>
                <p class="summary-comparison positive"><i class="fas fa-arrow-up"></i> 12.5% so với tháng trước</p>
            </div>
            <div class="summary-card">
                <p class="summary-title">Lợi Nhuận</p>
                <p class="summary-value">420,000,000 ₫</p>
                <p class="summary-comparison positive"><i class="fas fa-arrow-up"></i> 9.8% so với tháng trước</p>
            </div>
            <div class="summary-card">
                <p class="summary-title">Chi Phí</p>
                <p class="summary-value">55,200,000 ₫</p>
                <p class="summary-comparison negative"><i class="fas fa-arrow-down"></i> 5.2% so với tháng trước</p>
            </div>
            <div class="summary-card">
                <p class="summary-title">Đơn Hàng Trung Bình</p>
                <p class="summary-value">845,000 ₫</p>
                <p class="summary-comparison positive"><i class="fas fa-arrow-up"></i> 2.1% so với tháng trước</p>
            </div>
        </div>

        <div class="data-table-container">
            <h2>Doanh Thu Theo Tháng</h2>
            <table class="data-table">
                <thead>
                <tr>
                    <th>Tháng</th>
                    <th>Số Đơn Hàng</th>
                    <th>Doanh Thu</th>
                    <th>Thay Đổi</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>Tháng 10, 2025</td>
                    <td>1,480</td>
                    <td>1,250,000,000 ₫</td>
                    <td><span class="summary-comparison positive">+15%</span></td>
                </tr>
                <tr>
                    <td>Tháng 9, 2025</td>
                    <td>1,250</td>
                    <td>1,080,000,000 ₫</td>
                    <td><span class="summary-comparison positive">+8%</span></td>
                </tr>
                <tr>
                    <td>Tháng 8, 2025</td>
                    <td>1,100</td>
                    <td>990,000,000 ₫</td>
                    <td><span class="summary-comparison negative">-2%</span></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <!-- Account -->
    <div id="account" class="page-content">
        <header>
            <h1>Quản Lý Tài Khoản</h1>
            <button class="btn btn-primary" id="create-account-btn"><i class="fas fa-user-plus"></i> Thêm Tài Khoản Mới
            </button>
        </header>
        <div class="data-table-container">
            <table class="data-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên Người Dùng</th>
                    <th>Email</th>
                    <th>Vai Trò</th>
                    <th>Hành Động</th>
                </tr>
                </thead>
                <tbody>
                <tr
                        data-id="TK001"
                        data-username="Nguyễn Văn An"
                        data-email="nguyen.van.an@admin.com"
                        data-role="admin">
                    <td>#TK001</td>
                    <td>Nguyễn Văn An</td>
                    <td>nguyen.van.an@admin.com</td>
                    <td><span class="status-badge status-completed">Quản Trị Viên</span></td>
                    <td>
                        <button class="btn-action btn-edit" data-target="#edit-account-modal">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn-action btn-delete" data-target="#delete-account-modal">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                </tr>
                <tr
                        data-id="TK002"
                        data-username="Trần Thị Bích"
                        data-email="tran.thi.bich@editor.com"
                        data-role="editor">

                    <td>#TK002</td>
                    <td>Trần Thị Bích</td>
                    <td>tran.thi.bich@editor.com</td>
                    <td><span class="status-badge status-pending">Biên Tập Viên</span></td>
                    <td>
                        <button class="btn-action btn-edit" data-target="#edit-account-modal">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn-action btn-delete" data-target="#delete-account-modal">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                </tr>
                <tr
                        data-id="TK003"
                        data-username="Lê Văn Dũng"
                        data-email="le.van.dung@viewer.com"
                        data-role="viewer">

                    <td>#TK003</td>
                    <td>Lê Văn Dũng</td>
                    <td>le.van.dung@viewer.com</td>
                    <td><span class="status-badge status-cancelled">Người Xem</span></td>
                    <td>
                        <button class="btn-action btn-edit" data-target="#edit-account-modal">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn-action btn-delete" data-target="#delete-account-modal">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <!-- Form modal create account -->
    <div id="create-account-modal" class="modal">
        <div class="modal-content">
            <header class="modal-header">
                <h2 class="modal-title">Tạo Tài Khoản Mới</h2>
                <button class="close-btn">&times;</button>
            </header>
            <div class="modal-body">
                <form id="create-account-form" class="account-form">
                    <div class="form-group">
                        <label for="newFullName">Họ và Tên</label>
                        <input type="text" id="newFullName" placeholder="Nhập họ và tên đầy đủ">
                    </div>
                    <div class="form-group">
                        <label for="newEmail">Email</label>
                        <input type="email" id="newEmail" placeholder="Nhập địa chỉ email">
                    </div>
                    <div class="form-group">
                        <label for="newPasswordModal">Mật Khẩu</label>
                        <input type="password" id="newPasswordModal" placeholder="Tạo mật khẩu">
                    </div>
                    <div class="form-group">
                        <label for="newRole">Vai trò</label>
                        <select id="newRole">
                            <option value="" disabled selected>-- Chọn vai trò --</option>
                            <option value="viewer">Người Xem</option>
                            <option value="editor">Biên Tập Viên</option>
                            <option value="admin">Quản Trị Viên</option>
                        </select>
                    </div>
                </form>
            </div>
            <footer class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                <button type="button" form="create-account-form" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Tạo Tài Khoản
                </button>
            </footer>

        </div>
    </div>
    <!-- Form modal edit account -->
    <div id="edit-account-modal" class="modal">
        <div class="modal-content">
            <header class="modal-header">
                <h2 class="modal-title">Chỉnh Sửa Tài Khoản</h2>
                <button class="close-btn">&times;</button>
            </header>
            <div class="modal-body">
                <form id="edit-account-form">
                    <div class="form-group">
                        <label>Tên Người Dùng</label>
                        <input type="text" data-fill="username" placeholder="Nhập tên người dùng">
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" data-fill="email" placeholder="Nhập email">
                    </div>
                    <div class="form-group">
                        <label>Vai Trò</label>
                        <select data-fill="role">
                            <option value="admin">Quản Trị Viên</option>
                            <option value="editor">Biên Tập Viên</option>
                            <option value="viewer">Người Xem</option>
                        </select>
                    </div>
                    <input type="hidden" data-fill="id" name="account_id">
                </form>
            </div>
            <footer class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-primary">Lưu Thay Đổi</button>
            </footer>
        </div>
    </div>
    <!-- Form modal delete account -->
    <div id="delete-account-modal" class="modal">
        <div class="modal-content">
            <header class="modal-header">
                <h2 class="modal-title">Xác nhận Xóa</h2>
                <button class="close-btn">&times;</button>
            </header>
            <div class="modal-body">
                <p>Bạn có chắc muốn xóa tài khoản <strong data-fill-text="username">[Tên tài khoản]</strong> không?</p>
                <input type="hidden" data-fill="id" id="account-id-to-delete">
            </div>
            <footer class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-danger">Xóa</button>
            </footer>
        </div>
    </div>
    <!-- Staff -->
    <div id="staff" class="page-content">
        <header>
            <h1>Quản Lý Nhân Viên</h1>
            <button id="create-staff-btn" class="btn btn-primary"><i class="fas fa-user-plus"></i> Thêm Nhân Viên
            </button>
        </header>
        <div class="data-table-container">
            <table class="data-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Họ Tên</th>
                    <th>Chức Vụ</th>
                    <th>Email</th>
                    <th>Ngày Vào Làm</th>
                    <th>Hành Động</th>
                </tr>
                </thead>
                <tbody>
                <tr data-id="NV001"
                    data-name="Lê Minh Anh"
                    data-position="Quản lý Kho"
                    data-email="minhanh.le@company.com"
                    data-join-date="12/05/2024">

                    <td>#NV001</td>
                    <td>Lê Minh Anh</td>
                    <td>Quản lý Kho</td>
                    <td title="minhanh.le@company.com">minhanh.le@company.com</td>
                    <td>12/05/2024</td>
                    <td>
                        <button class="btn-action btn-edit" data-target="#edit-staff-modal"><i class="fas fa-edit"></i>
                        </button>
                        <button class="btn-action btn-delete" data-target="#delete-staff-modal"><i
                                class="fas fa-trash"></i></button>
                    </td>
                </tr>
                <tr data-id="NV002"
                    data-name="Hoàng Thị Bích"
                    data-position="Nhân viên Bán hàng"
                    data-email="bich.hoang@company.com"
                    data-join-date="20/08/2025">

                    <td>#NV002</td>
                    <td>Hoàng Thị Bích</td>
                    <td>Nhân viên Bán hàng</td>
                    <td title="bich.hoang@company.com">bich.hoang@company.com</td>
                    <td>20/08/2025</td>
                    <td>
                        <button class="btn-action btn-edit" data-target="#edit-staff-modal"><i class="fas fa-edit"></i>
                        </button>
                        <button class="btn-action btn-delete" data-target="#delete-staff-modal"><i
                                class="fas fa-trash"></i></button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <!-- Form create staff modal -->
    <div id="create-staff-modal" class="modal">
        <div class="modal-content">
            <header class="modal-header">
                <h2 class="modal-title">Thêm Nhân Viên Mới</h2>
                <button class="close-btn">&times;</button>
            </header>
            <div class="modal-body">
                <form id="create-staff-form">
                    <div class="form-group">
                        <label for="createStaffName">Họ và Tên</label>
                        <input type="text" id="createStaffName" placeholder="Nhập họ và tên đầy đủ">
                    </div>

                    <div class="form-group">
                        <label for="createStaffPosition">Chức Vụ</label>
                        <input type="text" id="createStaffPosition"
                               placeholder="Ví dụ: Nhân viên bán hàng, Quản lý kho...">
                    </div>

                    <div class="form-group">
                        <label for="createStaffEmail">Email</label>
                        <input type="email" id="createStaffEmail" placeholder="Nhập địa chỉ email">
                    </div>

                    <div class="form-group">
                        <label for="createStaffJoinDate">Ngày Vào Làm</label>
                        <div class="datepicker-wrapper">
                            <input type="text" id="createStaffJoinDate" placeholder="Chọn ngày vào làm...">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="createStaffPassword">Mật Khẩu</label>
                        <input type="password" id="createStaffPassword" placeholder="Tạo mật khẩu đăng nhập">
                    </div>
                </form>
            </div>
            <footer class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                <button type="button" form="create-staff-form" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Thêm Nhân Viên
                </button>
            </footer>
        </div>
    </div>
    <!-- Form edit staff modal -->
    <div id="edit-staff-modal" class="modal">
        <div class="modal-content">
            <header class="modal-header">
                <h2 class="modal-title">Chỉnh Sửa Nhân Viên</h2>
                <button class="close-btn">&times;</button>
            </header>
            <div class="modal-body">
                <form id="edit-staff-form">
                    <div class="form-group">
                        <label>Họ Tên</label>
                        <input type="text" data-fill="name" placeholder="Nhập họ tên">
                    </div>
                    <div class="form-group">
                        <label>Chức Vụ</label>
                        <input type="text" data-fill="position" placeholder="Nhập chức vụ">
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" data-fill="email" placeholder="Nhập email">
                    </div>
                    <div class="form-group">
                        <label>Ngày Vào Làm</label>
                        <input type="text" data-fill="joinDate" placeholder="DD/MM/YYYY">
                    </div>

                    <input type="hidden" data-fill="id" name="staff_id">
                </form>
            </div>
            <footer class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-primary">Lưu Thay Đổi</button>
            </footer>
        </div>
    </div>
    <!-- Form delete staff modal -->
    <div id="delete-staff-modal" class="modal">
        <div class="modal-content">
            <header class="modal-header">
                <h2 class="modal-title">Xác nhận Xóa</h2>
                <button class="close-btn">&times;</button>
            </header>
            <div class="modal-body">
                <p>Bạn có chắc muốn xóa nhân viên <strong data-fill-text="name">[Tên nhân viên]</strong> không?</p>

                <input type="hidden" data-fill="id" id="staff-id-to-delete">
            </div>
            <footer class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-danger">Xóa</button>
            </footer>
        </div>
    </div>
    <!-- Log -->
    <div id="log" class="page-content">
        <header>
            <h1>Nhật Ký Hệ Thống</h1>
            <input type="text" class="search-bar" placeholder="Tìm kiếm hành động, người dùng...">
        </header>
        <div class="data-table-container">
            <div class="log-entry">
                <div class="log-icon log-info"><i class="fas fa-user-edit"></i></div>
                <div class="log-message">
                    <strong>Nguyễn Văn An</strong> đã cập nhật sản phẩm <strong>Áo Hoodie Nỉ Bông (#SP001)</strong>.
                </div>
                <div class="log-timestamp">5 phút trước</div>
            </div>
            <div class="log-entry">
                <div class="log-icon log-success"><i class="fas fa-plus-circle"></i></div>
                <div class="log-message">
                    <strong>Hệ thống</strong> đã thêm đơn hàng mới <strong>#DH-1710</strong>.
                </div>
                <div class="log-timestamp">1 giờ trước</div>
            </div>
            <div class="log-entry">
                <div class="log-icon log-warning"><i class="fas fa-exclamation-triangle"></i></div>
                <div class="log-message">
                    Đăng nhập không thành công từ IP <strong>203.162.1.47</strong>.
                </div>
                <div class="log-timestamp">3 giờ trước</div>
            </div>
            <div class="log-entry">
                <div class="log-icon log-info"><i class="fas fa-sign-in-alt"></i></div>
                <div class="log-message">
                    <strong>Lê Minh Anh</strong> đã đăng nhập vào hệ thống.
                </div>
                <div class="log-timestamp">8 giờ trước</div>
            </div>
        </div>
    </div>
</div>
<!-- Chart(Biểu đồ doanh thu) -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="cat.js"></script>
</body>
</html>