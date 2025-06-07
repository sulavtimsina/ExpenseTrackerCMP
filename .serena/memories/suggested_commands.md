# Suggested Commands for CMP-Bookpedia Development

## Build Commands
```bash
# Clean build
./gradlew clean

# Build all targets
./gradlew build

# Build specific target
./gradlew :composeApp:assembleDebug          # Android debug
./gradlew :composeApp:assembleRelease        # Android release
./gradlew :composeApp:linkDebugFrameworkIos* # iOS frameworks
./gradlew :composeApp:runDebugExecutableDesktop # Desktop run
```

## Running the Application
```bash
# Android (requires connected device/emulator)
./gradlew :composeApp:installDebug

# Desktop
./gradlew :composeApp:run

# iOS (requires Xcode and iOS project setup)
# Open iosApp/iosApp.xcodeproj in Xcode
```

## Testing
```bash
# Run all tests
./gradlew test

# Run tests for specific target
./gradlew :composeApp:testDebugUnitTest       # Android unit tests
./gradlew :composeApp:desktopTest            # Desktop tests
```

## Database Management
```bash
# Generate Room schemas
./gradlew :composeApp:kspCommonMainKotlinMetadata

# Validate Room schemas
./gradlew :composeApp:validateDebugDatabaseSchema
```

## Code Quality
```bash
# Kotlin compilation check
./gradlew compileKotlin

# Check dependencies
./gradlew dependencies
./gradlew dependencyInsight --dependency <dependency-name>
```

## Package Generation
```bash
# Desktop packages
./gradlew :composeApp:packageDmg           # macOS DMG
./gradlew :composeApp:packageMsi           # Windows MSI
./gradlew :composeApp:packageDeb           # Linux DEB
```

## Development Utilities
```bash
# List all tasks
./gradlew tasks

# Check project structure
./gradlew projects

# Gradle properties
./gradlew properties
```

## System Commands (macOS/Darwin)
```bash
# File operations
find . -name "*.kt" -type f                 # Find Kotlin files
grep -r "pattern" --include="*.kt" .        # Search in Kotlin files
ls -la                                      # List files with details

# Git operations
git status
git add .
git commit -m "message"
git push origin main

# Process management
ps aux | grep gradle                        # Find Gradle processes
kill -9 <pid>                              # Kill process if needed
```

## IDE Integration
- **Android Studio**: Open project root directory
- **Xcode**: Open `iosApp/iosApp.xcodeproj` for iOS development
- **IntelliJ IDEA**: Open project root directory

## Troubleshooting
```bash
# Clean Gradle cache
./gradlew clean
rm -rf ~/.gradle/caches

# Reset iOS build
rm -rf iosApp/build
rm -rf composeApp/build

# Gradle daemon restart
./gradlew --stop
./gradlew --daemon
```