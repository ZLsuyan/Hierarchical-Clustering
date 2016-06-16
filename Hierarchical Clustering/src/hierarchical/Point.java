package hierarchical;
/**
 * @author ����  
 * @date 2015/12/15
 */

public  class Point{             //����һ��Point�࣬��ʾ���ݵ������
	private double x ;
	private double y ;
	private int clusterNum ;     //�����ĴصĲ�α��
	
	
	
	public Point() {
		super();
	}

	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public Point(double x, double y, int clusterNum) {
		super();
		this.x = x;
		this.y = y;
		this.clusterNum = clusterNum;
	}

	public int getClusterNum() {
		return clusterNum;
	}

	public void setClusterNum(int clusterNum) {
		this.clusterNum = clusterNum;
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

	public String toString() {
		return "("+x+","+y+")";
	} 	

}
