package Schedulers;
import java.awt.Color;
import static java.lang.Integer.parseInt;
public class Process {

    String name;
//    String color;
    Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    double arrivalTime;
    double burstTime;
    double priorityNumber;
    double AGATFactor;
    double quantum;
    double waitingTime;
    double BurstTime;
    double turnaround;


    public double getReadyQTime() {
        return readyQTime;
    }

    public void setReadyQTime(double readyQTime) {
        this.readyQTime = readyQTime;
    }

    double readyQTime;
    public double getTurnaround() {
        return turnaround;
    }

    public void setTurnaround(double turnaround) {
        this.turnaround = turnaround;
    }


    public double getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(double waitingTime) {
        this.waitingTime = waitingTime;
    }

    public Process(){
    }
    public double getRealQuantum() {
        return realQuantum;
    }

    public void setRealQuantum(double realQuantum) {
        this.realQuantum = realQuantum;
    }

    double realQuantum;

    public double getQuantum() {
        return quantum;
    }

    public void setQuantum(double quantum) {
        this.quantum = quantum;
    }

    public double getAGATFactor() {
        return AGATFactor;
    }

    public void setAGATFactor(double AGATFactor) {
        this.AGATFactor = AGATFactor;
    }

    public Process(String name,Color color, double arrivalTime, double burstTime, double priorityNumber,double quantum){
        this.arrivalTime = arrivalTime;
        this.name = name;
        this.color = color;
        this.burstTime = burstTime;
        this.BurstTime = burstTime;
        this.priorityNumber = priorityNumber;
        this.quantum = quantum;
        this.realQuantum = quantum;
        this.AGATFactor = -1;
        this.waitingTime = 0 ;
        this.turnaround = 0;
        this.readyQTime = this.arrivalTime;
    }
    public Process(Process pr){
        this.arrivalTime = pr.getArrivalTime();
        this.name = pr.getName();
        this.color = pr.getColor();
        this.BurstTime = pr.BurstTime;
        this.burstTime = pr.getBurstTime();
        this.priorityNumber = pr.getPriorityNumber();
        this.quantum = pr.getQuantum();
        this.realQuantum = pr.getRealQuantum();
        this.AGATFactor = pr.getAGATFactor() ;
        this.waitingTime = 0 ;
        this.turnaround = 0;
        this.readyQTime = this.arrivalTime;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setBurstTime(double burstTime) {
        this.burstTime = burstTime;
    }

    public void setPriorityNumber(double priorityNumber) {
        this.priorityNumber = priorityNumber;
    }

    public double getBurstTime() {
        return burstTime;
    }

    public double getPriorityNumber() {
        return priorityNumber;
    }

    public String getName() {
        return name;
    }
    
    
    
        
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + parseInt(name);
        return result;
    }


    public boolean equals(Object obj) {
        System.out.println("test");
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        Process other = (Process) obj;
        if (name != other.name) {
            return false;
        }
        return true;
    }

}