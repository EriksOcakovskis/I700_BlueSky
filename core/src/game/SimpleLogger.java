package game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by eriks on 15/11/2016.
 */
public class SimpleLogger {
    // Log level constants
    public static final int ALL = 5;
    public static final int DEBUG = 10;
    public static final int INFO = 20;
    public static final int WARN = 30;
    public static final int ERROR = 40;
    public static final int NONE = 100;

    // Log level set for input verification
    private static final Set<Integer> logLevels = new HashSet<Integer>(Arrays.asList(5,10,20,30,40,100));

    private File logPath;
    private int logLevel;

    // Set default log path as /logs/<current date>.log
    private Date date = new Date();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String strDate = sdf.format(date);
    private String basePath = System.getProperty("user.dir") + "/logs/";
    private String defaultLogFile = strDate + ".log";

    private static SimpleLogger logger = null;

    private SimpleLogger(){
        this.logPath = new File(basePath + defaultLogFile);
        this.logLevel = INFO;
    }

    public static SimpleLogger getLogger() {
        if (logger == null){
            logger = new SimpleLogger();
        }
        return logger;
    }

    public int getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(int logLevel) {
        if (logLevels.contains(logLevel)){
            this.logLevel = logLevel;
        }
    }

    public String getLogFile() {
        return logPath.toString();
    }

    public void setLogFile(String logFile) {
        if(logFile.matches("^[A-Za-z._\\-\\d ]++$")){
            this.logPath = new File(basePath + logFile + "_" + defaultLogFile);
        }
    }

    public void debug(String message){
        if (logLevel <= 10){
            handleWritingFile(logPath, "DEBUG", message);
        }
    }

    public void info(String message){
        if (logLevel <= 20){
            handleWritingFile(logPath, "INFO", message);
        }
    }

    public void warn(String message){
        if (logLevel <= 30){
            handleWritingFile(logPath, "WARNING", message);
        }
    }

    public void error(String message){
        if (logLevel <= 40){
            handleWritingFile(logPath, "ERROR", message);
        }
    }

    private void handleWritingFile(File file, String logLevel ,String input) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String strDate = sdf.format(date);

        BufferedWriter textBuffer = null;
        boolean append = true;

        if (!file.exists()){
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not create " + file.toString());
                e.printStackTrace();
            }
        }

        try{
            textBuffer = new BufferedWriter(new FileWriter(file,append));
            textBuffer.write(strDate + " " + logLevel + " " + input + "\n");
        } catch (Exception e){
            System.out.println("Could not log to " + file.toString() + ", logging to std out");
            System.out.println(strDate + " " + logLevel + " " + input);
            e.printStackTrace();
        } finally {
            try{
                textBuffer.close();
            } catch (Exception e){
                System.out.println("Could not close " + file.toString());
                e.printStackTrace();
            }
        }
    }
}
