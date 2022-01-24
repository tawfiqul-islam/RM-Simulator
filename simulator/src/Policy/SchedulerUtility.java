package Policy;

import Entity.Job;
import Entity.VM;
import Manager.Controller;
import Manager.StatusUpdater;
import Settings.Configurations;


class SchedulerUtility {

    static boolean placeExecutors(Job job) {

        if(job.getE()==job.getPlacementList().size()) {

            boolean hybridPlacement=false;
            job.setT_S(Controller.wallClockTime);
            job.setT_W(job.getT_S()-job.getT_A());

            //for hybrid cloud scheduling
            /*
            for(int i=0;i<job.getPlacementList().size();i++)  {
                if (job.getPlacementList().get(i).contains("C")) {
                    hybridPlacement=true;
                    break;
                }
            }
            if(hybridPlacement)
            {
                double durationIncrease=job.getT_est()* Configurations.networkPenalty;
                job.setT_est(job.getT_est()+(long)durationIncrease);
            }*/

            //for DRL scheduling
            hybridPlacement=true;
            String tmp=job.getPlacementList().get(0);
            for(int i=1;i<job.getPlacementList().size();i++)  {
                if (!job.getPlacementList().get(i).equals(tmp)) {
                    hybridPlacement=false;
                    break;
                }
            }
            if(hybridPlacement&&job.getType()!=3)
            {
                double durationIncrease=job.getT_est()* Configurations.networkPenalty;
                job.setT_est(job.getT_est()+(long)durationIncrease);
                job.setFineExecutorPlaced(false);
            }
            else if(!hybridPlacement&&job.getType()==3){
                double durationIncrease=job.getT_est()* Configurations.networkPenalty;
                job.setT_est(job.getT_est()+(long)durationIncrease);
                job.setFineExecutorPlaced(false);
            }
            else{
                job.setFineExecutorPlaced(true);
            }
            job.setT_F(job.getT_S()+job.getT_est());

            Controller.activeJobs.add(job);
            Controller.jobList.remove(job);

            System.out.println("T: "+Controller.wallClockTime+" SUCCESS: Placed all the executors for job: "+job.getJobID());

            return true;
        }
        else {

            for(int i=0;i<job.getPlacementList().size();i++) {
                for(int j=0;j< Controller.vmList.size();j++)
                    if(Controller.vmList.get(j).getVmID().equals(job.getPlacementList().get(i)))
                        StatusUpdater.addVMresource(Controller.vmList.get(j),job);
            }
            revertMaxT();
            while(job.getPlacementList().size()!=0) {
                job.getPlacementList().remove(0);
            }
            System.out.println("T: "+Controller.wallClockTime+" FAILURE: Not enough resources to place all the executors for job: "+job.getJobID());

            return false;
        }
    }

    static boolean resourceConstraints(Job currentJob, VM vm) {

        return vm.getC_free()>=currentJob.getC()&&vm.getM_free()>=currentJob.getM();
    }

    public static void savePrevMaxT() {
        for (int i = 0 ; i < Controller.vmList.size();i++ ) {
            VM vm = Controller.vmList.get(i);
            vm.setPrevMaxT(vm.getMaxT());
        }
    }
    public static void revertMaxT() {
        for (int i = 0 ; i < Controller.vmList.size();i++ ) {
            VM vm = Controller.vmList.get(i);
            vm.setMaxT(vm.getPrevMaxT());
        }
    }
}
