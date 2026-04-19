-- Varsayılan admin kullanıcısı (yoksa ekle)
INSERT INTO users (name, email, password, role)
SELECT 'Admin', 'admin@bookstore.com', 'admin123', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@bookstore.com');
