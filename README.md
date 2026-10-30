# Online Food Ordering System

A JavaFX desktop application for ordering food, connected to a MySQL database through JDBC.
Developed as a mini project for **OOP Using Java (CSE203)**, Kalasalingam Academy of Research and Education.

## Team
| Name | Register No. | Role |
|------|--------------|------|
| [Member 1] | [Reg. No.] | [e.g. GUI and cart module] |
| [Member 2] | [Reg. No.] | [e.g. database and payment module] |

**Guide:** [Faculty name]

## Features
- User login and registration
- Food menu with search and sorting [keep only if implemented]
- Cart with quantity update and total calculation
- Discount / coupon codes [keep only if implemented]
- Payment method selection: COD, UPI, Card (simulated, no real payment gateway)
- Order stored in MySQL after placing the order
- Admin view to see all orders

## Technologies Used
| Area | Technology |
|------|-----------|
| Language | Java [JDK version] |
| GUI | JavaFX [version] |
| Database | MySQL [version] |
| Connectivity | JDBC (MySQL Connector/J [version]) |
| Version control | Git and GitHub |

## OOP and Data Structure Concepts
- **Inheritance:** [e.g. Customer and Admin extend User]
- **Encapsulation:** [e.g. private fields with getters and setters]
- **Polymorphism / Abstraction:** [describe only if used]
- **Collections:** [e.g. ArrayList for cart items, HashMap for coupon codes]

## Project Structure
```
Online-Food-Ordering-System/
├── src/            Java source files
├── lib/            JAR files (MySQL connector) [if used]
├── database/       schema.sql (table creation script)
├── screenshots/    application screenshots
└── README.md
```

## How to Run
1. Install JDK, JavaFX SDK and MySQL Server.
2. Open MySQL and run the script `database/schema.sql` to create the tables.
3. In the database connection file, set your MySQL username and password:
   `[ConnectionClassName].java`
4. Compile and run: [paste the exact command you use, e.g. javac / java with --module-path]
5. The app starts at the login screen (`LoginScreen.java`).

## Screenshots
| Login | Menu |
|-------|------|
| ![Login](screenshots/login.png) | ![Menu](screenshots/menu.png) |

| Cart | Payment |
|------|---------|
| ![Cart](screenshots/cart.png) | ![Payment](screenshots/payment.png) |

| Order Confirmation | Admin View |
|--------------------|-----------|
| ![Confirm](screenshots/confirm.png) | ![Admin](screenshots/admin.png) |

## Database
Tables: [list your real table names, e.g. users, food, orders, order_items]
Setup script: `database/schema.sql`

## Limitations
- Payment is simulated; no real payment gateway is integrated.
- [Add any real limitation, e.g. passwords not hashed]

## Future Enhancements
- Real payment gateway integration
- Order tracking and delivery status
- Password hashing
- Food images and reviews