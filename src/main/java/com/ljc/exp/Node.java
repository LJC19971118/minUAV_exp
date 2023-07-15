package com.ljc.exp;

public class Node {
    public Sensor sensor;
    public Sink sink;
    public Depot depot;
    public UAV uav =new UAV(-2);
    public double x,y;
    public double weight;
    public int no;

    public Node(Sensor sensor) {
        this.x=sensor.getX();
        this.y=sensor.getY();
        this.no=sensor.getNo();
        this.weight=uav.getChrEnergy(sensor);
    }

    public Node(Sink sink) {
        this.x=sink.getX();
        this.y=sink.getY();
        this.no=sink.getNo();
        this.weight=uav.getColEnergy(sink);
    }

    public Node(Depot depot) {
        this.x=depot.getX();
        this.y=depot.getY();
        this.no=depot.getNo();
        this.weight=0;
    }

    public Node(dataDisk disk, int dataVol) {
        this.x=disk.getX();
        this.y=disk.getY();
        this.no=disk.getNo();
        this.weight=uav.getColEnergy(dataVol);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWeight() {
        return weight;
    }

    public int getNo() {
        return no;
    }
}
