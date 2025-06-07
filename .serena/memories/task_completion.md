# Task Completion Guidelines

## When a Development Task is Completed

### 1. Code Verification
- **Compile Check**: Ensure all targets compile successfully
  ```bash
  ./gradlew compileKotlin
  ```
- **Build Verification**: Run a clean build to verify everything works
  ```bash
  ./gradlew clean build
  ```

### 2. Testing
- **Run Unit Tests**: Execute all relevant tests
  ```bash
  ./gradlew test
  ```
- **Platform-Specific Testing**: Test on target platforms when applicable
  ```bash
  ./gradlew :composeApp:testDebugUnitTest    # Android
  ./gradlew :composeApp:desktopTest          # Desktop
  ```

### 3. Database Schema Validation (if database changes made)
- **Validate Room Schema**: Ensure database migrations are correct
  ```bash
  ./gradlew :composeApp:validateDebugDatabaseSchema
  ```
- **Update Schema Files**: Verify schema files in `/schemas` are updated

### 4. Code Quality Checks
- **Code Style**: Ensure code follows Kotlin official style guide
- **Architecture**: Verify clean architecture principles are maintained
- **Error Handling**: Ensure proper use of Result types and error handling

### 5. Platform Testing (when applicable)
- **Android**: Test on device/emulator
  ```bash
  ./gradlew :composeApp:installDebug
  ```
- **Desktop**: Run desktop application
  ```bash
  ./gradlew :composeApp:run
  ```
- **iOS**: Build and test in Xcode simulator

### 6. Documentation Updates
- Update relevant documentation if public APIs changed
- Update memory files if architectural patterns change
- Add inline code documentation for complex logic

### 7. Version Control
- **Commit Changes**: Create meaningful commit messages
  ```bash
  git add .
  git commit -m "feat: descriptive commit message"
  ```
- **Branch Strategy**: Follow project's branching strategy
- **Code Review**: Prepare for code review if working in a team

### 8. Performance Considerations
- **Memory Usage**: Check for memory leaks, especially in ViewModels
- **Network Efficiency**: Verify API calls are optimized
- **Image Loading**: Ensure proper image caching with Coil

### 9. Dependency Management
- **Version Compatibility**: Verify dependency versions in `libs.versions.toml`
- **Unused Dependencies**: Remove any unused dependencies
- **Security**: Check for known vulnerabilities in dependencies

### 10. Final Verification
- **Clean State Test**: Test from a clean build state
- **Multi-Platform Build**: Ensure all platforms build successfully
  ```bash
  ./gradlew build
  ```

## Red Flags to Check
- **Resource Leaks**: Proper cleanup of resources and listeners
- **State Management**: Proper state handling in Compose
- **Error Boundaries**: Graceful error handling throughout the app
- **Performance**: No blocking operations on main thread
- **Memory**: Proper lifecycle management of ViewModels and repositories