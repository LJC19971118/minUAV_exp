package com.ljc.exp;

public class Sensor {

    public final double Cycles = 16*Math.pow(10,6);  //计算传感器的数据量所需要的CPU圈数 16M
    public final double transPower=5;  //数据传输功率5W
    public final double β1=0.0002;  //信道功率增益2*10^(-3)
    public final double σ1=Math.pow(10,-7);  //噪声功率 10^(-20)
    public final double ζ=2;  //传感器与数据汇集器之间的损失指数
    public final double G1=2;  //正常数
    public final double scope=500;  //监测范围

    public double getScope() {
        return scope;
    }

    public int no;
    public double x,y;
    public double energy;
    public double dataVol;  //以b为单位

    public Sensor(int no, double x, double y, double energy, double dataVol) {
        this.no = no;
        this.x = x;
        this.y = y;
        this.energy = energy;
        this.dataVol = dataVol;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getEnergy() {
        return energy;
    }
    public double getDataVol() {
        return dataVol*Math.pow(2,20);
    }
    public int getNo() {
        return no;
    }

    //Sensor-->Sink
    public double getTransRate(Sink sink){
        double distance=Math.sqrt(Math.pow(this.getX()-sink.getX(),2)+Math.pow(this.getY()-sink.getY(),2));
        return sink.A1*Math.log(1+(transPower*β1*G1)/(σ1*Math.pow(distance,ζ)))/Math.log(2);
    }

    //Sensor将数据传输至Sink所需要的传输能耗
    public double getTransEnergy(Sink sink){
        return sink.recvPower*this.getDataVol()/getTransRate(sink);
    }
}
