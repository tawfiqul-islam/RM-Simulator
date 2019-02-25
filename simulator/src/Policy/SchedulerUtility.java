package Policy;

import Entity.Job;
import Entity.VM;
import Manager.Controller;

import java.util.ArrayList;

class SchedulerUtility {

    static boolean placeExecutors(ArrayList<VM> vmList, Job job) {

        if(job.getE()==job.getPlacementList().size()) {

            job.setT_S(Controller.wallClockTime);
            job.setT_W(job.getT_S()-job.getT_A());
            job.setT_F(job.getT_S()+job.getT_est());

            Controller.activeJobs.add(job);
            Controller.jobList.remove(job);

            System.out.println("T: "+Controller.wallClockTime+" SUCCESS: Placed all the executors for job: "+job.getJobID());

            Controller.vmList=(ArrayList<VM>)vmList.clone();
            return true;
        }
        else {
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
