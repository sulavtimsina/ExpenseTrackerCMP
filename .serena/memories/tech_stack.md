# Tech Stack and Dependencies

## Core Technologies
- **Kotlin Multiplatform** 2.0.21
- **Jetpack Compose Multiplatform** 1.7.0
- **Android Gradle Plugin** 8.5.2

## UI & Navigation
- Jetpack Compose for UI
- Navigation Compose 2.8.0-alpha10
- Compose Preview support
- Material Design components

## Dependency Injection
- **Koin** 4.0.0
  - koin-core (common)
  - koin-android (Android-specific)
  - koin-androidx-compose (Android Compose)
  - koin-compose (common Compose)
  - koin-compose-viewmodel (ViewModel injection)

## Networking
- **Ktor** 3.0.0
  - ktor-client-core
  - ktor-client-content-negotiation
  - ktor-client-auth
  - ktor-client-logging
  - ktor-serialization-kotlinx-json
  - Platform-specific clients: okhttp (Android/Desktop), darwin (iOS)

## Database
- **Room** 2.7.0-alpha11 (multiplatform)
- **SQLite** 2.5.0-alpha11 (bundled)
- KSP for annotation processing

## Image Loading
- **Coil3** 3.0.0-rc02
  - coil-compose
  - coil-compose-core
  - coil-network-ktor (integration with Ktor)

## Serialization
- **Kotlinx Serialization JSON** 1.7.3

## Lifecycle
- **AndroidX Lifecycle** 2.8.3
  - lifecycle-viewmodel
  - lifecycle-runtime-compose

## Testing
- Kotlin Test framework
- JUnit 4.13.2
- AndroidX Test libraries