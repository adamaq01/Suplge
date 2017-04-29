package fr.adamaq01.suplge;

import fr.adamaq01.suplge.api.ILogger;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Adamaq01 on 22/03/2017.
 */
public class Logger implements ILogger {

    private String name;
    private PrintStream stream;

    public Logger(String name, PrintStream stream) {
        this.name = name;
        this.stream = stream;
    }

    @Override
    public void info(String message) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        stream.println(simpleDateFormat.format(new Date(System.currentTimeMillis())) + " [" + name + " INFO] " + message);
    }

    @Override
    public void debug(String message) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        stream.println(simpleDateFormat.format(new Date(System.currentTimeMillis())) + " [" + name + " DEBUG] " + message);
    }

    @Override
    public void error(String message) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        stream.println(simpleDateFormat.format(new Date(System.currentTimeMillis())) + " [" + name + " ERROR] " + message);
    }
}
