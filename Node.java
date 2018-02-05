package ARPG;

public class Node extends Object{
	int i;
	int j;
	int d;
	int g;
	int f;
	int parenti;
	int parentj;
	public Node(int i, int j, int i2,int j2){
		this.i=i;
		this.j=j;
		f=0;
		g=0;
		d=distance(i,j,i2,j2);
	}
	
	public void setd(int d){
		this.d=d;	
	}
	public int getd(){
		return d;
	}
	public void setg(int g){
		this.g=g;	
	}
	public int getg(){
		return g;
	}
	public void setf(int f){
		this.f=f;	
	}
	public int getf(){
		return f;
	}
	
	public int getParenti(){
		return parenti;
	}
	public void setParenti(int i){
		parenti=i;
	}
	public int getParentj(){
		return parentj;
	}
	public void setParentj(){
		parentj=j;
	}
}
