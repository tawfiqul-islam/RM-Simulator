package Manager;

import Entity.Job;
import Entity.VM;
import Policy.SimpleScheduler;
import Workload.SimpleGen;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Controller {

    public static long wallClockTime=0;
    public static ArrayList<Job> jobList = new ArrayList<>();
    public static ArrayList<VM> vmList = new ArrayList<>();
    public static PriorityQueue<Job> activeJobs = new PriorityQueue<Job>(5,Utility.JobComparator);
    public static ArrayList<Job> finishedJobs = new ArrayList<>();
    public static boolean jobWaiting=false;

    public static void main(String args[]) {

        SimpleGen.generateClusterResources();
        SimpleGen.generateJobs();

        /*for(int i=0;i<jobList.size();i++) {
            System.out.println(jobList.get(i));
        }

        for(int i=0;i<vmList.size();i++)
        {
            System.out.println(vmList.get(i));
        }*/
        while(jobList.size()!=0) {
            Job nextFinishedJob=Controller.activeJobs.peek();
            Job newJob=Controller.jobList.get(0);

            //if nextFinishedJob is going to be finished before the newJob arrival
            if(nextFinishedJob!=null){
                if((nextFinishedJob.getT_C()<newJob.getT_A())||jobWaiting) {
                    jobWaiting=false;

                    wallClockTime=nextFinishedJob.getT_C();
                    if(nextFinishedJob.getT_C()+nextFinishedJob.getT_W()<nextFinishedJob.getT_D()) {
                        nextFinishedJob.setDeadlineMet(true);
                    }
                    else {
                        nextFinishedJob.setDeadlineMet(false);
                    }
                    Controller.finishedJobs.add(activeJobs.remove());
                    for(int i=0;i<nextFinishedJob.getPlacementList().size();i++) {
                        StatusUpdater.addVMresource(nextFinishedJob.getPlacementList().get(i),nextFinishedJob);
                    }
                    //process finished job

                }
            }
            //if newJob arrives before nextFinishedJob's completion
            else {
                wallClockTime=newJob.getT_A();
                //process new job
                //call scheduler
                jobWaiting=!SimpleScheduler.findSchedule(newJob);
            }

        }

        for(int i=0;i<finishedJobs.size();i++) {
            System.out.println(finishedJobs.get(i).toString());
        }
    }
}
