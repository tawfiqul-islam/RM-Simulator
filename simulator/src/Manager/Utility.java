package Manager;

import Entity.Job;
import Entity.VM;

import java.util.Comparator;

public class Utility {

    public static class VMComparator implements Comparator<VM> {
        @Override
        public int compare(VM a, VM b) {

            /*if (a.getResourceTotal() < b.getResourceTotal()) {
                return -1;
            } else if (a.getResourceTotal() > b.getResourceTotal()) {
                return 1;
            } else {
                return 0;
            }*/

            if(!a.isActive()&&b.isActive()) {
                return -1;
            }
            else if(a.isActive()&&!b.isActive()) {
                return 1;
            }
            else if(a.getPrice()<b.getPrice()) {
                return -1;
            }
            else if(a.getPrice()>b.getPrice()) {
                return 1;
            }
            else {
                //to sort agents in an increasing order of resource availability (small to big)
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

    public static class JobComparatorEDFplusFCFS implements Comparator<Job> {
        @Override
        public int compare(Job a, Job b) {

            if (a.getT_D() < b.getT_D()) {
                return -1;
            } else if (a.getT_D() > b.getT_D()) {
                return 1;
            } else {
                if (a.getT_A() < b.getT_A()) {
                    return -1;
                } else if (a.getT_A() > b.getT_A()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    public static class JobComparatorFCFS implements Comparator<Job> {
        @Override
        public int compare(Job a, Job b) {

            if (a.getT_A() < b.getT_A()) {
                return -1;
            } else if (a.getT_A() > b.getT_A()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static Comparator<Job> JobComparator = new Comparator<Job>() {

        @Override
        public int compare(Job a, Job b) {

            //to sort jobs with increasing order of completion time
            if (a.getT_F() < b.getT_F()) {
                return -1;
            } else if (a.getT_F() > b.getT_F()) {
                return 1;
            } else {
                return 0;
            }
        }
    };
}
