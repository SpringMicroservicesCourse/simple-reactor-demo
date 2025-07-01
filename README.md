# Simple Reactor Demo ⚡

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Project Reactor](https://img.shields.io/badge/Project%20Reactor-Core-blue.svg)](https://projectreactor.io/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 專案介紹

這是一個 **Project Reactor 響應式程式設計** 的示範專案，專門用來演示 Reactor Core 的基本概念與操作。透過簡潔的程式碼範例，讓開發者能夠快速理解響應式程式設計（Reactive Programming）的核心思想與實作方式。

專案主要展示：
- **Flux 流的建立與操作** - 演示如何使用 `Flux.range()` 建立資料流
- **資料轉換與處理** - 透過 `map()` 操作進行資料轉換
- **異常處理機制** - 示範 `onErrorResume()` 和 `onErrorReturn()` 的使用
- **生命週期鉤子** - 展示 `doOnRequest()` 和 `doOnComplete()` 的運作方式
- **訂閱與消費模式** - 演示如何正確訂閱和處理響應式流

> 💡 **為什麼選擇響應式程式設計？**
> - **高效能處理** - 非阻塞 I/O 操作，提升系統吞吐量
> - **回壓處理** - 內建背壓（backpressure）機制，防止系統過載
> - **組合式設計** - 流暢的 API 設計，便於組合複雜的異步操作

### 🎯 專案特色

- **實戰導向** - 包含常見的異常處理場景（除零錯誤）
- **詳細註解** - 每個重要操作都有清楚的中文註解說明
- **漸進式學習** - 從基本概念到進階操作，循序漸進
- **即時回饋** - 透過日誌輸出，即時觀察響應式流的運作過程

## 技術棧

### 核心框架
- **Spring Boot 3.4.5** - 現代化的 Java 應用程式框架
- **Project Reactor Core** - 響應式程式設計核心函式庫
- **SLF4J + Logback** - 日誌記錄與輸出

### 開發工具與輔助
- **Lombok** - 簡化 Java 程式碼，自動產生 getter/setter 等方法
- **Maven** - 專案建置與依賴管理工具
- **Spring Boot Maven Plugin** - 簡化 Spring Boot 應用程式的打包與執行

## 專案結構

```
simple-reactor-demo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── tw/fengqing/spring/reactor/simple/
│   │   │       └── SimpleReactorDemoApplication.java  # 主程式 - 演示響應式操作
│   │   └── resources/
│   │       └── application.properties                # 應用程式設定檔
│   └── test/
│       └── java/                                     # 測試程式目錄
├── target/                                           # Maven 編譯輸出目錄
├── pom.xml                                          # Maven 專案設定檔
├── .gitignore                                       # Git 忽略檔案設定
└── README.md                                        # 專案說明文件
```

## 快速開始

### 前置需求
- **Java 21** 或以上版本
- **Maven 3.6** 或以上版本
- **Git** 版本控制工具

### 安裝與執行

1. **克隆此倉庫：**
```bash
git clone https://github.com/SpringMicroservicesCourse/simple-reactor-demo.git
```

2. **進入專案目錄：**
```bash
cd simple-reactor-demo
```

3. **編譯專案：**
```bash
mvn clean compile
```

4. **執行應用程式：**
```bash
mvn spring-boot:run
```

## 進階說明

### 核心概念演示

此專案主要演示以下響應式程式設計概念：

```java
// 建立包含 1-6 數字的 Flux 資料流
Flux.range(1, 6)
    .doOnRequest(n -> log.info("Request {} number", n))    // 請求數量監聽
    .doOnComplete(() -> log.info("Publisher COMPLETE 1")) // 完成狀態監聽
    .map(i -> 10 / (i - 3))                              // 資料轉換（會產生異常）
    .subscribe(
        i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),     // 正常值處理
        e -> log.error("error {}", e.toString()),                         // 異常處理
        () -> log.info("Subscriber COMPLETE")                             // 完成回調
    );
```

### 異常處理機制

專案展示了兩種常見的異常處理方式：

```java
// 方式一：使用 onErrorResume 處理異常並回傳替代值
.onErrorResume(e -> {
    log.error("Exception {}", e.toString());
    return Mono.just(-1);
})

// 方式二：使用 onErrorReturn 直接回傳預設值
.onErrorReturn(-1)
```

### 執行緒模型

程式碼中包含了不同的執行緒調度選項：

```java
.publishOn(Schedulers.elastic())  // 在彈性執行緒池中執行
.subscribeOn(Schedulers.single()) // 在單一執行緒中訂閱
```

## 參考資源

- [Project Reactor 官方文件](https://projectreactor.io/docs/core/release/reference/)
- [Spring WebFlux 官方指南](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
- [響應式程式設計入門教學](https://www.baeldung.com/reactor-core)

## 注意事項與最佳實踐

### ⚠️ 重要提醒

| 項目 | 說明 | 建議做法 |
|------|------|----------|
| 異常處理 | 響應式流中的異常會中斷整個流 | 使用 `onErrorResume` 或 `onErrorReturn` |
| 執行緒安全 | 注意不同執行緒間的資料存取 | 避免共享可變狀態 |
| 背壓處理 | 生產者速度過快可能造成記憶體問題 | 合理使用 `request()` 控制需求 |

### 🔒 最佳實踐指南

- **明確的錯誤處理** - 每個響應式鏈都應該有適當的錯誤處理策略
- **適當的執行緒調度** - 根據 I/O 密集或 CPU 密集選擇合適的 Scheduler
- **資源清理** - 使用 `doFinally()` 確保資源得到正確釋放
- **測試覆蓋** - 使用 `StepVerifier` 進行響應式程式的單元測試

## 學習路徑建議

1. **基礎概念** - 理解 Publisher、Subscriber、Subscription 的關係
2. **操作符學習** - 掌握常用的 map、filter、flatMap 等操作符
3. **異常處理** - 學會使用各種錯誤處理策略
4. **執行緒模型** - 理解不同 Scheduler 的適用場景
5. **實戰應用** - 結合 Spring WebFlux 開發響應式 Web 應用

## 授權說明

本專案採用 MIT 授權條款，詳見 LICENSE 檔案。

## 關於我們

我們主要專注在敏捷專案管理、物聯網（IoT）應用開發和領域驅動設計（DDD）。喜歡把先進技術和實務經驗結合，打造好用又靈活的軟體解決方案。

## 聯繫我們

- **FB 粉絲頁**：[風清雲談 | Facebook](https://www.facebook.com/profile.php?id=61576838896062)
- **LinkedIn**：[linkedin.com/in/chu-kuo-lung](https://www.linkedin.com/in/chu-kuo-lung)
- **YouTube 頻道**：[雲談風清 - YouTube](https://www.youtube.com/channel/UCXDqLTdCMiCJ1j8xGRfwEig)
- **風清雲談 部落格**：[風清雲談](https://blog.fengqing.tw/)
- **電子郵件**：[fengqing.tw@gmail.com](mailto:fengqing.tw@gmail.com)

---

**📅 最後更新：2025年1月** 
**👨‍💻 維護者：風清雲談團隊** 