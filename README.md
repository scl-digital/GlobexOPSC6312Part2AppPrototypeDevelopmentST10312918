# Android User Authentication & Settings Prototype

A fully functional Android app prototype built with Kotlin and Jetpack Compose, demonstrating user authentication and settings management.

## 🚀 Features

### Core Functionality
- **User Registration & Login System**
  - Secure user registration with username, email, and password
  - Login with username or email
  - Password encryption using SHA-256 hashing
  - Input validation and error handling

- **Local Data Storage**
  - Room database for user data persistence
  - DataStore for user preferences and session management
  - Encrypted password storage

- **User Settings Screen**
  - Update display name
  - Toggle dark/light theme
  - Enable/disable notifications
  - Logout functionality

### Technical Features
- **Modern Android Architecture**
  - MVVM pattern with ViewModels
  - Repository pattern for data management
  - Kotlin Coroutines for asynchronous operations
  - Jetpack Compose for modern UI

- **Navigation**
  - Navigation Component with Compose
  - Automatic navigation based on authentication state
  - Proper back stack management

- **Theme Support**
  - Dynamic Material Design 3 theming
  - Dark/Light theme toggle
  - System theme detection
  - Persistent theme preferences

## 🏗️ Architecture

### Project Structure
```
app/src/main/java/com/example/opsc6312part2appprototypedevelopmentst10312918/
├── data/
│   ├── dao/                    # Data Access Objects
│   ├── database/              # Room database configuration
│   ├── entity/                # Database entities
│   ├── preferences/           # DataStore preferences
│   └── repository/            # Repository pattern implementation
├── navigation/                # Navigation setup
├── ui/
│   ├── screen/               # Compose UI screens
│   ├── theme/                # Theme configuration
│   └── viewmodel/            # ViewModels for UI state management
├── utils/                    # Utility classes (encryption, etc.)
└── MainActivity.kt           # Main activity
```

### Key Components

#### Database Layer
- **User Entity**: Stores user account information
- **UserSettings Entity**: Stores user preferences
- **UserDao & UserSettingsDao**: Database access methods
- **AppDatabase**: Room database configuration

#### Repository Layer
- **UserRepository**: Handles user authentication and data operations
- **PreferencesManager**: Manages app preferences with DataStore

#### UI Layer
- **AuthViewModel**: Manages authentication state
- **SettingsViewModel**: Manages user settings state
- **Compose Screens**: Modern UI with Material Design 3

#### Security
- **CryptoUtils**: Password hashing and encryption utilities
- **Secure Storage**: Encrypted password storage in local database

## 🛠️ Technologies Used

- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern Android UI toolkit
- **Room Database** - Local data persistence
- **DataStore** - Preferences storage
- **Navigation Component** - Screen navigation
- **Material Design 3** - UI design system
- **Coroutines** - Asynchronous programming
- **ViewModel** - UI state management
- **Security Crypto** - Data encryption

## 📱 Screens

### 1. Login Screen
- Username/email and password input
- Form validation
- Error/success message display
- Navigation to registration

### 2. Registration Screen
- Username, email, password, and confirm password fields
- Real-time validation
- Password strength requirements
- Automatic login after successful registration

### 3. Home Screen
- Welcome message with user information
- App feature showcase
- Navigation to settings

### 4. Settings Screen
- Profile management (display name editing)
- Theme toggle (dark/light mode)
- Notification preferences
- Logout functionality

## 🔧 Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24 or higher
- Kotlin 1.8+

### Installation
1. Clone the repository
2. Open the project in Android Studio
3. Sync the project with Gradle files
4. Run the app on an emulator or physical device

### Dependencies
The app uses the following key dependencies:
- Room: `2.6.0`
- Navigation Compose: `2.7.5`
- DataStore: `1.0.0`
- Security Crypto: `1.1.0-alpha06`
- Compose BOM: `2024.09.00`

## 🧪 Testing

The project includes unit tests for critical components:
- Password encryption and verification
- Repository operations
- ViewModel state management

Run tests with:
```bash
./gradlew test
```

## 🔐 Security Features

- **Password Hashing**: SHA-256 with salt for secure password storage
- **Local Storage**: Room database with encrypted sensitive data
- **Session Management**: Secure session handling with DataStore
- **Input Validation**: Comprehensive form validation and sanitization

## 🎨 UI/UX Features

- **Material Design 3**: Modern, accessible design system
- **Dark/Light Theme**: System-aware theme switching
- **Responsive Layout**: Optimized for different screen sizes
- **Loading States**: User feedback during operations
- **Error Handling**: Graceful error display and recovery

## 📋 Future Enhancements

- Biometric authentication
- Cloud data synchronization
- Password reset functionality
- Profile picture upload
- Push notifications
- Multi-language support

## 📄 License

This project is created as a prototype for educational purposes.