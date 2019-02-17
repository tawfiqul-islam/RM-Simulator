package Manager;

import Entity.Job;

import java.util.Comparator;

public class Utility {

    /*
    static class AgentComparator implements Comparator<Agent> {
        @Override
        public int compare(Agent a, Agent b) {

            if(!a.isActive()&&b.isActive()) {
                return -1;
            }
            else if(a.isActive()&&!b.isActive()) {
                return 1;
            }
            else {
                //to sort agents in an increasing order of resource capacity (small to big)
                if (a.getResourceTotal() < b.getResourceTotal()) {
                    return -1;
                } else if (a.getResourceTotal() > b.getResourceTotal()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }
   */

    public static Comparator<Job> JobComparator = new Comparator<Job>() {

        @Override
        public int compare(Job a, Job b) {

            //to sort jobs with increasing order of completion time
            if (a.getT_C() < b.getT_C()) {
                return -1;
            } else if (a.getT_C() > b.getT_C()) {
                return 1;
            } else {
                return 0;
            }
        }
    };
}
