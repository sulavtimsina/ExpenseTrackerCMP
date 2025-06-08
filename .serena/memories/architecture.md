# Project Structure and Architecture

## Directory Structure
```
CMP-Bookpedia/
├── composeApp/                    # Main application module
│   ├── src/
│   │   ├── commonMain/            # Shared code for all platforms
│   │   │   ├── kotlin/com/sulavtimsina/bookpedia/
│   │   │   │   ├── core/          # Core business logic
│   │   │   │   │   ├── domain/    # Domain models and interfaces
│   │   │   │   │   └── presentation/ # UI-related utilities
│   │   │   │   └── App.kt         # Main App composable
│   │   │   └── composeResources/  # Shared resources
│   │   ├── androidMain/           # Android-specific code
│   │   ├── iosMain/               # iOS-specific code
│   │   └── desktopMain/           # Desktop-specific code
│   ├── schemas/                   # Room database schemas
│   └── build.gradle.kts
├── iosApp/                        # iOS application wrapper
└── gradle/                        # Gradle configuration
```

## Architecture Patterns

### Clean Architecture
- **Core Domain**: Business logic, entities, and use cases
- **Data Layer**: Repository implementations, data sources
- **Presentation Layer**: ViewModels, UI components

### Error Handling
- Custom `Result<D, E>` sealed interface for functional error handling
- `Error` marker interface for domain errors
- Extension functions: `map()`, `onSuccess()`, `onError()`, `asEmptyDataResult()`

### Database Schema
- **BookEntity** table with comprehensive book information:
  - id, title, description, imageUrl
  - authors, languages, firstPublishYear
  - ratingsAverage, ratingsCount
  - numPagesMedian, numEditions

### Platform Entry Points
- **Android**: `MainActivity` with `setContent { App() }`
- **iOS**: `MainViewController()` with `ComposeUIViewController`
- **Desktop**: `main()` with `Window` and `App()`

## Package Structure
- Base package: `com.sulavtimsina.bookpedia`
- Core modules: `core.domain`, `core.presentation`
- Platform-specific implementations in respective source sets