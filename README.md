# Namibia Voting System

A comprehensive REST API system for managing elections and voting in Namibia. This system supports various types of elections including Presidential Elections, National Assembly Elections, Regional Council Elections, Local Authority/Municipal Elections, and Referendums/National Polls.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [System Architecture](#system-architecture)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Database Configuration](#database-configuration)
- [API Endpoints](#api-endpoints)
- [Usage Examples](#usage-examples)
- [File Upload](#file-upload)
- [Email Configuration](#email-configuration)
- [Security Considerations](#security-considerations)

## Overview

The Namibia Voting System is a Spring Boot-based REST API that enables:

- **Voters**: Access voting categories, view candidates, and submit votes without registration
- **Administrators**: Manage candidates, voting categories, regions, voter cards, and users
- **Vote Validation**: Ensures one vote per voter with multiple validation checks
- **Email Confirmation**: Sends confirmation emails to voters after successful vote submission

## Features

### For Voters
- ✅ No registration required - immediate access to voting dashboard
- ✅ View available voting categories
- ✅ Browse candidates with photos and party logos
- ✅ Submit votes with personal details
- ✅ Automatic email confirmation upon vote submission
- ✅ One vote per voter enforcement

### For Administrators
- ✅ User authentication (email and password)
- ✅ Full CRUD operations for:
  - Users (Admin accounts)
  - Vote Categories
  - Regions
  - Voter Cards
  - Candidates (with photo and logo upload)
- ✅ View all submitted votes
- ✅ Filter votes by candidate or category

### System Features
- ✅ Unique validation for National ID, Email, Phone Number
- ✅ Voter Card Number verification
- ✅ File upload for candidate photos and party logos
- ✅ Email notifications with beautifully designed HTML templates
- ✅ RESTful API design
- ✅ CORS enabled for frontend integration

## Technology Stack

- **Java 21**
- **Spring Boot 3.5.7**
- **Spring Data JPA**
- **PostgreSQL** (Database)
- **Spring Mail** (Email service)
- **Lombok** (Reducing boilerplate code)
- **Maven** (Build tool)

## System Architecture

```
votenam/
├── models/          # Entity classes (JPA)
├── repositories/    # Data access layer
├── services/        # Business logic
├── controllers/     # REST API endpoints
└── resources/
    ├── application.properties
    └── static/
```

### Models

1. **Users** - Admin user accounts
2. **VoteCategory** - Types of elections (Presidential, National Assembly, etc.)
3. **Regions** - Namibian regions for voter registration
4. **VotersCard** - Pre-registered voter card numbers
5. **Candidates** - Election candidates with photos and party logos
6. **VotersDetails** - Submitted votes with voter information

## Prerequisites

Before running this application, ensure you have:

- **Java 21** or higher
- **Maven 3.6+**
- **PostgreSQL 12+** (or any compatible version)
- **Gmail Account** (for email service - or configure your own SMTP)

## Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd votenam
```

### 2. Configure Database

Update `src/main/resources/application.properties` with your PostgreSQL credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/votenam_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Configure Email (Optional)

The email configuration is already set up in `application.properties`. If you want to use a different email account:

```properties
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
```

**Note**: For Gmail, you need to use an [App Password](https://support.google.com/accounts/answer/185833) instead of your regular password.

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

Or using the wrapper:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The application will start on `http://localhost:8080`

## Database Configuration

The application uses JPA with Hibernate. Tables will be automatically created on first run due to:

```properties
spring.jpa.hibernate.ddl-auto=update
```

### Initial Data Setup

After starting the application, you'll need to create:

1. **Admin User** - Use the admin API to create your first admin account
2. **Vote Categories** - Create categories like "Presidential Elections", "National Assembly Elections", etc.
3. **Regions** - Add Namibian regions (e.g., "Khomas", "Erongo", "Otjozondjupa", etc.)
4. **Voter Cards** - Register valid voter card numbers
5. **Candidates** - Add candidates with their details, photos, and party logos

## API Endpoints

### Voter Endpoints (Public)

#### Get Dashboard
```
GET /api/voter/dashboard
```
Returns all available voting categories.

**Response:**
```json
{
  "success": true,
  "categories": [
    {
      "id": 1,
      "categoryName": "Presidential Elections"
    }
  ]
}
```

#### Get Candidates by Category
```
GET /api/voter/category/{categoryId}/candidates
```
Returns all candidates for a specific voting category.

**Response:**
```json
{
  "success": true,
  "candidates": [
    {
      "id": 1,
      "fullName": "John Doe",
      "partyName": "Democratic Party",
      "position": "President",
      "photoUrl": "http://localhost:8080/api/photos/view/photo_123.jpg",
      "partyLogoUrl": "http://localhost:8080/api/logos/view/logo_123.jpg",
      "voteCategory": {
        "id": 1,
        "categoryName": "Presidential Elections"
      }
    }
  ]
}
```

#### Get All Regions
```
GET /api/voter/regions
```
Returns all regions for the dropdown menu.

#### Submit Vote
```
POST /api/voter/submit-vote
Content-Type: application/json
```

**Request Body:**
```json
{
  "fullName": "Jane Smith",
  "nationalIdNumber": "1234567890123",
  "dateOfBirth": "1990-01-15",
  "address": "123 Main Street, Windhoek",
  "region": {
    "id": 1
  },
  "phoneNumber": "+264811234567",
  "email": "jane.smith@email.com",
  "votersIdNumber": "VC123456789",
  "candidate": {
    "id": 1
  },
  "voteCategory": {
    "id": 1
  }
}
```

**Response:**
```json
{
  "success": true,
  "message": "Your vote has been successfully submitted. A confirmation email has been sent to your email address.",
  "vote": { ... }
}
```

### Admin Endpoints

#### Admin Login
```
POST /api/admin/login
Content-Type: application/json
```

**Request Body:**
```json
{
  "email": "admin@example.com",
  "password": "password123"
}
```

#### User Management
```
POST   /api/admin/users              # Create user
GET    /api/admin/users               # Get all users
GET    /api/admin/users/{id}          # Get user by ID
PUT    /api/admin/users/{id}          # Update user
DELETE /api/admin/users/{id}          # Delete user
```

#### Vote Category Management
```
POST   /api/admin/vote-categories     # Create vote category
GET    /api/admin/vote-categories     # Get all categories
GET    /api/admin/vote-categories/{id} # Get category by ID
PUT    /api/admin/vote-categories/{id} # Update category
DELETE /api/admin/vote-categories/{id} # Delete category
```

#### Region Management
```
POST   /api/admin/regions             # Create region
GET    /api/admin/regions             # Get all regions
GET    /api/admin/regions/{id}        # Get region by ID
PUT    /api/admin/regions/{id}        # Update region
DELETE /api/admin/regions/{id}        # Delete region
```

#### Voter Card Management
```
POST   /api/admin/voters-cards        # Create voter card
GET    /api/admin/voters-cards        # Get all voter cards
GET    /api/admin/voters-cards/{id}   # Get card by ID
PUT    /api/admin/voters-cards/{id}   # Update card
DELETE /api/admin/voters-cards/{id}   # Delete card
```

#### Candidate Management
```
POST   /api/admin/candidates          # Create candidate (multipart/form-data)
GET    /api/admin/candidates          # Get all candidates
GET    /api/admin/candidates/{id}     # Get candidate by ID
PUT    /api/admin/candidates/{id}     # Update candidate (multipart/form-data)
DELETE /api/admin/candidates/{id}     # Delete candidate
```

**Create Candidate Request (multipart/form-data):**
- `fullName`: String
- `partyName`: String
- `position`: String
- `voteCategoryId`: Long
- `photo`: File (optional)
- `partyLogo`: File (optional)

#### Vote Management
```
GET    /api/admin/votes               # Get all votes
GET    /api/admin/votes/{id}          # Get vote by ID
GET    /api/admin/votes/candidate/{candidateId}  # Get votes by candidate
GET    /api/admin/votes/category/{categoryId}    # Get votes by category
```

### File View Endpoints

#### View Images
```
GET /api/photos/view/{fileName}       # View candidate photo
GET /api/logos/view/{fileName}        # View party logo
```

#### Download Images
```
GET /api/photos/download/{fileName}   # Download candidate photo
GET /api/logos/download/{fileName}     # Download party logo
```

## Testing with Postman

This section provides comprehensive instructions for testing all API endpoints using Postman.

### Postman Setup

1. **Download Postman**: If you don't have Postman, download it from [postman.com](https://www.postman.com/downloads/)

2. **Base URL**: Set up an environment variable in Postman:
   - Variable Name: `baseUrl`
   - Initial Value: `http://localhost:8080`
   - Use `{{baseUrl}}` in all requests

3. **Create a Collection**: Create a new collection named "Namibia Voting System API"

### Testing Workflow

Follow this order for testing:

1. **Admin Setup** → Create admin user and login
2. **Master Data** → Create regions, vote categories, voter cards
3. **Candidates** → Create candidates with photos and logos
4. **Voting** → Test voter endpoints and vote submission
5. **View Results** → Check votes and statistics

---

## Detailed Postman Testing Guide

### 1. Admin User Management

#### 1.1 Create Admin User

**Request:**
- **Method**: `POST`
- **URL**: `{{baseUrl}}/api/admin/users`
- **Headers**:
  ```
  Content-Type: application/json
  ```
- **Body** (raw JSON):
  ```json
  {
    "email": "admin@votenam.gov.na",
    "password": "admin123",
    "username": "admin",
    "role": "ADMIN"
  }
  ```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "user": {
    "id": 1,
    "email": "admin@votenam.gov.na",
    "username": "admin",
    "role": "ADMIN"
  }
}
```

**Postman Steps:**
1. Create new request in Postman
2. Set method to `POST`
3. Enter URL: `http://localhost:8080/api/admin/users`
4. Go to **Headers** tab, add:
   - Key: `Content-Type`, Value: `application/json`
5. Go to **Body** tab, select **raw** and **JSON**
6. Paste the JSON body above
7. Click **Send**

---

#### 1.2 Admin Login

**Request:**
- **Method**: `POST`
- **URL**: `{{baseUrl}}/api/admin/login`
- **Headers**:
  ```
  Content-Type: application/json
  ```
- **Body** (raw JSON):
  ```json
  {
    "email": "admin@votenam.gov.na",
    "password": "admin123"
  }
  ```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "user": {
    "id": 1,
    "email": "admin@votenam.gov.na",
    "username": "admin",
    "role": "ADMIN"
  }
}
```

**Error Response (400 Bad Request):**
```json
{
  "success": false,
  "message": "Invalid email or password"
}
```

---

#### 1.3 Get All Users

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/admin/users`
- **Headers**: None required

**Expected Response (200 OK):**
```json
{
  "success": true,
  "users": [
    {
      "id": 1,
      "email": "admin@votenam.gov.na",
      "username": "admin",
      "role": "ADMIN"
    }
  ]
}
```

---

#### 1.4 Get User by ID

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/admin/users/1`
- **Headers**: None required

**Expected Response (200 OK):**
```json
{
  "success": true,
  "user": {
    "id": 1,
    "email": "admin@votenam.gov.na",
    "username": "admin",
    "role": "ADMIN"
  }
}
```

---

#### 1.5 Update User

**Request:**
- **Method**: `PUT`
- **URL**: `{{baseUrl}}/api/admin/users/1`
- **Headers**:
  ```
  Content-Type: application/json
  ```
- **Body** (raw JSON):
  ```json
  {
    "email": "admin@votenam.gov.na",
    "password": "newpassword123",
    "username": "admin",
    "role": "ADMIN"
  }
  ```

---

#### 1.6 Delete User

**Request:**
- **Method**: `DELETE`
- **URL**: `{{baseUrl}}/api/admin/users/1`
- **Headers**: None required

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "User deleted successfully"
}
```

---

### 2. Regions Management

#### 2.1 Create Region

**Request:**
- **Method**: `POST`
- **URL**: `{{baseUrl}}/api/admin/regions`
- **Headers**:
  ```
  Content-Type: application/json
  ```
- **Body** (raw JSON):
  ```json
  {
    "regionName": "Khomas"
  }
  ```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "region": {
    "id": 1,
    "regionName": "Khomas"
  }
}
```

**Create Multiple Regions:**
Repeat with different region names:
- "Erongo"
- "Otjozondjupa"
- "Omaheke"
- "Hardap"
- "Karas"
- "Kunene"
- "Ohangwena"
- "Omusati"
- "Oshana"
- "Oshikoto"
- "Otjozondjupa"
- "Zambezi"
- "Kavango East"
- "Kavango West"

---

#### 2.2 Get All Regions

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/admin/regions`

**Expected Response (200 OK):**
```json
{
  "success": true,
  "regions": [
    {
      "id": 1,
      "regionName": "Khomas"
    },
    {
      "id": 2,
      "regionName": "Erongo"
    }
  ]
}
```

---

#### 2.3 Update Region

**Request:**
- **Method**: `PUT`
- **URL**: `{{baseUrl}}/api/admin/regions/1`
- **Headers**:
  ```
  Content-Type: application/json
  ```
- **Body** (raw JSON):
  ```json
  {
    "regionName": "Khomas Region"
  }
  ```

---

#### 2.4 Delete Region

**Request:**
- **Method**: `DELETE`
- **URL**: `{{baseUrl}}/api/admin/regions/1`

---

### 3. Vote Categories Management

#### 3.1 Create Vote Category

**Request:**
- **Method**: `POST`
- **URL**: `{{baseUrl}}/api/admin/vote-categories`
- **Headers**:
  ```
  Content-Type: application/json
  ```
- **Body** (raw JSON):
  ```json
  {
    "categoryName": "Presidential Elections"
  }
  ```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "voteCategory": {
    "id": 1,
    "categoryName": "Presidential Elections"
  }
}
```

**Create Multiple Categories:**
- "National Assembly Elections"
- "Regional Council Elections"
- "Local Authority / Municipal Elections"
- "Referendums / National Polls"

---

#### 3.2 Get All Vote Categories

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/admin/vote-categories`

---

#### 3.3 Get Vote Category by ID

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/admin/vote-categories/1`

---

#### 3.4 Update Vote Category

**Request:**
- **Method**: `PUT`
- **URL**: `{{baseUrl}}/api/admin/vote-categories/1`
- **Headers**:
  ```
  Content-Type: application/json
  ```
- **Body** (raw JSON):
  ```json
  {
    "categoryName": "Presidential Elections 2024"
  }
  ```

---

#### 3.5 Delete Vote Category

**Request:**
- **Method**: `DELETE`
- **URL**: `{{baseUrl}}/api/admin/vote-categories/1`

---

### 4. Voter Cards Management

#### 4.1 Create Voter Card

**Request:**
- **Method**: `POST`
- **URL**: `{{baseUrl}}/api/admin/voters-cards`
- **Headers**:
  ```
  Content-Type: application/json
  ```
- **Body** (raw JSON):
  ```json
  {
    "cardNumber": "VC123456789",
    "cardName": "John Doe Voter Card"
  }
  ```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "votersCard": {
    "id": 1,
    "cardNumber": "VC123456789",
    "cardName": "John Doe Voter Card"
  }
}
```

**Create Multiple Voter Cards:**
```json
{
  "cardNumber": "VC987654321",
  "cardName": "Jane Smith Voter Card"
}
```

---

#### 4.2 Get All Voter Cards

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/admin/voters-cards`

---

#### 4.3 Get Voter Card by ID

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/admin/voters-cards/1`

---

#### 4.4 Update Voter Card

**Request:**
- **Method**: `PUT`
- **URL**: `{{baseUrl}}/api/admin/voters-cards/1`
- **Headers**:
  ```
  Content-Type: application/json
  ```
- **Body** (raw JSON):
  ```json
  {
    "cardNumber": "VC123456789",
    "cardName": "John Doe - Updated Voter Card"
  }
  ```

---

#### 4.5 Delete Voter Card

**Request:**
- **Method**: `DELETE`
- **URL**: `{{baseUrl}}/api/admin/voters-cards/1`

---

### 5. Candidates Management (with File Upload)

#### 5.1 Create Candidate with Photo and Logo

**Request:**
- **Method**: `POST`
- **URL**: `{{baseUrl}}/api/admin/candidates`
- **Headers**: 
  ```
  Content-Type: multipart/form-data
  ```
- **Body** (form-data):
  - `fullName`: `John Doe` (Text)
  - `partyName`: `Democratic Party` (Text)
  - `position`: `President` (Text)
  - `voteCategoryId`: `1` (Text)
  - `photo`: [Select File] - Choose a candidate photo (JPG/PNG)
  - `partyLogo`: [Select File] - Choose a party logo (JPG/PNG)

**Postman Steps for File Upload:**
1. Create new request
2. Set method to `POST`
3. Enter URL: `http://localhost:8080/api/admin/candidates`
4. Go to **Body** tab
5. Select **form-data** (NOT raw)
6. Add each field:
   - Click **Key** field, enter `fullName`, set type to **Text**, enter value
   - Click **Key** field, enter `partyName`, set type to **Text**, enter value
   - Click **Key** field, enter `position`, set type to **Text**, enter value
   - Click **Key** field, enter `voteCategoryId`, set type to **Text**, enter `1`
   - Click **Key** field, enter `photo`, change type to **File**, click **Select Files** and choose an image
   - Click **Key** field, enter `partyLogo`, change type to **File**, click **Select Files** and choose an image
7. Click **Send**

**Expected Response (200 OK):**
```json
{
  "success": true,
  "candidate": {
    "id": 1,
    "fullName": "John Doe",
    "partyName": "Democratic Party",
    "position": "President",
    "photoUrl": "http://localhost:8080/api/photos/view/photo_1234567890.jpg",
    "partyLogoUrl": "http://localhost:8080/api/logos/view/logo_1234567890.jpg",
    "voteCategory": {
      "id": 1,
      "categoryName": "Presidential Elections"
    }
  }
}
```

**Note**: Photo and logo are optional. You can create a candidate without them.

---

#### 5.2 Get All Candidates

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/admin/candidates`

**Expected Response (200 OK):**
```json
{
  "success": true,
  "candidates": [
    {
      "id": 1,
      "fullName": "John Doe",
      "partyName": "Democratic Party",
      "position": "President",
      "photoUrl": "http://localhost:8080/api/photos/view/photo_1234567890.jpg",
      "partyLogoUrl": "http://localhost:8080/api/logos/view/logo_1234567890.jpg",
      "voteCategory": {
        "id": 1,
        "categoryName": "Presidential Elections"
      }
    }
  ]
}
```

---

#### 5.3 Get Candidate by ID

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/admin/candidates/1`

---

#### 5.4 Update Candidate

**Request:**
- **Method**: `PUT`
- **URL**: `{{baseUrl}}/api/admin/candidates/1`
- **Headers**: 
  ```
  Content-Type: multipart/form-data
  ```
- **Body** (form-data):
  - `fullName`: `John Doe Updated` (Text) - Optional
  - `partyName`: `New Democratic Party` (Text) - Optional
  - `position`: `President` (Text) - Optional
  - `voteCategoryId`: `1` (Text) - Optional
  - `photo`: [Select File] - Optional, only if updating photo
  - `partyLogo`: [Select File] - Optional, only if updating logo

**Note**: Only include fields you want to update. Files are optional.

---

#### 5.5 Delete Candidate

**Request:**
- **Method**: `DELETE`
- **URL**: `{{baseUrl}}/api/admin/candidates/1`

---

### 6. Voter Endpoints (Public - No Authentication)

#### 6.1 Get Dashboard (Voting Categories)

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/voter/dashboard`
- **Headers**: None required

**Expected Response (200 OK):**
```json
{
  "success": true,
  "categories": [
    {
      "id": 1,
      "categoryName": "Presidential Elections"
    },
    {
      "id": 2,
      "categoryName": "National Assembly Elections"
    }
  ]
}
```

---

#### 6.2 Get Candidates by Category

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/voter/category/1/candidates`
- **Headers**: None required

**Expected Response (200 OK):**
```json
{
  "success": true,
  "candidates": [
    {
      "id": 1,
      "fullName": "John Doe",
      "partyName": "Democratic Party",
      "position": "President",
      "photoUrl": "http://localhost:8080/api/photos/view/photo_1234567890.jpg",
      "partyLogoUrl": "http://localhost:8080/api/logos/view/logo_1234567890.jpg",
      "voteCategory": {
        "id": 1,
        "categoryName": "Presidential Elections"
      }
    }
  ]
}
```

---

#### 6.3 Get All Regions (for Dropdown)

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/voter/regions`
- **Headers**: None required

**Expected Response (200 OK):**
```json
{
  "success": true,
  "regions": [
    {
      "id": 1,
      "regionName": "Khomas"
    },
    {
      "id": 2,
      "regionName": "Erongo"
    }
  ]
}
```

---

#### 6.4 Submit Vote

**Request:**
- **Method**: `POST`
- **URL**: `{{baseUrl}}/api/voter/submit-vote`
- **Headers**:
  ```
  Content-Type: application/json
  ```
- **Body** (raw JSON):
  ```json
  {
    "fullName": "Jane Smith",
    "nationalIdNumber": "1234567890123",
    "dateOfBirth": "1990-01-15",
    "address": "123 Main Street, Windhoek",
    "region": {
      "id": 1
    },
    "phoneNumber": "+264811234567",
    "email": "jane.smith@email.com",
    "votersIdNumber": "VC123456789",
    "candidate": {
      "id": 1
    },
    "voteCategory": {
      "id": 1
    }
  }
  ```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Your vote has been successfully submitted. A confirmation email has been sent to your email address.",
  "vote": {
    "id": 1,
    "fullName": "Jane Smith",
    "nationalIdNumber": "1234567890123",
    "dateOfBirth": "1990-01-15",
    "address": "123 Main Street, Windhoek",
    "email": "jane.smith@email.com",
    "phoneNumber": "+264811234567",
    "votersIdNumber": "VC123456789",
    "voteDate": "2024-11-20"
  }
}
```

**Error Responses:**

**Card Number Doesn't Exist (400 Bad Request):**
```json
{
  "success": false,
  "message": "Card number does not exist"
}
```

**Duplicate Data (400 Bad Request):**
```json
{
  "success": false,
  "message": "Already exist"
}
```

**Note**: The `votersIdNumber` must exist in the Voter Cards database. Make sure you've created the voter card first using the admin endpoint.

---

### 7. View Votes (Admin)

#### 7.1 Get All Votes

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/admin/votes`
- **Headers**: None required

**Expected Response (200 OK):**
```json
{
  "success": true,
  "votes": [
    {
      "id": 1,
      "fullName": "Jane Smith",
      "nationalIdNumber": "1234567890123",
      "email": "jane.smith@email.com",
      "phoneNumber": "+264811234567",
      "votersIdNumber": "VC123456789",
      "voteDate": "2024-11-20",
      "candidate": {
        "id": 1,
        "fullName": "John Doe"
      },
      "voteCategory": {
        "id": 1,
        "categoryName": "Presidential Elections"
      },
      "region": {
        "id": 1,
        "regionName": "Khomas"
      }
    }
  ]
}
```

---

#### 7.2 Get Vote by ID

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/admin/votes/1`

---

#### 7.3 Get Votes by Candidate

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/admin/votes/candidate/1`

**Expected Response (200 OK):**
```json
{
  "success": true,
  "votes": [
    {
      "id": 1,
      "fullName": "Jane Smith",
      "candidate": {
        "id": 1,
        "fullName": "John Doe"
      }
    }
  ]
}
```

---

#### 7.4 Get Votes by Category

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/admin/votes/category/1`

---

### 8. File View Endpoints

#### 8.1 View Candidate Photo

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/photos/view/photo_1234567890.jpg`
- **Headers**: None required

**Expected Response**: Image file displayed in browser/Postman

**Note**: Use the `photoUrl` from the candidate response. Extract the filename from the URL.

---

#### 8.2 View Party Logo

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/logos/view/logo_1234567890.jpg`

---

#### 8.3 Download Candidate Photo

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/photos/download/photo_1234567890.jpg`

---

#### 8.4 Download Party Logo

**Request:**
- **Method**: `GET`
- **URL**: `{{baseUrl}}/api/logos/download/logo_1234567890.jpg`

---

## Complete Testing Sequence

Follow this sequence to test the entire system:

### Step 1: Setup (Admin)
1. ✅ Create Admin User → `POST /api/admin/users`
2. ✅ Login as Admin → `POST /api/admin/login`

### Step 2: Master Data
3. ✅ Create Regions → `POST /api/admin/regions` (create 3-5 regions)
4. ✅ Create Vote Categories → `POST /api/admin/vote-categories` (create 2-3 categories)
5. ✅ Create Voter Cards → `POST /api/admin/voters-cards` (create 5-10 cards)

### Step 3: Candidates
6. ✅ Create Candidates → `POST /api/admin/candidates` (with photos and logos)
7. ✅ Verify Candidates → `GET /api/admin/candidates`

### Step 4: Voter Flow
8. ✅ Get Dashboard → `GET /api/voter/dashboard`
9. ✅ Get Regions → `GET /api/voter/regions`
10. ✅ Get Candidates → `GET /api/voter/category/1/candidates`
11. ✅ Submit Vote → `POST /api/voter/submit-vote`

### Step 5: View Results
12. ✅ Get All Votes → `GET /api/admin/votes`
13. ✅ Get Votes by Candidate → `GET /api/admin/votes/candidate/1`
14. ✅ Get Votes by Category → `GET /api/admin/votes/category/1`

### Step 6: Test Error Cases
15. ✅ Try submitting vote with invalid card number → Should return "Card number does not exist"
16. ✅ Try submitting duplicate vote → Should return "Already exist"
17. ✅ Try submitting with duplicate email → Should return "Already exist"

---

## Postman Collection Import

You can create a Postman Collection with all these requests. Here's a sample collection structure:

```
Namibia Voting System API
├── Admin
│   ├── Users
│   │   ├── Create User
│   │   ├── Login
│   │   ├── Get All Users
│   │   ├── Get User by ID
│   │   ├── Update User
│   │   └── Delete User
│   ├── Regions
│   │   ├── Create Region
│   │   ├── Get All Regions
│   │   ├── Update Region
│   │   └── Delete Region
│   ├── Vote Categories
│   │   ├── Create Category
│   │   ├── Get All Categories
│   │   ├── Update Category
│   │   └── Delete Category
│   ├── Voter Cards
│   │   ├── Create Voter Card
│   │   ├── Get All Cards
│   │   ├── Update Card
│   │   └── Delete Card
│   ├── Candidates
│   │   ├── Create Candidate
│   │   ├── Get All Candidates
│   │   ├── Update Candidate
│   │   └── Delete Candidate
│   └── Votes
│       ├── Get All Votes
│       ├── Get Vote by ID
│       ├── Get Votes by Candidate
│       └── Get Votes by Category
└── Voter (Public)
    ├── Get Dashboard
    ├── Get Regions
    ├── Get Candidates by Category
    └── Submit Vote
```

---

## Common Issues & Solutions

### Issue 1: 404 Not Found
**Problem**: Endpoint not found
**Solution**: 
- Check the URL is correct
- Ensure the application is running on port 8080
- Verify the endpoint path matches exactly

### Issue 2: File Upload Not Working
**Problem**: Photo/logo not uploading
**Solution**:
- Make sure you're using `form-data` (not `raw`)
- Set file fields to type `File` (not `Text`)
- Check file size is under 20MB
- Verify file is an image (JPG, PNG)

### Issue 3: "Card number does not exist"
**Problem**: Vote submission fails
**Solution**:
- First create the voter card using `POST /api/admin/voters-cards`
- Use the exact `cardNumber` when submitting vote

### Issue 4: "Already exist" Error
**Problem**: Duplicate data
**Solution**:
- National ID, Email, Phone Number, and Voter Card Number must be unique
- Use different values for testing multiple votes

### Issue 5: CORS Error
**Problem**: Cross-origin request blocked
**Solution**:
- CORS is enabled in the application
- If issues persist, check browser console for specific error

---

## Usage Examples

### 1. Create Admin User

```bash
curl -X POST http://localhost:8080/api/admin/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@votenam.gov.na",
    "password": "admin123",
    "username": "admin",
    "role": "ADMIN"
  }'
```

### 2. Login as Admin

```bash
curl -X POST http://localhost:8080/api/admin/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@votenam.gov.na",
    "password": "admin123"
  }'
```

### 3. Create Vote Category

```bash
curl -X POST http://localhost:8080/api/admin/vote-categories \
  -H "Content-Type: application/json" \
  -d '{
    "categoryName": "Presidential Elections"
  }'
```

### 4. Create Region

```bash
curl -X POST http://localhost:8080/api/admin/regions \
  -H "Content-Type: application/json" \
  -d '{
    "regionName": "Khomas"
  }'
```

### 5. Register Voter Card

```bash
curl -X POST http://localhost:8080/api/admin/voters-cards \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "VC123456789",
    "cardName": "John Doe Voter Card"
  }'
```

### 6. Create Candidate (with file upload)

Using Postman or similar tool with `multipart/form-data`:

- `fullName`: "John Doe"
- `partyName`: "Democratic Party"
- `position`: "President"
- `voteCategoryId`: 1
- `photo`: [Select file]
- `partyLogo`: [Select file]

### 7. Submit Vote

```bash
curl -X POST http://localhost:8080/api/voter/submit-vote \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Jane Smith",
    "nationalIdNumber": "1234567890123",
    "dateOfBirth": "1990-01-15",
    "address": "123 Main Street, Windhoek",
    "region": {"id": 1},
    "phoneNumber": "+264811234567",
    "email": "jane.smith@email.com",
    "votersIdNumber": "VC123456789",
    "candidate": {"id": 1},
    "voteCategory": {"id": 1}
  }'
```

## File Upload

### Upload Directories

Files are stored in:
- **Candidate Photos**: `uploads/photos/`
- **Party Logos**: `uploads/partylogo/`

These directories are created automatically on first upload.

### File Naming

Uploaded files are automatically renamed with timestamps to prevent conflicts:
- Original: `photo.jpg`
- Stored: `photo_1234567890.jpg`

### File Access

Uploaded files are accessible via:
- Photos: `http://localhost:8080/api/photos/view/{fileName}`
- Logos: `http://localhost:8080/api/logos/view/{fileName}`

## Email Configuration

The system sends HTML-formatted confirmation emails to voters after successful vote submission. The email includes:

- Company logo/branding
- Personalized greeting
- Vote details (category and candidate)
- Professional footer with contact information

### Email Template Features

- Responsive HTML design
- Gradient header with branding
- Vote confirmation details
- Professional styling
- Footer with contact information

## Security Considerations

⚠️ **Important Security Notes:**

1. **Password Storage**: Currently, passwords are stored in plain text. For production, implement password hashing (BCrypt recommended).

2. **Authentication**: The current login system is basic. Consider implementing:
   - JWT tokens for session management
   - Spring Security for enhanced security
   - Role-based access control (RBAC)

3. **API Security**: Consider adding:
   - Rate limiting
   - API key authentication
   - HTTPS/SSL encryption

4. **Data Validation**: Always validate input on both client and server side.

5. **Email Credentials**: Never commit email passwords to version control. Use environment variables or secure configuration management.

## Project Structure

```
src/main/java/com/example/votenam/
├── controllers/
│   ├── AdminController.java
│   ├── CandidatesController.java
│   ├── FileViewController.java
│   ├── RegionsController.java
│   ├── VoteCategoryController.java
│   ├── VoterController.java
│   ├── VotersCardController.java
│   └── VotesController.java
├── models/
│   ├── Candidates.java
│   ├── Regions.java
│   ├── Users.java
│   ├── VoteCategory.java
│   ├── VotersCard.java
│   └── VotersDetails.java
├── repositories/
│   ├── CandidatesRepository.java
│   ├── RegionsRepository.java
│   ├── UsersRepository.java
│   ├── VoteCategoryRepository.java
│   ├── VotersCardRepository.java
│   └── VotersDetailsRepository.java
└── services/
    ├── CandidatesService.java
    ├── EmailService.java
    ├── FileUploadService.java
    ├── RegionsService.java
    ├── UsersService.java
    ├── VoteCategoryService.java
    ├── VotersCardService.java
    └── VotersDetailsService.java
```

## Validation Rules

### Vote Submission Validation

1. **National ID Number**: Must be unique (one vote per National ID)
2. **Email**: Must be unique (one vote per email)
3. **Phone Number**: Must be unique (one vote per phone number)
4. **Voter Card Number**: 
   - Must exist in the VotersCard database
   - Must be unique (one vote per card)

### Error Messages

The system returns descriptive error messages for validation failures:
- **Card Number Not Found**: "Card number does not exist"
- **Duplicate Data** (National ID, Email, Phone Number, Voter Card Number): "Already exist"

## Troubleshooting

### Database Connection Issues

If you encounter database connection errors:
1. Verify PostgreSQL is running
2. Check database credentials in `application.properties`
3. Ensure the database exists: `CREATE DATABASE votenam_db;`

### Email Not Sending

If emails are not being sent:
1. Verify Gmail credentials in `application.properties`
2. For Gmail, ensure you're using an App Password, not your regular password
3. Check that "Less secure app access" is enabled (or use App Password)
4. Verify SMTP settings are correct

### File Upload Issues

If file uploads fail:
1. Check that upload directories have write permissions
2. Verify file size is within limits (20MB default)
3. Ensure file types are images (jpg, png, etc.)

## Contributing

This is a voting system for Namibia. When contributing:
- Follow Java coding conventions
- Write clear commit messages
- Test all endpoints before submitting
- Ensure data validation is in place

## License

[Specify your license here]

## Contact

For support or questions:
- Email: support@votenam.gov.na
- Phone: +264 61 XXX XXXX

---

**Namibia Voting System** - Strengthening Democracy Through Technology 🇳🇦

