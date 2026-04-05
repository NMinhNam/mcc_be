# Danh sách API Frontend (FE UI) đang sử dụng

Tài liệu này thống kê các Endpoint mà ứng dụng Frontend đang gọi tới Backend (mặc định chạy tại `http://localhost:8080`).

## 1. Cấu hình chung
- **Base URL**: `/api` (được Vite Proxy chuyển tiếp tới `http://localhost:8080`)
- **Headers**:
    - `Content-Type: application/json`
    - `Authorization: Bearer <token>` (Lấy từ localStorage sau khi đăng nhập)

---

## 2. Nhóm Xác thực (Authentication)
Quản lý bởi `src/stores/auth.ts`.

| Phương thức | Endpoint | Mô tả | Payload mẫu |
| :--- | :--- | :--- | :--- |
| **POST** | `/auth/login` | Đăng nhập hệ thống | `{ "username": "admin", "password": "..." }` |

---

## 3. Nhóm Quản lý Sản phẩm (Products)
Quản lý bởi `src/stores/product.ts` và `src/api/productApi.ts`.

| Phương thức | Endpoint | Mô tả | Ghi chú |
| :--- | :--- | :--- | :--- |
| **GET** | `/products` | Lấy toàn bộ danh sách | Dùng cho Dashboard & List hiện tại |
| **GET** | `/products/page` | Lấy danh sách phân trang | Cần tích hợp thêm vào UI |
| **GET** | `/products/{id}` | Lấy chi tiết 1 sản phẩm | Gọi khi vào trang Cấu hình |
| **POST** | `/products` | Tạo sản phẩm mới | Gửi JSON: `{ "name": "...", "code": "..." }` |
| **PUT** | `/products/{id}` | Cập nhật thông tin | Cập nhật tên, Dài, Rộng, Cao |
| **DELETE** | `/products/{id}` | Xóa sản phẩm | Xóa vĩnh viễn sản phẩm và BOM |

---

## 4. Nhóm Định mức (BOM - Production Order Components)
Quản lý bởi `src/stores/bom.ts` và `src/api/bomApi.ts`.

| Phương thức | Endpoint | Mô tả | Ghi chú |
| :--- | :--- | :--- | :--- |
| **GET** | `/production-order-components/order/{id}` | Lấy danh sách BOM | `id` ở đây là `productionOrderId` |
| **POST** | `/production-order-components` | Tạo mới 1 dòng BOM | Gọi khi lưu dòng mới trong Grid |
| **PUT** | `/production-order-components/{id}` | Cập nhật 1 dòng BOM | Gọi khi nhấn nút Lưu |
| **DELETE** | `/production-order-components/{id}` | Xóa 1 dòng BOM | Gọi khi xóa dòng trong Grid |

---

## 5. Nhóm Danh mục (Master Data)
Dùng để đổ dữ liệu vào các Dropdown/Select trong giao diện.

| Phương thức | Endpoint | Mô tả | Ghi chú |
| :--- | :--- | :--- | :--- |
| **GET** | `/materials/page` | Danh sách vật tư | Dùng để chọn Material trong Grid |
| **GET** | `/material-types` | Danh sách loại vật tư | Phân loại vật tư |
| **GET** | `/component-groups/product/{id}` | Nhóm linh kiện | Phân nhóm trong bảng BOM |

---

## 🚀 Đề xuất tối ưu cho Backend Developer
1. **Bulk Save**: Nên bổ sung `POST /production-order-components/bulk` để nhận một mảng các dòng BOM, giúp giảm số lượng request khi nhấn nút Lưu.
2. **Pagination**: Frontend nên chuyển hẳn sang dùng `/products/page` để hỗ trợ tập dữ liệu lớn.
3. **Validation**: Backend nên trả về lỗi chi tiết (400 Bad Request) kèm thông tin field nào bị lỗi để FE hiển thị thông báo chính xác.
