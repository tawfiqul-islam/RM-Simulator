package Settings;

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

            Configurations.simulatorHome=prop.getProperty("simulator.home");
            Configurations.workloadType=Integer.parseInt(prop.getProperty("workload.type"));
            Configurations.queuePolicy=Integer.parseInt(prop.getProperty("queue.policy"));
            Configurations.schedulerPolicy=Integer.parseInt(prop.getProperty("scheduler.policy"));
            Configurations.resourceSplitThreshold=Double.parseDouble(prop.getProperty("resourceSplitThreshold"));

            Configurations.jobTotal=Integer.parseInt(prop.getProperty("job.total"));
            Configurations.jobArrival=Integer.parseInt(prop.getProperty("job.arrival"));
            Configurations.failedJobQueue=Integer.parseInt(prop.getProperty("failed.queue"));
            Configurations.networkPenalty=Double.parseDouble(prop.getProperty("network.penalty"));
            Configurations.poissonMean=Integer.parseInt(prop.getProperty("poisson.mean"));
            Configurations.lambda=Double.parseDouble(prop.getProperty("lambda"));
            Configurations.deadlineTolerance=Integer.parseInt(prop.getProperty("deadline.tolerance"));

            Configurations.cloudVMType1=Integer.parseInt(prop.getProperty("cloud.vm.type1"));
            Configurations.cloudVMType2=Integer.parseInt(prop.getProperty("cloud.vm.type2"));
            Configurations.cloudVMType3=Integer.parseInt(prop.getProperty("cloud.vm.type3"));

            Configurations.localVMType1=Integer.parseInt(prop.getProperty("local.vm.type1"));
            Configurations.localVMType2=Integer.parseInt(prop.getProperty("local.vm.type2"));
            Configurations.localVMType3=Integer.parseInt(prop.getProperty("local.vm.type3"));

            Configurations.cloudVMPrice1=Double.parseDouble(prop.getProperty("cloud.vm.price1"));
            Configurations.cloudVMPrice2=Double.parseDouble(prop.getProperty("cloud.vm.price2"));
            Configurations.cloudVMPrice3=Double.parseDouble(prop.getProperty("cloud.vm.price3"));

            Configurations.localVMPrice1=Double.parseDouble(prop.getProperty("local.vm.price1"));
            Configurations.localVMPrice2=Double.parseDouble(prop.getProperty("local.vm.price2"));
            Configurations.localVMPrice3=Double.parseDouble(prop.getProperty("local.vm.price3"));


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