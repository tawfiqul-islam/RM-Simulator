package Manager;

import Entity.Job;
import Entity.VM;
import Policy.FirstFitScheduler;
import Policy.GIOScheduler;
import Policy.ILPScheduler;
import Settings.Configurations;
import Workload.SimpleGen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.logging.Level;

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


        //Load Configurations
        Settings.SettingsLoader.loadSettings();
        Log.SimulatorLogging.log(Level.INFO,Controller.class.getName()+": Loaded Settings from Configuration File");
        //Generate Workload
        SimpleGen.generateClusterResources();
        SimpleGen.generateJobs();

        //Choose Queue Policy
        if(Configurations.queuePolicy==1) {
            Collections.sort(jobList, new Utility.JobComparatorFCFS());
        }
        else if(Configurations.queuePolicy==2) {
            Collections.sort(jobList, new Utility.JobComparatorEDFplusFCFS());
        }
        else {
            Log.SimulatorLogging.log(Level.SEVERE,Controller.class.getName()+" Invalid/No queueing policy provided");
        }

        while(true) {

            Job currentJob=null;
            if(!Controller.activeJobs.isEmpty())
                currentJob=Controller.activeJobs.peek();

            Job newJob=null;
            if(!Controller.jobList.isEmpty()) {
                newJob = Controller.jobList.get(0);
            }

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
            for(int j=0;j< Controller.vmList.size();j++)
                if(Controller.vmList.get(j).getVmID().equals(currentJob.getPlacementList().get(i)))
                    StatusUpdater.addVMresource(Controller.vmList.get(j),currentJob);
        }
        System.out.println("T: "+Controller.wallClockTime+" SUCCESS: Finished execution of job: "+currentJob.getJobID());
    }

    static void scheduleNewJob(Job newJob) {

        wallClockTime=Math.max(newJob.getT_A(),wallClockTime);

        //Choose Scheduler
        if(Configurations.schedulerPolicy==1){
            jobWaiting=!FirstFitScheduler.findSchedule(newJob);
        }
        else if(Configurations.schedulerPolicy==2) {
            jobWaiting=!GIOScheduler.findSchedule(newJob);
        }
        else if(Configurations.schedulerPolicy==3) {
            jobWaiting=!ILPScheduler.findSchedule(newJob);
        }
        else if(Configurations.schedulerPolicy==4) {

        }
        else{
            Log.SimulatorLogging.log(Level.SEVERE,Controller.class.getName()+" Invalid/No scheduling policy provided");
            System.out.println("invalid scheduling policy");
        }
    }
}
