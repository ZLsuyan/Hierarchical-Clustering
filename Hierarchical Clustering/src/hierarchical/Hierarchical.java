package hierarchical;

/** 
 * @author ����
 * @date 2015/12/15
 */
import hierarchical.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import hierarchical.ReadDataset;

public class Hierarchical {
	/**
	 * ��ξ���-MIN
	 * @param MAXNUM  ���ݵ����
	 * @param K  ���õĴصĸ���
	 * @param Math.pow Mdissim[][] �����ƶȾ���
	 * @return ������
	 */
	
	// ���������Ҫ�Ĵصĸ���	 
	public static final int K = 3 ; 
	
	/**
	 * ����������룬�̻����ƶ�
	 * @param point1
	 * @param point2
	 * @return
	 */
	public static double distance(Point point1,Point point2){
		return Math.sqrt((point1.getX()-point2.getX())*(point1.getX()-point2.getX())+(point1.getY()-point2.getY())*(point1.getY()-point2.getY()));
	}
	
	
	/**
	 * ��������ƶȾ���
	 * @param n
	 * @param point
	 * @return
	 */
	public static double[][] disSimilarity(int n,Point[] point){
		double[][] Mdissim = new double[n][n];
		//��ʼ������
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				Mdissim[i][j]=0.0;
			}
		}
		/*
		 * ��������ƶ��Ծ���
		 * ��Ϊ�����ƶȾ����ǶԳƵģ����ֻ��Ҫ��������һ�뼴�ɣ�
		 * ʣ�µĶԳ���伴��
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
	 * �ϲ���
	 * �ϲ���ǰ����������ƶȵ�������
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
		* ��list1��list2�еĵ�ϲ����ر�Ž�С��
	    * ����ȥ,ͬʱ�޸Ĵر�Ŵ�Ĵ��е�Ĵر��
		*/
		for(int i =0;i<bigList.size();i++){
			Point point2 = bigList.get(i) ;
			//1_1.�޸Ĵ��е�Ĵر��
			point2.setClusterNum(smallListNum) ;
			//1_2.��cluster1��cluster2�еĵ�ϲ�
			smallList.add(point2);			
		}
		clusters.remove(bigList);		
	}
	
	 /**
	  * ������
	  * @param args
	  * @throws IOException
	  */
	public static void main(String[] args) throws IOException {
//		double tim1 = System.currentTimeMillis();
		//�����ݼ��л�ȡ���ݵ�ĸ���
		ReadDataset rds = new ReadDataset();
		int MAXNUM = rds.getTextLines();
		
		//����һ��Point���󣬱����ȡ�������ݼ��е����ݵ�
		Point[] my_point = new Point[MAXNUM]; 
		my_point = rds.getDataset();
		
		
		ArrayList<ArrayList<Point>> clusters = new ArrayList<ArrayList<Point>>() ;       		
		//��ʼ��ÿһ�����ݵ���Ϊһ����
		for(int i=0;i<MAXNUM;i++){                                    
    		ArrayList<Point> list = new ArrayList<Point>(); 
    		clusters.add(list);  
    		//��ʼ����ÿһ������Ϊһ���أ���i����ĴصĲ�α�ų�ʼ��Ϊi
    		my_point[i].setClusterNum(i) ;
			clusters.get(i).add(my_point[i]);
    	}		
		
		/*
		 * ���������ƶ��Ծ���ÿ�δ�1/2������ѡ�����СֵԪ�أ�
		 * �����±��Ӧ�����ݵ㼴Ϊ������Ҫ����ľ���������ƶȵ����ݵ�
		 */
		double[][] Mdissim = disSimilarity(MAXNUM,my_point);
		
		
		/*
		 * *******************************������ξ����㷨����************************************
		 * �㷨�� 1.ÿ�δӷ����ƶȾ�����ѡ����С��Ԫ�أ������ƶ����ĵ����ڵĴؽ��кϲ���
		 *       2.�ϲ�ͬʱ���������нϴ�ر�ŵĵ�Ĵر�Ÿ���Ϊ��С�Ĵر�ŵĵ�Ĵر�ţ�
		 *       3.ͬʱ��ؼ���һ�������������ݵ��дر�Ŵ��ڸոպϲ��Ľϴ�ر�ŵ����ݵ�Ĵر�ž���1 ��
		 *       4.�ϲ����Ҫ���·����ƶȾ���          
		 * �������MAXNUM
		 * ���Ĵصĸ�����K
		 * ���ܹ���������ΪMAXNUM-K��
		 */
		
		for(int t=0;t<MAXNUM-K;t++){
			//ÿ�ε���ʱ�����������СSSE
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
			 * ����һ�ε�����ѡȡ����num1�͵�num2�ľ�����С
			 */
			int t1 = my_point[num1].getClusterNum();
			int t2 = my_point[num2].getClusterNum();
			ArrayList<Point> list1 = clusters.get(t1) ;
			ArrayList<Point> list2 = clusters.get(t2) ;
					
			/*
			 * 1.�ϲ������������ڵĴ�
			 * 2.���µ�Ĵر��:���������ݵ�����Щ�ر��
			 * �������������� �ϴ�ر�ŵĵ�Ĵر��-1
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
				 * ����ʱѡ�е�������պ���ͬһ�����У�������������
				 * �����ƶȾ����ж�Ӧλ��Ԫ����Ϊһ���ܴ����
				 */
				Mdissim[num1][num2] = 1.0E12;
				t-- ;
			}			
			//������ѡ�е��������Ӧ�ķ����ƶȾ���ֵ��Ϊһ���ܴ����
			Mdissim[num1][num2] = 1.0E12;		
		}
		
		
		/*
		 * �����н�����*********************************************************************
		 */
		System.out.println("���ݵ�ĸ���Ϊ��"+MAXNUM+"���صĸ���Ϊ��"+clusters.size());
		for(int i=0;i<clusters.size();i++){			
			int pointNum = clusters.get(i).size();
			System.out.println("��"+(i+1)+"�����еĵ�d�ĸ���Ϊ��"+pointNum+"�����������ݵ�Ϊ��");
			//����ÿһ���أ�ÿ8�����ݵ㻻������������֮���ÿո����
    		for(int j =1;j<pointNum+1;j++){
    			if(j%8==0){
    				System.out.println(clusters.get(i).get(j-1).toString());
    			}else{
    				System.out.print(clusters.get(i).get(j-1).toString()+"  ");
    			}
    		}
            System.out.println();
		}
		
		
		//�������ĵ㣬���ݻ��ֵĴز�ͬ����������ļ���
        for(int i=1;i<(clusters.size()+1);i++){
        	//��ȡÿһ�����е����ݵ�ĸ���
        	int pointNum = clusters.get(i-1).size();
        	/*
        	 * ָ���ֱ𱣴�ÿһ���ص�X��Y������ļ�·�����ļ���
        	 * ��cu��+i+��x.txt����ʾ��i��x�����ļ�
        	 * ��cu��+i+��x.txt����ʾ��i��y�����ļ�
        	 */
        	File xfile= new File("C:/Users/Zengli/Desktop/�����ھ�-ʵ�����ݼ�/test2/cu"+i+"x.txt");
        	File yfile= new File("C:/Users/Zengli/Desktop/�����ھ�-ʵ�����ݼ�/test2/cu"+i+"y.txt");
        	for(int j=0;j<pointNum;j++){
        		//��ÿһ�����е��������ݵ��x��y�������ݷֱ�д��ָ���ļ���
        		ReadDataset.append(xfile, clusters.get(i-1).get(j).getX()+" ");
        		ReadDataset.append(yfile, clusters.get(i-1).get(j).getY()+" ");
            }
        }

		
//        double tim2 = System.currentTimeMillis();
//        System.out.println(tim2-tim1);
	}
	
}
