#include<ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <OneWire.h>
#include <DallasTemperature.h>

#define ONE_WIRE_BUS 12 //D6

const char* ssid = ""; //your WiFi Name
const char* password = "";  //Your Wifi Password
int relayPin = 14; // D5
int waterPin = 16; // D0
WiFiServer server(80);
const char* backEnd = "http://192.168.0.??:8080";
bool globalOn = false;
bool currentState = false;
const int workMax = 60000;
const int waitMax = 300000;
int millOn = 0;
int millOff = 0;

String getTemperature = "/smartSpace/events/temperature/add?triggerId=%d&state=%s";
String event="/smartSpace/events/add?triggerId=%d&state=%s";

const int boilerWaterId = 120;
const int tempUpId = 117;
const int tempDownId = 118;

float tempUp = 0.0;
float tempDown = 0.0;
int boilerWaterState = 0;

char buf[80];

OneWire oneWire(ONE_WIRE_BUS);
// Pass our oneWire reference to Dallas Temperature.
DallasTemperature sensors(&oneWire);

void setup() {
  Serial.begin(115200);
  delay(10);

  pinMode(relayPin, OUTPUT);
  pinMode(waterPin, INPUT);
  digitalWrite(relayPin, HIGH);
  sensors.begin();

  // Connect to WiFi network
  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);
  IPAddress ip(192, 168, 0, 50);
  IPAddress gateway(192, 168, 0, 254);
  IPAddress subnet(255, 255, 255, 0);
  WiFi.config(ip, gateway, subnet);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");

  // Start the server
  server.begin();
  Serial.println("Server started");

  // Print the IP address
  Serial.print("Use this URL to connect: ");
  Serial.print("http://");
  Serial.print(WiFi.localIP());
  Serial.println("/");

}

void loop() {
  delay(500);
  calculateTime();
  checkTemperature();
  checkWater();

  // Check if a client has connected
  WiFiClient client = server.available();
  if (!client) {
    return;
  }

  // Wait until the client sends some data
  Serial.println("new client");
  while (!client.available()) {
    Serial.println("!!!!!!!!!!!!");
    delay(1);
  }

  // Read the first line of the request
  String request = client.readStringUntil('\r');
  Serial.println(request);
  client.flush();

  // Match the request

  int value = LOW;
  if (request.indexOf("/relay=ON") != -1)  {
    turnOn();
    globalOn = true;
  }
  if (request.indexOf("/relay=OFF") != -1)  {
    turnOff();
    globalOn = false;
  }

  // Set ledPin according to the request
  //digitalWrite(ledPin, value);

  // Return the response
  client.println("HTTP/1.1 200 OK");
  client.println("Content-Type: text/html");
  client.println(""); //  do not forget this one
  client.println("<!DOCTYPE HTML>");
  client.println("<html>");

  client.print("Relay is now: ");

  if (currentState) {
    client.print("On");
    client.println("<br><br>");
    client.println("<a href=\"/relay=OFF\"\"><button>Turn Off </button></a><br />");
  } else {
    client.print("Off");
    client.println("<br><br>");
    client.println("<a href=\"/relay=ON\"\"><button>Turn On </button></a>");
  }
  client.println("</html>");

  delay(1);
  Serial.println("Client disonnected");
  Serial.println("");
}

void checkWater() {
  int digital = digitalRead(waterPin);
  if (digital != boilerWaterState) {
    boilerWaterState = digital;
    String state = "";
    if (boilerWaterState == 0) {
      state = "false";
    } else {
      state = "true";
    }
    Serial.println("Water leak alarm is " + state);
    sprintf(buf, event.c_str(), boilerWaterId, state.c_str());
    sendUpdate();
  }
}
void checkTemperature() {
  sensors.requestTemperatures();
  float currentUp = sensors.getTempCByIndex(0);
  float currentDown = sensors.getTempCByIndex(1);
  if (abs(currentUp - tempUp) > 0.5) {
    tempUp = currentUp;
    sprintf(buf, getTemperature.c_str(), tempUpId, String(tempUp, 1).c_str());
    sendUpdate();
    //  Serial.print("Temperature for the device 1 (index 0) is: ");
    //  Serial.println(sensors.getTempCByIndex(0));
  }
  if (abs(currentDown - tempDown) > 0.5) {
    tempDown = currentDown;
    sprintf(buf, getTemperature.c_str(), tempDownId, String(tempDown, 1).c_str());
    sendUpdate();
    //  Serial.print("Temperature for the device 2 (index 1) is: ");
    //  Serial.println(sensors.getTempCByIndex(1));
  }
}

void turnOn() {
  Serial.println("Need to turn it off");
  digitalWrite(relayPin, LOW);
  millOn = 0;
  currentState = true;
}
void turnOff() {
  Serial.println("Need to turn it on");
  digitalWrite(relayPin, HIGH);
  millOff = 0;
  currentState = false;
}
void calculateTime() {
  if (globalOn) {
    if (currentState) {
      millOn += 500;
      if (millOn >= workMax) {
        turnOff();
      }
    } else {
      millOff += 500;
      if (millOff >= waitMax) {
        turnOn();
      }
    }
  }
}
void sendUpdate() {
  String request = backEnd + String(buf);
  Serial.println(request);
  HTTPClient http;
  http.begin(request);
  int httpCode = http.GET();
  if (httpCode > 0) {
    String payload = http.getString();
    Serial.println(payload);
  }
  http.end();
}

