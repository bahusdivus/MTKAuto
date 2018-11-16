package ru.bahusdivus.mtkauto;

/**
 * This class contain car number plate. Only data and getters/setters
 */
class Car {
  private int id = 0;
  private String number;
  
  public Car(int id, String number) {
    this.id = id;
    this.number = number;
  }
  
  public Car(String number) {
    this.number = number;
  }
  
  public int getId() {
    return id;
  }
  
  public String getNumber() {
    return number;
  }
  
  public void setNumber(String number) {
    this.number = number;
  }
  
  @Override
  public String toString() {
    return String.format("ID: %s | Номер: %s", this.id, this.number);
  }
}

