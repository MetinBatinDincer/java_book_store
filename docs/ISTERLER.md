# Proje İster Durumu

**Mikroservis Mimarisi** ve **Mobil GUI** yapılmayacak.

---

## ZORUNLU (65 Puan)

| Kriter | Puan | Durum | Ne Yapıldı |
|--------|------|-------|------------|
| API & Back-end | 10 | ✅ Tamamlandı | Book/User/Order CRUD API, Controller→Service→Repository katmanları |
| Generic Yapılar | 10 | ✅ Tamamlandı | `ApiResponse<T>` — tüm API yanıtları generic wrapper ile dönüyor |
| Custom GUI | 10 | ⏳ Devam ediyor | Kişi 2 — JavaFX arayüzü + Custom Graphics |
| JDBC & NoSQL | 10 | ✅ Tamamlandı | PostgreSQL (JPA/JDBC) + MongoDB (ActivityLog — Docker'da çalışıyor) |
| SOLID & OOP | 10 | ✅ Tamamlandı | Katmanlı mimari + Strategy Pattern (DiscountStrategy) |
| Hata Yönetimi | 5 | ✅ Tamamlandı | GlobalExceptionHandler — 400, 404, 409, 500 HTTP kodları |
| Performans Testleri | 5 | ✅ Tamamlandı | k6 load_test.js + spike_test.js yazıldı |
| Analiz & Doküman | 5 | ✅ Tamamlandı | README.md + Mermaid mimari diyagramı GitHub'a eklenecek |

**Zorunlu Tamamlanan: 60/65** (Custom GUI kişi 2'de devam ediyor)

---

## EK ÖZELLİKLER (Seçilen)

| Kriter | Puan | Durum | Ne Yapıldı |
|--------|------|-------|------------|
| Gateway | +5 | ✅ Tamamlandı | Spring Cloud Gateway — localhost:8888 tüm trafiği yönlendiriyor |
| Test-Driven Geliştirme | +10 | ✅ Tamamlandı | BookServiceTest + UserServiceTest — Red→Green→Refactor + tarih damgası |
| Dockerize Sistem | +5 | ✅ Tamamlandı | docker-compose.yml + Dockerfile — `docker-compose up` ile tüm sistem kalkar |

**Ek Tamamlanan: 20/20**

---

## Sıradaki Görevler (Kişi 1)

1. [x] GlobalExceptionHandler — 400, 404, 409, 500 HTTP kodları
2. [x] SOLID — Strategy Pattern (DiscountStrategy)
3. [x] docker-compose.yml yaz
4. [x] TDD testlerini tamamla
5. [x] k6 performans testleri
6. [x] README + Mermaid diyagramı
7. [x] Spring Cloud Gateway

## Sıradaki Görevler (Kişi 2)

1. [ ] JavaFX arayüzünü tamamla
2. [ ] Custom Graphics bileşeni ekle
3. [ ] k6 performans testini çalıştır ve rapor yaz

## Sıradaki Görevler (Ortak)

1. [ ] Her iki taraf push → main'e merge
2. [ ] k6 test sonuçlarını README'ye ekle
3. [ ] Son kontrol ve sunum hazırlığı
