package Policy;

import Entity.VM;
import Entity.Job;
import Manager.Controller;
import Manager.StatusUpdater;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LPSolution;
import scpsolver.problems.LPWizard;
import scpsolver.problems.LPWizardConstraint;
import java.util.logging.Level;
import java.util.ArrayList;

public class ILPScheduler {

    public static boolean findSchedule(Job job) {

        SchedulerUtility.savePrevMaxT();
        /*System.out.println("VM status before ILP");
        for (int i= 0;i< Controller.vmList.size();i++) {
            System.out.println(Controller.vmList.get(i));
        }*/

        LPWizard lpw = new LPWizard();
        lpw.setMinProblem(true);
        //set objective function: agent selection as decision variables
        for (int i = 0; i < Controller.vmList.size(); i++) {

            double vmTime=0;
            if(Controller.vmList.get(i).isActive()) {
                vmTime=Controller.vmList.get(i).getMaxT()-(Controller.wallClockTime+job.getT_est());
                if(vmTime>=0) {
                    vmTime=0;
                }
                else {
                    vmTime=(Controller.wallClockTime+job.getT_est())-Controller.vmList.get(i).getMaxT();
                }
            }
            else {
                vmTime=job.getT_est();
            }
            lpw.plus("x-" + Controller.vmList.get(i).getVmID(), Controller.vmList.get(i).getPrice()*vmTime);
            lpw.setBoolean("x-" + Controller.vmList.get(i).getVmID());
        }


        //set constraints: 1. executor placement constraint-> 1 executor in at most 1 agent
        for (int i = 0; i < job.getE(); i++) {

            LPWizardConstraint tmpsConsP = lpw.addConstraint("pc" + i, 1, "=");

            for (int j = 0; j < Controller.vmList.size(); j++) {
                tmpsConsP.plus("y-" + i + "-" + Controller.vmList.get(j).getVmID(), 1);
                lpw.setBoolean("y-" + i + "-" + Controller.vmList.get(j).getVmID());
            }
        }


        //set constraints: 2. agent capacity constraint
        //cpu
        for (int j = 0; j < Controller.vmList.size(); j++) {

            LPWizardConstraint tmpsConsCC = lpw.addConstraint("cc_cpu" + j, 0, ">=");

            for (int i = 0; i < job.getE(); i++) {
                tmpsConsCC.plus("y-" + i + "-" + Controller.vmList.get(j).getVmID(), job.getC());
            }

            tmpsConsCC.plus("x-" + Controller.vmList.get(j).getVmID(), -Controller.vmList.get(j).getC_free());
        }

        //memory
        for (int j = 0; j < Controller.vmList.size(); j++) {

            LPWizardConstraint tmpsConsCM = lpw.addConstraint("cc_mem" + j, 0, ">=");

            for (int i = 0; i < job.getE(); i++) {
                tmpsConsCM.plus("y-" + i + "-" + Controller.vmList.get(j).getVmID(), Math.ceil(job.getM()));
            }
            tmpsConsCM.plus("x-" + Controller.vmList.get(j).getVmID(), -Controller.vmList.get(j).getM_free());

        }

        //bound constraints
        for (int i = 0; i < Controller.vmList.size(); i++) {
            lpw.addConstraint("bc0-x-" + Controller.vmList.get(i).getVmID(), 0, "<=").plus("x-" + Controller.vmList.get(i).getVmID(), 1);
            lpw.addConstraint("bc1-x-" + Controller.vmList.get(i).getVmID(), 1, ">=").plus("x-" + Controller.vmList.get(i).getVmID(), 1);

        }
        for (int i = 0; i < job.getE(); i++) {
            for (int j = 0; j < Controller.vmList.size(); j++) {
                lpw.addConstraint("bc0-y-" + i + "-" + Controller.vmList.get(j).getVmID(), 0, "<=").plus("y-" + i + "-" + Controller.vmList.get(j).getVmID(), 1);
                lpw.addConstraint("bc1-y-" + i + "-" + Controller.vmList.get(j).getVmID(), 1, ">=").plus("y-" + i + "-" + Controller.vmList.get(j).getVmID(), 1);
            }
        }

        //already turned-on machine constraints
        /* ************************** */
        for (int i = 0; i < Controller.vmList.size(); i++) {
            if (Controller.vmList.get(i).isActive()) {
                lpw.addConstraint("active-x-" + Controller.vmList.get(i).getVmID(), 1, "=").plus("x-" + Controller.vmList.get(i).getVmID(), 1);
            }
        }
        /*Log.SimulatorLogging.log(Level.INFO, ILPScheduler.class.getName() + ": Total Constraints: "+lpw.getLP().getConstraints().size());
        for (int i=0;i< lpw.getLP().getConstraints().size();i++) {
            Log.SimulatorLogging.log(Level.INFO,"constraint-"+i+": "+lpw.getLP().getConstraints().get(i).getName());
        }*/
        //Log.SimulatorLogging.log(Level.INFO, ILPScheduler.class.getName() + ": Solving LP");
        LinearProgramSolver solver = SolverFactory.newDefault();
        LPSolution lpsol = lpw.solve(solver);

        //Log.SimulatorLogging.log(Level.INFO, ILPScheduler.class.getName() + ": Finished solving LP. Objective Value: " + lpsol.getObjectiveValue());
        //Log.SimulatorLogging.log(Level.INFO, ILPScheduler.class.getName() + "\n" + lpsol.toString());
/*
        for (int i = 0; i < tmpVmList.size(); i++) {
            Log.SimulatorLogging.log(Level.INFO, ILPScheduler.class.getName() + ": " + tmpVmList.get(i).getVmID() + "-> CPU-" + tmpVmList.get(i).getC_free() + " MEM-" + tmpVmList.get(i).getM_free());
        }*/
        //Log.SimulatorLogging.log(Level.INFO, ILPScheduler.class.getName() + ": current job->coresPerExec: " + job.getC() + " memPerExec: " + job.getM() + " E: " + job.getE());
        double objVal = lpsol.getObjectiveValue();
        //objVal=Math.floor(objVal * 100 + 0.5) / 100;
        System.out.println("objval: "+objVal);
        //System.out.println("ILP SOlutioN: "+lpsol.toString());
        //(objVal == Math.floor(objVal)) &&

        if ( !Double.isInfinite(objVal) && objVal >= 0 ) {
            //Log.SimulatorLogging.log(Level.INFO, ILPScheduler.class.getName() + ":objval loop inside****** ");
            //System.exit(0);
            // Log.SchedulerLogging.log(Level.INFO,LPSolver.class.getName() + lpsol.toString());
            for (int i = 0; i < job.getE(); i++) {
                for (int j = 0; j < Controller.vmList.size(); j++) {
                    if (lpsol.getBoolean("y-" + i + "-" + Controller.vmList.get(j).getVmID())) {
                        if (Controller.vmList.get(j).getC_free() >= job.getC() &&
                                Controller.vmList.get(j).getM_free() >= job.getM()) {

                            StatusUpdater.subtractVMresource(Controller.vmList.get(j),job);
                            job.addplacementVM(Controller.vmList.get(j).getVmID());
                            //Log.SimulatorLogging.log(Level.INFO, ILPScheduler.class.getName() + "Added Agent " + job.getPlacementList().get(job.getPlacementList().size() - 1));
                            break;
                        }
                    }
                }
            }

            //TODO Resource Reservation for real implemnetation
            //SchedulerUtil.resourceReservation(chosenVMs, job, classVar);
            //calculate time //TODO

        }
        else{
            System.out.println("Infeasible model");
        }
        //TODO where to check?
        return SchedulerUtility.placeExecutors(job);
    }
}