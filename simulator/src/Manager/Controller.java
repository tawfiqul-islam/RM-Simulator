package Manager;

import Entity.Job;
import Entity.VM;
import Policy.FirstFitScheduler;
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
    public static double totalCost=0;
    public static double deadlineMet=0;

    public static void main(String args[]) {

        SimpleGen.generateClusterResources();
        SimpleGen.generateJobs();

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
                scheduleNewJob(newJob);
            }
            else if(newJob==null&&currentJob!=null){
                finishCurrentJob(currentJob);
            }
            else {
                if(jobWaiting) {
                    finishCurrentJob(currentJob);
                    scheduleNewJob(newJob);
                }
                else if(newJob.getT_A()<currentJob.getT_F()){
                    scheduleNewJob(newJob);
                }
                else{
                    finishCurrentJob(currentJob);
                }
            }
        }


        System.out.println("\n\n*********** JOBS ***********\n\n");
        for(int i=0;i<finishedJobs.size();i++) {
            if(finishedJobs.get(i).isDeadlineMet()) {
                deadlineMet+=1;
            }
            System.out.println(finishedJobs.get(i).toString());
        }

        System.out.println("\n\n*********** VMS ***********\n\n");
        for(int i=0;i<vmList.size();i++) {
            VM vm =vmList.get(i);
            vm.setCost(vm.getPrice()*vm.getT_used());
            totalCost+=vm.getCost();
            System.out.println(vm);
        }

        System.out.println("\n\n***Total Cost for the Scheduler: "+totalCost+"$");
        System.out.println("\n\n***Deadline Met: "+(deadlineMet/finishedJobs.size())*100+"%");

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

    static void scheduleNewJob(Job newJob) {
        wallClockTime=Math.max(newJob.getT_A(),wallClockTime);
        jobWaiting=!FirstFitScheduler.findSchedule(newJob);
    }
}
