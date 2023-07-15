package com.ljc.exp;

import java.util.HashSet;

import static com.ljc.exp.Main.map;

public class Sink {
    public double x,y;
    public int no;

    public final double sumF = 8*Math.pow(10,9);  //总功率8GHz
    public final double A1=Math.pow(10,6);  //信道带宽1MHz
    public final double η0=Math.pow(10,-22);
    public final double recvPower=10;  //数据接收功率10W
    public final double λ=3;
    public int CoNo;

    public Sink(int no, double x, double y) {
        this.no = no;
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getCoNo() {
        return CoNo;
    }

    public void setCoNo(int coNo) {
        CoNo = coNo;
    }

    public int getNo() {
        return no;
    }

    //Sink计算Sensor的数据所需要的能耗
    public double getCmpEnergy(Sensor sensor, HashSet<Sensor> set){
        return η0*Math.pow(sumF/set.size(),λ-1)*sensor.Cycles;
    }

    //所在聚簇内的所有数据量
    public double getTotalDataVol(int no){
        HashSet<Sensor> sensors = map.get(no);
        double sum=0;
        for (Sensor sensor : sensors) {
            sum+=sensor.getDataVol();
        }
        return sum;
    }

}
