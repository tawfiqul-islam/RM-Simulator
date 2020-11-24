package Policy;

import Entity.Job;
import Entity.VM;
import Manager.Controller;
import Manager.StatusUpdater;
import Settings.Configurations;

public class LocalityScheduler {

    private static double localCost;
    private static double hybridCost;

    public static boolean findSchedule(Job job) {

        localCost=0;
        hybridCost=0;

        boolean localOnly,hybrid;
        localOnly=AttemptLocal(job);
        ResetResources(job);

        hybrid=AttemptHybrid(job);
        ResetResources(job);

        System.out.println("localOnly: "+localOnly);
        System.out.println("hybrid: "+hybrid);
        System.out.println("localCost: "+localCost);
        System.out.println("hybridCost: "+hybridCost);
        if(!localOnly && !hybrid) {
            return false;
        }
        else {

            if(localOnly && localCost<hybridCost)
            {
                return PlaceLocal(job);
            }
            else{
                return PlaceHybrid(job);
            }
        }
    }

    private static boolean AttemptLocal(Job job) {

        SchedulerUtility.savePrevMaxT();
        int j=0;
        while(true) {

            boolean placementFound=false;
            double currentCost=1000000.0;
            int vmIndex=0;

            for (int i = 0 ; i < Controller.vmList.size();i++ ) {

                VM vm = Controller.vmList.get(i);
                if(vm.getVmID().contains("C"))
                    continue;
                if (SchedulerUtility.resourceConstraints(job, vm)) {
                    placementFound=true;
                    double tmpT=vm.getMaxT() >= (Controller.wallClockTime+job.getT_est())?0:(Controller.wallClockTime+job.getT_est()-vm.getMaxT());
                    double tmpCost=vm.getPrice()*tmpT;
                    if(tmpCost<currentCost) {
                        currentCost=tmpCost;
                        vmIndex=i;
                    }
                }
            }
            if(placementFound){
                StatusUpdater.subtractVMresource(Controller.vmList.get(vmIndex),job);
                job.addplacementVM(Controller.vmList.get(vmIndex).getVmID());
                j++;
                localCost+=currentCost;
            }
            else {
                break;
            }
            if (j == job.getE()) {
                return true;
            }
        }

        return false;
    }

    private static boolean AttemptHybrid(Job job) {

        SchedulerUtility.savePrevMaxT();
        int j=0;
        long durationIncrease=(long)(job.getT_est()* Configurations.networkPenalty);

        while(true) {

            boolean placementFound=false;
            double currentCost=1000000.0;
            int vmIndex=0;

            for (int i = 0 ; i < Controller.vmList.size();i++ ) {
                VM vm = Controller.vmList.get(i);
                if (SchedulerUtility.resourceConstraints(job, vm)) {
                    placementFound=true;
                    double tmpT=vm.getMaxT() >= (Controller.wallClockTime+job.getT_est()+durationIncrease)?0:(Controller.wallClockTime+job.getT_est()+durationIncrease-vm.getMaxT());
                    double tmpCost=vm.getPrice()*tmpT;
                    if(tmpCost<currentCost) {
                        currentCost=tmpCost;
                        vmIndex=i;
                    }
                }
            }
            if(placementFound){
                StatusUpdater.subtractVMresourceV2(Controller.vmList.get(vmIndex),job,durationIncrease);
                job.addplacementVM(Controller.vmList.get(vmIndex).getVmID());
                j++;
                hybridCost+=currentCost;
            }
            else {
                break;
            }
            if (j == job.getE()) {
                return true;
            }
        }
        return false;
    }

    private static boolean PlaceLocal(Job job) {

        SchedulerUtility.savePrevMaxT();
        int j=0;
        while(true) {

            boolean placementFound=false;
            double currentCost=1000000.0;
            int vmIndex=0;

            for (int i = 0 ; i < Controller.vmList.size();i++ ) {

                VM vm = Controller.vmList.get(i);
                if(vm.getVmID().contains("C"))
                    continue;
                if (SchedulerUtility.resourceConstraints(job, vm)) {
                    placementFound=true;
                    double tmpT=vm.getMaxT() >= (Controller.wallClockTime+job.getT_est())?0:(Controller.wallClockTime+job.getT_est()-vm.getMaxT());
                    double tmpCost=vm.getPrice()*tmpT;
                    if(tmpCost<currentCost) {
                        currentCost=tmpCost;
                        vmIndex=i;
                    }
                }
            }
            if(placementFound){
                StatusUpdater.subtractVMresource(Controller.vmList.get(vmIndex),job);
                job.addplacementVM(Controller.vmList.get(vmIndex).getVmID());
                j++;
                localCost+=currentCost;
            }
            else {
                break;
            }
            if (j == job.getE()) {
                break;
            }
        }

        return SchedulerUtility.placeExecutors(job);
    }

    private static boolean PlaceHybrid(Job job) {

        SchedulerUtility.savePrevMaxT();
        int j=0;
        long durationIncrease=(long)(job.getT_est()* Configurations.networkPenalty);

        while(true) {

            boolean placementFound=false;
            double currentCost=1000000.0;
            int vmIndex=0;

            for (int i = 0 ; i < Controller.vmList.size();i++ ) {
                VM vm = Controller.vmList.get(i);
                if (SchedulerUtility.resourceConstraints(job, vm)) {
                    placementFound=true;
                    double tmpT=vm.getMaxT() >= (Controller.wallClockTime+job.getT_est()+durationIncrease)?0:(Controller.wallClockTime+job.getT_est()+durationIncrease-vm.getMaxT());
                    double tmpCost=vm.getPrice()*tmpT;
                    if(tmpCost<currentCost) {
                        currentCost=tmpCost;
                        vmIndex=i;
                    }
                }
            }
            if(placementFound){
                StatusUpdater.subtractVMresourceV2(Controller.vmList.get(vmIndex),job,durationIncrease);
                job.addplacementVM(Controller.vmList.get(vmIndex).getVmID());
                j++;
            }
            else {
                break;
            }
            if (j == job.getE()) {
                break;
            }
        }
        return SchedulerUtility.placeExecutors(job);
    }

    private static void ResetResources(Job job)
    {
        for(int i=0;i<job.getPlacementList().size();i++) {
            for(int j=0;j< Controller.vmList.size();j++)
                if(Controller.vmList.get(j).getVmID().equals(job.getPlacementList().get(i)))
                    StatusUpdater.addVMresource(Controller.vmList.get(j),job);
        }
        SchedulerUtility.revertMaxT();
        while(job.getPlacementList().size()!=0) {
            job.getPlacementList().remove(0);
        }
    }
}
