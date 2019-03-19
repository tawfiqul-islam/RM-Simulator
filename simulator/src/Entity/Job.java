package Entity;

import Manager.Controller;

import java.util.ArrayList;

public class Job {
    private String jobID;
    private int C;
    private double M;
    private int E;
    private long T_A;
    private long T_S;
    private long T_D;
    private long T_W;
    private long T_F;
    private long T_est;
    private boolean deadlineMet;
    private ArrayList<String> placementList = new ArrayList<String>();

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public int getC() {
        return C;
    }

    public void setC(int c) {
        C = c;
    }

    public double getM() {
        return M;
    }

    public void setM(double m) {
        M = m;
    }

    public int getE() {
        return E;
    }

    public void setE(int e) {
        E = e;
    }

    public long getT_A() {
        return T_A;
    }

    public void setT_A(long t_A) {
        T_A = t_A;
    }

    public long getT_S() {
        return T_S;
    }

    public void setT_S(long t_S) {
        T_S = t_S;
    }

    public long getT_D() {
        return T_D;
    }

    public void setT_D(long t_D) {
        T_D = t_D;
    }

    public long getT_W() {
        return T_W;
    }

    public void setT_W(long t_W) {
        T_W = t_W;
    }

    public long getT_F() {
        return T_F;
    }

    public void setT_F(long t_F) {
        T_F = t_F;
    }

    public long getT_est() {
        return T_est;
    }

    public void setT_est(long t_est) {
        T_est = t_est;
    }

    public boolean isDeadlineMet() {
        return deadlineMet;
    }

    public void setDeadlineMet(boolean deadlineMet) {
        this.deadlineMet = deadlineMet;
    }

    public void addplacementVM(String vmID) {
        placementList.add(vmID);
    }

    public ArrayList<String> getPlacementList() {
        return placementList;
    }

    public void setPlacementList(ArrayList<String> placementList) {
        this.placementList = placementList;
    }

    public String getPlacementListStr(){
        String str="";
        for(int i=0;i<placementList.size();i++) {
            str+=placementList.get(i)+" ";
        }
        return str;
    }
    @Override
    public String toString() {

        return "Job{" +
                "jobID='" + jobID + '\'' +
                ", C=" + C +
                ", M=" + M +
                ", E=" + E +
                ", T_A=" + T_A +
                ", T_S=" + T_S +
                ", T_D=" + T_D +
                ", T_W=" + T_W +
                ", T_F=" + T_F +
                ", T_est=" + T_est +
                ", deadlineMet=" + deadlineMet +
                ", placementList=" + getPlacementListStr() +
                '}';
    }
}
