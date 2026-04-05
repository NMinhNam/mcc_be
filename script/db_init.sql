-- Database Init Script for erp_bom
-- PostgreSQL 15+

-- Create schema
CREATE SCHEMA IF NOT EXISTS erp_bom;

-- 1. Users table
CREATE TABLE IF NOT EXISTS erp_bom.users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    role VARCHAR(50),
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 2. Products table
CREATE TABLE IF NOT EXISTS erp_bom.products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(500),
    image TEXT,
    note TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 3. Material types table
CREATE TABLE IF NOT EXISTS erp_bom.material_types (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    note TEXT
);

-- 4. Materials table
CREATE TABLE IF NOT EXISTS erp_bom.materials (
    id BIGSERIAL PRIMARY KEY,
    material_type_id BIGINT REFERENCES erp_bom.material_types(id),
    name VARCHAR(255) NOT NULL,
    unit VARCHAR(50),
    price_vnd DECIMAL(18,2),
    price_usd DECIMAL(18,2),
    density DECIMAL(18,4),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 5. Component groups table
CREATE TABLE IF NOT EXISTS erp_bom.component_groups (
    id BIGSERIAL PRIMARY KEY,
    product_id UUID REFERENCES erp_bom.products(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    sort_order INTEGER
);

-- 6. Product components table
CREATE TABLE IF NOT EXISTS erp_bom.product_components (
    id BIGSERIAL PRIMARY KEY,
    product_id UUID REFERENCES erp_bom.products(id) ON DELETE CASCADE,
    group_id BIGINT REFERENCES erp_bom.component_groups(id) ON DELETE SET NULL,
    material_id BIGINT REFERENCES erp_bom.materials(id) ON DELETE SET NULL,
    name VARCHAR(255),
    thickness_mm DECIMAL(10,2),
    width_mm DECIMAL(10,2),
    length_mm DECIMAL(10,2),
    pcs_per_product DECIMAL(10,2),
    waste_factor DECIMAL(5,4),
    note TEXT,
    sort_order INTEGER,
    is_extra_row BOOLEAN DEFAULT false,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 7. Product hardware table
CREATE TABLE IF NOT EXISTS erp_bom.product_hardware (
    id BIGSERIAL PRIMARY KEY,
    product_id UUID REFERENCES erp_bom.products(id) ON DELETE CASCADE,
    hardware_item_id BIGINT,
    qty_per_product DECIMAL(10,2),
    note TEXT
);

-- 8. BOM rows table
CREATE TABLE IF NOT EXISTS erp_bom.bom_rows (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID REFERENCES erp_bom.products(id) ON DELETE CASCADE,
    stt INTEGER,
    group_id VARCHAR(100),
    name VARCHAR(255),
    material VARCHAR(255),
    thickness DECIMAL(10,2),
    width DECIMAL(10,2),
    length DECIMAL(10,2),
    pcs INTEGER,
    multiplier DECIMAL(10,4),
    volume_net DECIMAL(18,6),
    volume_raw DECIMAL(18,6),
    bolt_quantity INTEGER,
    unit_price DECIMAL(18,2),
    currency VARCHAR(20),
    total_price DECIMAL(18,2),
    note TEXT
);

-- 9. Production orders table
CREATE TABLE IF NOT EXISTS erp_bom.production_orders (
    id BIGSERIAL PRIMARY KEY,
    order_no VARCHAR(50) NOT NULL UNIQUE,
    product_id UUID REFERENCES erp_bom.products(id),
    order_quantity DECIMAL(18,2),
    redo_quantity DECIMAL(18,2),
    status VARCHAR(50),
    note TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 10. Production order components table
CREATE TABLE IF NOT EXISTS erp_bom.production_order_components (
    id BIGSERIAL PRIMARY KEY,
    production_order_id BIGINT REFERENCES erp_bom.production_orders(id) ON DELETE CASCADE,
    product_component_id BIGINT,
    thickness_mm DECIMAL(10,2),
    width_mm DECIMAL(10,2),
    length_mm DECIMAL(10,2),
    pcs_per_product DECIMAL(10,2),
    result_pcs DECIMAL(10,2),
    volume_net_m3 DECIMAL(18,6),
    volume_gross_m3 DECIMAL(18,6),
    total_volume_net_m3 DECIMAL(18,6),
    total_volume_gross_m3 DECIMAL(18,6),
    material_price DECIMAL(18,2),
    material_cost DECIMAL(18,2)
);

-- 11. Production order hardware table
CREATE TABLE IF NOT EXISTS erp_bom.production_order_hardware (
    id BIGSERIAL PRIMARY KEY,
    production_order_id BIGINT REFERENCES erp_bom.production_orders(id) ON DELETE CASCADE,
    hardware_item_id BIGINT,
    qty_per_product DECIMAL(10,2),
    total_qty DECIMAL(10,2),
    unit_price DECIMAL(18,2),
    total_cost DECIMAL(18,2)
);

-- 12. Production order costs table
CREATE TABLE IF NOT EXISTS erp_bom.production_order_costs (
    id BIGSERIAL PRIMARY KEY,
    production_order_id BIGINT REFERENCES erp_bom.production_orders(id) ON DELETE CASCADE UNIQUE,
    material_total DECIMAL(18,2),
    hardware_total DECIMAL(18,2),
    other_cost DECIMAL(18,2),
    grand_total_vnd DECIMAL(18,2),
    grand_total_usd DECIMAL(18,2),
    sell_price_vnd DECIMAL(18,2),
    sell_price_usd DECIMAL(18,2),
    profit_vnd DECIMAL(18,2),
    profit_usd DECIMAL(18,2),
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_products_code ON erp_bom.products(code);
CREATE INDEX IF NOT EXISTS idx_bom_rows_product_id ON erp_bom.bom_rows(product_id);
CREATE INDEX IF NOT EXISTS idx_component_groups_product_id ON erp_bom.component_groups(product_id);
CREATE INDEX IF NOT EXISTS idx_product_components_product_id ON erp_bom.product_components(product_id);
CREATE INDEX IF NOT EXISTS idx_product_hardware_product_id ON erp_bom.product_hardware(product_id);
CREATE INDEX IF NOT EXISTS idx_production_orders_order_no ON erp_bom.production_orders(order_no);
CREATE INDEX IF NOT EXISTS idx_production_order_components_order_id ON erp_bom.production_order_components(production_order_id);
CREATE INDEX IF NOT EXISTS idx_production_order_hardware_order_id ON erp_bom.production_order_hardware(production_order_id);
CREATE INDEX IF NOT EXISTS idx_production_order_costs_order_id ON erp_bom.production_order_costs(production_order_id);
CREATE INDEX IF NOT EXISTS idx_materials_material_type_id ON erp_bom.materials(material_type_id);

-- Insert default admin user (password: admin123)
INSERT INTO erp_bom.users (username, password, name, role) 
VALUES ('admin', '$2a$10$N9qo8uLOickgxEWZRDqkxWq.T5qO6zEJmWq3VHZYauvihXMJDQ5L8O.G', 'Administrator', 'ADMIN')
ON CONFLICT (username) DO NOTHING;

-- Create extension for UUID if not exists
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =====================================================
-- MOCK DATA
-- =====================================================

-- Material Types
INSERT INTO erp_bom.material_types (code, name, note) VALUES
('TONG', 'Tôn', 'Loại tôn thông thường'),
('THEP', 'Thép', 'Loại thép xây dựng'),
('INOX', 'Inox', 'Thép không gỉ'),
('NHUA', 'Nhựa', 'Các loại nhựa tổng hợp')
ON CONFLICT (code) DO NOTHING;

-- Materials
INSERT INTO erp_bom.materials (material_type_id, name, unit, price_vnd, price_usd, density, is_active) VALUES
(1, 'Tôn cuộn mạ kẽm', 'kg', 15000, 0.60, 7.85, true),
(1, 'Tôn tây trắng', 'kg', 18000, 0.72, 7.85, true),
(1, 'Tôn sóng zin', 'm2', 120000, 4.80, 7.85, true),
(2, 'Thép tròn đặc', 'kg', 25000, 1.00, 7.85, true),
(2, 'Thép hộp vuông', 'kg', 28000, 1.12, 7.85, true),
(2, 'Thép V', 'kg', 26000, 1.04, 7.85, true),
(3, 'Inox 201', 'kg', 45000, 1.80, 7.85, true),
(3, 'Inox 304', 'kg', 85000, 3.40, 7.85, true),
(3, 'Inox 316', 'kg', 120000, 4.80, 7.85, true),
(4, 'PVC cứng', 'kg', 25000, 1.00, 1.40, true),
(4, 'PP', 'kg', 22000, 0.88, 0.90, true),
(4, 'ABS', 'kg', 35000, 1.40, 1.04, true)
ON CONFLICT DO NOTHING;

-- Products
INSERT INTO erp_bom.products (code, name, note) VALUES
('SP001', 'Tủ điện công nghiệp TD-01', 'Tủ điện 2 cánh'),
('SP002', 'Tủ điện công nghiệp TD-02', 'Tủ điện 1 cánh'),
('SP003', 'Bồn nước Inox BN-500', 'Bồn nước 500 lít'),
('SP004', 'Bồn nước Inox BN-1000', 'Bồn nước 1000 lít'),
('SP005', 'Kệ để tài liệu KTL-01', 'Kệ 4 tầng'),
('SP006', 'Bàn làm việc BLV-01', 'Bàn 2 người'),
('SP007', 'Giá kho hàng GK-01', 'Giá 3 tầng'),
('SP008', 'Thùng rác công nghiệp TR-01', 'Thùng 120 lít')
ON CONFLICT (code) DO NOTHING;