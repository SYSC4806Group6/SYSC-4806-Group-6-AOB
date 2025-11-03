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

## - **[Azure App Service URL](https://4806project-h8baaucbawg4eahb.canadacentral-01.azurewebsites.net/books?searchTerm=fewoi&publisher=&tag=&sortBy=title&sortDirection=ASC&size=12)**

## \- \*\*Health Check Endpoint:\*\* `/actuator/health`

## 

## ---

## 

## \## 🧑‍💻 Team Roles

## 

## | Person | Role | Responsibilities |

## |--------|------|-----------------|

## | Person A | DevOps \& Release | CI/CD, Azure deployment, branch protection, README |

## | Person B | Domain \& Persistence | Book/User/Order entities, DB schema, seed data |

## | Person C | Browse \& Search UI | Book catalog, filters, sorting, details page |

## | Person D | Cart \& Checkout | Cart, inventory guard, checkout flow |

## | Person E | Admin CRUD \& Recommendations | Admin book management, recommendations stub, unit tests |

## 

## ---

## 

## \## ⚡ Features Implemented (M1)

## 

## \### Customer Features

## \- Browse all books, search/filter/sort, view book details

## \- Add to cart, update quantity

## \- Checkout with inventory enforcement

## 

## \### Admin Features

## \- Create/Edit/Delete Books

## \- Validate ISBN uniqueness, inventory ≥ 0

## \- Admin login (M1 placeholder)

## 

## \### Recommendations (Stub)

## \- Jaccard similarity on purchase history

## \- `/api/recommendations/{userId}` returns top 3 recommendations

## \- Preview displayed on home and book details pages

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



