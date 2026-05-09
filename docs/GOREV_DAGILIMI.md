# Proje Görev Dağılımı

**Proje:** Dijital Kitap Satış Otomasyonu
**Ders:** TBL324 - İleri Java Uygulamaları
**Ekip:** 2 Kişi

---

## Kişi 1 — Backend / API (Sen)

| Kriter | Puan | Açıklama |
|--------|------|----------|
| API & Back-end | 10 | Spring Boot REST API — Controller, Service, Repository katmanları |
| JDBC & NoSQL | 10 | PostgreSQL (JDBC) + Redis veya MongoDB entegrasyonu |
| Hata Yönetimi | 5 | 400, 404, 409, 500 HTTP durum kodları |
| Dockerize | +5 | docker-compose ile tüm sistemi ayağa kaldır |
| **Toplam** | **30** | |

### Yapılacaklar (Sırayla):
1. [x] Service katmanı ekle (BookService, UserService, OrderService)
2. [x] User ve Order modelleri + API'leri
3. [x] Hata yönetimi — GlobalExceptionHandler
4. [x] NoSQL entegrasyonu (MongoDB — ActivityLog)
5. [x] Docker-compose dosyası

---

## Kişi 2 — UI / Frontend (Diğer Kişi)

| Kriter | Puan | Açıklama |
|--------|------|----------|
| Custom GUI | 10 | Swing veya JavaFX ile masaüstü arayüz + Custom Graphics |
| Performans Testleri | 5 | JMeter veya k6 ile yük/kırılma testi + rapor |
| Gateway | +5 | Kong veya Spring Cloud Gateway ile yönlendirme |
| **Toplam** | **20** | |

### Yapılacaklar (Sırayla):
1. [x] JavaFX veya Swing proje iskeleti kur
2. [x] Kitap listeleme ekranı (API'den veri çek)
3. [x] Kitap ekleme/silme formu
4. [x] Custom Graphics bileşeni (gradient kapak renkleri + SalesView)
5. [x] JMeter/k6 performans testi
6. [x] Gateway kurulumu

---

## Ortak

| Kriter | Puan | Açıklama |
|--------|------|----------|
| Generic Yapılar | 10 | Backend'de `ApiResponse<T>`, UI'da generic tablo bileşeni |
| SOLID & OOP | 10 | Her iki taraf da SOLID prensiplerine uygun yazar |
| Analiz & Doküman | 5 | GitHub'a Mermaid diyagramı + teknik rapor |
| Test-Driven Geliştirme | +10 | Red-Green-Refactor döngüsü, tarih damgalı testler |
| **Toplam** | **35** | |

### Yapılacaklar:
1. [x] GitHub'a düzenli commit (her ikisi de)
2. [x] README.md — Mermaid mimari diyagramı
3. [x] TDD döngüsü ile test yazımı (ikisi de kendi alanında)

---

## Puan Özeti

| Kişi | Puan |
|------|------|
| Kişi 1 (Backend) | 30 |
| Kişi 2 (UI) | 20 |
| Ortak | 35 |
| **Toplam** | **85** |

---

## Haftalık Plan

### Hafta 1 — 26 Mart - 1 Nisan
| | Görev |
|--|-------|
| **Kişi 1** | Service katmanı ekle (BookService, UserService, OrderService) |
| **Kişi 1** | User ve Order modelleri + CRUD API'leri |
| **Kişi 2** | JavaFX veya Swing proje iskeleti kur, ana pencere aç |
| **Kişi 2** | API'den kitap listesini çekip ekranda göster |

### Hafta 2 — 2 Nisan - 8 Nisan
| | Görev |
|--|-------|
| **Kişi 1** | GlobalExceptionHandler — 400, 404, 409, 500 hata kodları |
| **Kişi 1** | Generic `ApiResponse<T>` yapısı (tüm API'ler bunu dönsün) |
| **Kişi 2** | Kitap ekleme / silme / güncelleme formları |
| **Kişi 2** | UI tarafında Generic tablo bileşeni `GenericTableView<T>` |

### Hafta 3 — 9 Nisan - 15 Nisan
| | Görev |
|--|-------|
| **Kişi 1** | NoSQL entegrasyonu (Redis cache veya MongoDB) |
| **Kişi 1** | TDD — Service testlerini Red-Green-Refactor döngüsüyle yaz |
| **Kişi 2** | Custom Graphics bileşeni (özel çizim, grafik, animasyon) |
| **Kişi 2** | TDD — UI bileşen testlerini yaz |

### Hafta 4 — 16 Nisan - 22 Nisan
| | Görev |
|--|-------|
| **Kişi 1** | Docker-compose dosyası (PostgreSQL + uygulama) |
| **Kişi 2** | JMeter veya k6 ile performans / yük testi |
| **Kişi 2** | Gateway kurulumu (Kong veya Spring Cloud Gateway) |
| **Ortak** | GitHub'a düzenli commit, branch'ler düzenlenir |

### Hafta 5 — 23 Nisan - 29 Nisan
| | Görev |
|--|-------|
| **Ortak** | README.md — Mermaid mimari diyagramı |
| **Ortak** | Performans testi raporu GitHub'a eklenir |
| **Ortak** | Son kontrol, eksik puanlar tamamlanır |
| **Ortak** | Sunum hazırlığı |

---

## Durum

> Son güncelleme: 2026-04-28

| Görev | Durum |
|-------|-------|
| PostgreSQL bağlantısı | ✅ Tamamlandı |
| Book CRUD API | ✅ Tamamlandı |
| Book DB testi | ✅ Tamamlandı |
| Service katmanı | ✅ Tamamlandı |
| User / Order API | ✅ Tamamlandı |
| NoSQL (MongoDB) | ✅ Tamamlandı |
| GUI (JavaFX) | ✅ Tamamlandı |
| GlobalExceptionHandler | ✅ Tamamlandı |
| Docker-compose | ✅ Tamamlandı |
| Gateway | ✅ Tamamlandı |
| k6 Performans Testleri | ✅ Tamamlandı |
| TDD Testleri | ✅ Tamamlandı |
| Strategy Pattern | ✅ Tamamlandı |
| ApiResponse\<T\> | ✅ Tamamlandı |
| GenericTableView\<T\> | ✅ Tamamlandı |
| Mermaid Diyagramı | ✅ Tamamlandı |
