# Contributing to Expense Tracker KMP

Thank you for your interest in contributing to the Expense Tracker Kotlin Multiplatform project! 🎉

## 🤝 Ways to Contribute

- 🐛 Report bugs and issues
- 💡 Suggest new features or improvements
- 📖 Improve documentation
- 🔧 Submit code changes and fixes
- 🧪 Write or improve tests
- 🎨 Enhance UI/UX design

## 🚀 Getting Started

1. **Fork the Repository**
   ```bash
   # Click the "Fork" button on GitHub, then clone your fork
   git clone https://github.com/yourusername/expense-tracker-kmp.git
   cd expense-tracker-kmp
   ```

2. **Set Up Development Environment**
   - Install JDK 11 or higher
   - Install Android Studio (latest stable)
   - Install Xcode (for iOS development, macOS only)
   - Sync project and verify it builds successfully

3. **Create a Feature Branch**
   ```bash
   git checkout -b feature/your-feature-name
   # or
   git checkout -b bugfix/issue-description
   ```

## 📝 Development Guidelines

### Code Style
- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add comments for complex business logic
- Keep functions small and focused

### Architecture
- Follow the existing MVVM + Clean Architecture pattern
- Place business logic in use cases
- Keep ViewModels lean and focused on UI state
- Use the Repository pattern for data access

### Testing
- Write unit tests for new features
- Test on multiple platforms before submitting
- Include tests for edge cases and error scenarios
- Maintain or improve test coverage

### Commits
- Use clear, descriptive commit messages
- Follow conventional commit format when possible:
  ```
  feat: add expense filtering by date range
  fix: resolve chart rendering issue on iOS
  docs: update setup instructions
  test: add unit tests for expense repository
  ```

### Platform-Specific Code
- Minimize platform-specific code
- Use `expect`/`actual` declarations for platform differences
- Test changes on all supported platforms
- Document any platform-specific behavior

## 🐛 Reporting Issues

When reporting bugs, please include:

1. **Environment Information**
   - Platform (Android/iOS/Desktop)
   - OS version
   - Device/simulator details
   - App version

2. **Bug Description**
   - Clear title and description
   - Steps to reproduce
   - Expected vs actual behavior
   - Screenshots/videos if applicable

3. **Additional Context**
   - Error logs or crash reports
   - Any workarounds you've tried

## 💡 Feature Requests

For new features, please provide:

1. **Feature Description**
   - Clear explanation of the feature
   - Use cases and benefits
   - Platform compatibility considerations

2. **Design Considerations**
   - UI/UX mockups if applicable
   - Technical implementation ideas
   - Potential challenges or limitations

## 🔄 Pull Request Process

1. **Before Submitting**
   - Ensure your code follows the style guidelines
   - Run tests and verify they pass
   - Test on multiple platforms
   - Update documentation if needed

2. **Pull Request Description**
   - Reference related issues
   - Describe what changes were made
   - Include screenshots for UI changes
   - Note any breaking changes

3. **Review Process**
   - Respond to review feedback promptly
   - Make requested changes in new commits
   - Squash commits before merging if requested

## 🧪 Testing

### Running Tests
```bash
# Run all tests
./gradlew test

# Platform-specific tests
./gradlew :composeApp:testDebugUnitTest        # Android
./gradlew :composeApp:desktopTest              # Desktop
```

### Test Coverage
- Aim for high test coverage on business logic
- Test critical user flows
- Include edge cases and error scenarios

## 📚 Documentation

- Update README.md for significant changes
- Add inline documentation for complex code
- Update API documentation if applicable
- Include code examples for new features

## 🎨 UI/UX Guidelines

- Follow Material Design principles
- Ensure consistency across platforms
- Test with different screen sizes
- Consider accessibility requirements
- Maintain smooth animations and transitions

## ❓ Questions and Support

- 💬 **GitHub Discussions** for general questions
- 🐛 **GitHub Issues** for bug reports
- 📧 **Email** for sensitive topics

## 📄 Code of Conduct

Please note that this project is released with a Contributor Code of Conduct. By participating in this project, you agree to abide by its terms:

- Be respectful and inclusive
- Welcome newcomers and help them learn
- Focus on constructive feedback
- Respect different viewpoints and experiences

## 🏷️ Issue Labels

We use the following labels to organize issues:

- `bug` - Something isn't working
- `enhancement` - New feature or request
- `documentation` - Improvements to docs
- `good first issue` - Good for newcomers
- `help wanted` - Extra attention needed
- `platform:android` - Android-specific
- `platform:ios` - iOS-specific
- `platform:desktop` - Desktop-specific

## 🎉 Recognition

Contributors will be recognized in:
- README.md contributors section
- Release notes for significant contributions
- Special thanks in major version releases

Thank you for contributing to make Expense Tracker KMP better for everyone! 🚀
