package Entity;

import Settings.Configurations;

import java.util.ArrayList;

public class VM {
    private String vmID;
    private String instanceFlavor;
    private int C_Cap;
    private int M_Cap;
    private int C_free;
    private int M_free;
    private double Price;
    private boolean isActive;
    private boolean isLocal;
    private long maxT;
    private long prevMaxT;
    private long T_used;
    private long T_S;
    private double cost;
    private double resourceTotal;

    private ArrayList<Job> allocationList = new ArrayList<>();

    public String getVmID() {
        return vmID;
    }

    public void setVmID(String vmID) {
        this.vmID = vmID;
    }

    public String getInstanceFlavor() {
        return instanceFlavor;
    }

    public void setInstanceFlavor(String instanceFlavor) {
        this.instanceFlavor = instanceFlavor;
    }

    public int getC_Cap() {
        return C_Cap;
    }

    public void setC_Cap(int c_Cap) {
        C_Cap = c_Cap;
    }

    public int getM_Cap() {
        return M_Cap;
    }

    public void setM_Cap(int m_Cap) {
        M_Cap = m_Cap;
    }

    public int getC_free() {
        return C_free;
    }

    public void setC_free(int c_free) {
        C_free = c_free;
    }

    public int getM_free() {
        return M_free;
    }

    public void setM_free(int m_free) {
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

    public long getPrevMaxT() {
        return prevMaxT;
    }

    public void setPrevMaxT(long prevMaxT) {
        this.prevMaxT = prevMaxT;
    }

    public long getT_used() {
        return T_used;
    }

    public void setT_used(long t_used) {
        T_used = t_used;
    }

    public long getT_S() {
        return T_S;
    }

    public void setT_S(long t_S) {
        T_S = t_S;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getResourceTotal() {
        setResourceTotal();
        return resourceTotal;
    }

    public void setResourceTotal() {
        this.resourceTotal = C_free * (1- Configurations.resourceSplitThreshold) + M_free * Configurations.resourceSplitThreshold;
    }
    @Override
    public String toString() {
        return "VM{" +
                "vmID='" + vmID + '\'' +
                ", instanceFlavor='" + instanceFlavor + '\'' +
                ", C_Cap=" + C_Cap +
                ", M_Cap=" + M_Cap +
                ", Price=" + Price +
                ", isLocal=" + isLocal +
                ", T_used=" + T_used +
                ", cost=" + cost +
                '}';
    }
}
