-- Admin kullanıcısı
INSERT INTO users (name, email, password, role)
SELECT 'Admin', 'admin1@gmail.com', '123456', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin1@gmail.com');
