package Policy;
import Entity.Job;
import Entity.VM;
import Manager.Controller;
import Manager.StatusUpdater;
import Manager.Utility;

import java.util.Collections;

public class AEPScheduler {

    public static boolean findSchedule(Job job) {

        if (job.getType()==3) {
            return RRScheduler.findSchedule(job);
        }
        else {
            return RRConsolidate.findSchedule(job);
        }
    }
}