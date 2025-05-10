# 🧪 RESTful Booking System – Postman API Test Suite

This repository contains a Postman **collection** and **environment configuration** for testing CRUD operations on the [Automation in Testing](https://automationintesting.online) RESTful Booking System.

---
 📂 Files Included

- **`Restful booker RestfulBookerCollection.json`**  
  Full Postman test collection covering:
  - User booking operations
  - Room creation, update, deletion
  - Admin message and report access
  - Token-based authentication

- **`RestfulBookerenvironment.json`**  
  Environment file containing:
  - Base URL (`url`)  
  - Token variable (`token`) – to be updated after login

---

 🚀 How to Use

### 1. Import into Postman

- Open Postman
- Click `Import` → Upload both `.json` files:
  - Collection: `Restful booker RestfulBookercollection.json`
  - Environment: `RestfulBookernvironment.json`

---
 2. Configure Environment

- Select `URL_Test` from the top-right environment dropdown
- Authenticate:
  - Use the **`POST /auth/login`** request to get a token
  - Copy the token and paste it into the `token` variable in your environment
- Make sure `{{url}}` is set to:  
  `https://automationintesting.online/api/`

---

 3. Run Collection or Individual Requests

- You can test the following operations:

 📥 **GET**
- Retrieve all or specific rooms
- Fetch all bookings or reports
- Read messages (admin)

 ➕ **POST**
- Book a room
- Send a contact message
- Authenticate (login)
- Create a new room (admin)

 ✏️ **PUT**
- Update room details
- Update booking information

❌ **DELETE**
- Delete a booking, room, or message (admin access required)

---
 ⚠️ Constraints & Notes

- Most admin routes require a valid **authentication token** in `Cookie` header
- Invalid tokens or missing fields return `401` or `400` responses
- Some data (like room or booking IDs) must be dynamically created beforehand
- Use `Collection Runner` or `Newman` for batch test execution

---
✅ Status Code Validation

Tests include checks for:
- `200 OK` – Successful operations
- `201 Created` – For POSTs
- `400 Bad Request` – Invalid input
- `401 Unauthorized` – Missing or invalid token
- `404 Not Found` – Invalid resource ID

---

📄 Author

Created for functional API testing and automation learning using Postman.

---

> 💡 Need help running this collection via command line using Newman? Let me know!
