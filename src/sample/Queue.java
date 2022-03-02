package sample;
import java.util.ArrayList;
import java.util.List;

public class Queue implements Runnable{
    private final List<Client> clients;
    private int queueWaitingTime;
    private boolean status;
    private final Thread queueThread;
    private final int queueNumber;
    private final UserInterface userInterface;

    public Queue(int queueNumber, UserInterface userInterface) {
        this.queueWaitingTime = 0;
        this.status = false;
        this.queueThread = new Thread(this);
        this.clients= new ArrayList<>();
        this.queueNumber=queueNumber;
        this.userInterface=userInterface;
        start();
    }

    public int getQueueNumber() {
        return queueNumber;
    }

    public void addClientToQueue(Client myClient) {
        this.clients.add(myClient);
        queueWaitingTime +=myClient.gettService();
    }

    public void removeClientFromQueue(Client myClient) {
        this.clients.remove(myClient);
    }

    @Override
    public void run() {
        while (status || clients.size() > 0) {
            if (!clients.isEmpty()) {
                try {
                    clients.get(0).settService(clients.get(0).gettService()-1);
                    queueWaitingTime--;
                    if(clients.get(0).gettService()==0){
                        int id=clients.get(0).getID();
                        removeClientFromQueue(clients.get(0));
                        Thread.sleep(1000);
                        userInterface.update("Client "+id+" left queue "+queueNumber+"\n");
                    }
                    else {
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    System.out.println();
                }
            } else {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println();
                }
            }
        }
    }

    public void start() {
        this.status= true;
        queueThread.start();
    }

    public void stop() {
        this.status = false;
    }

    public List<Client> getClients() {
        return clients;
    }

    public int getQueueWaitingTime() {
        return queueWaitingTime;
    }
}
