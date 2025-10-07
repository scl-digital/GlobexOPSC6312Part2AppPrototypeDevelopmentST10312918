#!/usr/bin/env python3
"""
Project Structure Verification Script
Verifies that all necessary files for the Android Authentication App are present.
"""

import os
import sys

def check_file_exists(filepath, description):
    """Check if a file exists and print status."""
    if os.path.exists(filepath):
        print(f"✅ {description}: {filepath}")
        return True
    else:
        print(f"❌ {description}: {filepath} - MISSING")
        return False

def main():
    """Main verification function."""
    print("🔍 Verifying Android Authentication App Project Structure...\n")
    
    missing_files = 0
    
    # Core configuration files
    files_to_check = [
        ("app/build.gradle.kts", "App build configuration"),
        ("gradle/libs.versions.toml", "Dependency versions"),
        ("app/src/main/AndroidManifest.xml", "Android manifest"),
        
        # Database layer
        ("app/src/main/java/com/example/opsc6312part2appprototypedevelopmentst10312918/data/entity/User.kt", "User entity"),
        ("app/src/main/java/com/example/opsc6312part2appprototypedevelopmentst10312918/data/entity/UserSettings.kt", "UserSettings entity"),
        ("app/src/main/java/com/example/opsc6312part2appprototypedevelopmentst10312918/data/dao/UserDao.kt", "User DAO"),
        ("app/src/main/java/com/example/opsc6312part2appprototypedevelopmentst10312918/data/dao/UserSettingsDao.kt", "UserSettings DAO"),
        ("app/src/main/java/com/example/opsc6312part2appprototypedevelopmentst10312918/data/database/AppDatabase.kt", "Room database"),
        
        # Repository layer
        ("app/src/main/java/com/example/opsc6312part2appprototypedevelopmentst10312918/data/repository/UserRepository.kt", "User repository"),
        ("app/src/main/java/com/example/opsc6312part2appprototypedevelopmentst10312918/data/preferences/PreferencesManager.kt", "Preferences manager"),
        
        # ViewModels
        ("app/src/main/java/com/example/opsc6312part2appprototypedevelopmentst10312918/ui/viewmodel/AuthViewModel.kt", "Auth ViewModel"),
        ("app/src/main/java/com/example/opsc6312part2appprototypedevelopmentst10312918/ui/viewmodel/SettingsViewModel.kt", "Settings ViewModel"),
        
        # UI Screens
        ("app/src/main/java/com/example/opsc6312part2appprototypedevelopmentst10312918/ui/screen/LoginScreen.kt", "Login screen"),
        ("app/src/main/java/com/example/opsc6312part2appprototypedevelopmentst10312918/ui/screen/RegisterScreen.kt", "Register screen"),
        ("app/src/main/java/com/example/opsc6312part2appprototypedevelopmentst10312918/ui/screen/HomeScreen.kt", "Home screen"),
        ("app/src/main/java/com/example/opsc6312part2appprototypedevelopmentst10312918/ui/screen/SettingsScreen.kt", "Settings screen"),
        
        # Navigation
        ("app/src/main/java/com/example/opsc6312part2appprototypedevelopmentst10312918/navigation/AppNavigation.kt", "App navigation"),
        
        # Utils
        ("app/src/main/java/com/example/opsc6312part2appprototypedevelopmentst10312918/utils/CryptoUtils.kt", "Crypto utilities"),
        
        # Theme
        ("app/src/main/java/com/example/opsc6312part2appprototypedevelopmentst10312918/ui/theme/ThemeManager.kt", "Theme manager"),
        
        # Main Activity
        ("app/src/main/java/com/example/opsc6312part2appprototypedevelopmentst10312918/MainActivity.kt", "Main Activity"),
        
        # Tests
        ("app/src/test/java/com/example/opsc6312part2appprototypedevelopmentst10312918/CryptoUtilsTest.kt", "Crypto utils test"),
        
        # Documentation
        ("README.md", "Project README"),
        ("SETUP_GUIDE.md", "Setup guide"),
    ]
    
    for filepath, description in files_to_check:
        if not check_file_exists(filepath, description):
            missing_files += 1
    
    print(f"\n📊 Verification Summary:")
    print(f"Total files checked: {len(files_to_check)}")
    print(f"Files present: {len(files_to_check) - missing_files}")
    print(f"Files missing: {missing_files}")
    
    if missing_files == 0:
        print("\n🎉 All required files are present! The project structure is complete.")
        print("\n📋 Next steps:")
        print("1. Open the project in Android Studio")
        print("2. Sync Gradle dependencies")
        print("3. Run the app on an emulator or device")
        print("4. Follow the SETUP_GUIDE.md for detailed instructions")
        return 0
    else:
        print(f"\n⚠️  {missing_files} files are missing. Please check the project structure.")
        return 1

if __name__ == "__main__":
    sys.exit(main())