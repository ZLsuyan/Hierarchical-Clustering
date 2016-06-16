package hierarchical;

/** 
 * @author 曾丽
 * @date 2015/12/15
 */
import hierarchical.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import hierarchical.ReadDataset;

public class Hierarchical {
	/**
	 * 层次聚类-MIN
	 * @param MAXNUM  数据点个数
	 * @param K  想获得的簇的个数
	 * @param Math.pow Mdissim[][] 非相似度矩阵
	 * @return 聚类层次
	 */
	
	// 定义最后想要的簇的个数	 
	public static final int K = 3 ; 
	
	/**
	 * 计算两点距离，刻画相似度
	 * @param point1
	 * @param point2
	 * @return
	 */
	public static double distance(Point point1,Point point2){
		return Math.sqrt((point1.getX()-point2.getX())*(point1.getX()-point2.getX())+(point1.getY()-point2.getY())*(point1.getY()-point2.getY()));
	}
	
	
	/**
	 * 构造非相似度矩阵
	 * @param n
	 * @param point
	 * @return
	 */
	public static double[][] disSimilarity(int n,Point[] point){
		double[][] Mdissim = new double[n][n];
		//初始化数组
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				Mdissim[i][j]=0.0;
			}
		}
		/*
		 * 计算非相似度性矩阵，
		 * 因为非相似度矩阵是对称的，因此只需要计算矩阵的一半即可，
		 * 剩下的对称填充即可
		 */
		for(int i=0;i<n;i++){
			for(int j=0;j<=i;j++){
				Mdissim[i][j] = distance(point[i],point[j]);
				Mdissim[j][i] = Mdissim[i][j];
			}
		}
		return Mdissim;
	}
	
	/**
	 * 合并簇
	 * 合并当前具有最大相似度的两个簇
	 * @param clusters
	 * @param list1
	 * @param list2
	 * @param hierarchy
	 * @param hierarchy1
	 * @param hierarchy2
	 * @param t
	 */
	public static void MergeClusters(ArrayList<ArrayList<Point>> clusters ,ArrayList<Point> smallList,ArrayList<Point> bigList){
		int smallListNum = smallList.get(0).getClusterNum();	
     	/*
		* 将list1和list2中的点合并到簇编号较小的
	    * 簇中去,同时修改簇编号大的簇中点的簇编号
		*/
		for(int i =0;i<bigList.size();i++){
			Point point2 = bigList.get(i) ;
			//1_1.修改簇中点的簇编号
			point2.setClusterNum(smallListNum) ;
			//1_2.将cluster1和cluster2中的点合并
			smallList.add(point2);			
		}
		clusters.remove(bigList);		
	}
	
	 /**
	  * 主函数
	  * @param args
	  * @throws IOException
	  */
	public static void main(String[] args) throws IOException {
//		double tim1 = System.currentTimeMillis();
		//从数据集中获取数据点的个数
		ReadDataset rds = new ReadDataset();
		int MAXNUM = rds.getTextLines();
		
		//创建一个Point对象，保存读取到的数据集中的数据点
		Point[] my_point = new Point[MAXNUM]; 
		my_point = rds.getDataset();
		
		
		ArrayList<ArrayList<Point>> clusters = new ArrayList<ArrayList<Point>>() ;       		
		//初始，每一个数据点作为一个簇
		for(int i=0;i<MAXNUM;i++){                                    
    		ArrayList<Point> list = new ArrayList<Point>(); 
    		clusters.add(list);  
    		//初始化：每一个点作为一个簇，第i个点的簇的层次编号初始化为i
    		my_point[i].setClusterNum(i) ;
			clusters.get(i).add(my_point[i]);
    	}		
		
		/*
		 * 遍历非相似度性矩阵，每次从1/2矩阵中选择出最小值元素，
		 * 这组下标对应的数据点即为接下来要处理的具有最大相似度的数据点
		 */
		double[][] Mdissim = disSimilarity(MAXNUM,my_point);
		
		
		/*
		 * *******************************单链层次聚类算法核心************************************
		 * 算法： 1.每次从非相似度矩阵中选出最小的元素，即相似度最大的点所在的簇进行合并；
		 *       2.合并同时将两个簇中较大簇编号的点的簇编号更新为较小的簇编号的点的簇编号；
		 *       3.同时最关键的一步，将所有数据点中簇编号大于刚刚合并的较大簇编号的数据点的簇编号均减1 ；
		 *       4.合并完后要更新非相似度矩阵          
		 * 点个数：MAXNUM
		 * 最后的簇的个数：K
		 * 则总共迭代次数为MAXNUM-K次
		 */
		
		for(int t=0;t<MAXNUM-K;t++){
			//每次迭代时保存矩阵中最小SSE
			double Min = 1.0E12;
			int num1=0 ,num2 = 0;
			
			for(int i=1;i<MAXNUM;i++){
				for(int j=0;j<i;j++){
					if(Mdissim[i][j]<Min){
						Min = Mdissim[i][j];
						num1 = i;
						num2 = j;
					}
				}
			}
			/*
			 * 假设一次迭代，选取出点num1和点num2的距离最小
			 */
			int t1 = my_point[num1].getClusterNum();
			int t2 = my_point[num2].getClusterNum();
			ArrayList<Point> list1 = clusters.get(t1) ;
			ArrayList<Point> list2 = clusters.get(t2) ;
					
			/*
			 * 1.合并这两个点所在的簇
			 * 2.更新点的簇编号:将所有数据点中那些簇编号
			 * 大于这两个点中 较大簇编号的点的簇编号-1
			 */
			if(t1<t2){
				MergeClusters(clusters,list1,list2);
				for(int i=0;i<MAXNUM;i++){
					if(my_point[i].getClusterNum()>t2){
						my_point[i].setClusterNum((my_point[i].getClusterNum()-1));
					}					
				}
			}else if(t1>t2){
				MergeClusters(clusters,list2,list1);
				for(int i=0;i<MAXNUM;i++){
					if(my_point[i].getClusterNum()>t1){
						my_point[i].setClusterNum((my_point[i].getClusterNum()-1));
					}
				}
			} else {
				/*
				 * 若此时选中的两个点刚好在同一个簇中，将这两个点在
				 * 非相似度矩阵中对应位置元素置为一个很大的数
				 */
				Mdissim[num1][num2] = 1.0E12;
				t-- ;
			}			
			//将本次选中的两个点对应的非相似度矩阵值置为一个很大的数
			Mdissim[num1][num2] = 1.0E12;		
		}
		
		
		/*
		 * 对运行结果输出*********************************************************************
		 */
		System.out.println("数据点的个数为："+MAXNUM+"，簇的个数为："+clusters.size());
		for(int i=0;i<clusters.size();i++){			
			int pointNum = clusters.get(i).size();
			System.out.println("第"+(i+1)+"个簇中的点d的个数为："+pointNum+"，包含的数据点为：");
			//对于每一个簇，每8个数据点换行输出，点与点之间用空格隔开
    		for(int j =1;j<pointNum+1;j++){
    			if(j%8==0){
    				System.out.println(clusters.get(i).get(j-1).toString());
    			}else{
    				System.out.print(clusters.get(i).get(j-1).toString()+"  ");
    			}
    		}
            System.out.println();
		}
		
		
		//将聚类后的点，根据划分的簇不同进行输出到文件中
        for(int i=1;i<(clusters.size()+1);i++){
        	//获取每一个簇中的数据点的个数
        	int pointNum = clusters.get(i-1).size();
        	/*
        	 * 指定分别保存每一个簇的X、Y坐标的文件路径和文件名
        	 * ”cu”+i+”x.txt”表示簇i的x坐标文件
        	 * ”cu”+i+”x.txt”表示簇i的y坐标文件
        	 */
        	File xfile= new File("C:/Users/Zengli/Desktop/数据挖掘-实验数据集/test2/cu"+i+"x.txt");
        	File yfile= new File("C:/Users/Zengli/Desktop/数据挖掘-实验数据集/test2/cu"+i+"y.txt");
        	for(int j=0;j<pointNum;j++){
        		//将每一个簇中的所有数据点的x和y坐标数据分别写入指定文件中
        		ReadDataset.append(xfile, clusters.get(i-1).get(j).getX()+" ");
        		ReadDataset.append(yfile, clusters.get(i-1).get(j).getY()+" ");
            }
        }

		
//        double tim2 = System.currentTimeMillis();
//        System.out.println(tim2-tim1);
	}
	
}
