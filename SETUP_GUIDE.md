# Android App Setup Guide

This guide will help you set up and run the Android User Authentication & Settings Prototype.

## 📋 Prerequisites

### Required Software
1. **Android Studio** (Arctic Fox or later)
   - Download from: https://developer.android.com/studio
   - Install with default settings

2. **Android SDK**
   - Minimum SDK: API 24 (Android 7.0)
   - Target SDK: API 36 (Android 14)
   - Compile SDK: API 36

3. **Java Development Kit (JDK)**
   - JDK 11 or higher
   - Usually bundled with Android Studio

### Hardware Requirements
- **For Emulator**: 8GB RAM minimum, 16GB recommended
- **For Physical Device**: Android 7.0+ with USB debugging enabled

## 🚀 Setup Instructions

### Step 1: Clone/Download the Project
```bash
git clone <repository-url>
cd android-auth-prototype
```

### Step 2: Open in Android Studio
1. Launch Android Studio
2. Click "Open an existing Android Studio project"
3. Navigate to the project folder and select it
4. Wait for Gradle sync to complete

### Step 3: Configure Android SDK
1. Go to **File > Project Structure**
2. Under **SDK Location**, ensure Android SDK path is set
3. In **Modules > app**, verify:
   - Compile SDK Version: 36
   - Build Tools Version: Latest available

### Step 4: Sync Dependencies
1. Click **File > Sync Project with Gradle Files**
2. Wait for all dependencies to download
3. Resolve any version conflicts if prompted

### Step 5: Set Up Emulator (Optional)
1. Open **Tools > AVD Manager**
2. Click **Create Virtual Device**
3. Choose a device (e.g., Pixel 6)
4. Select system image (API 24 or higher)
5. Click **Finish** and start the emulator

### Step 6: Run the App
1. Select your target device (emulator or physical device)
2. Click the **Run** button (green triangle) or press **Shift + F10**
3. Wait for the app to build and install

## 🔧 Troubleshooting

### Common Issues and Solutions

#### 1. Gradle Sync Failed
**Error**: "Could not resolve dependencies"
**Solution**:
- Check internet connection
- Go to **File > Invalidate Caches and Restart**
- Update Gradle version in `gradle/wrapper/gradle-wrapper.properties`

#### 2. SDK Not Found
**Error**: "SDK location not found"
**Solution**:
- Go to **File > Project Structure > SDK Location**
- Set the correct Android SDK path
- Usually: `~/Android/Sdk` (Mac/Linux) or `C:\Users\<username>\AppData\Local\Android\Sdk` (Windows)

#### 3. Build Failed - Missing Dependencies
**Error**: Various dependency resolution errors
**Solution**:
- Update `gradle/libs.versions.toml` with latest versions
- Run `./gradlew clean build` in terminal
- Check for conflicting dependencies

#### 4. App Crashes on Launch
**Possible Causes**:
- Database initialization issues
- Missing permissions
- Theme/UI rendering problems

**Solutions**:
- Check Logcat for specific error messages
- Clear app data from device settings
- Restart the emulator/device

#### 5. Room Database Issues
**Error**: "Cannot access database on the main thread"
**Solution**:
- Ensure all database operations use coroutines
- Check that DAOs are properly annotated
- Verify database migration if schema changes

## 📱 Testing the App

### User Registration Flow
1. Launch the app
2. Click "Sign Up" on the login screen
3. Fill in:
   - Username: `testuser`
   - Email: `test@example.com`
   - Password: `password123`
   - Confirm Password: `password123`
4. Click "Sign Up"
5. Should navigate to home screen automatically

### Login Flow
1. Use the credentials from registration
2. Try logging in with either username or email
3. Should navigate to home screen

### Settings Testing
1. From home screen, click "Open Settings"
2. Test display name editing
3. Toggle dark/light theme
4. Toggle notifications
5. Test logout functionality

## 🛠️ Development Tips

### Code Structure
- Follow MVVM architecture pattern
- Use Compose for all UI components
- Implement proper error handling
- Use Kotlin coroutines for async operations

### Database Management
- Room database is created automatically on first run
- Data persists between app sessions
- Use Android Studio's Database Inspector to view data

### Theme Customization
- Modify colors in `ui/theme/Color.kt`
- Update theme logic in `ui/theme/Theme.kt`
- Test both light and dark modes

### Adding New Features
1. Create new entities/DAOs if database changes needed
2. Update repository with new methods
3. Create/update ViewModels for state management
4. Build Compose UI screens
5. Update navigation as needed

## 📊 Performance Optimization

### Database
- Use proper indexing for frequently queried fields
- Implement database migrations for schema changes
- Consider using Flow for reactive data updates

### UI
- Use `remember` for expensive computations
- Implement proper state hoisting
- Optimize recomposition with stable parameters

### Memory
- Avoid memory leaks in ViewModels
- Use proper lifecycle management
- Clean up resources in onCleared()

## 🐛 Debugging

### Useful Tools
1. **Logcat**: View app logs and crash reports
2. **Layout Inspector**: Debug UI issues
3. **Database Inspector**: View Room database contents
4. **Network Inspector**: Monitor API calls (if added)

### Debug Build Configuration
The app includes debug-specific configurations:
- Detailed logging enabled
- Database exported for inspection
- UI tooling for Compose preview

## 📚 Additional Resources

- [Android Developer Documentation](https://developer.android.com/docs)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [Material Design 3](https://m3.material.io/)

## ⚠️ Important Notes

1. **Security**: This is a prototype - implement proper security measures for production
2. **Testing**: Add comprehensive unit and integration tests
3. **Error Handling**: Enhance error handling for production use
4. **Validation**: Implement server-side validation for real applications
5. **Backup**: Consider cloud backup for user data

## 🆘 Getting Help

If you encounter issues:
1. Check the troubleshooting section above
2. Review Android Studio's error messages
3. Check Logcat for runtime errors
4. Consult Android developer documentation
5. Search Stack Overflow for specific error messages