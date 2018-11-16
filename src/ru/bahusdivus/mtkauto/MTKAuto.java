package ru.bahusdivus.mtkauto;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.*;
import java.util.*;
import java.util.logging.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;

/**
 * @author Dionisii Iuzhakov
 * @version 0.01
 */
public class MTKAuto {
  
  public static void main (String[] args) {
    //TODO: Extend logging
    if (System.getProperty("java.util.logging.config.class") == null && System.getProperty("java.util.logging.config.file") == null) {
      try {
        Logger.getLogger("ru.bahusdivus.mtkauto").setLevel(Level.ALL);
        final int LOG_ROTATION_COUNT = 10;
        Handler handler = new FileHandler("%h/mtkauto.log", 0, LOG_ROTATION_COUNT);
        Logger.getLogger("ru.bahusdivus.mtkauto").addHandler(handler);
      } catch (IOException e) {
        Logger.getLogger("ru.bahusdivus.mtkauto").log(Level.SEVERE, "Can't create log file handler", e);
      }
    }
    
    EventQueue.invokeLater(() -> {
      MTKFrame frame = new MTKFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Logger.getLogger("ru.bahusdivus.mtkauto").fine("Showing frame");
      frame.setVisible(true);
    });
  }
}