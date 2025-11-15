## \# Amazin Bookstore - Milestone 1

## 

## \## 🌟 Project Overview

## Amazin Bookstore is a Spring Boot + Azure web application for browsing, searching, and purchasing books.  

## \*\*Milestone 1 Goal:\*\* Deliver one end-to-end user journey in production:

## 

## \*\*Browse/Search → View Book Details → Add to Cart → Checkout\*\*  

## Includes:

## \- Admin CRUD for books

## \- Recommendations (stub, M1 preview)

## \- Inventory enforcement on checkout

## \- Seeded demo data

## 

## ---

## 

## \## 🚀 Live Deployment 

## - **[Azure App Service URL](https://4806project-h8baaucbawg4eahb.canadacentral-01.azurewebsites.net/books?searchTerm=&publisher=&tag=&sortBy=title&sortDirection=ASC&size=12)**

## \- \*\*Health Check Endpoint:\*\* `/actuator/health`

## 

## ---

## 

## \## 🧑‍💻 Team Roles (Milestone 1)

## 

## | Person | Role | Responsibilities |

## |--------|------|-----------------|

## | Sebastian | DevOps \& Release | CI/CD, Azure deployment, branch protection, README |

## | Owen | Domain \& Persistence | Book/User/Order entities, DB schema, seed data |

## | Salim | Browse \& Search UI | Book catalog, filters, sorting, details page |

## | Renee | Cart \& Checkout | Cart, inventory guard, checkout flow |

## | Nolan | Admin CRUD \& Recommendations | Admin book management, recommendations stub, unit tests |

## 

## ---

## 

## \## ⚡ Milestone 1 Summary

## 

## - Customer journey: Browse/search the catalog, view detailed pages, add/update cart items, and complete checkout with inventory enforcement.

## - Admin tooling: Full book CRUD with ISBN validation, inventory rules, and a placeholder login so catalogue management stays secure.

## - Recommendations stub: Jaccard-similarity service at `/api/recommendations/{userId}` with previews on the home and detail pages.

## - Platform work: Seeded demo data, stood up CI/CD to Azure App Service, and exposed health checks for live monitoring.

## 

## ---

## 

## \## 📦 Technology Stack

## \- \*\*Backend:\*\* Spring Boot, Spring Data JPA, Thymeleaf

## \- \*\*Database:\*\* PostgreSQL (Azure) / H2 (local dev)

## \- \*\*Frontend:\*\* Thymeleaf templates + minimal CSS

## \- \*\*Deployment:\*\* Azure App Service

## \- \*\*CI/CD:\*\* GitHub Actions, branch protection rules

## 

## ---

## 

## \## 🛠️ Getting Started

## 

## \### Prerequisites

## \- Java 17+

## \- Maven

## \- PostgreSQL or H2 for local dev

## \- Git

## 

## \### Run Locally

## ```bash

## git clone <repo-url>

## cd amazin-bookstore

## mvn clean install

## mvn spring-boot:run


