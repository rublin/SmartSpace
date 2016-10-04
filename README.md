#  Smart Space
### Version is *1.5-SNAPSHOT*

SmartSpace is a Java Enterprise Web Application for manage your smart home or other IoT staff.
![SmartSpace logo](https://github.com/rublin/SmartSpace/Banner11.png)

### Features:

* simple WEB interface ([demo](http://free.rublin.org:8080/smartSpace/))
* authorization and role based authentication
* zones (parts of controlled object) include triggers and cameras
* triggers generate events
* [Telegram Bot API](https://github.com/rubenlagus/TelegramBots) used to manage system
* include REST API to communicate with client (Frontend)

### Telegram Bot API features

* take photo from camera
* show info from zones
* arming/disarming zones
* receiving notifications - *will be added in next release*
![Telegram Bot](https://github.com/rublin/SmartSpace/TelegramBot.png)

# Installation

### Prepare your database - [postgresql.properties](https://github.com/rublin/SmartSpace/resources/db/postgresql.properties):

```
 database.username=<login for DB server>
 database.password=<password for DB server>
 database.driverClassName=<driver for DB server>
 database.init=true
 jdbc.initLocation=initDB.sql
 jpa.showSql=false
 hibernate.format_sql=true
 hibernate.use_sql_comments=true
 database.url=<URL for DB server>
```
### Prepare notification properties - [mail.properties](https://github.com/rublin/SmartSpace/resources/notification/mail.properties):
```
mail.password=<password for mail server>
mail.smtp=<SMTP for mail server>
mail.from=<EMAIL FROM for mail server>
mail.login=<login for mail server>
telegram.bot.username=<username for telegram bot>
telegram.bot.token=<token for telegram bot>
```
