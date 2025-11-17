# Amazin Bookstore – Milestone 1

## 🌟 Project Overview
Amazin Bookstore is a Spring Boot + Azure web application for browsing, searching, and purchasing books.  
For Milestone 1 we committed to one complete customer journey:

- Browse and search the catalog with filters, sorting, and pagination.
- View detailed book pages.
- Add/update items in the cart and enforce inventory at checkout.
- Keep catalogue data healthy with admin CRUD, validation, and demo data.
- Preview recommendations via a Jaccard-similarity stub.

In Milestone 2 we:
- Completed the full checkout process with persistence of receipts
- Cleaned up the UI upon site entry
- See more in table details below...

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

## 🧑‍💻 Team Roles (Milestone 2)
| Person    | Role                          | Responsibilities                                                                                                                                                                                                                                                                                                                                                                       |
|-----------|-------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Sebastian | FF4J implementation | Added FF4J dependencies, and toggling feature enabling canary rollout and kill switch. Updated some Ui bugs and features (Layout and Cart button). Fixed some small test cases.                                                                                                                                                                                                                      |
| Owen      | Receipts & Diagram Updates    | Added process for converting a shopping cart to a receipt. Added views for users to see their receipt(s) and receipt details. Updated our diagrams to have the current UML and DB schema.                                                                                                                                                                                              |
| Salim     | UI Refresh                    | Modernized catalog header/search/cart controls, built collapsible sidebar filters with custom checkboxes, redesigned book cards with hover interactions and cart badge/CSRF fixes, refreshed README content, prepared presentation slides and script                                                                                                                                   |
| Renee     | Complete Cart & Checkout Flow | Made all add to cart functionalities effective (all books show in cart with correct quantity, can inc/dec amount on cart page with up/down buttons and text box), completed checkout flow (shoppnig -> viewing cart -> proceed to checkout -> input checkout info -> checkout -> confirmation page), persisted receipts for users, details of past receipts viewable from receipts tab |
| Nolan     | Login/Authentication | Implemented Spring Security to allow users to register and log in with a custom username and password, added an admin auth to make sure only admin users can access specific features (eg. admin panel) |

## 🧑‍💻 Team Roles Plan (Milestone 3)
| Person    | Role                                    | Responsibilities                                                                 |
|-----------|-----------------------------------------|----------------------------------------------------------------------------------|
| Sebastian | Testing/ extending admin                | Updating admin to be able to control other user accounts, and tests              |
| Owen      | UI / Tests / Functionality finalization | Ensure that everything behaves & looks as expected via the three mentioned tasks |
| Salim     | Functionality Clean Up                  | Thoroughly checking all funcitonalities and maximizing functionality                         |
| Renee     | UI Match                                | Match UI across tabs and pages                                                   |
| Nolan     | Third Party oAuth / Clean up and polish | So users can sign in using Google, and cleanup for the final product/testing     |


## ⚡ Milestone 1 Summary
- Delivered the customer journey end-to-end (browse → details → cart → checkout) with inventory enforcement.
- Built admin tooling for book CRUD, ISBN validation, and seed data to support demos.
- Shipped a recommendation stub exposed at `/api/recommendations/{userId}` and surfaced on catalog/detail views.
- Set up CI/CD to Azure App Service with health checks and demo content for rapid testing.

## ⚡ Milestone 2 Summary
- Introduced buyer authentication/registration plus revamped cart and checkout flows with persistent session storage.
- Added purchase receipt tracking, viewable history pages, and feature toggles (FF4J) to gradually roll out UI experiments.
- Refreshed the catalog UI (new header/search/cart badge, collapsible filters, modern book cards) and added cart page upgrades.
- Documented the milestone, README updates, and prepared presentation assets to support the final demo.

## ⚡ Milestone 3 Plan
- Add recommendation functionality to main browsing page.
- Continue testing to ensure high quality web app.
- General clean up and matching of UI across pages and tabs.
- Ensure all documentation is up to date and clear.

## Diagrams
- Navigate to the "diagrams" directory in the repository to view our diagrams online
### UML Diagram
- https://github.com/SYSC4806Group6/SYSC-4806-Group-6-AOB/blob/main/diagrams/ModelUMLDiagram.pdf
### Schema Diagram
- https://github.com/SYSC4806Group6/SYSC-4806-Group-6-AOB/blob/main/diagrams/DB-SchemaDiagram.pdf


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
