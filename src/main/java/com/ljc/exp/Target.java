package com.ljc.exp;

public class Target {
    public int no;
    public double x,y;

    public Target(int no, double x, double y) {
        this.no = no;
        this.x = x;
        this.y = y;
    }

    public Target(double x, double y, int no) {
        this.x = x;
        this.y = y;
        this.no = no;
    }

    public Target(double x, double y) {
        this.x = x;
        this.y = y;
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

    public int getNo() {
        return no;
    }
}
