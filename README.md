# Abz Test Task

## Overview

This project demonstrates an Android application that interacts with a REST API. The app consists of several screens, including a splash screen and user registration functionality. It supports fetching and displaying users from the API, implementing both GET and POST requests, and gracefully handling offline situations.

## Features

1. **Splash Screen**: Displays when the app is launched.
2. **User List**: 
   - Fetches and displays 6 users at a time from the API.
   - Users are sorted by registration date (newest first).
   - Supports infinite scrolling to load more users.
3. **Registration Form**: 
   - Validates user input as per the mockups.
   - Sends a POST request to register new users.
   - Displays new users in the User List after successful registration.
4. **Offline Mode**: Displays a relevant screen when the device is offline.

## Configuration

### API Endpoints

- **GET** `/users`: Fetch users, with pagination support.
- **GET** `/positions`: Fetch positions for the registration form.
- **GET** `/token`: Fetch a refresh token that is required to register a new user.
- **POST** `/users`: Register a new user.

To customize the API endpoint URLs, you can update the `URL` in the constants file.

### Pagination

- You can modify the number of users fetched per page by adjusting the `PAGE_SIZE` constant in the API constants file.

## Dependencies

This project uses the following libraries:

1. **Jetpack Compose**: For application UI.
2. **Retrofit**: For handling REST API calls.
3. **Paging 3**: For implementing paginated data loading in the user list.
4. **Koil**: For image loading and caching.
5. **Hilt**: For dependency injection.
6. **ViewModel**: To manage UI-related data and observe changes.

### How to Add Dependencies

All dependencies are listed in the `build.gradle.kts` file. 

## Setup Instructions

1. Clone the repository.
2. Open the project in Android Studio.
3. Ensure you have an active internet connection to fetch the required dependencies.
4. Build and run the app on an emulator or a physical device.

## Troubleshooting & Common Issues

### 1. **API Issues**
   - **Problem**: API responses return errors.
   - **Solution**: Ensure you have a valid internet connection and that the API is accessible. You may also need to verify that the correct API `URL` is being used.

---

## External Libraries and APIs


1. **androidx.material3**:
   - Provides Material Design 3 components in Compose.
   - **Reason**: To follow Material Design guidelines for building modern, accessible, and intuitive user interfaces.

2. **androidx.paging.compose**:
   - Supports the Paging 3 library for loading large datasets in a paginated manner with Jetpack Compose.
   - **Reason**: Paging makes it easier to load and display data efficiently, reducing memory consumption for large lists.

3. **androidx.navigation.compose**:
   - Handles navigation between screens in Jetpack Compose.
   - **Reason**: Provides a structured and efficient way to manage in-app navigation while integrating smoothly with Compose.

4. **androidx.hilt.navigation.compose**:
   - Hilt support for Jetpack Compose navigation.
   - **Reason**: Simplifies dependency injection in navigation components when using Hilt in Compose.

5. **androidx.constraintlayout.compose**:
   - Provides ConstraintLayout for Jetpack Compose.
   - **Reason**: To create complex layouts with flexible positioning of UI components, like in XML-based layouts.

6. **coil.compose**:
    - An image loading library for Android that supports Compose.
    - **Reason**: Coil is lightweight and integrates well with Compose, making it an efficient choice for image handling.

7. **retrofit**:
    - A type-safe HTTP client for Android and Java.
    - **Reason**: Retrofit simplifies API requests by converting them into method calls, handling requests, and parsing JSON responses.

8. **converter.gson**:
    - A converter that allows Retrofit to handle JSON responses using Gson.
    - **Reason**: Automatically converts API responses into Kotlin/Java objects using Gson.

9. **androidx.core.splashscreen**:
    - Manages splash screens for Android apps.
    - **Reason**: Provides a modern way to create a splash screen for consistent startup behavior across Android versions.

10. **accompanist.permissions**:
    - Provides utilities to request and manage runtime permissions in Compose.
    - **Reason**: Simplifies the complex process of handling Android permissions with a Compose-friendly API.

---

## Code Structure Overview

### **Main Modules**:

- **data**: 
  - Contains data models and repositories that handle communication with the REST API using Retrofit.

- **domain**: 
  - It serves as the core business logic layer, abstracting away the details of data sources and focusing on the business rules and use cases of the application.

- **core**:
  - The core module serves as a foundational layer in the app. It contains utility classes, constants, and common functionality that can be reused across different modules. This helps keep the project organized and avoids code duplication.

- **ui**: 
  - Contains all Jetpack Compose UI components, such as screens and composable functions. Follows the mockup designs for user list display, registration form, and handling loading/error states.

- **di**:
  - Handles dependency injection using Hilt, making it easy to manage dependencies throughout the app.
---
