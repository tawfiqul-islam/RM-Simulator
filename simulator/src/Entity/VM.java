package Entity;

import java.util.ArrayList;

public class VM {
    private String vmID;
    private int C_Cap;
    private double M_Cap;
    private int C_free;
    private double M_free;
    private double Price;
    private boolean isActive;
    private boolean isLocal;
    private long maxT;
    private ArrayList<Job> allocationList = new ArrayList<>();

    public String getVmID() {
        return vmID;
    }

    public void setVmID(String vmID) {
        this.vmID = vmID;
    }

    public int getC_Cap() {
        return C_Cap;
    }

    public void setC_Cap(int c_Cap) {
        C_Cap = c_Cap;
    }

    public double getM_Cap() {
        return M_Cap;
    }

    public void setM_Cap(double m_Cap) {
        M_Cap = m_Cap;
    }

    public int getC_free() {
        return C_free;
    }

    public void setC_free(int c_free) {
        C_free = c_free;
    }

    public double getM_free() {
        return M_free;
    }

    public void setM_free(double m_free) {
        M_free = m_free;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public long getMaxT() {
        return maxT;
    }

    public void setMaxT(long maxT) {
        this.maxT = maxT;
    }

    @Override
    public String toString() {
        return "VM{" +
                "vmID='" + vmID + '\'' +
                ", C_Cap=" + C_Cap +
                ", M_Cap=" + M_Cap +
                ", C_free=" + C_free +
                ", M_free=" + M_free +
                ", Price=" + Price +
                ", isActive=" + isActive +
                ", isLocal=" + isLocal +
                ", maxT=" + maxT +
                ", allocationList=" + allocationList +
                '}';
    }
}
