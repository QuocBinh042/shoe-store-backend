-- Sử dụng database
USE ShoeStore;

-- Thêm dữ liệu cho bảng Role
INSERT INTO role (roleType, description, createdAt, updatedAt) VALUES
('SUPER_ADMIN', 'Toàn quyền trên hệ thống', NOW(), NOW()),
('ADMIN', 'Quản lý sản phẩm và đơn hàng', NOW(), NOW()),
('CUSTOMER', 'Khách hàng mua hàng', NOW(), NOW());

-- Thêm dữ liệu cho bảng Permission
INSERT INTO permission (name, description, createdAt, updatedAt) VALUES
('MANAGE_USERS', 'Quản lý người dùng', NOW(), NOW()),
('MANAGE_PRODUCTS', 'Quản lý sản phẩm', NOW(), NOW()),
('MANAGE_ORDERS', 'Quản lý đơn hàng', NOW(), NOW()),
('VIEW_REPORTS', 'Xem báo cáo', NOW(), NOW()),
('CREATE_ORDER', 'Tạo đơn hàng', NOW(), NOW()),
('VIEW_ORDER', 'Xem đơn hàng', NOW(), NOW()),
('WRITE_REVIEW', 'Viết đánh giá', NOW(), NOW()),
('VIEW_PRODUCTS', 'Xem sản phẩm', NOW(), NOW()),
('VIEW_PROMOTION', 'Xem khuyến mãi', NOW(), NOW());

-- Gán quyền cho các vai trò
-- SUPER_ADMIN có tất cả quyền
INSERT INTO role_permissions (roleID, permissionID)
SELECT r.roleID, p.permissionID
FROM role r, permission p
WHERE r.roleType = 'SUPER_ADMIN';

-- ADMIN có quyền quản lý sản phẩm, đơn hàng, báo cáo
INSERT INTO role_permissions (roleID, permissionID)
SELECT r.roleID, p.permissionID
FROM role r, permission p
WHERE r.roleType = 'ADMIN'
AND p.name IN ('MANAGE_PRODUCTS', 'MANAGE_ORDERS', 'VIEW_REPORTS');

-- CUSTOMER có quyền xem sản phẩm, đặt hàng, xem đơn hàng, viết đánh giá
INSERT INTO role_permissions (roleID, permissionID)
SELECT r.roleID, p.permissionID
FROM role r, permission p
WHERE r.roleType = 'CUSTOMER'
AND p.name IN ('CREATE_ORDER', 'VIEW_ORDER', 'WRITE_REVIEW', 'VIEW_PRODUCTS', 'VIEW_PROMOTION');

-- Thêm dữ liệu vào bảng Users
-- Admins
INSERT INTO Users (email, name, password, phoneNumber, status, createdAt, updatedAt)
VALUES
('admin1@example.com', 'Admin User 1', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0334567890', 'Active', NOW(), NOW()),
('admin2@example.com', 'Admin User 2', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0987654321', 'Active', NOW(), NOW()),
('admin3@example.com', 'Admin User 3', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0322334455', 'Active', NOW(), NOW());

-- Customers
INSERT INTO Users (email, name, password, phoneNumber, status, customerGroup, createdAt, updatedAt)
VALUES
('customer1@example.com', 'John Doe', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0333445566', 'Active', 'NEW', NOW(), NOW()),
('customer2@example.com', 'Jane Smith', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0944556677', 'Active', 'EXISTING', NOW(), NOW()),
('customer3@example.com', 'Alice Johnson', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0355667788', 'Active', 'VIP', NOW(), NOW()),
('customer4@example.com', 'Bob Brown', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0366778899', 'Active', 'NEW', NOW(), NOW()),
('customer5@example.com', 'Charlie Davis', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0377889900', 'Active', 'EXISTING', NOW(), NOW()),
('customer6@example.com', 'Diana Prince', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0388990011', 'Active', 'VIP', NOW(), NOW()),
('customer7@example.com', 'Ethan Hunt', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0399001122', 'Active', 'NEW', NOW(), NOW()),
('customer8@example.com', 'Fiona Gallagher', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0300112233', 'Active', 'EXISTING', NOW(), NOW()),
('customer9@example.com', 'George Clooney', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0311121314', 'Active', 'VIP', NOW(), NOW()),
('customer10@example.com', 'Hannah Montana', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0312131415', 'Active', 'NEW', NOW(), NOW()),
('customer11@example.com', 'Isaac Newton', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0313141516', 'Active', 'EXISTING', NOW(), NOW()),
('customer12@example.com', 'Jack Sparrow', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0314151617', 'Active', 'VIP', NOW(), NOW()),
('customer13@example.com', 'Kara Danvers', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0315161718', 'Active', 'NEW', NOW(), NOW()),
('customer14@example.com', 'Liam Hemsworth', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0316171819', 'Active', 'EXISTING', NOW(), NOW()),
('customer15@example.com', 'Mia Wallace', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0317181920', 'Active', 'VIP', NOW(), NOW()),
('customer16@example.com', 'Nina Dobrev', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0318192021', 'Active', 'NEW', NOW(), NOW()),
('customer17@example.com', 'Oscar Isaac', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0319202122', 'Active', 'EXISTING', NOW(), NOW()),
('customer18@example.com', 'Peter Parker', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0320212223', 'Active', 'VIP', NOW(), NOW()),
('customer19@example.com', 'Quinn Fabray', '$2a$10$piYOHuFhF7WWTyziAev08.RtlRcnZuruhfrTrgYWO6phJ4l1XvSBm', '0321222324', 'Active', 'NEW', NOW(), NOW());
-- Gán vai trò cho người dùng
INSERT INTO User_Roles (userID, roleID) VALUES
-- Admins
(1, 1),
(2, 1),
(3, 1),
-- Customers
(4, 3),
(5, 3),
(6, 3),
(7, 3),
(8, 3),
(9, 3),
(10, 3),
(11, 3),
(12, 3),
(13, 3),
(14, 3),
(15, 3),
(16, 3),
(17, 3),
(18, 3),
(19, 3),
(20, 3),
(21, 3),
(22, 3);

-- Thêm dữ liệu cho bảng Address
INSERT INTO Address (city, district, street, ward, fullName, phone, type, isDefault, userID, createdAt, updatedAt)
VALUES
('Hà Nội', 'Ba Đình', '123 Lê Lợi', 'Phường Cống Vị', 'John Doe', '0123456789', 'Home', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Hà Nội', 'Cầu Giấy', '456 Hoàng Hoa Thám', 'Phường Nghĩa Đô', 'Jane Doe', '0987654321', 'Office', 0, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Hà Nội', 'Đống Đa', '789 Nguyễn Chí Thanh', 'Phường Láng Hạ', 'Alice Smith', '0912345678', 'Home', 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TP. Hồ Chí Minh', 'Quận 1', '12 Hai Bà Trưng', 'Phường Bến Nghé', 'Bob Johnson', '0901122334', 'Office', 0, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TP. Hồ Chí Minh', 'Quận 2', '34 Nguyễn Huệ', 'Phường Thủ Thiêm', 'Charlie Brown', '0933445566', 'Home', 1, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Đà Nẵng', 'Hải Châu', '56 Trần Phú', 'Phường Hải Châu 1', 'David Lee', '0977889900', 'Home', 0, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Đà Nẵng', 'Thanh Khê', '78 Điện Biên Phủ', 'Phường Thanh Khê Đông', 'Eva Green', '0911223344', 'Office', 1, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Cần Thơ', 'Ninh Kiều', '90 Võ Văn Kiệt', 'Phường An Cư', 'Frank White', '0933778899', 'Home', 0, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Hải Phòng', 'Lê Chân', '11 Nguyễn Trãi', 'Phường An Dương', 'Grace Black', '0976554433', 'Office', 1, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Hải Phòng', 'Ngô Quyền', '22 Hai Bà Trưng', 'Phường Đông Khê', 'Helen Blue', '0944556677', 'Home', 0, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Thừa Thiên Huế', 'Thành phố Huế', '33 Nguyễn Công Trứ', 'Phường Phú Hội', 'Isaac Green', '0911334455', 'Home', 1, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Thừa Thiên Huế', 'Thành phố Huế', '44 Lê Lợi', 'Phường Phú Nhuận', 'Jack Brown', '0933223344', 'Office', 0, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Khánh Hòa', 'Nha Trang', '55 Trần Phú', 'Phường Vạn Thạnh', 'Kathy White', '0988776655', 'Home', 1, 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Khánh Hòa', 'Nha Trang', '66 Nguyễn Thiện Thuật', 'Phường Phước Tiến', 'Leo Black', '0912445566', 'Office', 0, 14, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bà Rịa - Vũng Tàu', 'Vũng Tàu', '77 Trần Hưng Đạo', 'Phường 1', 'Mona Green', '0944332211', 'Home', 1, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bà Rịa - Vũng Tàu', 'Vũng Tàu', '88 Nguyễn Trãi', 'Phường Thắng Nhì', 'Nick White', '0933442233', 'Office', 0, 16, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Lâm Đồng', 'Đà Lạt', '99 Lê Hồng Phong', 'Phường 4', 'Olivia Brown', '0911222211', 'Home', 1, 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Lâm Đồng', 'Đà Lạt', '10 Nguyễn Văn Cừ', 'Phường 1', 'Paul Black', '0988334455', 'Office', 0, 18, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bình Định', 'Quy Nhơn', '20 Trần Hưng Đạo', 'Phường Lê Lợi', 'Quinn Green', '0944551122', 'Home', 1, 19, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bình Định', 'Quy Nhơn', '30 Võ Thị Sáu', 'Phường Trần Phú', 'Rita White', '0912342211', 'Office', 0, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Thêm dữ liệu cho bảng Brand
INSERT INTO Brand (name, createdAt, updatedAt)
VALUES
('Nike', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Adidas', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Puma', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Reebok', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Under Armour', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Thêm dữ liệu cho bảng Category
INSERT INTO Category (description, name, createdAt, updatedAt)
VALUES
('Shoes for running and sports', 'Running', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Shoes for casual wear', 'Lifestyle', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Shoes for basketball players', 'Basketball', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Shoes for American football players', 'Football', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Shoes for gym and training', 'Training', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('High-quality tennis shoes', 'Tennis', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Shoes for hiking and trekking', 'Hiking', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Specialized soccer shoes', 'Soccer', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Shoes for kids and youth', 'Kids', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Luxury brand shoes', 'Luxury', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Thêm dữ liệu cho bảng Supplier
INSERT INTO Supplier (address, supplierName, createdAt, updatedAt)
VALUES
('123 Main St, New York, NY', 'Global Sports Inc.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('456 Elm St, San Francisco, CA', 'Elite Shoes LLC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('789 Maple Ave, Chicago, IL', 'Active Wear Co.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('321 Oak St, Seattle, WA', 'Fit and Style Ltd.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('654 Pine St, Boston, MA', 'Performance Gear Corp.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('987 Cedar Rd, Austin, TX', 'Trendsetters Supply', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('159 Spruce St, Miami, FL', 'Urban Outfits Inc.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('753 Birch St, Denver, CO', 'Trail Blazers Ltd.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('951 Aspen Dr, Atlanta, GA', 'Southern Comfort Gear', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('852 Redwood Ave, Portland, OR', 'Pacific Edge Shoes', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Thêm dữ liệu cho bảng Supplier_PhoneNumber
INSERT INTO Supplier_PhoneNumber (supplierID, phoneNumbers)
VALUES
(1, '0911111111'),
(1, '0911222222'),
(2, '0913333333'),
(3, '0914444444'),
(4, '0915555555'),
(5, '0916666666'),
(5, '0911111156'),
(6, '0911222999'),
(6, '0913333458'),
(7, '0914444454'),
(8, '0915554564'),
(9, '0916666846');

-- Thêm dữ liệu cho bảng Product (phải chạy trước Promotion vì Promotion có khóa ngoại đến Product)
INSERT INTO Product (description, price, productName, status, brandID, categoryID, promotionID, supplierID, createdAt, updatedAt)
VALUES
('High-performance running shoes.', 1000000, 'Nike Air Max 270', 'AVAILABLE', 1, 1, NULL, 1, NOW(), NOW()),
('Lightweight basketball sneakers.', 1200000, 'Adidas Harden Vol. 5', 'AVAILABLE', 2, 2, NULL, 2, NOW(), NOW()),
('Stylish hiking boots.', 1500000, 'Timberland Premium', 'AVAILABLE', 3, 3, NULL, 3, NOW(), NOW()),
('Everyday wear sneakers.', 800000, 'Converse Chuck Taylor', 'AVAILABLE', 4, 4, NULL, 4, NOW(), NOW()),
('Elegant leather loafers.', 1000000, 'Clarks Originals', 'AVAILABLE', 5, 5, NULL, 5, NOW(), NOW()),
('Performance tennis shoes.', 1100000, 'Asics Gel-Resolution', 'AVAILABLE', 1, 6, NULL, 6, NOW(), NOW()),
('Classic skate shoes.', 750000, 'Vans Old Skool', 'AVAILABLE', 2, 7, NULL, 7, NOW(), NOW()),
('Premium long-distance runners.', 950000, 'Brooks Ghost 14', 'AVAILABLE', 3, 8, NULL, 8, NOW(), NOW()),
('Breathable sports sandals.', 500000, 'Teva Hurricane XLT2', 'AVAILABLE', 4, 9, NULL, 9, NOW(), NOW()),
('Luxury high heels.', 2000000, 'Jimmy Choo Romy 100', 'AVAILABLE', 5, 10, NULL, 10, NOW(), NOW()),
('Comfort-focused sneakers.', 850000, 'New Balance 574', 'AVAILABLE', 1, 1, NULL, 1, NOW(), NOW()),
('High-performance trainers.', 1200000, 'Under Armour HOVR', 'AVAILABLE', 2, 2, NULL, 2, NOW(), NOW()),
('Waterproof outdoor boots.', 1300000, 'Columbia Bugaboot', 'AVAILABLE', 3, 3, NULL, 3, NOW(), NOW()),
('Athletic running shoes.', 1000000, 'Saucony Endorphin', 'AVAILABLE', 4, 4, NULL, 4, NOW(), NOW()),
('Modern casual loafers.', 900000, 'Sperry Top-Sider', 'AVAILABLE', 5, 5, NULL, 5, NOW(), NOW()),
('Lightweight running shoes.', 750000, 'Reebok Floatride', 'AVAILABLE', 1, 6, NULL, 6, NOW(), NOW()),
('Classic canvas shoes.', 550000, 'Keds Champion', 'AVAILABLE', 2, 7, NULL, 7, NOW(), NOW()),
('Trail running shoes.', 1100000, 'Salomon Speedcross', 'AVAILABLE', 3, 8, NULL, 8, NOW(), NOW()),
('Minimalist sports sandals.', 600000, 'Xero Shoes Z-Trail', 'AVAILABLE', 4, 9, NULL, 9, NOW(), NOW()),
('Luxury dress shoes.', 2500000, 'Gucci Ace Sneakers', 'AVAILABLE', 5, 10, NULL, 10, NOW(), NOW()),
('Casual slip-on sneakers.', 700000, 'Skechers Go Walk', 'AVAILABLE', 1, 1, NULL, 1, NOW(), NOW()),
('Basketball performance shoes.', 1400000, 'Puma Clyde All-Pro', 'AVAILABLE', 2, 2, NULL, 2, NOW(), NOW()),
('Hiking boots with ankle support.', 1600000, 'North Face Vectiv', 'AVAILABLE', 3, 3, NULL, 3, NOW(), NOW()),
('Canvas skate shoes.', 750000, 'DC Shoes Trase', 'AVAILABLE', 4, 4, NULL, 4, NOW(), NOW()),
('Formal leather oxford shoes.', 1300000, 'Allen Edmonds Park Ave', 'AVAILABLE', 5, 5, NULL, 5, NOW(), NOW()),
('Cushioned tennis shoes.', 1150000, 'Fila Axilus 2 Energized', 'AVAILABLE', 1, 6, NULL, 6, NOW(), NOW()),
('Classic low-top sneakers.', 900000, 'Lacoste Carnaby Evo', 'AVAILABLE', 2, 7, NULL, 7, NOW(), NOW()),
('Trail running shoes for grip.', 1250000, 'Hoka Speedgoat', 'AVAILABLE', 3, 8, NULL, 8, NOW(), NOW()),
('Open-toe sports sandals.', 450000, 'Merrell Hydro MOC', 'AVAILABLE', 4, 9, NULL, 9, NOW(), NOW()),
('Elegant party heels.', 2200000, 'Manolo Blahnik BB', 'AVAILABLE', 5, 10, NULL, 10, NOW(), NOW()),
('Everyday cushioned sneakers.', 1000000, 'On Cloud 5', 'AVAILABLE', 1, 1, NULL, 1, NOW(), NOW()),
('Durable basketball sneakers.', 1500000, 'Jordan Retro 1', 'AVAILABLE', 2, 2, NULL, 2, NOW(), NOW()),
('Warm insulated boots.', 1800000, 'Sorel Caribou', 'AVAILABLE', 3, 3, NULL, 3, NOW(), NOW()),
('Stylish slip-ons.', 700000, 'Toms Alpargata', 'AVAILABLE', 4, 4, NULL, 4, NOW(), NOW()),
('Premium leather loafers.', 1900000, 'Tods Gommino', 'AVAILABLE', 5, 5, NULL, 5, NOW(), NOW()),
('High-traction trail runners.', 1300000, 'Arcteryx Norvan', 'AVAILABLE', 1, 6, NULL, 6, NOW(), NOW()),
('Skateboarding classics.', 750000, 'Globe Sabre', 'AVAILABLE', 2, 7, NULL, 7, NOW(), NOW()),
('All-terrain shoes.', 1400000, 'Altra Lone Peak', 'AVAILABLE', 3, 8, NULL, 8, NOW(), NOW()),
('Quick-dry sandals.', 500000, 'Chaco Z/Cloud', 'AVAILABLE', 4, 9, NULL, 9, NOW(), NOW()),
('Luxury red sole heels.', 8000000, 'Christian Louboutin So Kate', 'AVAILABLE', 5, 10, NULL, 10, NOW(), NOW()),
('Lightweight walking shoes.', 600000, 'Skechers Flex Appeal', 'AVAILABLE', 1, 1, NULL, 1, NOW(), NOW()),
('Responsive basketball shoes.', 1300000, 'Nike Zoom Freak', 'AVAILABLE', 2, 2, NULL, 2, NOW(), NOW()),
('Rugged hiking boots.', 1700000, 'Scarpa Zodiac Plus', 'DISCONTINUED', 3, 3, NULL, 3, NOW(), NOW()),
('Casual street sneakers.', 950000, 'Adidas Superstar', 'UNAVAILABLE', 4, 4, NULL, 4, NOW(), NOW()),
('Italian leather dress shoes.', 2500000, 'Santoni Double Monk', 'LIMITED_STOCK', 5, 5, NULL, 5, NOW(), NOW());

-- Thêm dữ liệu cho bảng Promotion (sau khi Product đã có dữ liệu)
INSERT INTO Promotion (
    name, description, type, discountValue, buyQuantity, getQuantity, gift_product_id,
    startDate, endDate, applicableTo, status, usageCount,
    createdAt, updatedAt
) VALUES
    ('Summer Sale 2023', 'Percentage (10%)', 'PERCENTAGE', 10.00, NULL, NULL, NULL,
     '2023-06-01 00:00:00', '2023-08-31 23:59:59', 'ALL', 'ACTIVE', 328,
     NOW(), NOW()),
    ('New Customer Discount', 'Fixed Amount ($20)', 'FIXED', 20000, NULL, NULL, NULL,
     '2023-05-15 00:00:00', '2023-12-31 23:59:59', 'ALL', 'ACTIVE', 145,
     NOW(), NOW()),
    ('Back to School', 'Buy 2 Get 1 Free', 'BUYX', NULL, 2, 1, NULL,
     '2023-08-01 00:00:00', '2023-09-15 23:59:59', 'CATEGORIES', 'UPCOMING', 0,
     NOW(), NOW()),
    ('Holiday Gift', 'Free Gift', 'GIFT', NULL, NULL, NULL, 1, -- productID = 1 đã tồn tại
     '2023-12-01 00:00:00', '2023-12-25 23:59:59', 'ALL', 'UPCOMING', 0,
     NOW(), NOW()),
    ('Black Friday', 'Percentage (30%)', 'PERCENTAGE', 30.00, NULL, NULL, NULL,
     '2022-11-24 00:00:00', '2022-11-28 23:59:59', 'ALL', 'EXPIRED', 512,
     NOW(), NOW()),
    ('Sale 10%', '10% off promotion for products', 'PERCENTAGE', 10.00, NULL, NULL, NULL,
     '2025-02-01 00:00:00', '2025-07-28 23:59:59', 'ALL', 'ACTIVE', 0,
     NOW(), NOW()),
    ('Sale 20%', '20% off promotion for products', 'PERCENTAGE', 20.00, NULL, NULL, NULL,
     '2025-02-01 00:00:00', '2025-07-28 23:59:59', 'ALL', 'ACTIVE', 0,
     NOW(), NOW()),
    ('Sale 50%', '50% off promotion', 'PERCENTAGE', 50.00, NULL, NULL, NULL,
     '2025-02-01 00:00:00', '2025-07-28 23:59:59', 'ALL', 'ACTIVE', 0,
     NOW(), NOW()),
    ('Sale 70%', '70% off promotion', 'PERCENTAGE', 70.00, NULL, NULL, NULL,
     '2025-02-01 00:00:00', '2025-07-28 23:59:59', 'ALL', 'ACTIVE', 0,
     NOW(), NOW()),
    ('Sale 5%', '5% off promotion for products', 'PERCENTAGE', 5.00, NULL, NULL, NULL,
     '2025-02-01 00:00:00', '2025-07-28 23:59:59', 'ALL', 'ACTIVE', 0,
     NOW(), NOW());

-- Thêm dữ liệu cho bảng promotion_customer_groups
-- Promotion 1: Summer Sale 2023 (customerGroup: EXISTING)
INSERT INTO promotion_customer_groups (promotion_id, customer_group) VALUES (1, 'EXISTING');

-- Promotion 2: New Customer Discount (customerGroup: NEW)
INSERT INTO promotion_customer_groups (promotion_id, customer_group) VALUES (2, 'NEW');

-- Promotion 3: Back to School (customerGroup: EXISTING)
INSERT INTO promotion_customer_groups (promotion_id, customer_group) VALUES (3, 'EXISTING');

-- Promotion 4: Holiday Gift (customerGroup: EXISTING)
INSERT INTO promotion_customer_groups (promotion_id, customer_group) VALUES (4, 'EXISTING');

-- Promotion 5: Black Friday (customerGroup: EXISTING)
INSERT INTO promotion_customer_groups (promotion_id, customer_group) VALUES (5, 'EXISTING');

-- Promotion 6: Sale 10% (customerGroup: EXISTING)
INSERT INTO promotion_customer_groups (promotion_id, customer_group) VALUES (6, 'EXISTING');

-- Promotion 7: Sale 20% (customerGroup: EXISTING)
INSERT INTO promotion_customer_groups (promotion_id, customer_group) VALUES (7, 'EXISTING');

-- Promotion 8: Sale 50% (customerGroup: EXISTING)
INSERT INTO promotion_customer_groups (promotion_id, customer_group) VALUES (8, 'EXISTING');

-- Promotion 9: Sale 70% (customerGroup: EXISTING)
INSERT INTO promotion_customer_groups (promotion_id, customer_group) VALUES (9, 'EXISTING');

-- Promotion 10: Sale 5% (customerGroup: EXISTING)
INSERT INTO promotion_customer_groups (promotion_id, customer_group) VALUES (10, 'EXISTING');

DELETE FROM promotion_customer_groups WHERE promotion_id = 1;

-- Thêm tất cả các nhóm khách hàng cho promotion 1
INSERT INTO promotion_customer_groups (promotion_id, customer_group) VALUES
(1, 'NEW'),
(1, 'EXISTING'),
(1, 'VIP');
-- Cập nhật lại Product để gán promotionID (sau khi Promotion đã có dữ liệu)
UPDATE Product SET promotionID = 1 WHERE productID = 1;
UPDATE Product SET promotionID = 2 WHERE productID = 2;
UPDATE Product SET promotionID = 4 WHERE productID = 3;
UPDATE Product SET promotionID = 5 WHERE productID = 4;
UPDATE Product SET promotionID = 1 WHERE productID = 5;
UPDATE Product SET promotionID = 3 WHERE productID = 6;
UPDATE Product SET promotionID = 2 WHERE productID = 7;

INSERT INTO ProductDetail (color, size, stockQuantity, productID, status, image, createdAt, updatedAt)
VALUES
('RED',    'SIZE_40', 50,  1, 'AVAILABLE',     CONCAT('project_ShoeStore/ImageProduct/', 1, '/product', 1, '_1'), NOW(), NOW()),
('BLUE',   'SIZE_41', 30,  1, 'LIMITED_STOCK', CONCAT('project_ShoeStore/ImageProduct/', 1, '/product', 1, '_2'), NOW(), NOW()),
('BLACK',  'SIZE_42', 25,  2, 'AVAILABLE',     CONCAT('project_ShoeStore/ImageProduct/', 2, '/product', 2, '_1'), NOW(), NOW()),
('WHITE',  'SIZE_43', 40,  2, 'AVAILABLE',     CONCAT('project_ShoeStore/ImageProduct/', 2, '/product', 2, '_2'), NOW(), NOW()),
('WHITE',  'SIZE_44', 20,  3, 'LIMITED_STOCK', CONCAT('project_ShoeStore/ImageProduct/', 3, '/product', 3, '_1'), NOW(), NOW()),
('PINK',   'SIZE_40', 15,  3, 'UNAVAILABLE',   CONCAT('project_ShoeStore/ImageProduct/', 3, '/product', 3, '_2'), NOW(), NOW()),
('YELLOW', 'SIZE_41', 35,  4, 'AVAILABLE',     CONCAT('project_ShoeStore/ImageProduct/', 4, '/product', 4, '_1'), NOW(), NOW()),
('WHITE',  'SIZE_42', 18,  5, 'AVAILABLE',     CONCAT('project_ShoeStore/ImageProduct/', 5, '/product', 5, '_1'), NOW(), NOW()),
('GREEN',  'SIZE_43', 22,  6, 'AVAILABLE',     CONCAT('project_ShoeStore/ImageProduct/', 6, '/product', 6, '_1'), NOW(), NOW()),
('YELLOW', 'SIZE_44', 28,  7, 'AVAILABLE',     CONCAT('project_ShoeStore/ImageProduct/', 7, '/product', 7, '_1'), NOW(), NOW()),
('BLACK',  'SIZE_40', 10,  8, 'UNAVAILABLE',   CONCAT('project_ShoeStore/ImageProduct/', 8, '/product', 8, '_1'), NOW(), NOW()),
('WHITE',  'SIZE_41', 14,  9, 'UNAVAILABLE',   CONCAT('project_ShoeStore/ImageProduct/', 9, '/product', 9, '_1'), NOW(), NOW()),
('BLUE',   'SIZE_42', 33, 10, 'AVAILABLE',     CONCAT('project_ShoeStore/ImageProduct/', 10, '/product', 10, '_1'), NOW(), NOW()),
('RED',    'SIZE_43', 27, 11, 'AVAILABLE',     CONCAT('project_ShoeStore/ImageProduct/', 11, '/product', 11, '_1'), NOW(), NOW()),
('PINK',   'SIZE_44', 12, 12, 'UNAVAILABLE',   CONCAT('project_ShoeStore/ImageProduct/', 12, '/product', 12, '_1'), NOW(), NOW()),
('PINK',   'SIZE_40', 31, 13, 'AVAILABLE',     CONCAT('project_ShoeStore/ImageProduct/', 13, '/product', 13, '_1'), NOW(), NOW()),
('GREEN',  'SIZE_41', 19, 14, 'UNAVAILABLE',   CONCAT('project_ShoeStore/ImageProduct/', 14, '/product', 14, '_1'), NOW(), NOW()),
('YELLOW', 'SIZE_42', 21, 15, 'LIMITED_STOCK', CONCAT('project_ShoeStore/ImageProduct/', 15, '/product', 15, '_1'), NOW(), NOW()),
('GREEN',  'SIZE_43', 26, 16, 'AVAILABLE',     CONCAT('project_ShoeStore/ImageProduct/', 16, '/product', 16, '_1'), NOW(), NOW()),
('YELLOW', 'SIZE_44', 17, 17, 'UNAVAILABLE',   CONCAT('project_ShoeStore/ImageProduct/', 17, '/product', 17, '_1'), NOW(), NOW());


-- Thêm dữ liệu cho bảng Product_ImageURL
INSERT INTO Product_ImageURL (productID, imageURL)
VALUES
(1, 'product1_1.png'), (1, 'product1_2.png'), (1, 'product1_3.png'), (1, 'product1_4.png'), (1, 'product1_5.png'),
(2, 'product2_1.png'), (2, 'product2_2.png'), (2, 'product2_3.png'), (2, 'product2_4.png'), (2, 'product2_5.png'),
(3, 'product3_1.png'), (3, 'product3_2.png'), (3, 'product3_3.png'), (3, 'product3_4.png'), (3, 'product3_5.png'),
(4, 'product4_1.png'), (4, 'product4_2.png'), (4, 'product4_3.png'), (4, 'product4_4.png'), (4, 'product4_5.png'),
(5, 'product5_1.png'), (5, 'product5_2.png'), (5, 'product5_3.png'), (5, 'product5_4.png'), (5, 'product5_5.png'),
(6, 'product6_1.png'), (6, 'product6_2.png'), (6, 'product6_3.png'), (6, 'product6_4.png'), (6, 'product6_5.png'),
(7, 'product7_1.png'), (7, 'product7_2.png'), (7, 'product7_3.png'), (7, 'product7_4.png'), (7, 'product7_5.png'),
(8, 'product8_1.png'), (8, 'product8_2.png'), (8, 'product8_3.png'), (8, 'product8_4.png'), (8, 'product8_5.png'),
(9, 'product9_1.png'), (9, 'product9_2.png'), (9, 'product9_3.png'), (9, 'product9_4.png'), (9, 'product9_5.png'),
(10, 'product10_1.png'), (10, 'product10_2.png'), (10, 'product10_3.png'), (10, 'product10_4.png'), (10, 'product10_5.png'),
(11, 'product11_1.png'), (11, 'product11_2.png'), (11, 'product11_3.png'), (11, 'product11_4.png'), (11, 'product11_5.png'),
(12, 'product12_1.png'), (12, 'product12_2.png'), (12, 'product12_3.png'), (12, 'product12_4.png'), (12, 'product12_5.png'),
(13, 'product13_1.png'), (13, 'product13_2.png'), (13, 'product13_3.png'), (13, 'product13_4.png'), (13, 'product13_5.png'),
(14, 'product14_1.png'), (14, 'product14_2.png'), (14, 'product14_3.png'), (14, 'product14_4.png'), (14, 'product14_5.png'),
(15, 'product15_1.png'), (15, 'product15_2.png'), (15, 'product15_3.png'), (15, 'product15_4.png'), (15, 'product15_5.png'),
(16, 'product16_1.png'), (16, 'product16_2.png'), (16, 'product16_3.png'), (16, 'product16_4.png'), (16, 'product16_5.png'),
(17, 'product17_1.png'), (17, 'product17_2.png'), (17, 'product17_3.png'), (17, 'product17_4.png'), (17, 'product17_5.png'),
(18, 'product18_1.png'), (18, 'product18_2.png'), (18, 'product18_3.png'), (18, 'product18_4.png'), (18, 'product18_5.png'),
(19, 'product19_1.png'), (19, 'product19_2.png'), (19, 'product19_3.png'), (19, 'product19_4.png'), (19, 'product19_5.png'),
(20, 'product20_1.png'), (20, 'product20_2.png'), (20, 'product20_3.png'), (20, 'product20_4.png'), (20, 'product20_5.png'),
(21, 'product21_1.png'), (21, 'product21_2.png'), (21, 'product21_3.png'), (21, 'product21_4.png'), (21, 'product21_5.png'),
(22, 'product22_1.png'), (22, 'product22_2.png'), (22, 'product22_3.png'), (22, 'product22_4.png'), (22, 'product22_5.png'),
(23, 'product23_1.png'), (23, 'product23_2.png'), (23, 'product23_3.png'), (23, 'product23_4.png'), (23, 'product23_5.png'),
(24, 'product24_1.png'), (24, 'product24_2.png'), (24, 'product24_3.png'), (24, 'product24_4.png'), (24, 'product24_5.png'),
(25, 'product25_1.png'), (25, 'product25_2.png'), (25, 'product25_3.png'), (25, 'product25_4.png'), (25, 'product25_5.png'),
(26, 'product26_1.png'), (26, 'product26_2.png'), (26, 'product26_3.png'), (26, 'product26_4.png'), (26, 'product26_5.png'),
(27, 'product27_1.png'), (27, 'product27_2.png'), (27, 'product27_3.png'), (27, 'product27_4.png'), (27, 'product27_5.png'),
(28, 'product28_1.png'), (28, 'product28_2.png'), (28, 'product28_3.png'), (28, 'product28_4.png'), (28, 'product28_5.png'),
(29, 'product29_1.png'), (29, 'product29_2.png'), (29, 'product29_3.png'), (29, 'product29_4.png'), (29, 'product29_5.png'),
(30, 'product30_1.png'), (30, 'product30_2.png'), (30, 'product30_3.png'), (30, 'product30_4.png'), (30, 'product30_5.png'),
(31, 'product31_1.png'), (31, 'product31_2.png'), (31, 'product31_3.png'), (31, 'product31_4.png'), (31, 'product31_5.png'),
(32, 'product32_1.png'), (32, 'product32_2.png'), (32, 'product32_3.png'), (32, 'product32_4.png'), (32, 'product32_5.png'),
(33, 'product33_1.png'), (33, 'product33_2.png'), (33, 'product33_3.png'), (33, 'product33_4.png'), (33, 'product33_5.png'),
(34, 'product34_1.png'), (34, 'product34_2.png'), (34, 'product34_3.png'), (34, 'product34_4.png'), (34, 'product34_5.png'),
(35, 'product35_1.png'), (35, 'product35_2.png'), (35, 'product35_3.png'), (35, 'product35_4.png'), (35, 'product35_5.png'),
(36, 'product36_1.png'), (36, 'product36_2.png'), (36, 'product36_3.png'), (36, 'product36_4.png'), (36, 'product36_5.png'),
(37, 'product37_1.png'), (37, 'product37_2.png'), (37, 'product37_3.png'), (37, 'product37_4.png'), (37, 'product37_5.png'),
(38, 'product38_1.png'), (38, 'product38_2.png'), (38, 'product38_3.png'), (38, 'product38_4.png'), (38, 'product38_5.png'),
(39, 'product39_1.png'), (39, 'product39_2.png'), (39, 'product39_3.png'), (39, 'product39_4.png'), (39, 'product39_5.png'),
(40, 'product40_1.png'), (40, 'product40_2.png'), (40, 'product40_3.png'), (40, 'product40_4.png'), (40, 'product40_5.png'),
(41, 'product41_1.png'), (41, 'product41_2.png'), (41, 'product41_3.png'), (41, 'product41_4.png'), (41, 'product41_5.png'),
(42, 'product42_1.png'), (42, 'product42_2.png'), (42, 'product42_3.png'), (42, 'product42_4.png'), (42, 'product42_5.png'),
(43, 'product43_1.png'), (43, 'product43_2.png'), (43, 'product43_3.png'), (43, 'product43_4.png'), (43, 'product43_5.png'),
(44, 'product44_1.png'), (44, 'product44_2.png'), (44, 'product44_3.png'), (44, 'product44_4.png'), (44, 'product44_5.png'),
(45, 'product45_1.png'), (45, 'product45_2.png'), (45, 'product45_3.png'), (45, 'product45_4.png'), (45, 'product45_5.png');

-- Thêm dữ liệu cho bảng Voucher
INSERT INTO Voucher (code, description, discountValue, discountType, minOrderValue, freeShipping, startDate, endDate, status, createdAt, updatedAt)
VALUES
('SPRINGSALE20', 'Get 20% off on your next order.', 20, 'PERCENT', 100000, 0, '2025-02-01T00:00:00', '2025-03-15T23:59:59', 1, NOW(), NOW()),
('FREESHIP100K', 'Free shipping for orders above 100,000 VND.', 0, 'FIXED', 100000, 1, '2025-02-01T00:00:00', '2025-03-10T23:59:59', 1, NOW(), NOW()),
('SUMMERSALE15', 'Save 15% on summer collection.', 15, 'PERCENT', 50000, 0, '2025-01-01T00:00:00', '2025-06-30T23:59:59', 1, NOW(), NOW()),
('NEWUSER50K', 'Get 50,000 VND off your first order.', 50000, 'FIXED', 50000, 0, '2025-01-01T00:00:00', '2025-01-31T23:59:59', 1, NOW(), NOW()),
('WINTERDISCOUNT25', 'Get 25% off on all winter wear.', 25, 'PERCENT', 200000, 0, '2024-12-01T00:00:00', '2025-12-31T23:59:59', 1, NOW(), NOW());

-- Thêm dữ liệu cho bảng Cart
INSERT INTO Cart (createdAt, updatedAt, userID)
VALUES
(NOW(), NOW(), 1),
(NOW(), NOW(), 2),
(NOW(), NOW(), 3),
(NOW(), NOW(), 4),
(NOW(), NOW(), 5),
(NOW(), NOW(), 6),
(NOW(), NOW(), 7),
(NOW(), NOW(), 8),
(NOW(), NOW(), 9),
(NOW(), NOW(), 10),
(NOW(), NOW(), 11),
(NOW(), NOW(), 12),
(NOW(), NOW(), 13),
(NOW(), NOW(), 14),
(NOW(), NOW(), 15),
(NOW(), NOW(), 16),
(NOW(), NOW(), 17),
(NOW(), NOW(), 18),
(NOW(), NOW(), 19),
(NOW(), NOW(), 20),
(NOW(), NOW(), 21);

-- Thêm dữ liệu cho bảng CartItem
INSERT INTO CartItem (quantity, cartID, productDetailID, createdAt, updatedAt)
VALUES
(2, 1, 1, NOW(), NOW()),
(1, 1, 2, NOW(), NOW()),
(3, 2, 3, NOW(), NOW()),
(1, 2, 4, NOW(), NOW()),
(2, 3, 5, NOW(), NOW()),
(1, 3, 6, NOW(), NOW()),
(2, 4, 7, NOW(), NOW()),
(1, 4, 8, NOW(), NOW()),
(3, 5, 9, NOW(), NOW()),
(2, 5, 10, NOW(), NOW()),
(1, 6, 11, NOW(), NOW()),
(2, 6, 12, NOW(), NOW()),
(1, 7, 13, NOW(), NOW()),
(3, 7, 14, NOW(), NOW()),
(1, 8, 15, NOW(), NOW()),
(2, 8, 16, NOW(), NOW()),
(1, 9, 17, NOW(), NOW()),
(2, 9, 18, NOW(), NOW()),
(3, 10, 19, NOW(), NOW()),
(1, 10, 20, NOW(), NOW());

-- Thêm dữ liệu cho bảng Orders
INSERT INTO Orders (orderDate, status, total, feeShip, code, voucherID, shippingAddress, paymentMethod,voucherDiscount, userID, createdAt, updatedAt)
VALUES
(NOW(), 'PENDING', 4400000, 30000, DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, '123 Maple St, City, Country', 'CASH', 0, 4, NOW(), NOW()),
(NOW(), 'SHIPPED', 3200000, 0, DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, '456 Pine Rd, City, Country', 'CASH', 0, 5, NOW(), NOW()),
(NOW(), 'DELIVERED', 4050000, 0, DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, '789 Oak Ave, City, Country', 'CASH', 0, 6, NOW(), NOW()),
(NOW(), 'PENDING', 3500000, 0, DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, '654 Cedar Ln, City, Country', 'VNPAY', 0, 7, NOW(), NOW()),
(NOW(), 'DELIVERED', 2150000, 0, DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), NULL, '987 Pine St, City, Country', 'VNPAY', 0, 8, NOW(), NOW());

-- Thêm dữ liệu cho bảng Receipt
INSERT INTO Receipt (total, receiptDate, orderID, createdAt, updatedAt)
VALUES
(4050000, NOW(), 3, NOW(), NOW()),
(2150000, NOW(), 5, NOW(), NOW());

-- Thêm dữ liệu cho bảng OrderDetail
INSERT INTO OrderDetail (
    orderID, 
    productDetailID, 
    quantity, 
    price, 
    createdAt, 
    updatedAt, 
    promotionID, 
    giftProductDetailID, 
    giftedQuantity, 
    promoDiscount
) VALUES
(1, 3, 2, 1500000, NOW(), NOW(), NULL, NULL, 0, NULL),
(1, 5, 1, 1400000, NOW(), NOW(), NULL, NULL, 0, NULL),
(2, 2, 2, 1200000, NOW(), NOW(), NULL, NULL, 0, NULL),
(2, 4, 1, 800000, NOW(), NOW(), NULL, NULL, 0, NULL),
(3, 6, 1, 1800000, NOW(), NOW(), NULL, NULL, 0, NULL),
(3, 7, 3, 750000, NOW(), NOW(), NULL, NULL, 0, NULL),
(4, 1, 2, 1000000, NOW(), NOW(), NULL, NULL, 0, NULL),
(4, 3, 1, 1500000, NOW(), NOW(), NULL, NULL, 0, NULL),
(5, 2, 1, 1200000, NOW(), NOW(), NULL, NULL, 0, NULL),
(5, 6, 1, 950000, NOW(), NOW(), NULL, NULL, 0, NULL);


-- Thêm dữ liệu cho bảng Payment
INSERT INTO Payment (orderID, paymentDate, status, createdAt, updatedAt) VALUES
(1, NOW(), 'PENDING', NOW(), NOW()),
(2, NOW(), 'PENDING', NOW(), NOW()),
(3, NOW(), 'SUCCESS', NOW(), NOW()),
(4, NOW(), 'PENDING', NOW(), NOW()),
(5, NOW(), 'SUCCESS', NOW(), NOW());

-- Thêm dữ liệu cho bảng Review
INSERT INTO Review (userID, productID, orderDetailID, rating, comment, createdAt, updatedAt)
VALUES
(1, 3, 1, 5, 'Sản phẩm tuyệt vời, chất lượng tốt!', NOW(), NOW()),
(2, 1, 4, 4, 'Giày rất đẹp nhưng hơi chật.', NOW(), NOW()),
(3, 3, 6, 3, 'Rất tiếc, sản phẩm không như mong đợi.', NOW(), NOW()),
(4, 1, 8, 5, 'Rất hài lòng với sản phẩm!', NOW(), NOW()),
(5, 1, 10, 2, 'Màu sắc không giống như hình.', NOW(), NOW());