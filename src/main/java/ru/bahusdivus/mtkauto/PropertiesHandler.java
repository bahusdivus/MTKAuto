package ru.bahusdivus.mtkauto;

import java.awt.*;
import java.io.*;
import java.util.Properties;

class PropertiesHandler {

    private int left, top, width, height, lastBackupN;
    private long lastBackupDate;
    private Properties settings;
    private File propertiesFile;

    PropertiesHandler() {
        Properties defaultSettings = new Properties();
        defaultSettings.setProperty("left", "0");
        defaultSettings.setProperty("top", "0");
        defaultSettings.setProperty("width", "1000");
        defaultSettings.setProperty("height", "600");
        defaultSettings.setProperty("lastBackupDate", "0");
        defaultSettings.setProperty("lastBackupN", "0");
        settings = new Properties(defaultSettings);

        String userDir = System.getProperty("user.home");
        File propertiesDir = new File(userDir, ".mtkauto");
        if (!propertiesDir.exists()) {
            if(!propertiesDir.mkdir()) System.out.println("Can't create properties folder");
        }
        propertiesFile = new File(propertiesDir, "program.properties");
        if (propertiesFile.exists()) {
            try (InputStream in = new FileInputStream(propertiesFile)) {
                settings.load(in);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        left = Integer.parseInt(settings.getProperty("left"));
        top = Integer.parseInt(settings.getProperty("top"));
        width = Integer.parseInt(settings.getProperty("width"));
        height = Integer.parseInt(settings.getProperty("height"));
        lastBackupDate = Long.parseLong(settings.getProperty("lastBackupDate"));
        lastBackupN = Integer.parseInt(settings.getProperty("lastBackupN"));
    }

    boolean checkIsBackupNeed() {
        return (lastBackupDate + 84600000L) < System.currentTimeMillis();
    }

    void setLastBackup() {
        lastBackupDate = System.currentTimeMillis();
        if (lastBackupN < 5) {
            lastBackupN++;
        } else {
            lastBackupN = 0;
        }
    }

    Rectangle getFrameRectangle() {
        return new Rectangle(left, top, width, height);
    }

    void setFrameRectangle(int left, int top, int width, int height) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    void save() {
        settings.setProperty("left", String.valueOf(left));
        settings.setProperty("top", String.valueOf(top));
        settings.setProperty("width", String.valueOf(width));
        settings.setProperty("height", String.valueOf(height));
        settings.setProperty("lastBackupDate", String.valueOf(lastBackupDate));
        settings.setProperty("lastBackupN", String.valueOf(lastBackupN));
        try (OutputStream out = new FileOutputStream(propertiesFile)) {
            settings.store(out, "Program Properties");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    int getLatBackupN() {
        return lastBackupN;
    }
}
