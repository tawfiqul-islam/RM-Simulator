package Manager;

import Entity.Job;
import Entity.VM;
import Policy.FirstFitScheduler;
import Policy.GIOScheduler;
import Policy.ILPScheduler;
import Policy.RRScheduler;
import Settings.Configurations;
import Workload.CSVReader;
import Workload.SimpleGen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
    public static ArrayList<Job> failedJobs = new ArrayList<>();
    public static boolean jobWaiting=false;
    public static double totalCost=0;
    public static double deadlineMet=0;
    public static double schedulingDelayTotal=0;

    public static int jobWaitedTotal=0;
    public static String fileSuffixStr;

    public static void main(String args[]) {

        //Load Configurations
        Settings.SettingsLoader.loadSettings();
        Log.SimulatorLogging.log(Level.INFO,Controller.class.getName()+": Loaded Settings from Configuration File");
        //Generate Workload
        SimpleGen.generateClusterResources();
        CSVReader.parseJobs();

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

        /*for(int i=0;i<Controller.vmList.size();i++) {
            System.out.println(Controller.vmList.get(i).getVmID()+" "+Controller.vmList.get(i).isActive()+Controller.vmList.get(i).getC_free()+" "+Controller.vmList.get(i).getM_free());
        }*/

        deadlineMet=0;
        totalCost=0;
        schedulingDelayTotal=0;
        System.out.println("\n\n*********** JOBS ***********\n\n");
        for(int i=0;i<finishedJobs.size();i++) {
            if(finishedJobs.get(i).isDeadlineMet()) {
                deadlineMet+=1;
            }
            if(finishedJobs.get(i).isWaited()) {
                jobWaitedTotal+=1;
            }
            schedulingDelayTotal+=finishedJobs.get(i).getSchedulingDelay();
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
        System.out.println("\n\n***Deadline Met: "+deadlineMet+" Percentage: "+(deadlineMet/(finishedJobs.size()+failedJobs.size()))*100+"%");
        System.out.println("\n\n***Average Scheduling Delay: "+schedulingDelayTotal/finishedJobs.size()+" seconds");
        System.out.println("\n\n***Job Waited: "+jobWaitedTotal);
        System.out.println("\n\n***Failed Jobs: "+failedJobs.size());
        setFileStrSuffix();
        writeJobResults();
        writeVMResults();

    }
    static void setFileStrSuffix()
    {
        if(Configurations.schedulerPolicy==1){
            fileSuffixStr="FF";
        }
        else if(Configurations.schedulerPolicy==2) {
            fileSuffixStr="GIO";
        }
        else if(Configurations.schedulerPolicy==3) {
            fileSuffixStr="ILP";
        }
        else if(Configurations.schedulerPolicy==4) {
            fileSuffixStr="RR";
        }
    }

    static void finishCurrentJob(Job currentJob) {
        wallClockTime=currentJob.getT_F();
        if(currentJob.getT_F()<currentJob.getT_D()+8000) {
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

        if(Configurations.failedJobQueue==1) {
            if (wallClockTime + newJob.getT_est() > newJob.getT_D() + 8000) {
                newJob.setDeadlineMet(false);
                jobList.remove(newJob);
                failedJobs.add(newJob);
                jobWaiting = false;

                return;
            }
        }
        double schedulingDelay=System.currentTimeMillis();
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
            jobWaiting=!RRScheduler.findSchedule(newJob);
        }
        else{
            Log.SimulatorLogging.log(Level.SEVERE,Controller.class.getName()+" Invalid/No scheduling policy provided");
            System.out.println("invalid scheduling policy");
        }
        schedulingDelay=(System.currentTimeMillis()-schedulingDelay)/1000.0;

        newJob.setSchedulingDelay(newJob.getSchedulingDelay()+schedulingDelay);

        if(jobWaiting){
            newJob.setWaited(true);
        }
        System.out.println("Scheduling Delay: "+schedulingDelay+" second");
    }

    public static void writeJobResults()
    {
        PrintWriter pw=null;
        try {
            pw = new PrintWriter(new File(Configurations.simulatorHome+"/jobs_"+fileSuffixStr+".csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("id");
        sb.append(',');
        sb.append("CPU (cores)");
        sb.append(',');
        sb.append("Memory (GB)");
        sb.append(',');
        sb.append("Executor");
        sb.append(',');
        sb.append("Arrival Time (T_A)");
        sb.append(',');
        sb.append("Start Time (T_S)");
        sb.append(',');
        sb.append("Finish Time (T_F)");
        sb.append(',');
        sb.append("Waiting Time (T_W)");
        sb.append(',');
        sb.append("Scheduler Delay (seconds)");
        sb.append(',');
        sb.append("Estimated Finish Time (T_est)");
        sb.append(',');
        sb.append("Deadline Violation Time (T_D)");
        sb.append(',');
        sb.append("Deadline Satisfied");
        sb.append(',');
        sb.append("Placement List");

        sb.append('\n');

        for(int i=0;i< finishedJobs.size();i++) {
            sb.append(finishedJobs.get(i).getJobID());
            sb.append(',');
            sb.append(finishedJobs.get(i).getC());
            sb.append(',');
            sb.append(finishedJobs.get(i).getM());
            sb.append(',');
            sb.append(finishedJobs.get(i).getE());
            sb.append(',');
            sb.append(finishedJobs.get(i).getT_A());
            sb.append(',');
            sb.append(finishedJobs.get(i).getT_S());
            sb.append(',');
            sb.append(finishedJobs.get(i).getT_F());
            sb.append(',');
            sb.append(finishedJobs.get(i).getT_W());
            sb.append(',');
            sb.append(finishedJobs.get(i).getSchedulingDelay());
            sb.append(',');
            sb.append(finishedJobs.get(i).getT_est());
            sb.append(',');
            sb.append(finishedJobs.get(i).getT_D());
            sb.append(',');
            sb.append(finishedJobs.get(i).isDeadlineMet());
            sb.append(',');
            sb.append(finishedJobs.get(i).getPlacementListStr());

            sb.append('\n');
        }

        sb.append('\n');
        sb.append("Total Jobs: ,"+Configurations.jobTotal+"\n");
        sb.append("Deadline Met: ,"+deadlineMet+"\n");
        sb.append("Deadline Met Percentage: ,"+(deadlineMet*100.0)/Configurations.jobTotal+"%\n");
        sb.append("Average Scheduler Delay: ,"+schedulingDelayTotal/Configurations.jobTotal+" seconds\n");
        pw.write(sb.toString());
        pw.close();
    }

    public static void writeVMResults()
    {
        PrintWriter pw=null;
        try {
            pw = new PrintWriter(new File(Configurations.simulatorHome+"/VMs_"+fileSuffixStr+".csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("id");
        sb.append(',');
        sb.append("Location");
        sb.append(',');
        sb.append("Instance Flavor");
        sb.append(',');
        sb.append("CPU Capacity");
        sb.append(',');
        sb.append("Memory Capacity");
        sb.append(',');
        sb.append("Price (AUD$)");
        sb.append(',');
        sb.append("Total Active Time (T_used)");
        sb.append(',');
        sb.append("Total Cost (AUD$)");
        sb.append('\n');

        for(int i=0;i< vmList.size();i++) {
            sb.append(vmList.get(i).getVmID());
            sb.append(',');
            sb.append(vmList.get(i).isLocal()?"Local":"Cloud");
            sb.append(',');
            sb.append(vmList.get(i).getInstanceFlavor());
            sb.append(',');
            sb.append(vmList.get(i).getC_Cap());
            sb.append(',');
            sb.append(vmList.get(i).getM_Cap());
            sb.append(',');
            sb.append(vmList.get(i).getPrice());
            sb.append(',');
            sb.append(vmList.get(i).getT_used());
            sb.append(',');
            sb.append(vmList.get(i).getCost());
            sb.append('\n');
        }

        sb.append('\n');
        sb.append("Total Cost: ,"+totalCost+"\n");
        pw.write(sb.toString());
        pw.close();
    }
}
