package Policy;

import Entity.Job;
import Entity.VM;
import Manager.Controller;
import Manager.StatusUpdater;

import java.util.ArrayList;

public class FirstFitScheduler {

    public static boolean findSchedule(Job job) {

        //Collections.sort(Controller.vmList, new Utility.VMComparator());

        ArrayList<VM> tmpVmList =(ArrayList<VM>)Controller.vmList.clone();

        for(int j=0,i=0;j<job.getE()&&i<tmpVmList.size(); ) {

            VM vm = tmpVmList.get(i);
            if (SchedulerUtility.resourceConstraints(job,vm)) {
                j++;
                StatusUpdater.subtractVMresource(vm,job);
                job.addplacementVM(vm.getVmID());
            }
            else {
                i++;
            }
        }

        return SchedulerUtility.placeExecutors(tmpVmList,job);
    }
}