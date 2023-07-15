package com.ljc.exp;

import java.io.*;
import java.util.*;

public class Main {

    public static HashMap<Integer,HashSet<Sensor>> map; //<聚簇编号,聚簇内传感器集合>
    public static final double Eu = 10800;
    public static final double Ef = 1800;
    public static final double DataU  = 50; //50Mb
    public static Sensor[] sensors;
    public static Target[] targets;
    public static Sink[] sinks;
    public static float[] utility;
    public static final double Battery=300000;  //UAV电容300kJ
    public static final double INF=Double.MAX_VALUE;
    public static final int inf=Integer.MIN_VALUE;
    public static final int Rs=160;   //数据汇集器的覆盖半径
    public static HashSet<Sensor> allChgSet;  //最终所有的充电节点
    public static ArrayList<dataDisk> H;
    public static Node[] allNodes;  //nodes数组的元素是最终待服务的所有节点
    public static int[] allNodesNo; //记录每个节点的下一个节点   若某个下标出现两次说明是一个欧拉回路
    public static int[][] adjMatrix;  //用于哈密顿回路  值为1表示两个顶点可以相通，值为-1表示两个顶点不能相通
    public static Depot[] depots;

    public static HashMap<Integer,LinkedList<Node>> trajectory;
    public static HashMap<Integer,LinkedList<Node>> finalTrajectory;
    public static int countAloneNode;  //记录单独一个节点作为连通分量的个数
    public static int minAloneNode;
    public static int resNUm=0;


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();

        //初始化所有元素的基本信息
        ArrayList<String> SensorStrLst;
        String fileName;
        BufferedReader sbr=null;
        int num1=0;
        int num2=0;
        int num3=0;
        int num4=0;  //算法1 联盟内平均能耗计数

        for (int j = 1; j < 101; j++) {

            fileName="G:\\科研\\无线充电\\阅读笔记\\目前在做\\实验结果\\sensor\\大论文第二点_100个sensor\\SensorData100_"+j+".txt";

            sbr=new BufferedReader(new InputStreamReader(
                    new FileInputStream(fileName), "UTF-8"));
            String sensorLine="";
            SensorStrLst = new ArrayList<>();
            while ((sensorLine=sbr.readLine())!=null){
                SensorStrLst.add(sensorLine);
            }
            sensors=new Sensor[SensorStrLst.size()];
            for (int i = 0; i < SensorStrLst.size(); i++) {
                String[] s = SensorStrLst.get(i).split("\\s+");
                sensors[i]= new Sensor(Integer.parseInt(s[0]),Double.parseDouble(s[1]),Double.parseDouble(s[2]),Double.parseDouble(s[3]),Double.parseDouble(s[4]));
            }
            SensorStrLst.clear();

            sbr.close();
            ArrayList<String> TargetStrLst = new ArrayList<>();
            InputStreamReader TargetReader = new InputStreamReader(
                    new FileInputStream("G:\\科研\\无线充电\\阅读笔记\\目前在做\\实验结果\\target\\TargetData15.txt"), "UTF-8");
            BufferedReader tbr = new BufferedReader(TargetReader);
            String targetLine="";
            while ((targetLine=tbr.readLine())!=null){
                TargetStrLst.add(targetLine);
            }
            TargetReader.close();
            tbr.close();
            targets=new Target[parameter.targetNum];
            for (int i = 0; i < parameter.targetNum; i++) {
                String[] s = TargetStrLst.get(i).split("\\s+");
                targets[i]=new Target(Integer.parseInt(s[0]), Double.parseDouble(s[1]),Double.parseDouble(s[2]));
            }
            TargetStrLst.clear();
            map=new HashMap<>();
            for (int i = 0; i < parameter.targetNum; i++) {
                map.put(i,new HashSet<>());
            }
            ArrayList<String> SinkStrLst = new ArrayList<>();
            InputStreamReader SinkReader = new InputStreamReader(
                    new FileInputStream("G:\\科研\\无线充电\\阅读笔记\\目前在做\\实验结果\\sink\\SinkData8.txt"), "UTF-8");
            BufferedReader sibr = new BufferedReader(SinkReader);
            String sinkLine="";
            while ((sinkLine=sibr.readLine())!=null){
                SinkStrLst.add(sinkLine);
            }
            SinkReader.close();
            sibr.close();
            sinks=new Sink[parameter.targetNum];
            for (int i = 0; i < parameter.targetNum; i++) {
                String[] s = SinkStrLst.get(i).split("\\s+");
                sinks[i]=new Sink(Integer.parseInt(s[0]), Double.parseDouble(s[1]),Double.parseDouble(s[2]));
            }
            //randClusterPartition();  //聚簇划分的对比算法1
            num4+=resNUm;
            //ClusterPartition2();  //聚簇划分
            //ChgNodeSelectionByTar(k);  //充电节点选择

            // finalTrajectory=new HashMap<>();
            // trajectory=new HashMap<>();

            //num2+=finalTrajectory.size();

            // num3+=resNUm;
            resNUm=0;
            // map.clear();
            // H.clear();
            // finalTrajectory.clear();
            // countAloneNode=0;
            System.out.println("已完成"+j+"次！");
            //ClusterPartition();
            //randClusterPartition();  //聚簇划分的对比算法
            //nearestClusterPartition();
            //lbClusterPartition();  //聚簇划分的对比算法

            //ChgNodeSelection(k);
            //randChgNodeSelection(k);  //节点选择的对比算法1
            //minChgNodeSelection(k);   //节点选择的对比算法2  集合覆盖算法

            //初始化finalTrajectory的个数为节点个数
            // minAloneNode=0;
            // finalTrajectory=new HashMap<>();
            // for (int i = 0; i < allChgSet.size()+parameter.targetNum; i++) {
            //     finalTrajectory.put(i,new LinkedList<>());
            // }

            //finalTrajectory=minUavDeployment();
            //minUavDeploymentComp1();
            //minUavDeploymentComp2();

            // System.out.println();
            // System.out.println("最终部署无人机飞行轨迹：");
            // for (int i = 0; i < finalTrajectory.size(); i++) {
            //     LinkedList<Node> list = finalTrajectory.get(i);
            //     for (int i1 = 0; i1 < list.size(); i1++) {
            //         if (i1==list.size()-1){
            //             System.out.print(list.get(i1).getNo());
            //         }else {
            //             System.out.print(list.get(i1).getNo()+" => ");
            //         }
            //     }
            //     System.out.println(" ;  ");
            // }
            //
            // int resLen=finalTrajectory.size()+minAloneNode;
            //
            // System.out.println(resLen);
            // System.out.println(resNUm);
        }
        System.out.println("avg_val : "+num4/100);



    }

    //1.基于联盟博弈的聚簇划分算法
    public static void ClusterPartition(){
        //初始化不相交联盟，选择距离最近的数据汇集器
        for (int i = 0; i < parameter.sensorNum; i++) {
            int temp=i;
            double minDis=INF;
            double x = sensors[i].getX();
            double y = sensors[i].getY();
            for (int j = 0; j < parameter.targetNum; j++) {
                double dis= getDistance(x,y,sinks[j].getX(),sinks[j].getY());
                if (dis<minDis){
                    minDis=dis;
                    temp=j;
                }
            }
            map.get(temp).add(sensors[i]);
        }

        //计算聚簇内每个传感器的效用
        utility=new float[parameter.sensorNum];
        for (int i = 0; i < parameter.sensorNum; i++) {
            int temp=ownCO(sensors[i]);

            float cost1=0;
            HashSet<Sensor> set1 = map.get(temp);
            for (Sensor sensor : set1) {
                cost1+=sensor.getTransEnergy(sinks[temp])+sinks[temp].getCmpEnergy(sensor,set1);
            }

            float cost2=0;
            set1.remove(sensors[i]);
            for (Sensor sensor : set1) {
                cost2+=sensor.getTransEnergy(sinks[temp])+sinks[temp].getCmpEnergy(sensor,set1);
            }
            set1.add(sensors[i]);  //复原
            utility[i]=cost2-cost1;
        }

        //更新传感器的行为策略
        int changeCount=parameter.sensorNum;  //变化次数
        //随机设置迭代次数
        int iter=100;
        while ((iter--)>0){
            changeCount=0;
            //对效用函数发生变化的传感器进行计数，如果没有效用发生改变说明达到稳定
            for (int i = 0; i < parameter.sensorNum; i++) {
                int maxNo=chooseCO(i,sensors[i]);  //选择具有最大效用的行为策略联盟编号
                int ownNo=ownCO(sensors[i]);
                if (maxNo!=-1){  //==-1表示当前节点已经处于自身效用最大的联盟
                    //行为策略更新
                    map.get(ownNo).remove(sensors[i]);  //从原有联盟中退出
                    map.get(maxNo).add(sensors[i]);  //加入到新联盟
                    changeCount++;
                }
            }
        }
        System.out.println("基于联盟博弈的聚簇划分结构：");
        for (int i = 0; i < map.size(); i++) {
            HashSet<Sensor> set = map.get(i);
            System.out.print("第"+i+"个聚簇 : ");
            for (Sensor sensor : set) {
                System.out.print(sensor.getNo()+"  ");
            }
            System.out.println();
        }

        // double sumx=0;
        // for (int i = 0; i < parameter.targetNum; i++) {
        //     HashSet<Sensor> set = map.get(i);
        //
        //     for (Sensor sensor : set) {
        //         System.out.print(sensor.getTransEnergy(sinks[i])+" ");
        //         System.out.print(sinks[i].getCmpEnergy(sensor,set));
        //         System.out.println();
        //         sumx+=sensor.getTransEnergy(sinks[i])+sinks[i].getCmpEnergy(sensor,set);
        //     }
        // }
        // System.out.println("结果：");
        // System.out.print(sumx / parameter.targetNum);

    }

    //2.候选充电传感器节点的选择
    private static void ChgNodeSelection(int k) {
        allChgSet=new HashSet<>();
        double allSumChg=0;
        double curEnergyComp=0;
        for (int i = 0; i < parameter.targetNum; i++) {
            //每次初始化一个虚拟UAV
            UAV uav = new UAV(-1);

            //计算当前簇内的剩余电量，若大于k倍EU,则不需要进行补充电量
            int curEnergy=0;
            //遍历每一个聚簇，得到发起充电请求的节点
            HashSet<Sensor> set = new HashSet<>();
            for (Sensor sensor : map.get(i)) {
                curEnergy+=sensor.getEnergy();
                if (sensor.getEnergy()<Ef){
                    set.add(sensor);
                }
            }
            if (curEnergy>k*Eu){
                continue;
            }
            //初始值为数据汇集器直接到到该点的能耗
            double[] lowcost = new double[parameter.sensorNum];
            for (int j = 0; j < parameter.sensorNum; j++) {
                if (!inChgSet(set,j)){
                    lowcost[j]=INF;
                }else {
                    lowcost[j]=uav.getChrEnergy(sensors[j])+uav.getMovEnergy(sensors[j],sinks[i]);
                    // lowcost[j]=(uav.getColEnergy(sinks[i])+uav.getChrEnergy(sensors[j]))/2+uav.getMovEnergy(sensors[j],sinks[i]);
                }
            }
            
            //最小生成树路径选择  只需要节点，不需要具体的路径
            int sum=0;
            //当前聚簇候选充电节点集合
            HashSet<Sensor> selectSet = new HashSet<>();
            while (curEnergy+sum<k*Eu){
                double minCost=INF;
                int index=-1;
                for (int j = 0; j < parameter.sensorNum; j++) {
                    if (!selectSet.contains(sensors[j])&&set.contains(sensors[j])){
                        //12.2改动，增加中继节点的操作
                        double min=lowcost[j];
                        for (Sensor sensor : selectSet) {
                            min=Math.min(min,uav.getChrEnergy(sensors[j])+uav.getMovEnergy(sensors[j],sensor));
                        }
                        if (min<minCost){
                            minCost=min;
                            index=j;
                        }
                    }
                }

                //将其加入到allSet  更新该节点边权
                sum+=Eu-sensors[index].getEnergy();
                selectSet.add(sensors[index]);
                if (index!=-1){
                    lowcost[index]=minCost;
                }
            }
            double sumChg=0;
            for (Sensor sensor : selectSet) {
                int no = sensor.getNo();
                sumChg+=lowcost[no]/Math.pow(10,3);
            }
            sumChg+=uav.getColEnergy(sinks[i])/Math.pow(10,3);
            allSumChg+=sumChg;
            System.out.println(sumChg);
            allChgSet.addAll(selectSet);
        }
        System.out.println("最终平均簇内uav能耗："+allSumChg/parameter.targetNum);
    }

    //3.UAV数量最小化部署算法
    public static HashMap<Integer,LinkedList<Node>> minUavDeployment() {
        int size=allChgSet.size()+parameter.targetNum;
        //注：数组下标不是节点的序号
        allNodes = new Node[size];  //nodes数组的元素是最终待服务的所有节点
        //首先将节点归一化
        PriorityQueue<Node> que1=new PriorityQueue<>(Comparator.comparingInt(Node::getNo));
        HashSet<Node> tmpp = new HashSet<>();
        for (Sensor sensor : allChgSet) {
            tmpp.add(new Node(sensor));
        }
        for (Sink sink : sinks) {
            tmpp.add(new Node(sink));
        }
        que1.addAll(tmpp);
        for (int i = 0; i < allNodes.length; i++) {
            allNodes[i]=que1.poll();
        }

        //构造最终所有需要访问的节点之间的完全无向图
        //System.out.println("完全无向图邻接矩阵：");
        double[][] weights = new double[size][size];
        UAV uav = new UAV(-100);
        for (int i = 0; i < size; i++) {
            Node nodei = allNodes[i];
            for (int j = 0; j < size; j++) {
                Node nodej = allNodes[j];
                if (i==j){
                    weights[i][j]=INF;
                }else {
                    weights[i][j]=uav.getMovEnergy(nodei,nodej)+(nodei.getWeight()+nodej.getWeight())/2;
                }
                //System.out.print(weights[i][j]+" ");
            }
            //System.out.println();
        }

        trajectory=new HashMap<>();
        for (int i = 2; i <= size; i++) {
            countAloneNode=0;
            HashSet<Node> AloneNode = new HashSet<>();  //记录因为边权阈值而被孤立的单节点连通分量
            int count=0; //记录i时 最终飞行轨迹个数
            double threshold= Battery/i;

            //移除边权大于threshold的边
            HashMap<Integer,HashSet<Edge>> component=delEdge(weights,AloneNode,threshold);  //存储连通分量对应最小生成树的集合 已验证

            //1 G''
            HashMap<Integer, Edge[]> componenti = transferEdgeArray(component);   //已验证
            if (componenti.size()+AloneNode.size()==0){
                break;
            }
            //2找到所有最小生成树中的奇数度节点集合，找到最佳匹配  G0
            HashMap<Integer,Edge[]> oddComponent=findOddDegreeNode(componenti,weights);

            //将1和2两步得到的图进行合并 ，并将结果更新到componenti中  （不能简单的直接合并，需要去重）
            for (int j = 0; j < componenti.size(); j++) {
                PriorityQueue<Edge> queue = new PriorityQueue<>((o1, o2) -> (int) (o1.getWeight()-o2.getWeight()));  //升序
                queue.addAll(List.of(componenti.get(j)));

                Edge[] edges = oddComponent.get(j);
                for (Edge edge : edges) {
                    if (!isContains(componenti.get(j),edge)){
                        queue.add(edge);
                    }
                }
                int queSizeJ = queue.size();
                Edge[] finalJEdges = new Edge[queSizeJ];
                for (int k = 0; k < queSizeJ; k++) {
                    finalJEdges[k]=queue.poll();
                }
                componenti.put(j,finalJEdges);
            }

            adjMatrix=new int[allNodes.length][allNodes.length];
            getHamiltonCircuit(adjMatrix,componenti);

            allNodesNo=new int[allNodes.length];  //用于回溯 ，记录哈密顿路径
            for (int j = 0; j < allNodes.length; j++) {
                allNodesNo[j]=inf;  //初始化，未选中起点及未到达任何顶点
            }

            //此时componenti为所有边的图 即G'''
            //对于每个连通分量根据B划分来寻找欧拉回路
            for (int j = 0; j < componenti.size(); j++) {
                Edge[] edges = componenti.get(j);  //此时数组中的边按照边权升序
                edges = sortByStartNo(edges);  //转换为按照边的起点升序
                // System.out.println("按边的起点编号升序后的G'''：");
                // for (Edge edge : edges) {
                //     System.out.print(edge+" ");
                // }
                Node start = edges[j].getStart();
                int position1 = getPosition(start, allNodes);

                boolean[] used = new boolean[allNodes.length];  //应该使用节点的度进行计数，回路中所有的节点的度均为2
                used[position1]=true;

                LinkedList<Node> initList = new LinkedList<>();
                initList.addLast(start);

                int[] countUsed = new int[allNodes.length];  //记录每个节点当前的度
                countUsed[position1]++;

                dfs(position1,used,countUsed,1,countNodeNum(edges),adjMatrix,initList);

                // System.out.println();
                // System.out.print("当前哈密顿回路：");
                // for (int i1 = 0; i1 < initList.size(); i1++) {
                //     if (i1==initList.size()-1){
                //         System.out.print(initList.get(i1).getNo());
                //     }else {
                //         System.out.print(initList.get(i1).getNo()+" => ");
                //     }
                // }
                int sum=0;
                LinkedList<Node> tmp=new LinkedList<>();
                tmp.addLast(start);
                for (int k = 1; k < initList.size(); k++) {
                    if (sum+(uav.getMovEnergy(initList.get(k-1),initList.get(k))+(initList.get(k-1).getWeight()+initList.get(k).getWeight())/2)
                            +(uav.getMovEnergy(initList.get(k),start)+(initList.get(k).getWeight()+start.getWeight())/2)>Battery){

                        tmp.addLast(start);  //构成回路
                        trajectory.put(count++,tmp);  //添加一条符合B的回路

                        //更新起点
                        start=initList.get(k);
                        sum=0;

                        //新建链表
                        tmp=new LinkedList<>();
                        tmp.addLast(start);
                    }else {
                        //满足B的约束，将该节点加入到序列中
                        tmp.addLast(initList.get(k));
                        sum+=(uav.getMovEnergy(initList.get(k-1),initList.get(k))+(initList.get(k-1).getWeight()+initList.get(k).getWeight())/2);
                    }
                }
                //剩余最后一个子回路未添加，进行补充
                tmp.addLast(start);
                trajectory.put(count++,tmp);
            }

            // System.out.println();
            // System.out.println("无人机飞行轨迹：");
            // for (int i2= 0; i2 < trajectory.size(); i2++) {
            //     LinkedList<Node> list = trajectory.get(i2);
            //     for (int i1 = 0; i1 < list.size(); i1++) {
            //         if (i1==list.size()-1){
            //             System.out.print(list.get(i1).getNo());
            //         }else {
            //             System.out.print(list.get(i1).getNo()+" => ");
            //         }
            //     }
            //     System.out.println(" ;  ");
            // }
            int tempLen=trajectory.size()+countAloneNode;
            System.out.println("此时部署的无人机数目："+tempLen);

            if (trajectory.size()<finalTrajectory.size()){
                finalTrajectory.clear();
                finalTrajectory.putAll(trajectory);
                minAloneNode=countAloneNode;
            }

            // System.out.println();
            // System.out.println("-----------------------------i="+i+"分界线-------------------------------------");
        }
        return finalTrajectory;
    }

    //dfs
    private static void dfs(int start, boolean[] used, int[] countUsed, int step, int len, int[][] adjMatrix, LinkedList<Node> initList) {

        if (step>=len){
            return;
        }
        for (int i = 0; i < used.length; i++) {
            if (adjMatrix[start][i]==1&&!used[i]){ //若当前节点未访问过，则可以加入
                used[i]=true;
                countUsed[i]++;
                initList.addLast(allNodes[i]);
                dfs(i,used, countUsed, step+1,len,adjMatrix,initList);
            }else if (adjMatrix[start][i]==1&&used[i]){  //若当前节点已访问过，继续dfs直到找到未访问过的节点
                if (step==len-1){
                    return;
                }
                if (allNodes[i].getNo()!=initList.get(0).getNo()&&countUsed[i]==2){
                    dfs(i,used, countUsed, step,len,adjMatrix,initList);
                }
            }
        }
    }

    private static int countNodeNum(Edge[] edges) {
        HashSet<Node> nodes = new HashSet<>();
        for (Edge edge : edges) {
            Node start = edge.getStart();
            Node end = edge.getEnd();
            nodes.add(start);
            nodes.add(end);
        }
        return nodes.size();
    }

    //判断当前队列中是否已经存在该边，去重
    private static boolean isContains(Edge[] edges, Edge edge) {
        Node start = edge.getStart();
        Node end = edge.getEnd();
        for (Edge e : edges) {
            if (start.getNo()==e.getStart().getNo()||start.getNo()==e.getEnd().getNo()){
                if (end.getNo()==e.getStart().getNo()||end.getNo()==e.getEnd().getNo()){
                    return true;
                }
            }
        }
        return false;
    }

    private static void getHamiltonCircuit(int[][] adjMatrix, HashMap<Integer, Edge[]> componenti) {
        for (int i = 0; i < componenti.size(); i++) {
            Edge[] edges = componenti.get(i);
            for (int j = 0; j < edges.length; j++) {
                Node start = edges[j].getStart();
                Node end = edges[j].getEnd();
                int p1=getPositionInAllNodes(start,allNodes);
                int p2=getPositionInAllNodes(end,allNodes);
                adjMatrix[p1][p2]=1;
                adjMatrix[p2][p1]=1;
            }
        }
    }

    //得到节点在总Node数组中的位置下标
    private static int getPositionInAllNodes(Node node, Node[] allNodes) {
        for (int i = 0; i < allNodes.length; i++) {
            if (node.getNo()==allNodes[i].getNo()){
                return i;
            }
        }
        return -1;
    }

    private static Edge[] sortByStartNo(Edge[] edges) {
        PriorityQueue<Edge> queue = new PriorityQueue<>((o1, o2) -> {
            if (o1.getStart()==o2.getStart()){
                return o1.getEnd().getNo()-o2.getEnd().getNo();
            }else{
                return o1.getStart().getNo()-o2.getStart().getNo();
            }
        });
        queue.addAll(List.of(edges));

        for (int i = 0; i < edges.length; i++) {
            edges[i]=queue.poll();
        }
        return edges;
    }

    //找到所有最小生成树中的奇数度节点集合，找到最小权重完美匹配
    private static HashMap<Integer, Edge[]> findOddDegreeNode(HashMap<Integer, Edge[]> componenti ,double[][] weights) {
        int size = componenti.size();
        HashMap<Integer, Edge[]> res = new HashMap<>();
        int[] count;
        //找到所有奇数度的节点
        for (int i = 0; i < size; i++) {
            Edge[] edges = componenti.get(i);
            Node[] nodes = getNodesInEdges(edges); //nodes数组只是当前连通分量中的节点，而不是所有待服务的节点
            count=new int[nodes.length];
            for (Edge edge : edges) {
                Node start = edge.getStart();
                Node end = edge.getEnd();
                int p1 = getPosition(start, nodes);
                int p2 = getPosition(end, nodes);
                count[p1]++;
                count[p2]++;
            }

            //最小权重完美匹配 （仅采用贪心算法，可能不是最优解）
            boolean[] visited = new boolean[nodes.length];  //判断该节点是否已经被匹配
            PriorityQueue<Edge> queue = new PriorityQueue<>((o1, o2) -> (int) (o1.getWeight()-o2.getWeight()));  //升序,加入的边由奇数度节点最小权重完美匹配而得

            UAV uav = new UAV(0);
            for (int j = 0; j < nodes.length; j++) {  //因为奇数度的节点的个数一定是偶数，所以他们最终都会配对成功
                double min=INF;
                if (!visited[j]&&count[j]%2!=0){
                    int tmpk=j;
                    visited[j]=true;
                    for (int k = 0; k < nodes.length; k++) {
                        if (!visited[k]&&count[k]%2!=0
                                &&(uav.getMovEnergy(nodes[j],nodes[k])+(nodes[j].getWeight()+nodes[k].getWeight())/2)<min){  //不能使用被边权阈值处理过的weight[][]数组
                            min=uav.getMovEnergy(nodes[j],nodes[k])+(nodes[j].getWeight()+nodes[k].getWeight())/2;
                            tmpk=k;
                        }
                    }
                    visited[tmpk]=true;
                    queue.add(new Edge(nodes[j],nodes[tmpk],uav.getMovEnergy(nodes[j],nodes[tmpk])+(nodes[j].getWeight()+nodes[tmpk].getWeight())/2));  //存放奇数度节点构成的最佳匹配边的集合
                }
            }
            //每次直接贪心选择组成最短的匹配
            int oddEdgeLen = queue.size();
            Edge[] oddEdgeArray = new Edge[oddEdgeLen];
            int index=0;
            while (!queue.isEmpty()){
                Edge poll = queue.poll();
                oddEdgeArray[index++]=poll;
            }
            res.put(i,oddEdgeArray);
        }
        return res;
    }

    private static Node[] getNodesInEdges(Edge[] edges) {
        PriorityQueue<Node> nodes = new PriorityQueue<>(Comparator.comparingInt(Node::getNo));
        for (Edge edge : edges) {
            Node start = edge.getStart();
            Node end = edge.getEnd();
            if (!nodes.contains(start)){
                nodes.add(start);
            }
            if (!nodes.contains(end)){
                nodes.add(end);
            }
        }
        int nodeSize = nodes.size();
        Node[] res = new Node[nodeSize];
        for (int i = 0; i <nodeSize ; i++) {
            res[i]=nodes.poll();
        }
        return res;
    }

    //删除无向图中所哟大于边权的边，并返回每个连通分量转换为mst的集合
    private static HashMap<Integer, HashSet<Edge>> delEdge(double[][] weights, HashSet<Node> aloneNode, double threshold) {

        int count=0;
        int size= allNodes.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (weights[i][j]>threshold){
                    weights[i][j]=INF;  //无穷大表示两节点之间无边
                }
            }
        }
        HashMap<Integer, HashSet<Edge>> hashMap = new HashMap<>();  //对应每个连通分量中的边
        boolean[] visited = new boolean[size];  //表示节点是否已经访问过
        ArrayDeque<Node> curNodes = new ArrayDeque<>();  //每次存储一条边的起点

        //缺少单独一个节点形成连通分量的情况 需要单独考虑
        for (int i = 0; i < size; i++) {
            HashSet<Edge> edges=new HashSet<>();
            if (!visited[i]){
                if (curNodes.isEmpty()){
                    curNodes.addLast(allNodes[i]);
                    visited[i]=true;
                }else {
                    while (!curNodes.isEmpty()){
                        Node node = curNodes.pollFirst();
                        //判断该节点在nodes中的下标
                        int temp=getPosition(node,allNodes);
                        for (int j = 0; j < size; j++) {
                            if (weights[temp][j]!=INF&&!visited[j]){
                                curNodes.addLast(allNodes[j]);
                                visited[j]=true;
                                //判断是否存在以该点为顶点的边
                                edges.add(new Edge(node,allNodes[j],weights[temp][j]));  //该权值是已经预处理过后的边权，不是移动能耗
                            }
                        }

                        if (curNodes.isEmpty()){
                            aloneNode.add(node);
                            countAloneNode++;
                        }
                    }
                    hashMap.put(count++,edges);
                }
            }
        }
        HashMap<Integer, HashSet<Edge>> hashmap = new HashMap<>();  //用于存储空连通分量
        int index=0;
        for (int i = 0; i < hashMap.size(); i++) {
            if (!hashMap.get(i).isEmpty()){
                hashmap.put(index,hashMap.get(i));
            }
        }
        int num=hashmap.size()+aloneNode.size();
        //
        // for (int i = 1; i <= hashmap.size(); i++) {
        //     HashSet<Edge> edgess = hashmap.get(i-1);
        //     System.out.println("第"+i +"个连通分量：");
        //     for (Edge edge : edgess) {
        //         System.out.println(edge.getStart().getNo()+"->"+edge.getEnd().getNo());
        //     }
        // }
        // int c=hashmap.size()+1;
        // for (Node node : aloneNode) {
        //     System.out.println("第"+c +"个连通分量：");
        //     System.out.println(node.getNo());
        //     c++;
        // }
        // System.out.println("================================");
        //对每个连通分量进行最小生成树算法
        HashMap<Integer, HashSet<Edge>> minTreeMap = new HashMap<>();  //map.size就是连通分量的个数
        for (int i = 0; i < hashmap.size(); i++) {
            HashSet<Edge> edgesTmp = hashmap.get(i);  //当前连通分量的边集合
            minTreeMap.put(i,findMinTree(edgesTmp));
        }
        //System.out.println(aloneNode.size());
        return minTreeMap;
    }

    //克鲁斯卡尔算法
    private static HashSet<Edge> findMinTree(HashSet<Edge> edgesTmp) {

        HashSet<Node> nodeTmp = new HashSet<>();
        for (Edge edge : edgesTmp) {
            nodeTmp.add(edge.getStart());
            nodeTmp.add(edge.getEnd());
        }
        //将当前连通分量中的节点存储在vertax中，按照no升序
        PriorityQueue<Node> queue1 = new PriorityQueue<>(Comparator.comparingInt(Node::getNo));  //升序
        queue1.addAll(nodeTmp);
        Node[] vertax = new Node[nodeTmp.size()];
        for (int i = 0; i < nodeTmp.size(); i++) {
            vertax[i]=queue1.poll();
        }

        //将当前连通分量中的边存储在sortedEdges中，按照边权升序
        PriorityQueue<Edge> queue = new PriorityQueue<>((o1, o2) -> (int) (o1.getWeight() - o2.getWeight()));  //升序
        queue.addAll(edgesTmp);
        // System.out.println(queue.size());

        Edge[] sortedEdges=new Edge[queue.size()];  //按照权值升序排序的边数组
        int edgeNum = queue.size();
        for (int i = 0; i < edgeNum; i++) {
            sortedEdges[i]=queue.poll();
            //System.out.print(sortedEdges[i]+"  ");
        }
        //System.out.println();

        int[] ends = new int[nodeTmp.size()]; //用于保存“已有最小生成树”中的每个顶点在MST中终点
        HashSet<Edge> edges = new HashSet<>();

        //遍历
        for (int i = 0; i < sortedEdges.length; i++) {
            int p1=getPosition(sortedEdges[i].getStart(),vertax);
            int p2=getPosition(sortedEdges[i].getEnd(),vertax);

            int m=getEnd(ends,p1);
            int n=getEnd(ends,p2);

            if (m!=n){
                ends[m]=n;
                edges.add(sortedEdges[i]);
            }
        }
        return edges;
    }
    //获取下标为i的顶点的终点，用于后面判断两个顶点的终点是否相同
    private static int getEnd(int[] ends, int i) {
        while (ends[i]!=0){
            i=ends[i];
        }
        return i;
    }
    //返回当前顶点对应的下标
    private static int getPosition(Node node, Node[] vertax) {
        for (int i = 0; i < vertax.length; i++) {
            if (node.getNo()==vertax[i].getNo()){
                return i;
            }
        }
        return -1;
    }

    ////将由hashset构成的map转换为Edge数组构成的map
    public static HashMap<Integer,Edge[]> transferEdgeArray(HashMap<Integer,HashSet<Edge>> hashMap){
        HashMap<Integer, Edge[]> map = new HashMap<>();
        PriorityQueue<Edge> queue1=new PriorityQueue<>((o1, o2) -> (int) (o1.getWeight()-o2.getWeight()));
        int size=hashMap.size();
        for (int i = 0; i < size; i++) {
            int len=hashMap.get(i).size();
            Edge[] edges = new Edge[len];
            queue1.addAll(hashMap.get(i));
            int queSize = queue1.size();
            for (int j = 0; j < queSize; j++) {
                edges[j]=queue1.poll();
            }
            map.put(i,edges);
        }
        return map;
    }

    //聚簇划分算法：随机划分  距离最近划分  负载均衡划分
    //1随机划分
    public static void randClusterPartition(){
        //首先查看每个传感器的监测范围内的目标兴趣点的个数，若只有则直接距离最近，若有多个则随机分配其中一个
        for (int i = 0; i < parameter.sensorNum; i++) {
            int temp=-1;
            ArrayList<Integer> tars=getMonitorableTarget(i);  //返回该节点范围内的所有目标兴趣点的编号
            if (tars.isEmpty()){
                double minDis=Double.MAX_VALUE;
                double x = sensors[i].getX();
                double y = sensors[i].getY();
                for (int j = 0; j < parameter.targetNum; j++) {
                    double dis= getDistance(x,y,sinks[j].getX(),sinks[j].getY());
                    if (dis<minDis){
                        minDis=dis;
                        temp=j;
                    }
                }
            }else if (tars.size()==1){
                temp=tars.get(0);
            }else {
                temp= tars.get((int) (Math.random()*tars.size()));
            }
            map.get(temp).add(sensors[i]);
        }

        double sumx=0;
        for (int i = 0; i < parameter.targetNum; i++) {
            HashSet<Sensor> set = map.get(i);

            for (Sensor sensor : set) {
                sumx+=sensor.getTransEnergy(sinks[i])+sinks[i].getCmpEnergy(sensor,set);

            }
        }
        System.out.println("结果：");
        resNUm+=sumx / parameter.targetNum;
        System.out.print(sumx / parameter.targetNum);

    }
    //2最近距离划分
    private static void nearestClusterPartition() {
        //初始化不相交联盟，选择距离最近的数据汇集器
        for (int i = 0; i < parameter.sensorNum; i++) {
            int temp=i;
            double minDis=INF;
            double x = sensors[i].getX();
            double y = sensors[i].getY();
            for (int j = 0; j < parameter.targetNum; j++) {
                double dis= getDistance(x,y,sinks[j].getX(),sinks[j].getY());
                if (dis<minDis){
                    minDis=dis;
                    temp=j;
                }
            }
            map.get(temp).add(sensors[i]);
        }
        double sumx=0;
        for (int i = 0; i < parameter.targetNum; i++) {
            HashSet<Sensor> set = map.get(i);
            for (Sensor sensor : set) {
                sumx+=sensor.getTransEnergy(sinks[i])+sinks[i].getCmpEnergy(sensor,set);
            }
        }
        System.out.println(sumx / parameter.targetNum);
    }
    //3负载均衡划分
    private static void lbClusterPartition() {
        for (int i = 0; i < parameter.sensorNum; i++) {
            ArrayList<Integer> list = getMonitorableTarget(i);
            int temp=-1;
            int minNum=Integer.MAX_VALUE;
            if (list.isEmpty()){
                double minDis=Double.MAX_VALUE;
                double x = sensors[i].getX();
                double y = sensors[i].getY();
                for (int j = 0; j < parameter.targetNum; j++) {
                    double dis= getDistance(x,y,sinks[j].getX(),sinks[j].getY());
                    if (dis<minDis){
                        minDis=dis;
                        temp=j;
                    }
                }
            }else {
                for (Integer integer : list) {
                    if (map.get(integer).size()<minNum){
                        minNum=map.get(integer).size();
                        temp=integer;
                    }
                }
            }

            map.get(temp).add(sensors[i]);
        }

        // double sumx=0;
        // for (int i = 0; i < parameter.targetNum; i++) {
        //     HashSet<Sensor> set = map.get(i);
        //     for (Sensor sensor : set) {
        //         sumx+=sensor.getTransEnergy(sinks[i])+sinks[i].getCmpEnergy(sensor,set);
        //     }
        // }
        // System.out.println(sumx / parameter.targetNum);

        // System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("基于负载均衡的聚簇划分结构：");
        for (int i = 0; i < map.size(); i++) {
            HashSet<Sensor> set = map.get(i);
            System.out.print("第"+i+"个聚簇 : ");
            for (Sensor sensor : set) {
                System.out.print(sensor.getNo()+"  ");
            }
            System.out.println();
        }
    }
    //返回该节点范围内的所有目标兴趣点的编号
    private static ArrayList<Integer> getMonitorableTarget(int i) {
        ArrayList<Integer> tarNo = new ArrayList<>();

        for (int j = 0; j < parameter.targetNum; j++) {
            if (getDistance(targets[j].getX(),targets[j].getY(),sensors[i].getX(),sensors[i].getY())<=sensors[i].getScope()){
                tarNo.add(j);
            }
        }
        return tarNo;
    }

    //充电节点选择对比算法：电量最小贪心选择（集合覆盖）
    private static void minChgNodeSelection(int k) {
        allChgSet=new HashSet<>();
        double allSumChg=0;
        for (int i = 0; i < parameter.targetNum; i++) {
            //每次初始化一个虚拟UAV
            UAV uav = new UAV(-1);

            //计算当前簇内的剩余电量，若大于k倍EU,则不需要进行补充电量
            int curEnergy=0;
            //遍历每一个聚簇，得到发起充电请求的节点
            HashSet<Sensor> set = new HashSet<>();
            for (Sensor sensor : map.get(i)) {
                curEnergy+=sensor.getEnergy();
                if (sensor.getEnergy()<Ef){
                    set.add(sensor);
                }
            }
            if (curEnergy>k*Eu){
                continue;
            }
            HashSet<Sensor> selectedSensor = new HashSet<>();
            PriorityQueue<Sensor> queue = new PriorityQueue<>((o1, o2) -> (int) (uav.getChrEnergy(o2) - uav.getChrEnergy(o1)));
            queue.addAll(set);
            while (curEnergy<k*Eu){
                Sensor poll = queue.poll();
                curEnergy+=Eu-poll.getEnergy();
                selectedSensor.add(poll);
            }

            double sumChg=0;
            for (Sensor sensor : selectedSensor) {
                sumChg+=(uav.getChrEnergy(sensor)+uav.getMovEnergy(sensor,sinks[i]))/Math.pow(10,3);
            }
            sumChg+=uav.getColEnergy(sinks[i])/Math.pow(10,3);
            allSumChg+=sumChg;
            System.out.println(sumChg);

            allChgSet.addAll(selectedSensor);
        }

        System.out.println("最终平均簇内uav能耗："+allSumChg/parameter.targetNum);
    }

    //UAV最小化部署对比算法
    //MCCPAlg1
    private static void minUavDeploymentComp1() {
        int size=allChgSet.size()+parameter.targetNum;
        //注：数组下标不是节点的序号
        allNodes = new Node[size];  //nodes数组的元素是最终待服务的所有节点
        //首先将节点归一化
        PriorityQueue<Node> que1=new PriorityQueue<>(Comparator.comparingInt(Node::getNo));
        HashSet<Node> tmpp = new HashSet<>();
        for (Sensor sensor : allChgSet) {
            tmpp.add(new Node(sensor));
        }
        for (Sink sink : sinks) {
            tmpp.add(new Node(sink));
        }
        que1.addAll(tmpp);
        for (int i = 0; i < allNodes.length; i++) {
            allNodes[i]=que1.poll();
        }
        System.out.print("所有待访问节点的序号：");
        for (int i = 0; i < allNodes.length; i++) {
            System.out.print(allNodes[i].getNo()+"  ");
        }
        System.out.println();

        //构造最终所有需要访问的节点之间的完全无向图
        double[][] weights = new double[size][size];
        UAV uav = new UAV(-100);
        for (int i = 0; i < size; i++) {
            Node nodei = allNodes[i];
            for (int j = 0; j < size; j++) {
                Node nodej = allNodes[j];
                if (i==j){
                    weights[i][j]=INF;
                }else {
                    weights[i][j]=uav.getMovEnergy(nodei,nodej)+(nodei.getWeight()+nodej.getWeight())/2;
                }
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(weights[i][j]+" ");
            }
            System.out.println();
        }

        HashSet<Node> AloneNode = new HashSet<>();  //记录因为边权阈值而被孤立的单节点连通分量
        double threshold= Battery/6;

        //移除边权大于threshold的边
        HashMap<Integer,HashSet<Edge>> component=delEdge(weights,AloneNode,threshold);  //存储连通分量对应最小生成树的集合 已验证
        //将所有的连通分量分为两类：轻分量  重分量
        HashMap<Integer,HashSet<Edge>> lightComponent=new HashMap<>();
        int indexL=0;
        HashMap<Integer,HashSet<Edge>> heavyComponent=new HashMap<>();
        int indexH=0;

        for (int i = 0; i < component.size(); i++) {
            HashSet<Edge> com = component.get(i);
            double comSum=0;
            for (Edge edge : com) {
                comSum+=edge.getWeight();
            }

            if (comSum<=Battery/12){
                lightComponent.put(indexL++,com);
            }else {
                heavyComponent.put(indexH++,com);
            }
        }
        HashMap<Integer, Edge[]> T = new HashMap<>();
        if (!lightComponent.isEmpty()){
            HashMap<Integer, Edge[]> lightComponenti = transferEdgeArray(lightComponent);
            //2在轻分量上找到最大基数匹配  M0  并将他们加入至T中
            findMaxMatching(lightComponenti,T);
            int curIndex = T.size();
            //对重分量进行分割，并加入到T中
            for (int i = 0; i < curIndex; i++) {
                double w=getweight(T.get(i));
                resNUm+=Math.ceil(2*w/Battery);
            }
        }
        if (!heavyComponent.isEmpty()){
            HashMap<Integer, Edge[]> heavyComponenti = transferEdgeArray(heavyComponent);
            for (int i = 0; i < heavyComponenti.size(); i++) {
                double w=getweight(heavyComponenti.get(i));
                resNUm+=Math.ceil(2*w/Battery);
            }
        }
        resNUm+=AloneNode.size();

    }

    private static double getweight(Edge[] edges) {
        double sum=0;
        for (Edge edge : edges) {
            sum+=edge.getWeight();
        }

        return sum;
    }

    private static void findMaxMatching(HashMap<Integer, Edge[]> lightComponent, HashMap<Integer, Edge[]> T) {
        int size = lightComponent.size();
        int index=0;
        HashMap<Integer, Node[]> lightNodes = new HashMap<>();
        for (int i = 0; i < size; i++) {
            Node[] nodes = getNodesInEdges(lightComponent.get(i));
            lightNodes.put(i,nodes);
        }

        UAV uav = new UAV(0);
        boolean[] visited = new boolean[size]; //判断当前连通分量是否已经匹配过了
        for (int i = 0; i < size; i++) {
            if (visited[i]){
                continue;
            }
            Node[] nodesL = lightNodes.get(i);
            double min=INF;
            int tmp=-1;
            for (Node node : nodesL) {
                Node tmpNode=null;
                for (int j = 0; j < size; j++) {
                    if (i==j||visited[j]){
                        continue;
                    }
                    Node[] nodesR = lightNodes.get(j);
                    for (Node node1 : nodesR) {
                        if ((node.getWeight()+node1.getWeight())/2+uav.getMovEnergy(node,node1)<min){
                            min=(node.getWeight()+node1.getWeight())/2+uav.getMovEnergy(node,node1);
                            tmp=j;
                            tmpNode=node1;
                        }
                    }
                }
                if (tmp!=-1){
                    //将两个连通分量进行合并
                    Edge[] edges1 = lightComponent.get(i);
                    Edge[] edges2 = lightComponent.get(tmp);
                    PriorityQueue<Edge> queue = new PriorityQueue<>((o1, o2) -> (int) (o1.getWeight()-o2.getWeight()));
                    queue.addAll(List.of(edges1));
                    queue.addAll(List.of(edges2));
                    queue.add(new Edge(node,tmpNode,min));
                    int size1 = queue.size();
                    Edge[] edges = new Edge[size1];
                    for (int i1 = 0; i1 < size1; i1++) {
                        edges[i1]=queue.poll();
                    }
                    T.put(index++,edges);

                    visited[i]=true;
                    visited[tmp]=true;
                }
            }
        }
        //将单独落空的连通分量加入至T中
        for (int i = 0; i < visited.length; i++) {
            if (!visited[i]){
                T.put(index,lightComponent.get(i));
            }
        }
    }

    //MCCPAlg2
    private static void minUavDeploymentComp2() {
        int size=allChgSet.size()+parameter.targetNum;
        //注：数组下标不是节点的序号
        allNodes = new Node[size];  //nodes数组的元素是最终待服务的所有节点
        //首先将节点归一化
        PriorityQueue<Node> que1=new PriorityQueue<>(Comparator.comparingInt(Node::getNo));
        HashSet<Node> tmpp = new HashSet<>();
        for (Sensor sensor : allChgSet) {
            tmpp.add(new Node(sensor));
        }
        for (Sink sink : sinks) {
            tmpp.add(new Node(sink));
        }
        que1.addAll(tmpp);
        for (int i = 0; i < allNodes.length; i++) {
            allNodes[i]=que1.poll();
        }
        System.out.print("所有待访问节点的序号：");
        for (int i = 0; i < allNodes.length; i++) {
            System.out.print(allNodes[i].getNo()+"  ");
        }
        System.out.println();

        //构造最终所有需要访问的节点之间的完全无向图
        double[][] weights = new double[size][size];
        UAV uav = new UAV(-100);
        for (int i = 0; i < size; i++) {
            Node nodei = allNodes[i];
            for (int j = 0; j < size; j++) {
                Node nodej = allNodes[j];
                if (i==j){
                    weights[i][j]=INF;
                }else {
                    weights[i][j]=uav.getMovEnergy(nodei,nodej)+(nodei.getWeight()+nodej.getWeight())/2;
                }
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(weights[i][j]+" ");
            }
            System.out.println();
        }

        HashSet<Node> AloneNode = new HashSet<>();  //记录因为边权阈值而被孤立的单节点连通分量
        double threshold= Battery/7;

        //移除边权大于threshold的边
        HashMap<Integer,HashSet<Edge>> component=delEdge(weights,AloneNode,threshold);  //存储连通分量对应最小生成树的集合 已验证
        //将所有的连通分量分为两类：轻分量  重分量
        HashMap<Integer,HashSet<Edge>> lightComponent=new HashMap<>();
        int indexL=0;
        HashMap<Integer,HashSet<Edge>> heavyComponent=new HashMap<>();
        int indexH=0;

        for (int i = 0; i < component.size(); i++) {
            HashSet<Edge> com = component.get(i);
            double comSum=0;
            for (Edge edge : com) {
                comSum+=edge.getWeight();
            }

            if (comSum<=3*Battery/28){
                lightComponent.put(indexL++,com);
            }else {
                heavyComponent.put(indexH++,com);
            }
        }
        HashMap<Integer, Edge[]> T = new HashMap<>();
        if (!lightComponent.isEmpty()){
            HashMap<Integer, Edge[]> lightComponenti = transferEdgeArray(lightComponent);
            //2在轻分量上找到最大基数匹配  M0  并将他们加入至T中
            findMaxMatching(lightComponenti,T);
            int curIndex = T.size();
            //对重分量进行分割，并加入到T中
            for (int i = 0; i < curIndex; i++) {
                double w=getweight(T.get(i));
                resNUm+=Math.ceil(2*w/Battery);
            }
        }
        if (!heavyComponent.isEmpty()){
            HashMap<Integer, Edge[]> heavyComponenti = transferEdgeArray(heavyComponent);
            for (int i = 0; i < heavyComponenti.size(); i++) {
                double w=getweight(heavyComponenti.get(i));
                resNUm+=Math.ceil(2*w/Battery);
            }
        }
        resNUm+=AloneNode.size();
    }

    //判断属于哪一个聚簇
    public static int ownCO(Sensor sensor){
        for (int j = 0; j < parameter.targetNum; j++) {
            if (map.get(j).contains(sensor)){
                return j;
            }
        }
        return -1;
    }
    //根据传感器编号判断是否在请求充电集合中
    public static boolean inChgSet(HashSet<Sensor> set,int i){
        for (Sensor sensor : set) {
            if (sensor.getNo()==i){
                return true;
            }
        }
        return false;
    }

    private static int chooseCO(int number,Sensor sensor) {
        float tmpUility=0;
        int temp=-1;
        for (int i = 0; i < parameter.targetNum; i++) {
            if (!map.get(i).contains(sensor)){
                float cost1=0;
                HashSet<Sensor> set = map.get(i);
                for (Sensor s : set) {
                    cost1+=s.getTransEnergy(sinks[i])+sinks[i].getCmpEnergy(s,set);
                }
                float cost2=0;
                set.add(sensor);
                for (Sensor s : set) {
                    cost2+=s.getTransEnergy(sinks[i])+sinks[i].getCmpEnergy(s,set);
                }
                set.remove(sensor);  //复原
                tmpUility=cost1-cost2;
                if (tmpUility>utility[number]){
                    temp=i;
                    utility[number]=tmpUility;  //传感器当前最大效用更新
                }
            }
        }
        return temp;
    }

    //两个对象之间距离
    public static double getDistance(double x1,double y1,double x2,double y2){
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }

    public static double setSinkX(double x){
        if (x<100){
            return Math.random()*(x+100+1);
        }else if (x>parameter.area-100){
            return Math.random()*(parameter.area-x+100+1)+x-100;
        }else {
            return Math.random()*201+x-100;
        }
    }

    public static double setSinkY(double y){
        if (y<100){
            return Math.random()*(y+100+1);
        }else if (y>parameter.area-100){
            return Math.random()*(parameter.area-y+100+1)+y-100;
        }else {
            return Math.random()*201+y-100;
        }
    }

    //设置传感器初始电量
    public static double randElec(double EU){
        double rand=Math.random();
        if (rand>=0.3){
            return Math.random()*(Ef+1);
        }else {
            return Math.random()*(EU+1);
        }
    }


    //判断点是否被覆盖
    private static boolean isCoverage(dataDisk c, Sensor sensor) {
        double x = c.getX();
        double y = c.getY();
        double sx = sensor.getX();
        double sy = sensor.getY();

        if (Math.pow(x-sx,2)+Math.pow(y-sy,2)<=c.getR()*c.getR()){
            return true;
        }
        return false;
    }

    //随机选择一个节点
    private static Sensor getRandSen(ArrayList<Sensor> senSet) {
        int size = senSet.size();
        int num= (int) (Math.random()*(size));
        return senSet.get(num);
    }

    //以目标为中心进行充电节点选择  改版
    private static void ChgNodeSelectionByTar(int k) {
        allChgSet=new HashSet<>();
        double allSumChg=0;
        for (int i = 0; i < parameter.targetNum; i++) {
            //每次初始化一个虚拟UAV
            UAV uav = new UAV(-1);

            //计算当前簇内的剩余电量，若大于k倍EU,则不需要进行补充电量
            int curEnergy=0;
            //遍历每一个聚簇，得到发起充电请求的节点
            HashSet<Sensor> set = new HashSet<>();
            for (Sensor sensor : map.get(i)) {
                curEnergy+=sensor.getEnergy();
                if (sensor.getEnergy()<Ef){
                    set.add(sensor);
                }
            }
            if (curEnergy>k*Eu){
                continue;
            }
            //初始值为数据汇集器直接到到该点的能耗
            double[] lowcost = new double[parameter.sensorNum];
            for (int j = 0; j < parameter.sensorNum; j++) {
                if (!inChgSet(set,j)){
                    lowcost[j]=INF;
                }else {
                    lowcost[j]=uav.getChrEnergy(sensors[j])+uav.getMovEnergy(sensors[j],targets[i]);
                }
            }

            //最小生成树路径选择  只需要节点，不需要具体的路径
            int sum=0;
            //当前聚簇候选充电节点集合
            HashSet<Sensor> selectSet = new HashSet<>();
            while (curEnergy+sum<k*Eu){
                double minCost=INF;
                int index=-1;
                for (int j = 0; j < parameter.sensorNum; j++) {
                    if (!selectSet.contains(sensors[j])&&set.contains(sensors[j])){
                        //12.2改动，增加中继节点的操作
                        double min=lowcost[j];
                        for (Sensor sensor : selectSet) {
                            min=Math.min(min,uav.getChrEnergy(sensors[j])+uav.getMovEnergy(sensors[j],sensor));
                        }
                        if (min<minCost){
                            minCost=min;
                            index=j;
                        }
                    }
                }

                //将其加入到allSet  更新该节点边权
                sum+=Eu-sensors[index].getEnergy();
                selectSet.add(sensors[index]);
                if (index!=-1){
                    lowcost[index]=minCost;
                }
            }
            double sumChg=0;
            // for (Sensor sensor : selectSet) {
            //     int no = sensor.getNo();
            //     sumChg+=lowcost[no]/Math.pow(10,3);
            // }
            // allSumChg+=sumChg;
            // System.out.println(sumChg);
            allChgSet.addAll(selectSet);
        }
    }

    //得到树的权重
    private static double getTreeWeight(Edge[] ti) {
        double sum=0;
        HashSet<Node> isUsed = new HashSet<>();  //判断节点是否已经加入过权重
        for (int i = 0; i < ti.length; i++) {
            Edge edge = ti[i];
            Node start = edge.getStart();
            Node end = edge.getEnd();
            sum+=edge.getWeight();
            if (!isUsed.contains(start)){
                sum+=start.getWeight();
                isUsed.add(start);
            }
            if (!isUsed.contains(end)){
                sum+=end.getWeight();
                isUsed.add(end);
            }
        }
        return sum;
    }
}