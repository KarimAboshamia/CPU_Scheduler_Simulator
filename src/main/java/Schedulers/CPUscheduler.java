package Schedulers;

import java.awt.Color;
import java.util.*;
import java.lang.Math;

public class CPUscheduler{
    private ArrayList<Process> processes;
    private ArrayList<Process> AgatProcesses;
    int contextSwitch;
    int processNum;
    double AVGWaiting;
    double AVGTurn;
    ArrayList<String> pnames;
    ArrayList<Double> turnround;
    ArrayList<Double> agatWaitingTimes ;
    private ArrayList<Process> AGATResults ;
    private ArrayList<Process> SRTFOrder;

    public CPUscheduler() {
        this.processes = new ArrayList<Process>();
        this.processNum = 0;
        this.contextSwitch = 0;
    }

    ArrayList getAGATResults()
    {
        return AGATResults;
    }

    public CPUscheduler(int contextSwitch, int processNum) {
        this.processes = new ArrayList<Process>();
        this.processNum = processNum;
        this.contextSwitch = contextSwitch;
    }


    public void add(Process p){
        processes.add(p);
    }

    boolean isEmpty()
    {
        return processes.isEmpty();
    }
    
    public void shortestRemainingTimeFirst(){
        turnround = new ArrayList<>();
        pnames = new ArrayList<>();
        ArrayList<Process> Ps =  new ArrayList<Process>();
        for (int nedo = 0 ; nedo < processNum ; nedo++)
        {
            Ps.add(new Process(processes.get(nedo)));
        }
        ArrayList<Process> ready = new ArrayList<Process>();
        ArrayList<Double> waiting = new ArrayList<>();
        
        ArrayList<Process> Porder = new ArrayList<Process>();
        ArrayList<Double> Norder = new ArrayList<>();
        double time = 0;
        int j = 0;
        Process execute = Ps.get(0);
        int num=0;
        int x=1;
        while(processNum - turnround.size() != 0) {
            int i ;

            // adding the process to the ready queue
            if (j < processNum) { //condition to avoid selecting out of range element
                while (Ps.get(j).arrivalTime <= time) {
                    ready.add(Ps.get(j));
                    j += 1;
                    if (j == processNum) break; //condition to avoid selecting out of range element
                }
            }
            if (!ready.isEmpty()) {
                double min = 1000;
                // searching for the shortest burst time process
                for (i = 0; i < ready.size(); i++) {
                    if (ready.get(i).burstTime < min) {
                        min = ready.get(i).burstTime;
                        num = i;
                    }
                }
                if (ready.get(num).getName().equals(execute.getName())) {
                    execute.burstTime -= 1;
                    time += 1;
                    Porder.add(execute);
                    Norder.add(time);
                }else if(x%4==0){
                    execute = ready.get(num);
                    execute.burstTime -= 1;
                    time+=1;
                    Porder.add(execute);
                    Norder.add(time);
                } else {
                    time += contextSwitch;
                    Porder.add(execute);
                    Norder.add(time);
                    execute = ready.get(num);
                    execute.burstTime -= 1;
                    time+=1;
                    Porder.add(execute);
                    Norder.add(time);
                }
                if (execute.burstTime == 0) {
                    double TurnRound = time - execute.arrivalTime;
                    waiting.add(TurnRound - execute.BurstTime);
                    turnround.add(TurnRound);
                    pnames.add(execute.getName());
                    ready.remove(num);
                    x += 1;
                    if (x % 4 == 0 && !ready.isEmpty()) {
                        time+=contextSwitch;
                        Porder.add(execute);
                        Norder.add(time);
                        for (i = 0; i < ready.size(); i++) {
                            waiting.add(time - ready.get(i).arrivalTime);
                            turnround.add((time - ready.get(i).arrivalTime) + ready.get(i).burstTime);
                            pnames.add(ready.get(i).getName());
                            for(int number = 0; number<ready.get(i).burstTime; number++){
                                time+=1;
                                Porder.add(ready.get(i));
                                Norder.add(time);
                            }
                            time+=contextSwitch;
                            Porder.add(ready.get(i));
                            Norder.add(time);
                            execute = ready.get(i);
                            ready.remove(i);

                        }
                    }
                }
            }else {
                time+=1;
                Porder.add(new Process());
                Norder.add(time);
            }
        }

        System.out.println("Shortest remaining time first");
        System.out.println(pnames);
        System.out.println("waiting time");
        System.out.println(waiting);
        System.out.println("average waiting time");
        double sum = 0;
        for(Double d : waiting) sum += d;
        System.out.println(sum/waiting.size());
        AVGWaiting = sum/waiting.size();
        System.out.println("turn round time");
        System.out.println(turnround);
        System.out.println("average turn round time");
        sum = 0;
        for(Double d : turnround) sum += d;
        System.out.println(sum/turnround.size());
        SRTFOrder = Porder;
            System.out.print(Norder);
        AVGTurn = sum/turnround.size();
        for(int i = 0; i<processNum;i++){
            for(j = 0; j<processNum;j++){
                if(processes.get(i).getName().equals(pnames.get(j))){
                    processes.get(i).setWaitingTime(waiting.get(j));
                    processes.get(i).setTurnaround(turnround.get(j));
                }
            }
        }
    }
    
    ArrayList<Process> getSRTFOrder()
    {
        return SRTFOrder;
    }
    
    public void shortestJobFirst(){
        //define wanted variables
        turnround = new ArrayList<>();
        pnames = new ArrayList<>();
        ArrayList<Process> Ps =  new ArrayList<Process>();
        for (int nedo = 0 ; nedo < processNum ; nedo++)
        {
            Ps.add(new Process(processes.get(nedo)));
        }
        ArrayList<Process> ready = new ArrayList<Process>();
        ArrayList<Double> waiting = new ArrayList<>();
        
        double time = 0;
        int j = 0;
        int x = 0;
        // working till all process are finished
        while(processNum - waiting.size() != 0){
            // adding the process to the ready queue
            if(j < processNum){ //condition to avoid selecting out of range element
                while(Ps.get(j).arrivalTime<=time ){
                    ready.add(Ps.get(j));
                    j+=1;
                    if(j==processNum)break; //condition to avoid selecting out of range element
                }
            }
            // condition to check if the ready queue have elements to run
            if(!ready.isEmpty()){
                double min = 1000;
                Process excecute = new Process();
                // searching for the shortest burst time process
                for (int i= 0; i<ready.size();i++ ){
                    if(ready.get(i).burstTime<min){
                        min = ready.get(i).burstTime;
                        excecute = ready.get(i);
                    }
                }
                // calculating the waiting time and turn round time
                double waitingTime = time - excecute.getArrivalTime();
                waiting.add(waitingTime);
                time+=excecute.burstTime ;
                turnround.add(waitingTime+ excecute.getBurstTime());
                pnames.add(excecute.getName());
                x+=1;
                //remove the process from ready queue
                for (int i= 0; i<ready.size();i++ ){
                    if(ready.get(i).getName().equals(excecute.getName())){
                        ready.remove(i);
                    }
                }
                //handling the starvation problem
                // by emptying the ready queue after executing 3 shortest process
                if(!ready.isEmpty() && x%3 == 0){
                    for (int i= 0; i<ready.size();i++ ){
                        excecute = ready.get(i);
                        waitingTime = time - excecute.getArrivalTime();
                        waiting.add(waitingTime);
                        time+=excecute.burstTime;
                        turnround.add(waitingTime+ excecute.getBurstTime());
                        pnames.add(excecute.getName());
                        ready.remove(i);
                    }
                }
            }else {
                time+=1;
            }
        }
        //printing the findings
        System.out.println("Shortest job first");
        System.out.println(pnames);
        System.out.println("waiting time");
        System.out.println(waiting);
        System.out.println("average waiting time");
        double sum = 0;
        for(Double d : waiting) sum += d;
        System.out.println(sum/waiting.size());
        AVGWaiting = sum/waiting.size();
        System.out.println("turn round time");
        System.out.println(turnround);
        System.out.println("average turn round time");
        sum = 0;
        for(Double d : turnround) sum += d;
        System.out.println(sum/turnround.size());
        AVGTurn = sum/turnround.size();
        for(int i = 0; i<processNum;i++){
            for(j = 0; j<processNum;j++){
                if(processes.get(i).getName().equals(pnames.get(j))){
                    processes.get(i).setWaitingTime(waiting.get(j));
                    processes.get(i).setTurnaround(turnround.get(j));
                }
            }
        }
    }
    public void priorityScheduling(){
        turnround = new ArrayList<>();
        pnames = new ArrayList<>();
        ArrayList<Process> Ps =  new ArrayList<Process>();
        for (int nedo = 0 ; nedo < processNum ; nedo++)
        {
            Ps.add(new Process(processes.get(nedo)));
        }
        ArrayList<Process> ready = new ArrayList<Process>();
        ArrayList<Double> waiting = new ArrayList<>();
        
        double time = 0;
        int j = 0;
        while(processNum - waiting.size() != 0){
            if(j < processNum){
                while(Ps.get(j).arrivalTime<=time ){
                    ready.add(Ps.get(j));
                    j+=1;
                    if(j==processNum)break;
                }
            }
            if(!ready.isEmpty()){
                double min = 1000;
                Process excecute = new Process();
                for (int i= 0; i<ready.size();i++ ){
                    if(ready.get(i).priorityNumber<min){
                        min = ready.get(i).priorityNumber;
                        excecute = ready.get(i);
                    }
                }
                double waitingTime = time - excecute.getArrivalTime();
                waiting.add(waitingTime);
                time+=excecute.burstTime + contextSwitch;
                turnround.add(waitingTime+ excecute.getBurstTime()+contextSwitch);
                pnames.add(excecute.getName());
                if(!ready.isEmpty()){
                    for (int i= 0; i<ready.size();i++ ){
                        ready.get(i).priorityNumber-=1;
                    }
                }
                for (int i= 0; i<ready.size();i++ ){
                    if(ready.get(i).getName().equals(excecute.getName())){
                        ready.remove(i);
                    }
                }
            }else {
                time+=1;
            }
        }
        System.out.println("priority scheduling");
        System.out.println(pnames);
        System.out.println("waiting time");
        System.out.println(waiting);
        System.out.println("average waiting time");
        double sum = 0;
        for(Double d : waiting) sum += d;
        System.out.println(sum/waiting.size());
        AVGWaiting = sum/waiting.size();
        System.out.println("turn round time");
        System.out.println(turnround);
        System.out.println("average turn round time");
        sum = 0;
        for(Double d : turnround) sum += d;
        System.out.println(sum/turnround.size());
        AVGTurn = sum/turnround.size();
        for(int i = 0; i<processNum;i++){
            for(j = 0; j<processNum;j++){
                if(processes.get(i).getName().equals(pnames.get(j))){
                    processes.get(i).setWaitingTime(waiting.get(j));
                    processes.get(i).setTurnaround(turnround.get(j));
                }
            }
        }
    }
    

    public double getTurn()
    {
        return AVGTurn;
    }

    public double getWaiting()
    {
        return AVGWaiting;
    }

    public ArrayList getTurnAround()
    {
        return turnround;
    }

    public ArrayList getpNames()
    {
        return pnames;
    }

    public ArrayList getProcesses()
    {
        return processes;
    }

    public double getV1(ArrayList<Process> processes){
        double maxArrivalTime = -1;
        for (Process pr : processes){
            if (pr.getBurstTime() == 0) continue;
            if (pr.getArrivalTime() > maxArrivalTime){
                maxArrivalTime = pr.getArrivalTime();
            }
        }
        if (maxArrivalTime > 10) {
            maxArrivalTime = maxArrivalTime / (double) (10);
            return maxArrivalTime;
        }else {
            return 1;
        }
    }
    public double getV2(int i){
        double maxRemainingBurstTime = -1;
        for (Process pr : processes){
            if (pr.getBurstTime() == 0) continue;
            if (pr.getArrivalTime() > i) continue;
            if (pr.getBurstTime() > maxRemainingBurstTime){
                maxRemainingBurstTime =  (pr.getBurstTime());
            }
        }
        if (maxRemainingBurstTime > 10) {
            maxRemainingBurstTime = maxRemainingBurstTime / (double)(10);
            return maxRemainingBurstTime;
        }else {
            return 1;
        }
    }

    public ArrayList<Process> Agat(){
        AgatProcesses = new ArrayList<Process>();

        for (Process pr : processes){
            AgatProcesses.add(new Process(pr));
        }

        AGATResults = new ArrayList<Process>();
        agatWaitingTimes = new ArrayList<Double>();
        int sumSecs = 0;
        for (Process pr: AgatProcesses){
            sumSecs += pr.getBurstTime();
        }
        double minArrivalTime = 99999999;
        int minArrivalTimeIndex = 0;
        for (int i=0;i<AgatProcesses.size();i++ ){
            if (AgatProcesses.get(i).getArrivalTime() < minArrivalTime){
                minArrivalTimeIndex = i;
                minArrivalTime = AgatProcesses.get(i).getArrivalTime();
            }
        }
        ArrayList<Process> result = new ArrayList<Process>();
        ArrayList<Integer> resultSec  = new ArrayList<Integer>( );
        ArrayList<Integer> readyQ = new ArrayList<Integer>();
        ArrayList<Process> deadList = new ArrayList<Process>();

        int runningProcessIndex = minArrivalTimeIndex;
        int runningProcessCurrentTime =  0;
        int i= 0;

        double v1 =getV1(AgatProcesses);
        boolean notFinished = true;
        while (notFinished){
            notFinished = false;
            for (Process pr : AgatProcesses){
                if (pr.getBurstTime() != 0) notFinished =true;
            }
            if (!notFinished) break;
            if (AgatProcesses.get(runningProcessIndex).getBurstTime() == 0) {
                AGATResults.add(new Process("nothing",Color.white,0,0,0,0));
                i +=1;
                resultSec.add(i);
                for (int x=0;x< AgatProcesses.size();x++){
                    if (AgatProcesses.get(x).getArrivalTime() == i) {
                        readyQ.add(x);
                        AgatProcesses.get(x).setReadyQTime(AgatProcesses.get(x).getArrivalTime());
                    }
                }
                if (!readyQ.isEmpty()) {
                    runningProcessIndex = readyQ.get(0);
                    readyQ.remove(0);
                    runningProcessCurrentTime = 0;

                }
                continue;


            }

            i += 1;
            runningProcessCurrentTime += 1;
            AgatProcesses.get(runningProcessIndex).setBurstTime(AgatProcesses.get(runningProcessIndex).getBurstTime() -1);

            if (AgatProcesses.get(runningProcessIndex).getBurstTime()  == 0){
                deadList.add(AgatProcesses.get(runningProcessIndex));
                AgatProcesses.get(runningProcessIndex).setTurnaround(i-AgatProcesses.get(runningProcessIndex).getArrivalTime());
                AGATResults.add(new Process(AgatProcesses.get(runningProcessIndex)));
                AgatProcesses.get(runningProcessIndex).setQuantum(0);
                AgatProcesses.get(runningProcessIndex).setRealQuantum(0);


                resultSec.add(i);
                for (int x=0;x< AgatProcesses.size();x++){
                    if (AgatProcesses.get(x).getArrivalTime() == i) {
                        readyQ.add(x);
                        AgatProcesses.get(x).setReadyQTime(AgatProcesses.get(x).getArrivalTime());
                    }
                }
                if (!readyQ.isEmpty()) {

                    runningProcessIndex = readyQ.get(0);
                    readyQ.remove(0);
                    runningProcessCurrentTime = 0;
                    agatWaitingTimes.add(i - AgatProcesses.get(runningProcessIndex).getReadyQTime());
                    AgatProcesses.get(runningProcessIndex).setWaitingTime(i - AgatProcesses.get(runningProcessIndex).getReadyQTime() +AgatProcesses.get(runningProcessIndex).getWaitingTime());
                }
                continue;
            }
            for (int x=0;x< AgatProcesses.size();x++){
                if (AgatProcesses.get(x).getArrivalTime() == i) {
                    readyQ.add(x);
                    AgatProcesses.get(x).setReadyQTime(AgatProcesses.get(x).getArrivalTime());
                }
            }
            for (int x =0; x<readyQ.size();x++){
                if (AgatProcesses.get(readyQ.get(x)).getBurstTime() == 0){
                    readyQ.remove(x);
                }
            }

            if (runningProcessCurrentTime >= AgatProcesses.get(runningProcessIndex).getQuantum()){
                readyQ.add(runningProcessIndex);
                AgatProcesses.get(runningProcessIndex).setReadyQTime(i);
                AGATResults.add(new Process(AgatProcesses.get(runningProcessIndex)));
                resultSec.add(i);
                AgatProcesses.get(runningProcessIndex).setQuantum(AgatProcesses.get(runningProcessIndex).getQuantum() + 2 );
                runningProcessIndex = readyQ.get(0);
                agatWaitingTimes.add(i - AgatProcesses.get(runningProcessIndex).getReadyQTime());
                AgatProcesses.get(runningProcessIndex).setWaitingTime(i - AgatProcesses.get(runningProcessIndex).getReadyQTime() +AgatProcesses.get(runningProcessIndex).getWaitingTime());
                readyQ.remove(0);
                runningProcessCurrentTime =0;
                continue;
            }

            if (runningProcessCurrentTime >=  Math.round((double) (AgatProcesses.get(runningProcessIndex).getQuantum()) * 0.4)){
                
                double v2 = getV2(i);
                int minAGATIndex = runningProcessIndex;
                for (int j =0;j< AgatProcesses.size();j++){
                    if (AgatProcesses.get(j).getArrivalTime() > i || AgatProcesses.get(j).getBurstTime() == 0 )  {
                        continue;
                    }
                    AgatProcesses.get(j).setAGATFactor((int) (10 -AgatProcesses.get(j).getPriorityNumber() +  Math.ceil(AgatProcesses.get(j).getArrivalTime()/ v1) + Math.ceil(AgatProcesses.get(j).getBurstTime()/v2)));
                    if (AgatProcesses.get(j).getAGATFactor() < AgatProcesses.get(minAGATIndex).getAGATFactor()){
                        minAGATIndex = j;
                    }

                }

                if (minAGATIndex != runningProcessIndex){
                    
                    double currQ = AgatProcesses.get(runningProcessIndex).getRealQuantum();
                    readyQ.add(runningProcessIndex);
                    AgatProcesses.get(runningProcessIndex).setReadyQTime(i);
                    AgatProcesses.get(runningProcessIndex).setQuantum(AgatProcesses.get(runningProcessIndex).getQuantum()+ (currQ-runningProcessCurrentTime));
                    AGATResults.add(new Process(AgatProcesses.get(runningProcessIndex)));
                    resultSec.add(i);
                    runningProcessCurrentTime =0;
                    runningProcessIndex = minAGATIndex;
                    agatWaitingTimes.add(i - AgatProcesses.get(runningProcessIndex).getReadyQTime());
                    AgatProcesses.get(runningProcessIndex).setWaitingTime(i - AgatProcesses.get(runningProcessIndex).getReadyQTime() + AgatProcesses.get(runningProcessIndex).getWaitingTime());
                    for (int s=0;s<readyQ.size();s++){
                        if (readyQ.get(s) == minAGATIndex){
                            readyQ.remove(s);
                            break;
                        }
                    }
                    continue;
                }else{
                    AGATResults.add(new Process(AgatProcesses.get(runningProcessIndex)));
                    resultSec.add(i);
                    continue;
                }

            }
            AGATResults.add(new Process(AgatProcesses.get(runningProcessIndex)));
            resultSec.add(i);

        }

        for(int last=0;last<AGATResults.size();last++){
            System.out.println(resultSec.get(last) +" "+ AGATResults.get(last).getName());
        }
        
        
        for (Process pr : AgatProcesses){
            for (int k =0 ;k< processes.size();k++){
                if (processes.get(k).getName().equals(pr.getName())){
                    processes.get(k).setTurnaround(pr.getTurnaround());
                    processes.get(k).setWaitingTime(pr.getWaitingTime());
                    break;
                }
            }
        }
        
        return AGATResults;
    }

    public  double getAGATAvgWaitingTime(){
        double avgWaitingAgatTime = 0;
        for (Process d: AgatProcesses){
            avgWaitingAgatTime += d.getWaitingTime()/ (double) AgatProcesses.size();
        }

        return avgWaitingAgatTime;
    }
    public double getAvgAGATTurnaroundTime(){
        double avgTurnaroundTime = 0;
        for(Process pr : AgatProcesses){
            avgTurnaroundTime += pr.getTurnaround() / (double)AgatProcesses.size();
        }
        return avgTurnaroundTime;
    }

    public Map<String,ArrayList<Double>> getAGATQuantumTimeHistory(){
        Map<String,LinkedHashSet<Double>> myMap= new HashMap<String,LinkedHashSet<Double>>();
        for(int i=0;i<AGATResults.size();i++){
            if (myMap.containsKey(AGATResults.get(i).getName())){
                myMap.get(AGATResults.get(i).getName()).add(AGATResults.get(i).getQuantum());
            }else{
                myMap.put(AGATResults.get(i).getName(),new LinkedHashSet<Double>());
                myMap.get(AGATResults.get(i).getName()).add(AGATResults.get(i).getQuantum());
            }
        }
        Map<String,ArrayList<Double>> retMap  = new HashMap<>();
        for (String key: myMap.keySet() ){
            retMap.put (key, new ArrayList<>(myMap.get(key)));
        }

        return retMap;
    }

    public Map<String,ArrayList<Double>> getAgetAGATFactorHistory(){
        Map<String,LinkedHashSet<Double>> myMap= new HashMap<String,LinkedHashSet<Double>>();
        for(int i=0;i<AGATResults.size();i++){
            if (myMap.containsKey(AGATResults.get(i).getName())){
                myMap.get(AGATResults.get(i).getName()).add(AGATResults.get(i).getAGATFactor());
            }else{
                myMap.put(AGATResults.get(i).getName(),new LinkedHashSet<Double>());
                myMap.get(AGATResults.get(i).getName()).add(AGATResults.get(i).getAGATFactor());
            }
        }

        Map<String,ArrayList<Double>> retMap  = new HashMap<String,ArrayList<Double>>();
        for (String key: myMap.keySet() ){
            retMap.put (key, new ArrayList<>(myMap.get(key)));
        }

        return retMap;
    }


    public ArrayList<Process> getAgatResults(){
        return AGATResults;
    }
    
    
   
    public static  void main(String[] args){

        CPUscheduler cpu = new CPUscheduler(1,4);

        Process p1 = new Process("p1",Color.red , 0 , 17 , 4 , 4);
        Process p2 = new Process("p2",Color.black , 3 , 6 , 9 , 3);
        Process p3 = new Process("p3",Color.yellow, 4 , 10 , 3 , 5);
        Process p4 = new Process("p4",Color.green, 29 , 4 , 8 , 2);
        cpu.add(p1);
        cpu.add(p2);
        cpu.add(p3);
        cpu.add(p4);
        
        cpu.priorityScheduling();
        cpu.shortestJobFirst();
        cpu.shortestRemainingTimeFirst();
        cpu.Agat();
        
        //final2

    }
}