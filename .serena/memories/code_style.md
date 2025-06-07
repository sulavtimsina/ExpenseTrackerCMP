# Code Style and Conventions

## Kotlin Code Style
- **Official Kotlin code style** (`kotlin.code.style=official`)
- Standard Kotlin naming conventions
- PascalCase for classes and interfaces
- camelCase for functions, properties, and variables

## Project Conventions

### Package Naming
- Base package: `com.plcoding.bookpedia`
- Feature-based package organization
- Platform-specific packages mirror common structure

### Architecture Conventions
- **Result Type**: Use custom `Result<D, E>` for error handling instead of exceptions
- **Error Handling**: Implement domain-specific error types extending the `Error` interface
- **Functional Approach**: Prefer functional programming patterns with extension functions

### Code Organization
- **CommonMain**: Shared business logic, UI components, and domain models
- **Platform-specific**: Only platform-specific implementations (e.g., database drivers, platform APIs)
- **Clean separation**: Domain, data, and presentation layers

### Dependency Injection
- Use Koin for dependency injection
- Define modules in commonMain when possible
- Platform-specific dependencies in respective source sets

### Compose Guidelines
- **Preview Support**: Use `@Preview` annotations for Compose functions
- **State Management**: Use `remember`, `mutableStateOf` for local state
- **ViewModels**: Use lifecycle-aware ViewModels for business logic

### Database Conventions
- **Room**: Use Room for cross-platform database access
- **Entity Naming**: Suffix entity classes with "Entity" (e.g., `BookEntity`)
- **Schema Management**: Keep database schemas in `/schemas` directory

## File Naming
- Kotlin files: PascalCase (e.g., `MainActivity.kt`)
- Resources: snake_case (e.g., `book_error_2.xml`)
- Gradle files: Standard Gradle naming

## JVM Configuration
- Target JVM 11 for Android
- Kotlin daemon: 2048M heap size
- Gradle: 2048M heap size