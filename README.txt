================================================================
   DIJITAL KITAP SATIS OTOMASYONU
   TBL324 - Ileri Java Uygulamalari Projesi
================================================================

----------------------------------------------------------------
GRUP BILGISI
----------------------------------------------------------------
Ders    : TBL324 - Ileri Java Uygulamalari
Proje   : Dijital Kitap Satis Otomasyonu
GitHub  : https://github.com/MetinBatinDincer/java_book_store

----------------------------------------------------------------
EKIP UYELERI
----------------------------------------------------------------
1. Metin Batin Dincer       - 221307021
2. Uygar Devrim Akkus       - 221307096

----------------------------------------------------------------
PROJE KONUSU
----------------------------------------------------------------
Mikroservis mimarisiyle gelistirilmis dijital kitap satis
otomasyonu sistemi. Sistem; kitap, kullanici ve siparis
yonetimini Spring Boot REST API uzerinden saglar. Tum trafik
Spring Cloud Gateway uzerinden yonlendirilir. Islem loglari
MongoDB'ye kaydedilir, asil veriler PostgreSQL'de tutulur.
JavaFX ile masaustu kullanici arayuzu saglanmistir.

----------------------------------------------------------------
KULLANILAN TEKNOLOJILER
----------------------------------------------------------------
Backend      : Spring Boot 3.2, Java 21
Iliski. DB   : PostgreSQL 16 (TimescaleDB)
NoSQL DB     : MongoDB
Mimari       : Mikroservis, Spring Cloud Gateway (port 8888)
Arayu        : JavaFX
Containerize : Docker / docker-compose
Perf. Test   : k6 (yuklu test, ani yuklenme testi)
Desen        : Strategy Pattern (DiscountStrategy)
Test         : JUnit 5, TDD (Red-Green-Refactor)

----------------------------------------------------------------
GOREV DAGILIMI
----------------------------------------------------------------

--- Metin Batin Dincer (221307021) ---
  Sorumluluk : Backend / API

  - Spring Boot REST API (Book, User, Order CRUD)
  - Controller -> Service -> Repository katman mimarisi
  - PostgreSQL entegrasyonu (JPA/JDBC)
  - MongoDB entegrasyonu (ActivityLog - NoSQL)
  - GlobalExceptionHandler (400, 404, 409, 500 HTTP kodlari)
  - Generic ApiResponse<T> yapisi
  - Strategy Pattern: DiscountStrategy, FixedDiscountStrategy,
    PercentageDiscountStrategy, NoDiscountStrategy
  - TDD: BookServiceTest, UserServiceTest (Red-Green-Refactor)
  - Docker / docker-compose yapilandirmasi
  - Spring Cloud Gateway kurulumu ve yonlendirme kurallari
  - k6 performans testleri (load_test.js, spike_test.js)
  - README ve Mermaid mimari diyagrami

--- Uygar Devrim Akkus (221307096) ---
  Sorumluluk : Kullanici Arayuzu / Frontend

  - JavaFX masaustu arayuzu (ana pencere, navigasyon)
  - Kitap listeleme, ekleme, silme, guncelleme ekranlari
  - Siparis ekrani ve satis gorunumu
  - Generic tablo bileseni: GenericTableView<T>
  - Custom Graphics bileseni (gradient kapak renkleri, SalesView)
  - API baglantilari (HTTP istemcisi, JSON parse)

--- Ortak ---
  - SOLID prensiplerine uygun katmanli mimari
  - GitHub'a duzenli commit ve branch yonetimi
  - Sunum hazirligi

----------------------------------------------------------------
ISTERLER VE DURUM
----------------------------------------------------------------

ZORUNLU ISTERLER                    PUAN   DURUM
  API & Back-end                      10   Tamamlandi
  Generic Yapilar (ApiResponse<T>)    10   Tamamlandi
  Custom GUI (JavaFX)                 10   Tamamlandi
  JDBC & NoSQL (PostgreSQL + Mongo)   10   Tamamlandi
  SOLID & OOP (Strategy Pattern)      10   Tamamlandi
  Hata Yonetimi                        5   Tamamlandi
  Performans Testleri (k6)             5   Tamamlandi
  Analiz & Dokuman (README+Mermaid)    5   Tamamlandi

EK OZELLIKLER                       PUAN   DURUM
  Gateway (+5)                        +5   Tamamlandi
  Mikroservis Mimarisi               +10   Tamamlandı
  Dockerize Sistem (+5)               +5   Tamamlandi

----------------------------------------------------------------
PROJE YAPISI
----------------------------------------------------------------
digital_book_sales_automation/
  book-service/        -> Spring Boot REST API
    controller/        -> HTTP endpoint'leri
    service/           -> Is mantigi
    repository/        -> DB erisimi (JPA + Mongo)
    model/             -> Entity siniflari
    dto/               -> ApiResponse<T>
    exception/         -> GlobalExceptionHandler
    strategy/          -> DiscountStrategy (Strategy Pattern)
  gateway/             -> Spring Cloud Gateway (port 8888)
  ui/                  -> JavaFX arayuzu
  k6/                  -> Performans testleri
  docs/                -> Proje dokumanlari
  docker-compose.yml   -> Tum sistem

----------------------------------------------------------------
KURULUM
----------------------------------------------------------------
Gereksinimler: Java 21+, Docker Desktop, Maven

  # Tum sistemi Docker ile baslat
  docker-compose up

  # Sadece backend (gelistirme)
  cd book-service && mvnw spring-boot:run

  # Gateway
  cd gateway && mvnw spring-boot:run

  # UI
  cd ui && mvnw javafx:run

API erişimi  : http://localhost:8080/api/...
Gateway      : http://localhost:8888/api/...

================================================================
