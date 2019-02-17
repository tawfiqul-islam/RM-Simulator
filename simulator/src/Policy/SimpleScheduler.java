package Policy;

import Entity.Job;
import Entity.VM;
import Manager.Controller;
import Manager.StatusUpdater;

public class SimpleScheduler {

    public static boolean findSchedule(Job currentJob) {
        System.out.println("trying to find schedule");

        for(int j=0,i=0;j<currentJob.getE()&&i<Controller.vmList.size(); ) {

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

        System.out.println(currentJob.getE());
        System.out.println(currentJob.getPlacementList().size());

        if(currentJob.getE()==currentJob.getPlacementList().size()) {
            currentJob.setT_S(Controller.wallClockTime);
            currentJob.setT_W(currentJob.getT_S()-currentJob.getT_A());
            currentJob.setT_C(currentJob.getT_C()+currentJob.getT_W());

            Controller.activeJobs.add(currentJob);
            Controller.jobList.remove(currentJob);

            System.out.println("Successfully placed all the executors for job: "+currentJob.getJobID());
            return true;
        }
        else {
            for(int i=0;i<currentJob.getPlacementList().size();i++) {
                StatusUpdater.addVMresource(currentJob.getPlacementList().get(i),currentJob);
            }
            while(currentJob.getPlacementList().size()!=0) {
                currentJob.getPlacementList().remove(0);
            }
            return false;
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
