# REST API
The REST APIs provide programmatic access to read and write SmartSpace data. You can manage system as ADMIN and read event data as USER. The REST API identifies users using basic authentication; responses are available in JSON.
# Overview
SmartSpace model level contains:

- **Zone** is a part of controlled object (like __*Home floor 1*__ or __*Garage*__). Zone contains Triggers and Cameras
- **Trigger** is a sensor for incoming data (like __*temperature*__ or __*move sensors*__). Trigger can be DIGITAL (has BOOLEAN state) and ANALOG (has DOUBLE state)
- **Camera** is a ip camera (Hikvision in my example). It uses for notification
- **Event** is a message from triggers. Event also can be DIGITAL (from DIGITAL trigger) or ANALOG (from ANALOG trigger). Event generates by Arduino, using HTTP GET request.
- **User** has ROLE_ADMIN or ROLE_USER. User has mobile, email and telegram for manage and notifications.

## GET /rest/zone/

Returns a collection of Zones.

#### Example result

```json
[
  {
    "id": 10,
    "name": "Home",
    "shortName": "h1",
    "status": "GREEN",
    "secure": false
  }
]
```

# Latest Updates

Added REST API methods
