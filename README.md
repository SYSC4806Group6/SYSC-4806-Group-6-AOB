# Amazin Bookstore – Milestone 1

## 🌟 Project Overview
Amazin Bookstore is a Spring Boot + Azure web application for browsing, searching, and purchasing books.  
For Milestone 1 we committed to one complete customer journey:

- Browse and search the catalog with filters, sorting, and pagination.
- View detailed book pages.
- Add/update items in the cart and enforce inventory at checkout.
- Keep catalogue data healthy with admin CRUD, validation, and demo data.
- Preview recommendations via a Jaccard-similarity stub.

## 🚀 Live Deployment
- **App URL:** [https://4806project-h8baaucbawg4eahb.canadacentral-01.azurewebsites.net/books?searchTerm=&publisher=&tag=&sortBy=title&sortDirection=ASC&size=12](https://4806project-h8baaucbawg4eahb.canadacentral-01.azurewebsites.net/books?searchTerm=&publisher=&tag=&sortBy=title&sortDirection=ASC&size=12)
- **Health Check:** `https://4806project-h8baaucbawg4eahb.canadacentral-01.azurewebsites.net/actuator/health`

## 🧑‍💻 Team Roles (Milestone 1)
| Person    | Role                  | Responsibilities                                                   |
|-----------|-----------------------|--------------------------------------------------------------------|
| Sebastian | DevOps & Release      | CI/CD, Azure deployment, branch protection, README                 |
| Owen      | Domain & Persistence  | Book/User/Order entities, DB schema, seed data                     |
| Salim     | Browse & Search UI    | Book catalog, filters, sorting, details page                       |
| Renee     | Cart & Checkout       | Cart, inventory guard, checkout flow                               |
| Nolan     | Admin CRUD & Recs     | Admin book management, recommendation stub, unit tests             |

## ⚡ Milestone 1 Summary
- Delivered the customer journey end-to-end (browse → details → cart → checkout) with inventory enforcement.
- Built admin tooling for book CRUD, ISBN validation, and seed data to support demos.
- Shipped a recommendation stub exposed at `/api/recommendations/{userId}` and surfaced on catalog/detail views.
- Set up CI/CD to Azure App Service with health checks and demo content for rapid testing.

## 📦 Technology Stack
- **Backend:** Spring Boot, Spring Data JPA, Thymeleaf
- **Database:** PostgreSQL (Azure) / H2 for local development
- **Frontend:** Server-side Thymeleaf templates + lightweight CSS
- **Deployment:** Azure App Service
- **CI/CD:** GitHub Actions with branch protection rules

## 🛠️ Getting Started
### Prerequisites
- Java 17+
- Maven
- Git
- PostgreSQL or H2 (default profile uses H2)

### Run Locally
```bash
git clone <repo-url>
cd amazin-bookstore
mvn clean install
mvn spring-boot:run
```
