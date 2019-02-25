package Workload;

import Entity.Job;
import Entity.VM;
import Manager.Controller;
import Settings.Configurations;

import java.util.UUID;

public class SimpleGen {

    public static void generateJobs() {

        //jobs
        for(int i = 0; i< Configurations.jobTotal; i++) {
            Job job = new Job();
            job.setC(4);
            job.setM(4);
            job.setE(5);
            //job.setJobID(UUID.randomUUID().toString());
            job.setJobID("job-"+i);
            job.setT_A(i*10);
            job.setT_est(100);
            job.setT_D(200+job.getT_A());
            job.setT_W(0);
            Controller.jobList.add(job);
        }
    }

    public static void generateClusterResources() {

        //local resources

        for(int i = 0; i< Configurations.localVMType1; i++) {

            VM vm1 = new VM();

            vm1.setC_Cap(4);
            vm1.setC_free(4);
            vm1.setM_Cap(16);
            vm1.setM_free(16);
            vm1.setActive(false);
            vm1.setMaxT(0);
            vm1.setLocal(true);
            vm1.setPrice(Configurations.localVMPrice1);
            //vm1.setVmID(UUID.randomUUID().toString());
            vm1.setVmID("VM-L-small-"+i);
            vm1.setInstanceFlavor("small");
            Controller.vmList.add(vm1);
        }

        for(int i = 0; i< Configurations.localVMType2; i++) {

            VM vm2 = new VM();

            vm2.setC_Cap(8);
            vm2.setC_free(8);
            vm2.setM_Cap(32);
            vm2.setM_free(32);
            vm2.setActive(false);
            vm2.setMaxT(0);
            vm2.setLocal(true);
            vm2.setPrice(Configurations.localVMPrice2);
            //vm2.setVmID(UUID.randomUUID().toString());
            vm2.setVmID("VM-L-medium-"+i);
            vm2.setInstanceFlavor("medium");
            Controller.vmList.add(vm2);

        }
        for(int i = 0; i< Configurations.localVMType3; i++) {

            VM vm3 = new VM();

            vm3.setC_Cap(12);
            vm3.setC_free(12);
            vm3.setM_Cap(48);
            vm3.setM_free(48);
            vm3.setActive(false);
            vm3.setMaxT(0);
            vm3.setLocal(true);
            vm3.setPrice(Configurations.localVMPrice3);
            //vm3.setVmID(UUID.randomUUID().toString());
            vm3.setVmID("VM-L-large-"+i);
            vm3.setInstanceFlavor("large");
            Controller.vmList.add(vm3);
        }


        //cloud resources
        for(int i = 0; i< Configurations.cloudVMType1; i++) {

            VM vm1 = new VM();

            vm1.setC_Cap(4);
            vm1.setC_free(4);
            vm1.setM_Cap(16);
            vm1.setM_free(16);
            vm1.setActive(false);
            vm1.setMaxT(0);
            vm1.setLocal(false);
            vm1.setPrice(Configurations.cloudVMPrice1);
            //vm1.setVmID(UUID.randomUUID().toString());
            vm1.setVmID("VM-C-small-"+i);
            vm1.setInstanceFlavor("small");
            Controller.vmList.add(vm1);
        }

        for(int i = 0; i< Configurations.cloudVMType2; i++) {

            VM vm2 = new VM();

            vm2.setC_Cap(8);
            vm2.setC_free(8);
            vm2.setM_Cap(32);
            vm2.setM_free(32);
            vm2.setActive(false);
            vm2.setMaxT(0);
            vm2.setLocal(false);
            vm2.setPrice(Configurations.cloudVMPrice2);
            //vm2.setVmID(UUID.randomUUID().toString());
            vm2.setVmID("VM-C-medium-"+i);
            vm2.setInstanceFlavor("medium");
            Controller.vmList.add(vm2);
        }

        for(int i = 0; i< Configurations.cloudVMType3; i++) {

            VM vm3 = new VM();

            vm3.setC_Cap(12);
            vm3.setC_free(12);
            vm3.setM_Cap(48);
            vm3.setM_free(48);
            vm3.setActive(false);
            vm3.setMaxT(0);
            vm3.setLocal(false);
            vm3.setPrice(Configurations.cloudVMPrice3);
            //vm3.setVmID(UUID.randomUUID().toString());
            vm3.setVmID("VM-C-large-"+i);
            vm3.setInstanceFlavor("large");
            Controller.vmList.add(vm3);
        }
    }
}
