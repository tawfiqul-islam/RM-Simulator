package Policy;

import Entity.Job;
import Entity.VM;
import Manager.Controller;
import Manager.StatusUpdater;

public class SimpleScheduler {

    public static boolean findSchedule(Job currentJob) {

        for(int j=0,i=0;j<currentJob.getE()&&i<Controller.vmList.size(); ) {

            VM vm = Controller.vmList.get(i);
            if (resourceConstraints(currentJob,vm)) {
                j++;
                StatusUpdater.substractVMresource(vm,currentJob);
                currentJob.addplacementVM(vm);
                vm.setActive(true);
                if(vm.getMaxT()<currentJob.getT_F()) {
                    vm.setMaxT(currentJob.getT_F());
                }
            }
            else {
                i++;
            }
        }

        if(currentJob.getE()==currentJob.getPlacementList().size()) {
            currentJob.setT_S(Controller.wallClockTime);
            currentJob.setT_W(currentJob.getT_S()-currentJob.getT_A());
            currentJob.setT_F(currentJob.getT_S()+currentJob.getT_est());

            Controller.activeJobs.add(currentJob);
            Controller.jobList.remove(currentJob);

            System.out.println("T: "+Controller.wallClockTime+" SUCCESS: Placed all the executors for job: "+currentJob.getJobID());
            return true;
        }
        else {
            for(int i=0;i<currentJob.getPlacementList().size();i++) {
                StatusUpdater.addVMresource(currentJob.getPlacementList().get(i),currentJob);
            }
            while(currentJob.getPlacementList().size()!=0) {
                currentJob.getPlacementList().remove(0);
            }
            System.out.println("T: "+Controller.wallClockTime+" FAILURE: Not enough resources to place all the executors for job: "+currentJob.getJobID());
            return false;
        }
    }

    static boolean resourceConstraints(Job currentJob, VM vm) {

        if(vm.getC_free()>=currentJob.getC()&&vm.getM_free()>=currentJob.getM()) {
            return true;
        }
        else return false;
    }
}
