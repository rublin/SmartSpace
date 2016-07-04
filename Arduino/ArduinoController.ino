/**
* Controller for triggers (SmartSpace http://github.com/rublin/SmartSpace/)
* Platform: Arduino UNO R3 + EthernetShield W5100
* IDE: Arduino 1.6.6
* 
* My triggers:
* Digital 4 - 
* Digital 5 - move 1 floor id=1000
* Digital 6 - 
* Digital 7 - 
* Digital 8 - 
*  
*
*
**/
#include <Ethernet.h>
#include <SPI.h>
#include <math.h>

// Arduino MAC address
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
byte ip[] = { 192, 168, 0, 20 };
byte subnet[] = { 255, 255, 255, 0 };
byte gateway[] = { 192, 168, 0, 1 };
byte dns_server[] = { 192, 168, 0, 1 };
// Server ip address
byte server[] = { 192, 168, 0, 10 };

EthernetClient rclient;

//constants
const long MINUTE=60000;
const long TWO_MINUTES=120000;

// initial values
float tempC;
int old_door1_2=0;
int oldMovement1=0;
long oldMoveTime1=0;
int old_movement_2=0;
char buf[80];
char ipbuff[16];
String get="GET /smartSpace/states?action=addEvent&triggerId=%d&state=%s";

// ------------------------------------------------------------------------------------------------

// send HTTP get request
void sendHTTPRequest() {
  Serial.println(buf); 
  //Serial.println(server);
  if (rclient.connect(server, 8080)) { 
   Serial.println("OK"); 
   rclient.print(buf);
   rclient.println(" HTTP/1.0");
   rclient.print("Host: ");
   sprintf(ipbuff, "%u.%u.%u.%u", ip[0], ip[1], ip[2], ip[3]);
   rclient.println(ipbuff); // controller ip in text mode
   rclient.print("Content-Type: text/html\n");
   rclient.println("Connection: close\n");
   delay(2000);
   rclient.stop();
  } else {
   Serial.println("FAILED");     
  }
}

boolean isTimeOver(long oldTime, long newTime, long timeOut) {

  return newTime-oldTime>timeOut;
}

// ------------------------------------------------------------------------------------------------
void setup()
{
  // Print to console
  Serial.begin(9600);
  Serial.println("Start");

 Ethernet.begin(mac, ip, dns_server, gateway, subnet); // Ethernet Shield

 pinMode(3, INPUT); // door1.2
 old_door1_2=digitalRead(3);

 pinMode(5, INPUT); // move1
 oldMovement1=digitalRead(5);

}

// ------------------------------------------------------------------------------------------------
void loop()
{  
  int valid_sensor=0;

  delay(1000); // 1 sec delay 

  //MOVEMENT 1 SENSOR
   //Serial.println("M2");
   int curMovement1=digitalRead(5);
   long curMoveTime1=millis();

   if (curMovement1==0 && oldMovement1!=curMovement1) {
    oldMovement1=curMovement1;
    oldMoveTime1=curMoveTime1;
    sprintf(buf, get.c_str(), 1000, curMovement1==0 ? "false" : "true");
    sendHTTPRequest();
   } else if (curMovement1==1 && oldMovement1!=curMovement1 && curMoveTime1-oldMoveTime1>TWO_MINUTES) {
    oldMovement1=curMovement1;
    oldMoveTime1=curMoveTime1;
    sprintf(buf, get.c_str(), 1000, curMovement1==0 ? "false" : "true");
    sendHTTPRequest();
   }
}
