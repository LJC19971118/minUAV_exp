
import com.ljc.exp.parameter;

import java.io.*;


import static com.ljc.exp.Main.*;

public class gen {

    public static void main(String[] args) throws IOException {
        String fileName;
        BufferedWriter bw = null;
        for (int i = 1; i < 101; i++) {
            fileName="G:\\科研\\无线充电\\阅读笔记\\目前在做\\实验结果\\sensor\\大论文第二点_350个sensor\\SensorData350_"+i+".txt";
            bw=new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName),"utf-8"));
            for (int j = 0; j < 350; j++) {
                bw.write(j+"       "+(int) (Math.random()*(parameter.area+1))+"          "+(int) (Math.random()*(parameter.area+1))+"          "
                        +(int)randElec1(Eu)+"         "+(int)(Math.random()*(DataU+1)));
                bw.newLine();
            }
            bw.flush();
        }
        bw.close();
    }

    public static double randElec1(double EU){
        double rand=Math.random();
        if (rand>=0.3){
            return Math.random()*(Ef+1);
        }else {
            return Math.random()*(EU+1);
        }
    }
}
