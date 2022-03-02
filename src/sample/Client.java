package sample;

import java.util.Random;

public class Client {
    private int ID;
    private int tArrival;
    private int tService;

    public Client(int ID, int tArrival, int tService) {
        this.ID = ID;
        this.tArrival = tArrival;
        this.tService = tService;
    }

    public void settService(int tService) {
        this.tService = tService;
    }

    public void randomGenerator(int id, int minArrival, int maxArrival, int minService, int maxService) {
        this.ID = id + 1;
        Random n = new Random();
        this.tArrival = n.nextInt(maxArrival - minArrival + 1) + minArrival;
        this.tService = n.nextInt(maxService - minService + 1) + minService;

    }

    public int getID() {
        return ID;
    }

    public int gettArrival() {
        return tArrival;
    }

    public int gettService() {
        return tService;
    }
}