# 💰 Expense Tracker - Kotlin Multiplatform

A modern, cross-platform expense tracking application built with **Kotlin Multiplatform** and **Jetpack Compose Multiplatform**. Track your expenses, visualize spending patterns, and manage your finances across Android, iOS, and Desktop platforms.

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.7.0-blue)](https://github.com/JetBrains/compose-multiplatform)
[![Platform](https://img.shields.io/badge/Platform-Android%20|%20iOS%20|%20Desktop-brightgreen.svg)](https://kotlinlang.org/docs/multiplatform.html)

## ✨ Features

### 📊 **Expense Management**
- ➕ Add, edit, and delete expenses with ease
- 🏷️ Categorize expenses (Food, Transportation, Entertainment, Shopping, Bills, Healthcare, Education, Travel, Other)
- 📝 Add notes and attach images to expense entries
- 📅 Date-based expense tracking

### 📈 **Analytics & Insights**
- 🥧 **Pie Charts** - Visual breakdown of spending by category
- 📉 **Line Charts** - Track spending trends over time (daily/monthly)
- 📊 **Period Analysis** - Filter data by custom date ranges
- 🎯 **Smart Analytics** - Comprehensive spending summaries

### 🎨 **Modern UI/UX**
- 🌙 Material Design with clean, intuitive interface
- 📱 **Bottom Navigation** - Easy access to all features
- 🔄 **Smooth Animations** - Polished user experience
- 📊 **Interactive Charts** - Tap to explore data

### 🔧 **Technical Features**
- 💾 **Local Database** - SQLDelight for efficient data storage
- 🔄 **Real-time Updates** - Reactive UI with Flow
- 🏗️ **Clean Architecture** - MVVM pattern with proper separation of concerns
- 🧪 **Sample Data** - 47+ pre-loaded expense entries for testing

## 📱 Screenshots

### Android
<table>
  <tr>
    <td><strong>Home / Analytics</strong></td>
    <td><strong>Expense List</strong></td>
    <td><strong>Add Expense</strong></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/7adbf33e-cc2a-4dcf-98cd-8777346ec5c1" width="250" style="border:1px solid #ccc; border-radius:8px;" /></td>
    <td><img src="https://github.com/user-attachments/assets/2303d90c-7a55-45bf-9c31-03df5608bbd4" width="250" style="border:1px solid #ccc; border-radius:8px;" /></td>
    <td><img src="https://github.com/user-attachments/assets/324fccec-513a-41e1-8895-57e9d2df717d" width="250" style="border:1px solid #ccc; border-radius:8px;" /></td>
  </tr>
</table>


### Desktop
<table>
  <tr>
    <td><strong>Analytics Dashboard</strong></td>
  </tr>
  <tr>
    <td>
      <img src="https://github.com/user-attachments/assets/c4aa707b-4a4e-42dd-b844-029dd741ae6e" width="100%" style="border:1px solid #ccc; border-radius:8px;" />
    </td>
  </tr>
  <tr>
    <td><strong>Expense Addition</strong></td>
  </tr>
  <tr>
    <td>
      <img src="https://github.com/user-attachments/assets/9c41d887-ac4e-4caa-8090-ba6f315485d5" width="100%" style="border:1px solid #ccc; border-radius:8px;" />
    </td>
  </tr>
  <tr>
    <td><strong>Expense Management</strong></td>
  </tr>
  <tr>
    <td>
      <img width="1124" alt="image" src="https://github.com/user-attachments/assets/c9d9d0b2-c6f8-4639-a899-c0f9ae2c9e20" style="border:1px solid #ccc; border-radius:8px; margin-bottom:8px;" />
    </td>
  </tr>
</table>


## 🛠️ Tech Stack

### **Core Technologies**
- **[Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)** 2.0.21 - Share business logic across platforms
- **[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform)** 1.7.0 - Modern declarative UI
- **[Kotlinx DateTime](https://github.com/Kotlin/kotlinx-datetime)** - Cross-platform date/time handling

### **Architecture & Design Patterns**
- **MVVM (Model-View-ViewModel)** - Clean separation of concerns
- **Repository Pattern** - Abstract data access
- **Use Cases** - Business logic encapsulation
- **Clean Architecture** - Maintainable and testable code

### **Database & Storage**
- **[SQLDelight](https://sqldelight.github.io/sqldelight/)** 2.0.2 - Type-safe SQL database
- **Cross-platform Database** - Consistent data layer across all platforms

### **Dependency Injection**
- **[Koin](https://insert-koin.io/)** 4.0.0 - Lightweight DI framework

### **UI & Navigation**
- **[Navigation Compose](https://developer.android.com/jetpack/compose/navigation)** - Type-safe navigation
- **[Material Icons Extended](https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary)** - Comprehensive icon set

### **Image Loading**
- **[Coil3](https://coil-kt.github.io/coil/)** 3.0.0-rc02 - Async image loading

### **Charts & Visualization**
- **Custom Canvas Drawing** - Native chart implementations
- **Compose Graphics** - Smooth animations and interactions

## 🚀 Getting Started

### Prerequisites
- **JDK 11** or higher
- **Android Studio** (latest stable version)
- **Xcode** (for iOS development, macOS only)
- **Git**

### Clone Repository
```bash
git clone https://github.com/yourusername/expense-tracker-kmp.git
cd expense-tracker-kmp
```

### Initial Setup
```bash
# Make gradlew executable (macOS/Linux)
chmod +x gradlew

# Check project setup
./gradlew tasks
```

## 🏗️ Building & Running

### Android
```bash
# Debug build
./gradlew :composeApp:assembleDebug

# Install on connected device/emulator
./gradlew :composeApp:installDebug
```

### Desktop
```bash
# Run desktop application
./gradlew :composeApp:run

# Create distribution packages
./gradlew :composeApp:packageDmg        # macOS
./gradlew :composeApp:packageMsi        # Windows  
./gradlew :composeApp:packageDeb        # Linux
```

### iOS
```bash
# Generate iOS framework
./gradlew :composeApp:linkDebugFrameworkIosArm64

# Open iOS project in Xcode
open iosApp/iosApp.xcodeproj
```

## 📁 Project Structure

```
expense-tracker-kmp/
├── composeApp/                          # Shared code
│   ├── src/
│   │   ├── commonMain/                  # Shared business logic & UI
│   │   │   ├── kotlin/
│   │   │   │   └── com/sulavtimsina/expensetracker/
│   │   │   │       ├── analytics/       # Analytics feature
│   │   │   │       ├── expense/         # Expense management
│   │   │   │       ├── settings/        # App settings
│   │   │   │       ├── core/           # Core utilities
│   │   │   │       ├── data/           # Sample data provider
│   │   │   │       └── di/             # Dependency injection
│   │   │   └── sqldelight/             # Database schema
│   │   ├── androidMain/                # Android-specific code
│   │   ├── iosMain/                    # iOS-specific code
│   │   └── desktopMain/                # Desktop-specific code
│   └── build.gradle.kts
├── iosApp/                             # iOS application
│   └── iosApp.xcodeproj
├── gradle/
│   └── libs.versions.toml              # Dependency versions
└── README.md
```

## 🎯 Key Features Breakdown

### Expense Management
- **CRUD Operations** - Full expense lifecycle management
- **Smart Categories** - 9 predefined categories with custom icons
- **Rich Data Entry** - Notes, images, and precise date/time tracking
- **Validation** - Input validation and error handling

### Analytics Engine
- **Multiple Chart Types** - Pie charts for categories, line charts for trends
- **Flexible Time Periods** - Last week, month, quarter, year, or custom ranges
- **Interactive Visualizations** - Tap charts for detailed breakdowns
- **Real-time Updates** - Charts update immediately when data changes

### Cross-Platform Consistency
- **Shared Business Logic** - 95% code sharing across platforms
- **Platform-Specific UI** - Native look and feel on each platform
- **Consistent Data** - SQLDelight ensures data consistency across platforms

## 🧪 Sample Data

The app comes with **47 realistic expense entries** to demonstrate functionality:
- Diverse categories and amounts ($2.50 - $450.00)
- 90-day date range for trend analysis
- Realistic expense descriptions and patterns

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Development Guidelines
- Follow **Kotlin coding conventions**
- **ktlint formatter** runs automatically on each commit to ensure code style consistency
- Write **unit tests** for new features
- Update **documentation** as needed
- Test on **multiple platforms** before submitting

## 📝 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **JetBrains** for Kotlin Multiplatform and Compose Multiplatform
- **Google** for Material Design guidelines
- **SQLDelight** team for excellent database tooling
- **Koin** team for lightweight dependency injection

---

⭐ **Star this repo** if you find it helpful!

**Built with ❤️ using Kotlin Multiplatform**
