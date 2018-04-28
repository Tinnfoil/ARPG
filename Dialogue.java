package ARPG;

public class Dialogue extends Object{
	
	private String str;
	private int length;
	private int currlength;
	private int currdelay;
	private int delay;
	private int wait;
	private int currwait;
	private int currintialwait;
	private int intialwait;
	
	private int xoff;
	private int yoff;
	
	public Dialogue(String str){
		setString(str);
		setLength(str.length());
		setCurrlength(0);
		setCurrwait(0);
		setDelay(10);
		setCurrintialwait(0);
		setIntialwait(0);
		xoff=0;
		yoff=0;
	}

	public String getCurrstring(){
		return str.substring(0, currlength);
	}
	
	public String getString() {
		return str;
	}

	public void setString(String str) {
		this.str = str;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getCurrlength() {
		return currlength;
	}

	public void setCurrlength(int currlength) {
		this.currlength = currlength;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getWait() {
		return wait;
	}

	public void setWait(int wait) {
		this.wait = wait;
	}

	public int getCurrdelay() {
		return currdelay;
	}

	public void setCurrdelay(int currdelay) {
		this.currdelay = currdelay;
	}

	public int getCurrwait() {
		return currwait;
	}

	public void setCurrwait(int currwait) {
		this.currwait = currwait;
	}

	public int getIntialwait() {
		return intialwait;
	}

	public void setIntialwait(int intialwait) {
		this.intialwait = intialwait;
	}

	public int getCurrintialwait() {
		return currintialwait;
	}

	public void setCurrintialwait(int currintialwait) {
		this.currintialwait = currintialwait;
	}

	public int getXoff() {
		return xoff;
	}

	public void setXoff(int xoff) {
		this.xoff = xoff;
	}

	public int getYoff() {
		return yoff;
	}

	public void setYoff(int yoff) {
		this.yoff = yoff;
	}
	
	public void postCredits(Play p){
		int lifetime=7200;
		p.addString("",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("Credits:",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("Kenny Doan: 			Director, Game Design, & Programer",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("Pampreet Gill: 		Music Producer",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("Brian Tran: 			Sound Design",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("Benjamin Lee: 		Alpha/Beta Tester",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("Lynn Doan: 			Concept designs & Kenny's imouto",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("Special Thanks to:",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("Adrian Tsang",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("William Cheng",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("Joe Yuan",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("Michael",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("Ryan Chang",0,0,lifetime);
		p.addString("-\"fite me ssbm\"",0,0,lifetime);p.addString("",0,0,lifetime);
		p.addString("James \"Mongoloid\" Lai",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("Andy Banh",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("David Jian",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("Catherine Nguyen",0,0,lifetime);
		p.addString("",0,0,lifetime);
		p.addString("Nolan Tonthat",0,0,lifetime);
		p.addString("-\"Yikes\"",0,0,lifetime);p.addString("",0,0,lifetime);
		p.addString("Eddie Chi",0,0,lifetime);
		p.addString("-\"Why am I here?\"",0,0,lifetime);p.addString("",0,0,lifetime);
		
	}
}
