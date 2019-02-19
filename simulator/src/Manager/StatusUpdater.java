package Manager;

import Entity.Job;
import Entity.VM;

public class StatusUpdater {

    public static void subtractVMresource(VM vm, Job job) {
        vm.setC_free(vm.getC_free()-job.getC());
        vm.setM_free(vm.getM_free()-job.getM());
        if(!vm.isActive()) {
            vm.setActive(true);
            vm.setT_S(Controller.wallClockTime);
        }
    }

    public static void addVMresource(VM vm, Job job){
        vm.setC_free(vm.getC_free()+job.getC());
        vm.setM_free(vm.getM_free()+job.getM());

        if((vm.getC_free()==vm.getC_Cap())&&(vm.getM_free()==vm.getM_Cap())) {
            vm.setActive(false);
            vm.setT_used(vm.getT_used()+(Controller.wallClockTime-vm.getT_S()));
        }
    }
}
