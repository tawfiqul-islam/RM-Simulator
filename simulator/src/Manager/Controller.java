package Manager;

import Entity.Job;
import Entity.VM;
import Workload.SimpleGen;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Controller {

    public static long wallClockTime=0;
    public static ArrayList<Job> jobList = new ArrayList<>();
    public static ArrayList<VM> vmList = new ArrayList<>();
    public static PriorityQueue<Job> activeJobs = new PriorityQueue<Job>(5,Utility.JobComparator);
    public static ArrayList<Job> finishedJobs = new ArrayList<>();

    public static void main(String args[]) {

        SimpleGen.generateClusterResources();
        SimpleGen.generateJobs();

        while(jobList.size()!=0) {

            if(Controller.activeJobs.peek().getT_C()>Controller.jobList.get(0).getT_A()) {
                //process finished job

            }
            else {
                //process new job
                //call scheduler
            }
        }
    }
}
