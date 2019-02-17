package Manager;

import Entity.Job;

public class StatusUpdater {

    public static void updateStates() {
        Job currentJob;
        long maxT=0;
        for(int i=0;i<Controller.activeJobs.size();i++) {
            currentJob=Controller.activeJobs.get(i);
            if(currentJob.getT_C()>maxT) {
                maxT=currentJob.getT_C();
            }
        }

    }

}
