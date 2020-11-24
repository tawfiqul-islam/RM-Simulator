package Policy;

import Entity.Job;
import Entity.VM;
import Manager.Controller;
import Manager.StatusUpdater;

import java.util.ArrayList;

public class GIOScheduler {

    public static boolean findSchedule(Job job) {

        SchedulerUtility.savePrevMaxT();
        int j=0;
        while(true) {

            boolean placementFound=false;
            double currentCost=1000000.0;
            int vmIndex=0;

            for (int i = 0 ; i < Controller.vmList.size();i++ ) {
                VM vm = Controller.vmList.get(i);
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
            }
            else {
                break;
            }
            if (j == job.getE()) {
                break;
            }
        }

        //TODO implement DeadlineFailed Queue and push the predicted fails in that queue and continue
        //TODO change settings files to reflect these changes
        return SchedulerUtility.placeExecutors(job);
    }
}
