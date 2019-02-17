package Manager;

import Entity.Job;
import Entity.VM;
import Workload.SimpleGen;

import java.util.ArrayList;

public class Controller {

    public static long wallClockTime=0;
    public static ArrayList<Job> jobList = new ArrayList<>();
    public static ArrayList<VM> vmList = new ArrayList<>();
    public static ArrayList<Job> activeJobs = new ArrayList<>();
    public static ArrayList<Job> finishedJobs = new ArrayList<>();

    public static void main(String args[]) {

        SimpleGen.generateClusterResources();
        SimpleGen.generateJobs();
    }
}
