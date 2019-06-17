/**
 * 
 * Made by Luka Vinkešević in 2019. for University of Zagreb
 * Faculty of Electrical Engineering and Computing
 * 
**/

#define BAUD_RATE 9600
#define TIMEOUT 100
#define ANALOGS 6 // Number of analog pins
#define PINS 12   // Number of digital pins

bool previous[PINS];
bool inputsDigital[PINS];
bool inputsAnalog[ANALOGS];

int analogs[] = {3, 5, 6, 9, 10, 11}; // PWM pins as analog outputs
int OFFSET = PINS + 2;

void setup() {
  Serial.setTimeout(TIMEOUT);
  Serial.begin(BAUD_RATE);

  for(int i = 0; i < PINS; i++) {
    if(i < ANALOGS) inputsAnalog[i] = false;

    inputsDigital[i] = false;
    previous[i] = false;
  }
}

/*
 * Reading format: Operation | Pin ID | Value (only for operation A)
 * 
 * Operation list:
 *  0 - sets the pin to low state
 *  1 - sets the pin to high state
 *  O - uses the pin as output
 *  I - uses the pin as input
 *  A - sets the analog pin to given value
 * 
 * Examples:
 *  I02, O13, 103, 014, A10218
 * 
 */
void readInputs() {
  if(Serial.available() > 0) {
    String input = Serial.readString();

    int id = input.substring(1, 3).toInt();
    boolean isAnalog = false;

    if(id > 13) {
      id += 2;
      
      isAnalog = true;
    } else if(id < 2) {
      id += OFFSET;

      isAnalog = true;
    }
    
    if(input.startsWith("I")) {
      pinMode(id, INPUT);

      if(isAnalog) inputsAnalog[id - OFFSET] = true;
      else inputsDigital[id] = true;
    } else if(input.startsWith("O")) {
      pinMode(id, OUTPUT);

      if(isAnalog) inputsAnalog[id - OFFSET] = false;
      else inputsDigital[id] = false;
    } else if(input.startsWith("1")) {
        pinMode(id, OUTPUT);
        digitalWrite(id, HIGH);
    } else if(input.startsWith("0")) {
        pinMode(id, OUTPUT);
        digitalWrite(id, LOW);
    } else if(input.startsWith("A")) {
      int value = input.substring(2).toInt();

      pinMode(analogs[id - OFFSET], OUTPUT);
      analogWrite(analogs[id - OFFSET], value);
    }
  }
}

/*
 * Writing format: Operation | Pin ID | Value (only for operation A)
 * 
 * Operation list:
 *  0 - sends low as the pin's state
 *  1 - sends high as the pin's state
 *  A - sends the analog input pin's value
 * 
 * Examples:
 *  111, 004, A01023
 * 
 */
void writeOutputs() {
  for(int i = 0; i < PINS; i++) {
    if(i < ANALOGS && inputsAnalog[i]) {
      int current = analogRead(i + OFFSET);
      
      Serial.print("A");
      Serial.print(i);
      Serial.println(current);

      delay(20);
    }
    
    if(inputsDigital[i]) {
      int id = i + 2;
      bool current = digitalRead(id);
    
      if(current != previous[i]) {
        Serial.print(current ? "1" : "0");
        Serial.println(id);
      
        previous[i] = current;
      }
    }
  }
}

void loop() {
  readInputs();
  writeOutputs();
}
