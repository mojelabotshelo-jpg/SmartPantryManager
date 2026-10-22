# 🥫 Smart Pantry Manager

A native Android application designed to help users efficiently track household food inventory, reduce kitchen waste, and instantly discover recipes they can cook using what they already have on hand.

## ✨ Key Features

* **Pantry Inventory Tracking (CRUD):**
    * Add items with specific quantities, units, and expiration dates.
    * Seamlessly update quantities via an interactive dialog or delete items with a single tap.
* **Strict-Matching Recipe Engine:**
    * Cross-references available pantry items and quantities against recipe requirements in real-time.
    * Only unlocks recipes where the user has *all* required ingredients in sufficient amounts.
* **Custom Recipe Creator:**
    * Allows users to expand their cookbook by submitting new recipes with step-by-step instructions and dynamic ingredient lists.
    * Uses atomic SQLite database transactions to ensure data integrity.
* **Settings & Database Utilities:**
    * Dedicated settings screen with a database reset utility to wipe test data and restore default initial seed recipes.
* **Modern Material UI / UX:**
    * Built using Material Design 3 components (`MaterialCardView`), offering clean card elevation, rounded corners, and smooth state feedback.

---

## 🛠️ Tech Stack & Architecture

* **Language:** Java
* **IDE:** Android Studio
* **Database:** SQLite (Version 3) with relational schema design, foreign keys, and cascading deletes.
* **UI Framework:** Android XML layouts, Material Components (`com.google.android.material`).

---

## 🗄️ Database Architecture

The application uses a normalized relational SQLite database consisting of three main tables managed via `DatabaseHelper.java`:
1. **`pantry`**: Stores current household food inventory items, quantities, units, and expiration dates.
2. **`recipes`**: Stores recipe headers (title, instructions).
3. **`recipe_ingredients`**: Relational mapping table linking recipes to their required ingredients and quantities (utilizing foreign keys with `ON DELETE CASCADE`).

---

## 🚀 Getting Started & Installation

1. **Clone the Repository:**
   ```bash
   git clone [https://github.com/your-username/smart-pantry-manager.git](https://github.com/your-username/smart-pantry-manager.git)