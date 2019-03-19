package Policy;

import Entity.Job;
import Entity.VM;
import Manager.Controller;
import Manager.StatusUpdater;
import java.util.ArrayList;

public class RRScheduler {
    public static boolean findSchedule(Job job) {

        boolean found=false;
        for(int j=0,i=0;j<job.getE();i++) {

            if(i==Controller.vmList.size()){
                i=0;

                if(!found)
                    break;

                found=false;
            }
            VM vm = Controller.vmList.get(i);
            if (SchedulerUtility.resourceConstraints(job,vm)) {
                j++;
                StatusUpdater.subtractVMresource(vm,job);
                job.addplacementVM(vm.getVmID());
                found=true;
            }
        }

        return SchedulerUtility.placeExecutors(job);
    }
}
