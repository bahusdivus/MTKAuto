package ru.bahusdivus.mtkauto;

import java.text.*;
import java.util.Date;

/**
 * This class contain maintenance entry. Only data and getters/setters
 */
class TO {
  private int id = 0;
  private int carId;
  private String name;
  private String model;
  private Date date;
  private int odometr;
  private long appointment;
  
  public TO () {};
  
  public TO (int id, int carId, String name, String model, Date date, int odometr, long appointment) {
    this.id = id;
    this.carId = carId;
    this.name = name;
    this.model = model;
    this.date = date;
    this.odometr = odometr;
    this.appointment = appointment;
  }

  public TO (int carId, String name, String model, Date date, int odometr, long appointment) {
    this.carId = carId;
    this.name = name;
    this.model = model;
    this.date = date;
    this.odometr = odometr;
    this.appointment = appointment;
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
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getModel() {
    return model;
  }
  
  public void setModel(String model) {
    this.model = model;
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
  
  public long getAppointment() {
    return appointment;
  }
  
  public void setAppointment(long appointment) {
    this.appointment = appointment;
  }
  
  
  // 2 methods for JTable editing. MB must be moved to TableModel
  public Object getPropertyByN(int n) {
    switch (n) {
      case 0:
        return id;
      case 1:
        return carId;
      case 2:
        return name;
      case 3:
        return model;
      case 4:
        return date;
      case 5:
        return odometr;
      case 6:
        return appointment;
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
        name = (String) newValue;
        break;
      case 3:
        model = (String) newValue;
        break;
      case 4:
        try {
          date = new SimpleDateFormat("dd.MM.yyyy").parse((String) newValue);
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;
      case 5:
        odometr = (int) newValue;
        break;
      case 6:
        appointment = (int) newValue;
        break;
   }
  }
    
  @Override
  public String toString() {
    return String.format("ID: %s | ID машины: %s | Наименование: %s | Марка: %s | Дата замены: %s | Пробег, км: %s | Следующая замена, км: %s", 
                         this.id, this.carId, this.name, this.model, this.date, this.odometr, this.appointment);
  }

}