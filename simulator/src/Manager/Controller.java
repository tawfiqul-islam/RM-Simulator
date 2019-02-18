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
        while(true) {

            Job currentJob=null;
            if(!Controller.activeJobs.isEmpty())
                currentJob=Controller.activeJobs.peek();

            Job newJob=null;
            if(!Controller.jobList.isEmpty())
                newJob=Controller.jobList.get(0);

            if(newJob==null&&currentJob==null) {
                break;
            }
            else if(newJob!=null&&currentJob==null){

                wallClockTime=Math.max(newJob.getT_A(),wallClockTime);
                jobWaiting=!SimpleScheduler.findSchedule(newJob);
            }
            else if(newJob==null&&currentJob!=null){
                finishCurrentJob(currentJob);
            }
            else {
                if(jobWaiting) {
                    finishCurrentJob(currentJob);
                }
                else if(newJob.getT_A()<currentJob.getT_F()){
                    wallClockTime=Math.max(newJob.getT_A(),wallClockTime);
                    jobWaiting=!SimpleScheduler.findSchedule(newJob);
                }
                else{
                    finishCurrentJob(currentJob);
                }
            }
            /*if(nextFinishedJob!=null&&newJob!=null) {
                if((nextFinishedJob.getT_F()<newJob.getT_A())||jobWaiting) {

                    wallClockTime=nextFinishedJob.getT_F();
                    if(nextFinishedJob.getT_F()+nextFinishedJob.getT_W()<nextFinishedJob.getT_D()) {
                        nextFinishedJob.setDeadlineMet(true);
                    }
                    else {
                        nextFinishedJob.setDeadlineMet(false);
                    }
                    Controller.finishedJobs.add(activeJobs.remove());
                    for(int i=0;i<nextFinishedJob.getPlacementList().size();i++) {
                        StatusUpdater.addVMresource(nextFinishedJob.getPlacementList().get(i),nextFinishedJob);
                    }
                    System.out.println("T: "+Controller.wallClockTime+" SUCCESS: Finished execution of job: "+nextFinishedJob.getJobID());
                }
                else {
                    //wallClockTime=newJob.getT_A();
                    //process new job
                    //call scheduler
                    jobWaiting=!SimpleScheduler.findSchedule(newJob);
                }
            }
            else if(nextFinishedJob==null&&newJob!=null) {
                //process new job
                //call scheduler
                jobWaiting=!SimpleScheduler.findSchedule(newJob);
            }
            else if(newJob==null&&nextFinishedJob!=null) {
                jobWaiting=false;

                wallClockTime=nextFinishedJob.getT_F();
                if(nextFinishedJob.getT_F()<nextFinishedJob.getT_D()) {
                    nextFinishedJob.setDeadlineMet(true);
                }
                else {
                    nextFinishedJob.setDeadlineMet(false);
                }
                Controller.finishedJobs.add(activeJobs.remove());
                for(int i=0;i<nextFinishedJob.getPlacementList().size();i++) {
                    StatusUpdater.addVMresource(nextFinishedJob.getPlacementList().get(i),nextFinishedJob);
                }
            }
            else {
                //nothing to do
            }*/
        }

        for(int i=0;i<finishedJobs.size();i++) {
            System.out.println(finishedJobs.get(i).toString());
        }
    }
    static void finishCurrentJob(Job currentJob) {
        wallClockTime=currentJob.getT_F();
        if(currentJob.getT_F()<currentJob.getT_D()) {
            currentJob.setDeadlineMet(true);
        }
        else {
            currentJob.setDeadlineMet(false);
        }
        Controller.finishedJobs.add(activeJobs.remove());
        for(int i=0;i<currentJob.getPlacementList().size();i++) {
            StatusUpdater.addVMresource(currentJob.getPlacementList().get(i),currentJob);
        }
        System.out.println("T: "+Controller.wallClockTime+" SUCCESS: Finished execution of job: "+currentJob.getJobID());
    }
}
