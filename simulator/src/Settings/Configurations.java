package Settings;

/*
 * Holds the settings for the Simulator
 *
 * @author: Muhammed Tawfiqul Islam
 */
public class Configurations {


    public static String simulatorHome;

    public static int workloadType;
    public static int queuePolicy;
    public static int schedulerPolicy;


    public static double resourceSplitThreshold=0.2;

    public static int jobTotal;
    public static int jobArrival;
    public static int failedJobQueue;
    public static double networkPenalty;
    public static int poissonMean;
    public static double lambda;
    public static int deadlineTolerance;

    public static int cloudVMType1;
    public static int cloudVMType2;
    public static int cloudVMType3;

    public static int localVMType1;
    public static int localVMType2;
    public static int localVMType3;

    public static double cloudVMPrice1;
    public static double cloudVMPrice2;
    public static double cloudVMPrice3;


    public static double localVMPrice1;
    public static double localVMPrice2;
    public static double localVMPrice3;


}