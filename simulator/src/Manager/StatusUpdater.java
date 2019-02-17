package Manager;

import Entity.Job;
import Entity.VM;

public class StatusUpdater {

    public static void updateStates() {
        Job currentJob;
        long maxT=0;

    }

    public static void substractVMresource(VM vm, Job job) {
        vm.setC_free(vm.getC_free()-job.getC());
        vm.setM_free(vm.getM_free()-job.getM());
    }

    public static void addVMresource(VM vm, Job job){
        vm.setC_free(vm.getC_free()+job.getC());
        vm.setM_free(vm.getM_free()+job.getM());

        if((vm.getC_free()==vm.getC_Cap())&&(vm.getM_free()==vm.getM_Cap())) {
            vm.setActive(false);
        }
    }
}
