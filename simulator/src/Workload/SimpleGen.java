package Workload;

import Entity.Job;
import Entity.VM;
import Manager.Controller;

import java.util.UUID;

public class SimpleGen {

    public static void generateJobs() {

        for(int i=0;i<6;i++) {
            Job nJob = new Job();
            nJob.setC(4);
            nJob.setM(4);
            nJob.setE(10);
            nJob.setJobID(UUID.randomUUID().toString());
            nJob.setT_A(i*40);
            nJob.setT_C(100);
            nJob.setT_D(101+nJob.getT_A());
            nJob.setT_W(0);
            Controller.jobList.add(nJob);
        }
    }

    public static void generateClusterResources() {

        //local resources

        for(int i=0;i<1;i++) {
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

            vm1.setActive(false);
            vm1.setMaxT(0);
            vm2.setActive(false);
            vm2.setMaxT(0);
            vm3.setActive(false);
            vm3.setMaxT(0);


            vm1.setLocal(true);
            vm1.setPrice(0.001);
            vm1.setVmID(UUID.randomUUID().toString());
            vm2.setLocal(true);
            vm2.setPrice(0.002);
            vm2.setVmID(UUID.randomUUID().toString());
            vm3.setLocal(true);
            vm3.setPrice(0.003);
            vm3.setVmID(UUID.randomUUID().toString());

            Controller.vmList.add(vm1);
            Controller.vmList.add(vm2);
            Controller.vmList.add(vm3);
        }


        //cloud resources
        for(int i=0;i<3;i++) {

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

            vm1.setActive(false);
            vm1.setMaxT(0);
            vm2.setActive(false);
            vm2.setMaxT(0);
            vm3.setActive(false);
            vm3.setMaxT(0);

            vm1.setLocal(false);
            vm1.setPrice(0.005);
            vm1.setVmID(UUID.randomUUID().toString());
            vm2.setLocal(false);
            vm2.setPrice(0.010);
            vm2.setVmID(UUID.randomUUID().toString());
            vm3.setLocal(false);
            vm3.setPrice(0.015);
            vm3.setVmID(UUID.randomUUID().toString());

            Controller.vmList.add(vm1);
            Controller.vmList.add(vm2);
            Controller.vmList.add(vm3);
        }

    }
}
