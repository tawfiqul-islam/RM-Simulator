package Settings;

import Manager.Utility;

import java.util.Properties;
import java.lang.*;
import java.io.*;
import java.util.logging.Level;

/*
 * Loads the settings from the profiler configuration file
 *
 * @author: Muhammed Tawfiqul Islam
 *
 */

public class SettingsLoader {
    public static void loadSettings() {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            //specify properties file with full path... or only file name if it's in current directory
            input = new FileInputStream("simulator.ini");

            // load a properties file
            prop.load(input);

            Settings.simulatorHome=prop.getProperty("simulator.home");
            Settings.workloadType=Integer.parseInt(prop.getProperty("workload.type"));
            Settings.queuePolicy=Integer.parseInt(prop.getProperty("queue.policy"));
            Settings.schedulerPolicy=Integer.parseInt(prop.getProperty("scheduler.policy"));
            Settings.resourceSplitThreshold=Double.parseDouble(prop.getProperty("resourceSplitThreshold"));

            Settings.jobTotal=Integer.parseInt(prop.getProperty("job.total"));
            Settings.jobArrival=Integer.parseInt(prop.getProperty("job.arrival"));

            Settings.cloudVMType1=Integer.parseInt(prop.getProperty("cloud.vm.type1"));
            Settings.cloudVMType2=Integer.parseInt(prop.getProperty("cloud.vm.type2"));
            Settings.cloudVMType3=Integer.parseInt(prop.getProperty("cloud.vm.type3"));

            Settings.localVMType1=Integer.parseInt(prop.getProperty("local.vm.type1"));
            Settings.localVMType2=Integer.parseInt(prop.getProperty("local.vm.type2"));
            Settings.localVMType3=Integer.parseInt(prop.getProperty("local.vm.type3"));

            Settings.cloudVMPrice1=Double.parseDouble(prop.getProperty("cloud.vm.price1"));
            Settings.cloudVMPrice2=Double.parseDouble(prop.getProperty("cloud.vm.price2"));
            Settings.cloudVMPrice3=Double.parseDouble(prop.getProperty("cloud.vm.price3"));

            Settings.localVMPrice1=Double.parseDouble(prop.getProperty("local.vm.price1"));
            Settings.localVMPrice2=Double.parseDouble(prop.getProperty("local.vm.price2"));
            Settings.localVMPrice3=Double.parseDouble(prop.getProperty("local.vm.price3"));


        } catch (IOException ex) {
            Log.SimulatorLogging.log(Level.SEVERE,SettingsLoader.class.getName()+" Exception while loading settings: "+ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.SimulatorLogging.log(Level.SEVERE,SettingsLoader.class.getName()+" Exception while closing inputstream: "+e);
                }
            }
        }
    }
}