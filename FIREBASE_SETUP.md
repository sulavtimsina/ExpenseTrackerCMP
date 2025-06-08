# Firebase Setup Instructions

## 1. Create Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project named "expense-tracker-kmp"
3. Enable Firestore Database
4. Set Firestore rules to allow authenticated users:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /expenses/{document} {
      allow read, write: if request.auth != null && 
                        resource.data.userId == request.auth.uid;
    }
  }
}
```

## 2. Platform Setup

We're using GitLive Firebase KMP library, which requires minimal configuration:

### All Platforms
The GitLive Firebase library will automatically initialize Firebase using the configuration provided during build.

## 3. Enable Authentication
1. Go to Authentication > Sign-in method
2. Enable "Anonymous" authentication
3. This allows users to sync data without creating accounts

## 4. Firestore Database Structure
```
expenses (collection)
  - documentId (expense.id)
    - id: String
    - amount: Double
    - category: String
    - note: String?
    - date: String (ISO format)
    - imagePath: String?
    - userId: String
    - lastModified: Long (timestamp)
    - isDeleted: Boolean
```

## 5. Test the Implementation
1. Run the app on different platforms
2. Add expenses on one platform
3. Verify they sync to other platforms in real-time
