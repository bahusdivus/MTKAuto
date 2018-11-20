package ru.bahusdivus.mtkauto;

import java.text.*;
import java.util.Date;

/**
 * This class contain entry about part replacement. Only data and getters/setters
 */
class Part {
  private int id = 0;
  private int carId;
  private int type;
  private String name;
  private int value;
  private float price;
  private String dealer;
  private Date date;
  private int odometr;
  
  public Part() {};
  
  public Part (int id, int carId, int type, String name, int value, float price, String dealer, Date date, int odometr) {
    this.id = id;
    this.carId = carId;
    this.type = type;
    this.name = name;
    this.value = value;
    this.price = price;
    this.dealer = dealer;
    this.date = date;
    this.odometr = odometr;
  }
  
  public Part (int carId, int type, String name, int value, float price, String dealer, Date date, int odometr) {
    this.carId = carId;
    this.type = type;
    this.name = name;
    this.value = value;
    this.price = price;
    this.dealer = dealer;
    this.date = date;
    this.odometr = odometr;
  }
  
  public int getId() {
    return id;
  }
  
  public int getCarId() {
    return carId;
  }
  
  public void setCarId(int carId) {
    this.carId = carId;
  }
  
  public int getType() {
    return type;
  }
  
  public void setType(int type) {
    this.type = type;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public int getValue() {
    return value;
  }
  
  public void setValue(int value) {
    this.value = value;
  }
  
  public float getPrice() {
    return price;
  }
  
  public void setPrice(float price) {
    this.price = price;
  }
  
  public String getDealer() {
    return dealer;
  }
  
  public void setDealer(String dealer) {
    this.dealer = dealer;
  }
  
  public Date getDate() {
    return date;
  }
  
  public void setDate(Date date) {
    this.date = date;
  }
  
  public int getOdometr() {
    return odometr;
  }
  
  public void setOdometr(int odometr) {
    this.odometr = odometr;
  }
  
  // 2 methods for JTable editing. MB must be moved to TableModel
  public Object getPropertyByN(int n) {
    switch (n) {
      case 0:
        return id;
      case 1:
        return carId;
      case 2:
        return type;
      case 3:
        return name;
      case 4:
        return value;
      case 5:
        return price;
      case 6:
        return dealer;
      case 7:
        return date;
      case 8:
        return odometr;
   }
   return null; 
  }
  
  public void setPropertyByN(int n, Object newValue) {
    switch (n) {
      case 0:
        id = (int) newValue;
        break;
      case 1:
        carId = (int) newValue;
        break;
      case 2:
        type = (int) newValue;
        break;
      case 3:
        name = (String) newValue;
        break;
      case 4:
        value = (int) newValue;
        break;
      case 5:
        price = (Float) newValue;
        break;
      case 6:
        dealer = (String) newValue;
        break;
      case 7:
        try {
          date = new SimpleDateFormat("dd.MM.yyyy").parse((String) newValue);
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;
      case 8:
        odometr = (int) newValue;
        break;
   }
  }
  
  @Override
  public String toString() {
    return String.format("ID: %s | ID машины: %s | Тип: %s | Наименование: %s | Количество: %s | Стоимость: %s | Поставщик: %s | Дата установки: %s | Пробег км: %s", 
                         this.id, this.carId, this.type, this.name, this.value, this.price, this.dealer, this.date, this.odometr);
  }

}