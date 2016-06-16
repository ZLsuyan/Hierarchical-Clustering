package hierarchical;
/**
 * @author 曾丽  
 * @date 2015/12/15
 */

public  class Point{             //定义一个Point类，表示数据点的坐标
	private double x ;
	private double y ;
	private int clusterNum ;     //所属的簇的层次编号
	
	
	
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
