package Entity;

import java.util.ArrayList;

public class Job {
    private int C;
    private double M;
    private int E;
    private long T_A;
    private long T_D;
    private long T_W;
    private long T_C;
    private ArrayList<VM> placementList = new ArrayList<>();

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

    public long getT_C() {
        return T_C;
    }

    public void setT_C(long t_C) {
        T_C = t_C;
    }
}
