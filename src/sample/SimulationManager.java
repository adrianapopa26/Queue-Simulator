package sample;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationManager implements Runnable{
    private final int nrServices;
    private final int nrClients;
    private final int timeLimit;
    private final int tMinArrival;
    private final int tMaxArrival;
    private final int tMinService;
    private final int tMaxService;
    private int averageServiceTime;
    private int averageWaitingTime;
    private int enteredClients;
    private AtomicInteger currentTime = new AtomicInteger();
    private int peakHour;
    private int maxClients;
    private final List<Client> clients;
    private final List<Queue> queues;
    private final UserInterface userInterface;

    public SimulationManager(int services, int clients, int timeLimit, int tMinArrival, int tMaxArrival, int tMinService, int tMaxService, UserInterface user) {
        this.nrServices = services;
        this.nrClients = clients;
        this.timeLimit = timeLimit;
        this.tMinArrival = tMinArrival;
        this.tMaxArrival = tMaxArrival;
        this.tMinService = tMinService;
        this.tMaxService = tMaxService;
        this.peakHour=0;
        this.averageServiceTime=0;
        this.averageWaitingTime=0;
        this.enteredClients=0;
        this.maxClients=0;
        this.clients = new ArrayList<>();
        this.queues = new ArrayList<>();
        this.userInterface=user;
        initializeSimulation();
    }

    public void initializeSimulation(){
        for(int i=0;i<nrClients;i++)
        {
            clients.add(new Client(0,0,0));
            clients.get(i).randomGenerator(i,tMinArrival,tMaxArrival,tMinService,tMaxService);
        }
        clients.sort(Comparator.comparingInt(Client::gettArrival));
        for(int i=0;i<nrServices;i++){
            queues.add(new Queue(i+1,userInterface));
        }
    }

    public int getShortestQueue() {
        int minIndex = 0;
        int minQ = queues.get(0).getQueueWaitingTime() ;
        for (int i = 0; i < nrServices; i++) {
            if (queues.get(i).getQueueWaitingTime() < minQ) {
                minQ = queues.get(i).getQueueWaitingTime() ;
                minIndex = i;
            }
        }
        return minIndex;
    }

    public boolean openQueues(){
        for(int i=0;i<nrServices;i++) {
            if(!queues.get(i).getClients().isEmpty()){
                return true;
            }
        }
        return false;
    }

    public void stop(){
        for(Queue queue:queues) {
            queue.stop();
        }
        try{
            FileWriter myWriter=new FileWriter("MessageLog.txt",true);
            myWriter.write("Simulation stopped\n");
            myWriter.write("Average Service time: "+(averageServiceTime*1.00/enteredClients)+"\n");
            myWriter.write("Average waiting time: "+(averageWaitingTime*1.00/enteredClients)+"\n");
            myWriter.write("Peak hour: "+peakHour+"\n");
            myWriter.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void display(AtomicInteger currentTime){
        try{
            FileWriter myWriter=new FileWriter("MessageLog.txt",true);
            myWriter.write("Time "+currentTime+"\n");
            if(!clients.isEmpty()) {
                myWriter.write("Waiting clients:");
                for(Client client: clients){
                    myWriter.write(" ("+client.getID()+","+client.gettArrival()+","+client.gettService()+");");
                }
                myWriter.write("\n");
            }
            for(Queue queue:queues){
                myWriter.write("Queue"+queue.getQueueNumber()+":");
                if(queue.getClients().isEmpty()){
                    myWriter.write("closed\n");
                }
                else{
                    for(Client client: queue.getClients()){
                        myWriter.write(" ("+client.getID()+","+client.gettArrival()+","+client.gettService()+");");
                    }
                    myWriter.write("\n");
                }
            }
            myWriter.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void addClients(){
        while(!clients.isEmpty() && clients.get(0).gettArrival()==currentTime.get() ) {
            int minQIndex = getShortestQueue();
            queues.get(minQIndex).addClientToQueue(clients.get(0));
            averageWaitingTime+=queues.get(minQIndex).getQueueWaitingTime();
            enteredClients++;
            userInterface.update("Client "+" ("+clients.get(0).getID()+","+clients.get(0).gettArrival()+","+clients.get(0).gettService()+") arrived at queue "+queues.get(minQIndex).getQueueNumber()+"\n");
            averageServiceTime+=clients.get(0).gettService();
            clients.remove(0);
        }
    }

    public void calculatePeakHour(){
        int n=0;
        for(Queue queue:queues){
            n+=queue.getClients().size();
        }
        if(n>maxClients){
            maxClients=n;
            peakHour=currentTime.get();
        }
    }

    public void run(){
        while(currentTime.get()< timeLimit){
            userInterface.update("Time: "+currentTime.get()+"\n");
            addClients();
            calculatePeakHour();
            display(currentTime);
            if(clients.isEmpty() && !openQueues()) {
                currentTime.getAndAdd(timeLimit);
            }
            currentTime.getAndIncrement();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stop();
        userInterface.update("End Simulation\n");
    }
}