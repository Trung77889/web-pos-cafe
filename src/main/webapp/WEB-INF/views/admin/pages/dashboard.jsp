<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!-- Dashboard (Chứa Tổng quan doanh thu, đơn hàng, sản phẩm đã bán,)-->
<div id="dashboard" class="page-content">
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
                        <p class="order-id">#DH-10583
                            <span class="order-time">1 phút trước</span>
                        </p>
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
                        <p class="order-id">#DH-10582
                            <span class="order-time">10 phút trước</span>
                        </p>
                        <p class="order-summary">1x Set Lẩu Thái, 2x Bia Tiger</p>
                    </div>
                    <p class="order-total">580,000 ₫</p>
                    <div class="order-actions">
                        <button class="btn btn-payment"><i class="fa-solid fa-money-check"></i> Thanh toán
                        </button>
                    </div>
                </li>

                <li class="order-item status-new">
                    <div class="order-table">
                        <i class="fas fa-user-circle"></i>
                        <span>Bàn 02</span>
                    </div>
                    <div class="order-details">
                        <p class="order-id">#DH-10584
                            <span class="order-time">15 giây trước</span>
                        </p>
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
<!-- Chart(Biểu đồ doanh thu) -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>