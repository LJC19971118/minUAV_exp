package com.ljc.exp;

public class Depot {
    public double x,y;
    public double weight;
    public int no;

    public Depot(double x, double y, double weight, int no) {
        this.x = x;
        this.y = y;
        this.weight = weight;
        this.no = no;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }
}
