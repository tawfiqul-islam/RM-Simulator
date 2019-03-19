package Policy;

import Entity.Job;
import Entity.VM;
import Manager.Controller;
import Manager.StatusUpdater;

import java.util.ArrayList;

class SchedulerUtility {

    static boolean placeExecutors(Job job) {

        if(job.getE()==job.getPlacementList().size()) {

            job.setT_S(Controller.wallClockTime);
            job.setT_W(job.getT_S()-job.getT_A());
            job.setT_F(job.getT_S()+job.getT_est());

            Controller.activeJobs.add(job);
            Controller.jobList.remove(job);

            System.out.println("T: "+Controller.wallClockTime+" SUCCESS: Placed all the executors for job: "+job.getJobID());

            return true;
        }
        else {

            for(int i=0;i<job.getPlacementList().size();i++) {
                for(int j=0;j< Controller.vmList.size();j++)
                    if(Controller.vmList.get(j).getVmID().equals(job.getPlacementList().get(i)))
                        StatusUpdater.addVMresource(Controller.vmList.get(j),job);
            }

            while(job.getPlacementList().size()!=0) {
                job.getPlacementList().remove(0);
            }
            System.out.println("T: "+Controller.wallClockTime+" FAILURE: Not enough resources to place all the executors for job: "+job.getJobID());

            return false;
        }
    }

    static boolean resourceConstraints(Job currentJob, VM vm) {

        return vm.getC_free()>=currentJob.getC()&&vm.getM_free()>=currentJob.getM();
    }
}
