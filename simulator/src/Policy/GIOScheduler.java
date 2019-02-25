package Policy;

import Entity.Job;
import Entity.VM;
import Manager.Controller;
import Manager.StatusUpdater;

import java.util.ArrayList;

public class GIOScheduler {

    public static boolean findSchedule(Job job) {
        ArrayList <VM> tmpVmList =(ArrayList<VM>)Controller.vmList.clone();

        int j=0;
        while(true) {

            boolean placementFound=false;
            double currentCost=1000000.0;
            int vmIndex=0;

            for (int i = 0 ; i < tmpVmList.size();i++ ) {
                VM vm = tmpVmList.get(i);
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
                StatusUpdater.subtractVMresource(tmpVmList.get(vmIndex),job);
                job.addplacementVM(tmpVmList.get(vmIndex).getVmID());
                j++;
            }
            else {
                break;
            }
            if (j == job.getE()) {
                break;
            }
        }

        return SchedulerUtility.placeExecutors(tmpVmList,job);
    }
}
