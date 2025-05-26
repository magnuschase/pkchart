# 🃏 pkchart

**pkchart** is a Spring Boot application for managing Pokemon TCG cards, including user portfolios, marketplace listings, card/set suggestions, and admin price management. It features authentication, role-based access, and a REST API for all major operations.

---

## 🚀 Features

- **🔐 User Authentication & Authorization**
  - JWT-based login and registration
  - Role management (user/admin)
- **📂 Portfolio Management**
  - Add/remove cards to/from user portfolio
  - View portfolio statistics and total value
- **🛒 Marketplace**
  - List cards for sale
  - Create buy offers
  - Browse all active listings
- **📝 Card & Set Suggestion/Approval**
  - Users can suggest new cards/sets
  - Admins can approve/reject requests
- **💰 Admin Price Management**
  - Set/update card prices
  - Bulk price updates
- **✅ Integration & Cucumber Tests**
  - End-to-end feature tests for all flows

---

## 🗂 Project Structure

```
src/
  main/
    java/org/magnuschase/pkchart/
      controller/    # REST controllers (API endpoints)
      entity/        # JPA entities (DB models)
      interfaces/    # DTOs (Data Transfer Objects)
      model/         # Enums and domain models
      repository/    # Spring Data JPA repositories
      security/      # JWT, authentication, security config
      service/       # Business logic services
  test/
    java/org/magnuschase/pkchart/integration/  # Cucumber step definitions
    resources/                                 # .feature files (Gherkin)
docker-compose.yml
Dockerfile
```

---

## 🔗 APIs

- **🔐 Auth:** `/auth/register`, `/auth/login`, `/auth/user-details`, `/auth/change-password`
- **📂 Portfolio:** `/portfolio`, `/portfolio/add/{cardId}`, `/portfolio/remove/{entryId}`
- **🛒 Marketplace:** `/marketplace`, `/marketplace/sell`, `/marketplace/buy`
- **📝 Card/Set Requests:** `/requests/card/add`, `/requests/set/add`, `/requests/all`, `/requests/approve/{requestId}`
- **💰 Price:** `/prices/update/{cardId}`, `/prices/update-multiple`

---

## ▶️ Running the Application

1. **Configure Environment:**

   - Copy `.env.example` to `.env` and set secrets (e.g., `JWT_SECRET`) - easily generated via `openssl`
   - Set up PostgreSQL (see `docker-compose.yml` or `init.sql`) - you can use ready to go IntelliJ configurations from `.run` directory

2. **Build & Run:**

   ```sh
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

   Or use Docker:

   ```sh
   docker-compose up --build
   ```

3. **API Docs:**
   - Visit `/swagger` for OpenAPI documentation.

---

## 🧪 Testing

- **Run all tests:**
  ```sh
  ./mvnw test -Dtest=org.magnuschase.pkchart.integration.CucumberIntegrationTestRunner
  ```
- **Cucumber reports:** See `target/cucumber-reports.html` after running tests.
- **CI:** All Cucumber tests are automatically run on every push and pull request using [GitHub Actions](.github/workflows/ci.yml).  
  The Cucumber HTML report is uploaded as an artifact and can be downloaded from the Actions tab after each run.

---

## ✅ Cucumber Test Results

[![CI](https://github.com/magnuschase/pkchart/actions/workflows/ci.yml/badge.svg)](https://github.com/magnuschase/pkchart/actions/workflows/ci.yml)

- **Features tested:** 6
- **Scenarios:** 22 passed / 0 failed
- **Steps:** 130 passed / 0 failed / 0 skipped
- **Duration:** ~17 seconds (locally), 36 seconds (GitHub Actions)

📄 The full Cucumber HTML report is uploaded as a [GitHub Actions artifact](https://github.com/magnuschase/pkchart/actions?query=workflow%3ACI).  
After each CI run, you can download it from the "Artifacts" section of the workflow run in the Actions tab.

---

## 🧠 Design Patterns Used

- Controller (MVC)
- Service Layer
- Repository
- DTO (Data Transfer Object)
- Factory (static factory methods)
- Singleton (Spring beans)
- Strategy (Security config)

---

## 📄 License

This project is licensed under the [MIT License](./LICENSE).
