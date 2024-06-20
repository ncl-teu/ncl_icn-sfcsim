package net.gripps.ccn.icnsfc.logger;

import java.io.*;

import net.gripps.ccn.icnsfc.core.AutoEnvironment;

public class ISNetworkEnvLog {
    public static void makeNetworkEnvLog(String logName, AutoEnvironment environment) {
        File file = new File("./is/" + logName + ".dat");
        File outputFile = new File(file.getAbsolutePath());
        AutoEnvironment env = environment;

        try {
            FileOutputStream fos = null;
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile));
            oos.writeObject(env);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("writeObject Failed");
        }
    }

    public static AutoEnvironment getNetworkEnvLog(String logName) {
        File file = new File("./is/" + logName + ".dat");
        File inputFile = new File(file.getAbsolutePath());


        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFile));
            AutoEnvironment environment = (AutoEnvironment) ois.readObject();
            return environment;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("readObject Failed");
        }
        System.out.println("return null");
        return null;
    }
}
