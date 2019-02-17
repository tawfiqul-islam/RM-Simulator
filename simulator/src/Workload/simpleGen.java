package Workload;

import Entity.Job;
import Entity.VM;
import Manager.Controller;

public class simpleGen {

    void generateJobs() {
        Job nJob = new Job();
        nJob.setC(2);
        nJob.setM(4);
        nJob.setE(3);

        for(int i=0;i<50;i++) {
            nJob.setT_A(i*50);
            nJob.setT_C(50);
            nJob.setT_D(60);
            nJob.setT_W(0);
            Controller.jobList.add(nJob);
        }
    }
    void generateClusterResources() {

        VM vm1 = new VM();
        VM vm2 = new VM();
        VM vm3 = new VM();

        vm1.setC_Cap(4);
        vm1.setC_free(4);
        vm2.setC_Cap(8);
        vm2.setC_free(8);
        vm3.setC_Cap(12);
        vm3.setC_free(12);

        vm1.setM_Cap(16);
        vm1.setM_free(16);
        vm2.setM_Cap(32);
        vm2.setM_free(32);
        vm3.setM_Cap(48);
        vm3.setM_free(48);



    }
}
