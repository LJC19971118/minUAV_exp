package com.ljc.exp;

import static com.ljc.exp.Main.Eu;

public class UAV {
    public int no;

    public double chrPower;  //由于假设无人机的飞行高度恒定且采用一对一充电方式，所以该值为常数
    public final double h=10;  //UAV飞行高度
    public final double R=100;  //UAV充电半径
    public final double Battery=300000;  //UAV电容300kJ

    public final double v= 8;  //UAV移速为10m/s
    public final double η1=8; //UAV悬停功率为15J/s
    public final double η2=15; //UAV数据采集功率为15J/s
    public final double c1=150;  //每kJ需要能耗150J/s
    public final double c2=20;  //单位时间移动能耗10J/m

    public final double α=13000;
    public final double β=9.5;

    public final double A2=2*Math.pow(10,6);  //信道带宽2MHz
    public final double transPower=10;  //数据传输功率10W
    public final double β2=0.04;  //信道功率增益4*10^(-3)
    public final double N0=Math.pow(10,-8);  //噪声功率 10^(-20)
    public final double θ=Math.PI/4;  //噪声功率 10^(-20)
    public final double G2=8;  //正常数

    public UAV(int no) {
        this.no = no;
    }

    public void setChrPower(double chrPower) {
        this.chrPower = chrPower;
    }

    public double getH() {
        return h;
    }

    public double getR() {
        return R;
    }

    public double getBattery() {
        return Battery;
    }

    public double getV() {
        return v;
    }

    public int getNo() {
        return no;
    }

    //UAV-->Sensor
    public double getChrPower() {
        chrPower=α/Math.pow((β+h),2);
        return chrPower;
    }

    //Sink-->UAV
    public double getTransRate(Sink sink){
        return A2*Math.log(1+(transPower*β2*G2)/(N0*A2*Math.pow(θ,2)*Math.pow(h,2)));
    }

    public double getTransRate(){
        return A2*Math.log(1+(transPower*β2*G2)/(N0*A2*Math.pow(θ,2)*Math.pow(h,2)));
    }

    //UAV 从v1->v2的移动能耗
    public double getMovEnergy(Sensor sensor1,Sensor sensor2){
        double dis=Math.sqrt(Math.pow(sensor1.getX()-sensor2.getX(),2)+Math.pow(sensor1.getY()- sensor2.getY(),2));
        return c2*dis;
    }

    //UAV 从sink->sensor的移动能耗
    public double getMovEnergy(Sensor sensor,Sink sink){
        double dis=Math.sqrt(Math.pow(sensor.getX()-sink.getX(),2)+Math.pow(sensor.getY()- sink.getY(),2));
        return c2*dis;
    }

    public double getMovEnergy(Node nodei,Node nodej){
        double dis=Math.sqrt(Math.pow(nodei.getX()-nodej.getX(),2)+Math.pow(nodei.getY()- nodej.getY(),2));
        return c2*dis;
    }

    public double getMovEnergy(Sensor sensor, Target target) {
        double dis=Math.sqrt(Math.pow(sensor.getX()-target.getX(),2)+Math.pow(sensor.getY()- target.getY(),2));
        return c2*dis;
    }

    //UAV在Sensor上空进行充电需要的能耗  单位:J
    public double getChrEnergy(Sensor sensor){
        return (c1+η1)*(Eu-sensor.getEnergy())/getChrPower();
    }

    //UAV在Sink上空进行数据采集需要的能耗  单位:J
    public double getColEnergy(Sink sink){
        return (η1+η2)*sink.getTotalDataVol(sink.getNo())/getTransRate(sink);
    }

    public double getColEnergy(int dataVol){
        return (η1+η2)*dataVol/getTransRate();
    }
}
