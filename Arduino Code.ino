#define BAUD_RATE 9600
#define TIMEOUT 100
#define ANALOGS 6
#define PINS 12

bool previous[PINS];
bool inputsDigital[PINS];
bool inputsAnalog[ANALOGS];

int analogs[] = {3, 5, 6, 9, 10, 11};
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
