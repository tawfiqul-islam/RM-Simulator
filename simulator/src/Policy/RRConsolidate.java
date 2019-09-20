package Policy;

import Entity.Job;
import Entity.VM;
import Manager.Controller;
import Manager.StatusUpdater;
import Manager.Utility;

import java.util.Collections;

public class RRConsolidate {

    public static boolean findSchedule(Job job) {

        Collections.sort(Controller.vmList, new Utility.VMComparatorConsolidate());

        for(int j=0,i=0;j<job.getE()&&i<Controller.vmList.size(); ) {

            VM vm = Controller.vmList.get(i);
            if (SchedulerUtility.resourceConstraints(job,vm)) {
                j++;
                StatusUpdater.subtractVMresource(vm,job);
                job.addplacementVM(vm.getVmID());
            }
            else {
                i++;
            }
        }

        return SchedulerUtility.placeExecutors(job);
    }
}