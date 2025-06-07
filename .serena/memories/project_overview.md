# CMP-Bookpedia Project Overview

## Purpose
CMP-Bookpedia is a Kotlin Compose Multiplatform application for book management. The app allows users to:
- Browse and search for books
- View detailed book information (title, description, authors, ratings, etc.)
- Favorite books for later reference
- Manage a local collection of favorite books

## Target Platforms
- **Android** (API 24+, targeting API 35)
- **iOS** (arm64, x64, simulator arm64)
- **Desktop** (JVM-based, packaged as DMG/MSI/DEB)

## Key Features
- Cross-platform UI with Compose Multiplatform
- Local database storage for favorites using Room
- Network capabilities for fetching book data
- Image loading and caching
- Clean architecture with proper separation of concerns