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
        ArrayList<VM> tmpVmList = (ArrayList<VM>) Controller.vmList.clone();

        LPWizard lpw = new LPWizard();
        lpw.setMinProblem(true);
        //set objective function: agent selection as decision variables
        for (int i = 0; i < tmpVmList.size(); i++) {

            double vmTime=0;
            if(tmpVmList.get(i).isActive()) {
                vmTime=tmpVmList.get(i).getMaxT()-(Controller.wallClockTime+job.getT_est());
                if(vmTime<=0) {
                    vmTime=0;
                }
                else {
                    vmTime=(Controller.wallClockTime+job.getT_est())-tmpVmList.get(i).getMaxT();
                }
            }
            else {
                vmTime=job.getT_est();
            }
            lpw.plus("x-" + tmpVmList.get(i).getVmID(), tmpVmList.get(i).getPrice()*vmTime);
            lpw.setBoolean("x-" + tmpVmList.get(i).getVmID());
        }


        //set constraints: 1. executor placement constraint-> 1 executor in at most 1 agent
        for (int i = 0; i < job.getE(); i++) {

            LPWizardConstraint tmpsConsP = lpw.addConstraint("pc" + i, 1, "=");

            for (int j = 0; j < tmpVmList.size(); j++) {
                tmpsConsP.plus("y-" + i + "-" + tmpVmList.get(j).getVmID(), 1);
                lpw.setBoolean("y-" + i + "-" + tmpVmList.get(j).getVmID());
            }
        }


        //set constraints: 2. agent capacity constraint
        //cpu
        for (int j = 0; j < tmpVmList.size(); j++) {

            LPWizardConstraint tmpsConsCC = lpw.addConstraint("cc_cpu" + j, 0, ">=");

            for (int i = 0; i < job.getE(); i++) {
                tmpsConsCC.plus("y-" + i + "-" + tmpVmList.get(j).getVmID(), job.getC());
            }

            tmpsConsCC.plus("x-" + tmpVmList.get(j).getVmID(), -tmpVmList.get(j).getC_free());
        }

        //memory
        for (int j = 0; j < tmpVmList.size(); j++) {

            LPWizardConstraint tmpsConsCM = lpw.addConstraint("cc_mem" + j, 0, ">=");

            for (int i = 0; i < job.getE(); i++) {
                tmpsConsCM.plus("y-" + i + "-" + tmpVmList.get(j).getVmID(), Math.ceil(job.getM()));
            }
            tmpsConsCM.plus("x-" + tmpVmList.get(j).getVmID(), -tmpVmList.get(j).getM_free());

        }

        //bound constraints
        for (int i = 0; i < tmpVmList.size(); i++) {
            lpw.addConstraint("bc0-x-" + tmpVmList.get(i).getVmID(), 0, "<=").plus("x-" + tmpVmList.get(i).getVmID(), 1);
            lpw.addConstraint("bc1-x-" + tmpVmList.get(i).getVmID(), 1, ">=").plus("x-" + tmpVmList.get(i).getVmID(), 1);

        }
        for (int i = 0; i < job.getE(); i++) {
            for (int j = 0; j < tmpVmList.size(); j++) {
                lpw.addConstraint("bc0-y-" + i + "-" + tmpVmList.get(j).getVmID(), 0, "<=").plus("y-" + i + "-" + tmpVmList.get(j).getVmID(), 1);
                lpw.addConstraint("bc1-y-" + i + "-" + tmpVmList.get(j).getVmID(), 1, ">=").plus("y-" + i + "-" + tmpVmList.get(j).getVmID(), 1);
            }
        }

        //already turned-on machine constraints
        /* ************************** */
        for (int i = 0; i < tmpVmList.size(); i++) {
            if (tmpVmList.get(i).isActive()) {
                lpw.addConstraint("active-x-" + tmpVmList.get(i).getVmID(), 1, "=").plus("x-" + tmpVmList.get(i).getVmID(), 1);
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
        //(objVal == Math.floor(objVal)) &&
        if ( !Double.isInfinite(objVal) && objVal > 0 ) {
            //Log.SimulatorLogging.log(Level.INFO, ILPScheduler.class.getName() + ":objval loop inside****** ");
            //System.exit(0);
            // Log.SchedulerLogging.log(Level.INFO,LPSolver.class.getName() + lpsol.toString());
            for (int i = 0; i < job.getE(); i++) {
                for (int j = 0; j < tmpVmList.size(); j++) {
                    if (lpsol.getBoolean("y-" + i + "-" + tmpVmList.get(j).getVmID())) {
                        if (tmpVmList.get(j).getC_free() >= job.getC() &&
                                tmpVmList.get(j).getM_free() >= job.getM()) {

                            StatusUpdater.subtractVMresource(tmpVmList.get(j),job);
                            job.addplacementVM(tmpVmList.get(j).getVmID());
                            //Log.SimulatorLogging.log(Level.INFO, ILPScheduler.class.getName() + "Added Agent " + job.getPlacementList().get(job.getPlacementList().size() - 1));
                            break;
                        }
                    }
                }
            }

            //TODO Resource Reservationfor real implemnetation
            //SchedulerUtil.resourceReservation(chosenVMs, job, classVar);
            //calculate time //TODO

        }
        else{
            System.out.println("Infeasible model");
        }
        //TODO where to check?
        return SchedulerUtility.placeExecutors(tmpVmList,job);
    }
}