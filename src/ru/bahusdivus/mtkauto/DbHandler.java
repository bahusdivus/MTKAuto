package ru.bahusdivus.mtkauto;

import org.sqlite.JDBC;

import java.text.*;
import java.sql.*;
import java.util.*;
import java.util.Date;


/**
 * This class handle various database operations
 * SELECT operations dont retrive "deleted" column, I keep "deleted" entries just in case. Dont know, good approach or not
 */
class DbHandler {

    private static final String CONNECTION_ADDRESS = "jdbc:sqlite:mtkauto.s3db";
    private Connection connection;
    private SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");

    // Singleton pattern
    private static DbHandler instance = null;

    static synchronized DbHandler getInstance() throws SQLException {
        if (instance == null)
            instance = new DbHandler();
        return instance;
    }

    private DbHandler() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(CONNECTION_ADDRESS);
    }


    // Tables initiation
    void creatDB() {
        try (Statement statement = this.connection.createStatement()) {
            statement.execute("CREATE TABLE if not exists 'cars' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'number' text, 'deleted' BOOLEAN);");
            statement.execute("CREATE TABLE if not exists 'parts' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'car_id' INTEGER, 'type' INTEGER, 'name' text, 'value' INTEGER, 'price' FLOAT, 'dealer' text, 'date' TIMESTAMP, 'odometr' INTEGER, 'deleted' BOOLEAN);");
            statement.execute("CREATE TABLE if not exists 'tos' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'car_id' INTEGER, 'name' text, 'model' text, 'date' TIMESTAMP, 'odometr' INTEGER, 'appointment' INTEGER, 'deleted' BOOLEAN);");

            //Altering tables, if old tables have not "deleted" column =(
            boolean isExist = false;
            ResultSet resultSet = statement.executeQuery("PRAGMA table_info(cars)");
            while (resultSet.next()) {
                String colName = resultSet.getString(2);
                if (colName.equals("deleted")) isExist = true;
            }
            if (isExist == false) statement.execute("ALTER TABLE 'cars' ADD deleted BOOLEAN DEFAULT(0);");

            isExist = false;
            resultSet = statement.executeQuery("PRAGMA table_info(parts)");
            while (resultSet.next()) {
                String colName = resultSet.getString(2);
                if (colName.equals("deleted")) isExist = true;
            }
            if (isExist == false) statement.execute("ALTER TABLE 'parts' ADD deleted BOOLEAN DEFAULT(0);");

            isExist = false;
            resultSet = statement.executeQuery("PRAGMA table_info(tos)");
            while (resultSet.next()) {
                String colName = resultSet.getString(2);
                if (colName.equals("deleted")) isExist = true;
            }
            if (isExist == false) statement.execute("ALTER TABLE 'tos' ADD deleted BOOLEAN DEFAULT(0);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Boolean makeBackup(int n) {
        try (Statement statement = this.connection.createStatement()) {
            statement.executeUpdate("backup to backup" + n + ".s3db");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    List<Car> getCars() {
        try (Statement statement = this.connection.createStatement()) {
            List<Car> cars = new ArrayList<Car>();
            ResultSet resultSet = statement.executeQuery("SELECT id, number FROM cars WHERE deleted = 0");
            while (resultSet.next()) {
                cars.add(new Car(resultSet.getInt("id"), resultSet.getString("number")));
            }
            return cars;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Add or edit car depending on id existence
    void saveCar(Car car) {
        int id = car.getId();
        try (Statement statement = this.connection.createStatement()) {
            if (id != 0) {
                statement.execute("UPDATE cars SET number = '" + car.getNumber() + "' WHERE id = " + id);
            } else {
                statement.execute("INSERT INTO cars (number) VALUES ('" + car.getNumber() + "')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void deleteCar(int id) {
        try (Statement statement = this.connection.createStatement()) {
            statement.execute("UPDATE cars SET deleted = 1 WHERE id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    void deletePart(int id) {
        try (Statement statement = this.connection.createStatement()) {
            statement.execute("UPDATE parts SET deleted = 1 WHERE id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get part list, filtered by carID & Type
    List<Part> getParts(int carId, int type) {
        try (Statement statement = this.connection.createStatement()) {
            List<Part> parts = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery("SELECT id, car_id, type, name, value, price, dealer, date, odometr FROM parts WHERE deleted = 0 AND car_id = " + carId + " AND type = " + type);
            while (resultSet.next()) {
                parts.add(new Part(
                        resultSet.getInt("id"),
                        resultSet.getInt("car_id"),
                        resultSet.getInt("type"),
                        resultSet.getString("name"),
                        resultSet.getInt("value"),
                        resultSet.getFloat("price"),
                        resultSet.getString("dealer"),
                        new Date(resultSet.getLong("date")),
                        resultSet.getInt("odometr")
                ));
            }
            return parts;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    void savePart(Part part) {
        int id = part.getId();
        try (Statement statement = this.connection.createStatement()) {
            if (id != 0) {
                statement.execute("UPDATE parts SET car_id = " + part.getCarId() + ", "
                        + "type = " + part.getType() + ", "
                        + "name = '" + part.getName() + "', "
                        + "value = " + part.getValue() + ", "
                        + "price = " + part.getPrice() + ", "
                        + "dealer = '" + part.getDealer() + "', "
                        + "date = " + part.getDate().getTime() + ", "
                        + "odometr = " + part.getOdometr()
                        + " WHERE id = " + id);
            } else {
                statement.execute("INSERT INTO parts (car_id, type, name, value, price, dealer, date, odometr) VALUES ("
                        + part.getCarId() + ", "
                        + part.getType() + ", "
                        + "'" + part.getName() + "', "
                        + part.getValue() + ", "
                        + part.getPrice() + ", "
                        + "'" + part.getDealer() + "', "
                        + part.getDate().getTime() + ", "
                        + part.getOdometr() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void deleteTO(int id) {
        try (Statement statement = this.connection.createStatement()) {
            statement.execute("UPDATE tos SET deleted = 1 WHERE id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get TO list, filtered by car_id
    List<TO> getTOByCarId(int carId) {
        try (Statement statement = this.connection.createStatement()) {
            List<TO> tos = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery("SELECT id, car_id, name, model, date, odometr, appointment FROM `tos` WHERE deleted = 0 AND car_id = " + carId);
            while (resultSet.next()) {
                tos.add(new TO(
                        resultSet.getInt("id"),
                        resultSet.getInt("car_id"),
                        resultSet.getString("name"),
                        resultSet.getString("model"),
                        new Date(resultSet.getLong("date")),
                        resultSet.getInt("odometr"),
                        resultSet.getInt("appointment")
                ));
            }
            return tos;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    void saveTO(TO to) {
        int id = to.getId();
        try (Statement statement = this.connection.createStatement()) {
            if (id != 0) {
                statement.execute("UPDATE `tos` SET car_id = " + to.getCarId() + ", "
                        + "name = '" + to.getName() + "', "
                        + "model = '" + to.getModel() + "', "
                        + "date = " + to.getDate().getTime() + ", "
                        + "odometr = " + to.getOdometr() + ", "
                        + "appointment = " + to.getAppointment()
                        + " WHERE id = " + id);
            } else {
                statement.execute("INSERT INTO `tos` (car_id, name, model, date, odometr, appointment) VALUES ("
                        + to.getCarId() + ", "
                        + "'" + to.getName() + "', "
                        + "'" + to.getModel() + "', "
                        + to.getDate().getTime() + ", "
                        + to.getOdometr() + ", "
                        + to.getAppointment() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}