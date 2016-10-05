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

## GET /rest/zone/:id

Returns a single Zone, specified by the id parameter.

#### Example result

```json
{
  "id": 10,
  "name": "Home",
  "shortName": "h1",
  "status": "GREEN",
  "secure": false
}
```

# Zone

## POST /rest/zone/admin/

Creates a new Zone, returns the created zone if successful. 
You need to specify these fields:
* **name** - string, user friendly name
* **shortName** - string, short name
* **status** - enum(GREEN, YELLOW, RED), is this Zone status

Required JSON in the body:

```json
{
  "name": "Test Zone",
  "shortName": "tz",
  "status": "GREEN"
}
```

#### Example result

```json
{
  "id": 11,
  "name": "Test Zone",
  "shortName": "tz",
  "status": "GREEN",
  "secure": false
}
```

## PUT /rest/zone/admin/:id

Updates the Zone specified by the required ID parameter. Required JSON in the body:

```json
{
  "name": "Home Home",
  "shortName": "hh",
  "status": "YELLOW"
}
```

## PUT /rest/zone/:id/secure/:secure_status

Arming (if *secure_status* is TRUE) or (if *secure_status* is TRUE) disarming Zone by the required ID parameter.

## DEL /rest/zone/admin/:id

Deletes the Zone specified by the required ID parameter.

# Trigger

## GET /rest/trigger/

Returns a collection of Triggers.

#### Example result

```json
[
  {
    "id": 103,
    "zone": {
      "id": 10,
      "name": "Home",
      "shortName": "h1",
      "status": "GREEN",
      "secure": false
    },
    "name": "Door 1 floor",
    "type": "DIGITAL",
    "secure": true,
    "state": true,
    "minThreshold": 0,
    "maxThreshold": 0,
    "new": false
  },
  {
    "id": 104,
    "zone": {
      "id": 10,
      "name": "Home",
      "shortName": "h1",
      "status": "GREEN",
      "secure": false
    },
    "name": "Temperature 2 floor",
    "type": "ANALOG",
    "secure": false,
    "state": true,
    "minThreshold": 15,
    "maxThreshold": 25,
    "new": false
  }
]
```

## GET /rest/trigger/:id

Returns a single Trigger, specified by the id parameter.

#### Example result

```json
{
  "id": 104,
  "zone": {
    "id": 10,
    "name": "Home",
    "shortName": "h1",
    "status": "GREEN",
    "secure": false
  },
  "name": "Temperature 2 floor",
  "type": "ANALOG",
  "secure": false,
  "state": true,
  "minThreshold": 15,
  "maxThreshold": 25,
  "new": false
}
```

## GET /rest/trigger/state/:state

Returns a collection of Triggers matching a specified (TRUE or FALSE)

## POST /rest/trigger/admin/

Creates a new Trigger, returns the created trigger if successful.
 You need to specify these fields:
* **zone** - int, specify only the id of zone 
* **name** - string, user friendly name
* **type** - enum(ANALOG, DIGITAL), specified type of trigger
* **secure** - boolean, is this trigger for security (security triggers generate alarms only when zone has secure=true)
* **state** - boolean, current state of trigger (true by default)
* **minThreshold** - double, for ANALOG triggers, the lowest possible value without alarm
* **maxThreshold** - double, for ANALOG triggers, the biggest possible value without alarm

Required JSON in the body:

```json
{
  "zone": {
    "id": 10
  },
  "name": "door 3",
  "type": "DIGITAL",
  "secure": true,
  "state": true,
  "minThreshold": 0,
  "maxThreshold": 0
}
```

#### Example result

```json
{
  "id": 106,
  "zone": {
    "id": 10,
    "name": null,
    "shortName": null,
    "status": null,
    "secure": false
  },
  "name": "door 3",
  "type": "DIGITAL",
  "secure": true,
  "state": true,
  "minThreshold": 0,
  "maxThreshold": 0,
  "new": false
}
```

## PUT /rest/trigger/admin/:id

Updates the Trigger specified by the required ID parameter. Required JSON in the body:

```json
{
  "zone": {
    "id": 10,
    "name": "Home",
    "shortName": "h1",
    "status": "GREEN",
    "secure": false
  },
  "name": "Temperature 2 ",
  "type": "ANALOG",
  "secure": false,
  "state": true,
  "minThreshold": 15,
  "maxThreshold": 25,
  "new": false
}
```

## DEL /rest/trigger/admin/:id

Deletes the Trigger specified by the required ID parameter.

# Event

System receives Events from another device (Arduino), so user has no needs to create or update events.

## GET /rest/event/

Returns a collection of Events.

#### Example result

```json
[
  {
    "id": 1000,
    "time": [
      2016,
      6,
      20,
      11,
      0
    ],
    "trigger": {
      "id": 103,
      "zone": {
        "id": 10,
        "name": "Home",
        "shortName": "h1",
        "status": "GREEN",
        "secure": false
      },
      "name": "Door 1 floor",
      "type": "DIGITAL",
      "secure": true,
      "state": true,
      "minThreshold": 0,
      "maxThreshold": 0,
      "new": false
    },
    "type": "DIGITAL",
    "alarm": true,
    "state": true,
    "digital": true,
    "new": false
  },
  {
    "id": 1001,
    "time": [
      2016,
      5,
      30,
      11,
      0
    ],
    "trigger": {
      "id": 103,
      "zone": {
        "id": 10,
        "name": "Home",
        "shortName": "h1",
        "status": "GREEN",
        "secure": false
      },
      "name": "Door 1 floor",
      "type": "DIGITAL",
      "secure": true,
      "state": true,
      "minThreshold": 0,
      "maxThreshold": 0,
      "new": false
    },
    "type": "DIGITAL",
    "alarm": true,
    "state": false,
    "digital": true,
    "new": false
  }
]
```

## GET /rest/event/:id

Returns a collection of Events, specified by the Trigger's id parameter.

## GET /rest/event/between?from=2016-05-30T13:00&to=2016-05-31T13:00

Returns a collection of Events, specified by the LocalDateTime __**from**__ and __**to**__ parameters.

## GET /rest/event/alarmed

Returns a collection of alarmed Events

# Camera

## GET /rest/camera/

Returns a collection of Cameras.

#### Example result

```json
[
  {
    "id": 102,
    "name": "Cam 1 floor 1",
    "login": "image",
    "password": "123",
    "ip": "192.168.0.31",
    "zone": {
      "id": 10,
      "name": "Home",
      "shortName": "h1",
      "status": "GREEN",
      "secure": false
    },
    "url": "http://192.168.0.31/Streaming/channels/1/picture",
    "new": false
  }
]
```

## GET /rest/camera/:id

Returns a single Camera, specified by the id parameter.

#### Example result

```json
{
    "id": 102,
    "name": "Cam 1 floor 1",
    "login": "image",
    "password": "123",
    "ip": "192.168.0.31",
    "zone": {
      "id": 10,
      "name": "Home",
      "shortName": "h1",
      "status": "GREEN",
      "secure": false
    },
    "url": "http://192.168.0.31/Streaming/channels/1/picture",
    "new": false
}
```

## POST /rest/camera/admin/

Creates a new Camera, returns the created camera if successful.
 You need to specify these fields:
* **zone** - int, specify only the id of zone 
* **name** - string, user friendly name
* **login** - string, user name for this camera
* **password** - string, password for this login
* **ip** - string, IP address for this camera
* **url** - string, URL addres with current picture from the camera 

Required JSON in the body:

```json
{
  "name": "Cam 2 floor 1",
  "login": "image",
  "password": "123",
  "ip": "192.168.0.32",
  "zone": {
    "id": 10
  },
  "url": "http://192.168.0.32/Streaming/channels/1/picture"
}
```

#### Example result

```json
{
  "id": 107,
  "name": "Cam 2 floor 1",
  "login": "image",
  "password": "123",
  "ip": "192.168.0.32",
  "zone": {
    "id": 10,
    "name": null,
    "shortName": null,
    "status": null,
    "secure": false
  },
  "url": "http://192.168.0.32/Streaming/channels/1/picture",
  "new": false
}
```

## PUT /rest/camera/admin/:id

Updates the trigger specified by the required ID parameter. Required JSON in the body:

```json
{
  "id": 107,
  "name": "Cam 2 floor",
  "login": "image",
  "password": "123",
  "ip": "192.168.0.32",
  "zone": {
    "id": 10
  },
  "url": "http://192.168.0.32/Streaming/channels/1/picture"
}
```

## DEL /rest/camera/admin/:id

Deletes the trigger specified by the required ID parameter.

# User

## GET /rest/admin/user/

Returns a collection of Users.

#### Example result

```json
[
  {
    "id": 101,
    "firstName": "Ivan",
    "lastName": "Mazepa",
    "email": "admin@gmail.com",
    "password": "P@ssw0rd",
    "telegramId": 241931659,
    "telegramName": "rublinua",
    "mobile": "+380951234567",
    "roles": [
      "ROLE_ADMIN"
    ],
    "enabled": true,
    "registered": 1475590946491,
    "new": false
  },
  {
    "id": 100,
    "firstName": "Bohdan",
    "lastName": "Khmelnytsky",
    "email": "user@gmail.com",
    "password": "P@ssw0rd",
    "telegramId": null,
    "telegramName": "Helenko",
    "mobile": "+380957654321",
    "roles": [
      "ROLE_USER"
    ],
    "enabled": true,
    "registered": 1475590946483,
    "new": false
  }
]
```

## GET /rest/admin/user/:id

Returns a single User, specified by the id parameter.

#### Example result

```json
{
  "id": 101,
  "firstName": "Ivan",
  "lastName": "Mazepa",
  "email": "admin@gmail.com",
  "password": "P@ssw0rd",
  "telegramId": 241931659,
  "telegramName": "rublinua",
  "mobile": "+380951234567",
  "roles": [
    "ROLE_ADMIN"
  ],
  "enabled": true,
  "registered": 1475590946491,
  "new": false
}
```

## POST /rest/admin/user/admin/

Creates a new User, returns the created trigger if successful.
 You need to specify these fields:
* **firstName** - string, first name
* **lastName** - string, last name
* **email** - string, email (used as login and email notifications)
* **password** - string, password for web login
* **telegramName** - string, telegram user name (used for telegram authentications, manage and notifications) 
* **mobile** - string, mobile number (used for sms notifications)
* **roles** - collection, ROLE_ADMIN for admin and ROLE_USER for user
* **enabled** - boolean, for enabling and disabling users

Required JSON in the body:

```json
{
  "firstName": "Ivan3",
  "lastName": "Mazepa3",
  "email": "admin3@gmail.com",
  "password": "P@ssw0rd",
  "telegramName": "ua",
  "mobile": "+38095124567",
  "roles": [
    "ROLE_ADMIN"
  ],
  "enabled": true
}
```

#### Example result

```json
{
  "id": 109,
  "firstName": "Ivan3",
  "lastName": "Mazepa3",
  "email": "admin3@gmail.com",
  "password": "$2a$10$KyiyofZqM9VymeSgvMQB5e.DASDbgYQGvKJfI32PvKOC/TNs5jY8C",
  "telegramId": null,
  "telegramName": "ua",
  "mobile": "+38095124567",
  "roles": [
    "ROLE_ADMIN"
  ],
  "enabled": true,
  "registered": 1475663391721,
  "new": false
}
```

## PUT /rest/admin/user/admin/:id

Updates the User specified by the required ID parameter. Required JSON in the body:

```json
{
  "id": 100,
  "firstName": "Ivan2",
  "lastName": "Mazepa2",
  "email": "admin2@gmail.com",
  "password": "P@ssw0rd",
  "telegramId": 2431659,
  "telegramName": "rubl2inua",
  "mobile": "+380951234562",
  "roles": [
    "ROLE_ADMIN"
  ],
  "enabled": true
}
```

## PUT /rest/admin/user/admin/enable/:id

Enable or disable User specified by the required ID parameter.

## DEL /rest/admin/user/admin/:id

Deletes the User specified by the required ID parameter.

# Latest Updates

Added REST API methods
