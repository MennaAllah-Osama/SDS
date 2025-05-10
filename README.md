# SDS


Personal Budgeting System
This project is a Personal Budgeting System developed using Java, which allows users to manage their finances by tracking income, expenses, setting reminders, and generating insights. The system applies Object-Oriented Programming (OOP) principles and follows SOLID design patterns for maintainability and scalability.

Features Implemented
1. Sign Up
User Registration: Users can create accounts by providing necessary details.

Data Storage: Implemented IUserStorage interface with a FileUserStorage class to persist user data.

Authentication: AuthService follows SOLID principles and Strategy Pattern to manage authentication logic.

2. Login
Login System: Users can securely log in with their credentials.

Decoupled Authentication: Authentication logic is separated via IAuthMethod interface, ensuring the system is open for future login strategy extensions without modifying existing code.

3. Track Income
Income Management: Users can track their income with attributes like title, amount, date, and associated user ID.

File Storage: Income data is saved and loaded from the file system, ensuring persistence.

4. Track Expenses
Expense Tracking: Expenses are categorized and linked to each user for detailed analysis.

File Storage: Expenses are stored in files for easy retrieval and management.

5. Reminders
Reminder Setup: Users can set reminders with titles, dates, and times.

Reminder Storage: Implemented a ReminderStorageService to save and load reminders.

6. Budgeting & Analysis
Financial Overview: The system aggregates income and expense data, calculates the user’s balance, and offers basic insights into spending habits.
