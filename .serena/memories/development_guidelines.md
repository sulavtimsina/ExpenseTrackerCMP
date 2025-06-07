# Development Guidelines and Best Practices

## Kotlin Multiplatform Specific Guidelines

### Shared Code (commonMain)
- **Domain Logic**: Keep all business logic in commonMain
- **UI Components**: Use Compose Multiplatform for shared UI
- **Data Models**: Define data classes and entities in common code
- **Repository Interfaces**: Define contracts in commonMain, implement in platform-specific code when needed

### Platform-Specific Code
- **Minimize Platform Code**: Only use platform-specific code when absolutely necessary
- **Expect/Actual Pattern**: Use expect/actual for platform-specific implementations
- **Platform APIs**: Access platform-specific APIs only in respective sourcesets

## Architecture Best Practices

### Clean Architecture
- **Dependency Rule**: Dependencies should point inward (presentation → domain ← data)
- **Single Responsibility**: Each class should have one reason to change
- **Interface Segregation**: Prefer small, focused interfaces

### State Management
- **ViewModels**: Use for UI-related business logic and state management
- **Repositories**: Single source of truth for data access
- **Use Cases**: Encapsulate business logic operations

### Error Handling
- **Result Types**: Always use `Result<D, E>` for operations that can fail
- **Domain Errors**: Create specific error types for different failure scenarios
- **Graceful Degradation**: Provide fallbacks for network/data failures

## Compose Guidelines

### Performance
- **Stable Parameters**: Ensure Composable parameters are stable
- **Remember**: Use `remember` for expensive calculations
- **LazyColumn**: Use lazy layouts for large lists
- **Avoid Recomposition**: Structure composables to minimize unnecessary recomposition

### State Management
- **State Hoisting**: Move state up to the lowest common parent
- **ViewModel Integration**: Use ViewModels for business logic, not UI state
- **Side Effects**: Use appropriate side-effect APIs (`LaunchedEffect`, `DisposableEffect`, etc.)

## Database Best Practices

### Room Database
- **Entity Design**: Keep entities simple and focused
- **Migration Strategy**: Plan for database migrations from the start
- **Query Optimization**: Use appropriate indices and efficient queries
- **Type Converters**: Handle complex types with proper converters

### Data Layer
- **Repository Pattern**: Abstract data sources behind repository interfaces
- **Caching Strategy**: Implement proper caching for offline support
- **Data Synchronization**: Handle conflicts between local and remote data

## Networking Best Practices

### Ktor Client
- **Client Configuration**: Configure timeout, retry, and logging appropriately
- **Content Negotiation**: Use proper serialization for API communication
- **Error Handling**: Handle network errors gracefully
- **Authentication**: Implement secure authentication if required

### API Integration
- **Rate Limiting**: Respect API rate limits
- **Offline Support**: Cache data for offline access
- **Data Validation**: Validate all incoming data from external sources

## Dependency Injection (Koin)

### Module Organization
- **Feature Modules**: Organize dependencies by feature
- **Platform Modules**: Separate platform-specific dependencies
- **Scope Management**: Use appropriate scopes (single, factory, scoped)

### Best Practices
- **Interface Injection**: Inject interfaces, not concrete implementations
- **Lazy Injection**: Use lazy injection for expensive objects
- **Testing**: Provide test doubles for unit testing

## Testing Guidelines

### Unit Testing
- **Test Structure**: Follow Arrange-Act-Assert pattern
- **Mocking**: Mock external dependencies
- **Coverage**: Aim for high test coverage of business logic

### Integration Testing
- **Database Testing**: Test database operations with in-memory databases
- **Network Testing**: Mock network responses for consistent testing
- **UI Testing**: Test Compose UI with testing frameworks

## Code Review Guidelines

### What to Look For
- **Architecture Compliance**: Ensure clean architecture principles
- **Error Handling**: Verify proper error handling patterns
- **Performance**: Check for potential performance issues
- **Security**: Review for security vulnerabilities
- **Documentation**: Ensure complex logic is documented

### Code Quality
- **Readability**: Code should be self-documenting
- **Maintainability**: Follow SOLID principles
- **Testability**: Code should be easily testable
- **Consistency**: Follow established patterns and conventions