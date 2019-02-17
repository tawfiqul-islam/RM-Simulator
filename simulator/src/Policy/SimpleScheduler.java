package Policy;

import Entity.Job;
import Entity.VM;
import Manager.Controller;
import Manager.StatusUpdater;

public class SimpleScheduler {

    public static void findSchedule(Job currentJob) {

        for(int j=0,i=0;j<currentJob.getE()||i==Controller.vmList.size(); ) {

            VM vm = Controller.vmList.get(i);
            if (resourceConstraints(currentJob,vm)) {
                j++;
                StatusUpdater.substractVMresource(vm,currentJob);
                vm.setActive(true);
                if(vm.getMaxT()<currentJob.getT_C()) {
                    vm.setMaxT(currentJob.getT_C());
                }
            }
            else {
                i++;
            }
        }

        if(currentJob.getE()==currentJob.getPlacementList().size()) {
            Controller.activeJobs.add(currentJob);
            Controller.jobList.remove(currentJob);
        }
        else {
            for(int i=0;i<currentJob.getPlacementList().size();i++) {
                StatusUpdater.addVMresource(currentJob.getPlacementList().get(i),currentJob);
            }
        }
    }

    static boolean resourceConstraints(Job currentJob, VM vm) {

        if(vm.getC_free()>=currentJob.getC()&&vm.getM_free()>=currentJob.getM()) {
            currentJob.addplacementVM(vm);
            return true;
        }
        else return false;
    }
}
