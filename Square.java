package ARPG;

import java.awt.Rectangle;

public class Square extends Object
{
	
    private int width;
    private int height;
    private int health;
    private int maxhealth;
    private int skillpoints;
    private boolean fireupgrade;
    private boolean canfreeze; private int freezeframes; private int currfreezecooldown; private int freezecooldown;
    private boolean phasewalkupgrade; private int currphasewalkcooldown; private int phasewalkcooldown;
    
    private int attackdamage;
    private boolean attacking;
    private double attackangle;
    private int attackcooldown;
    private int currattackcooldown;
    private int attackamount;
    private int healthonhit;
    
    
    private int shotcharges;
    private int projectileshots;
    
    private boolean isDashing;//Boolean to determine whether or not the player is dashing.
    private int dashcooldown;
    private int currdashcooldown;
    private int parryframes;
    private int parrycooldown;
    private int currparrycooldown;
    /**
     * Constructor for objects of class Square
     */
    public Square()
    {
    	width=30;
    	height=30;
        setX(500);//4150
        setY(500);
        Rectangle rect= new Rectangle(getX(),getY(),width,height);
        setRect(rect);
        setSkillpoints(0);
        fireupgrade=false;
        canfreeze=false; setFreezeframes(0); setCurrfreezecooldown(0); setFreezecooldown(600);
        phasewalkupgrade=true; setCurrphasewalkcooldown(0); setPhasewalkcooldown(480);
        
        setMaxspeed(4);
        setAcceleration(2);
        setMaxhealth(100);
        setHealth(getMaxhealth());//Base 100, can change
        setAttackdamage(50);
        setAttackcooldown(20);
        setHealthonhit(0);
        
        setShotcharges(3);
        setProjectileshots(1);
        
        setInvunerable(false);
        isDashing=false;
        setDashcooldown(60);//60 frames
        setParrycooldown(120);
        setSpeedboostduration(75);
    }
    
    public int getWidth(){
    	return width;
    }
    public int getHeight(){
    	return height;
    }
    
    public boolean getDashing(){
    	return isDashing;
    }
    public void setDashing(boolean b){
    	isDashing=b;
    }

	public int getDashcooldown() {
		return dashcooldown;
	}

	public void setDashcooldown(int dashcooldown) {
		this.dashcooldown = dashcooldown;
	}

	public int getCurrdashcooldown() {
		return currdashcooldown;
	}

	public void setCurrdashcooldown(int currdashcooldown) {
		this.currdashcooldown = currdashcooldown;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
		if(health>=maxhealth){
			this.health=maxhealth;
		}
	}

	public int getMaxhealth() {
		return maxhealth;
	}

	public void setMaxhealth(int maxhealth) {
		this.maxhealth = maxhealth;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public double getAttackangle() {
		return attackangle;
	}

	public void setAttackangle(double attackangle) {
		this.attackangle = attackangle;
	}

	public int getAttackamount() {
		return attackamount;
	}

	public void setAttackamount(int attackamount) {
		this.attackamount = attackamount;
	}

	public int getAttackcooldown() {
		return attackcooldown;
	}

	public void setAttackcooldown(int attackcooldown) {
		this.attackcooldown = attackcooldown;
	}

	public int getCurrattackcooldown() {
		return currattackcooldown;
	}

	public void setCurrattackcooldown(int currattackcooldown) {
		this.currattackcooldown = currattackcooldown;
	}

	public int getParryframes() {
		return parryframes;
	}

	public void setParryframes(int parryframes) {
		this.parryframes = parryframes;
	}

	public int getParrycooldown() {
		return parrycooldown;
	}

	public void setParrycooldown(int parrycooldown) {
		this.parrycooldown = parrycooldown;
	}

	public int getCurrparrycooldown() {
		return currparrycooldown;
	}

	public void setCurrparrycooldown(int currparrycooldown) {
		this.currparrycooldown = currparrycooldown;
	}

	public int getShotcharges() {
		return shotcharges;
	}

	public void setShotcharges(int shotcharges) {
		if(shotcharges<=3){//temporary
			this.shotcharges = shotcharges;
		}
	}

	public int getAttackdamage() {
		return attackdamage;
	}

	public void setAttackdamage(int attackdamage) {
		this.attackdamage = attackdamage;
	}

	public int getProjectileshots() {
		return projectileshots;
	}

	public void setProjectileshots(int projectileshots) {
		this.projectileshots = projectileshots;
	}

	public int getSkillpoints() {
		return skillpoints;
	}

	public void setSkillpoints(int skillpoints) {
		this.skillpoints = skillpoints;
	}

	public boolean isFireupgrade() {
		return fireupgrade;
	}

	public void setFireupgrade(boolean fireupgrade) {
		this.fireupgrade = fireupgrade;
	}

	public int getHealthonhit() {
		return healthonhit;
	}

	public void setHealthonhit(int healthonhit) {
		this.healthonhit = healthonhit;
	}

	public boolean isCanfreeze() {
		return canfreeze;
	}

	public void setCanfreeze(boolean canfreeze) {
		this.canfreeze = canfreeze;
	}

	public int getFreezeframes() {
		return freezeframes;
	}

	public void setFreezeframes(int freezeframes) {
		this.freezeframes = freezeframes;
	}
	
	public void freezeTime(){
		if(isCanfreeze()==true&&getCurrfreezecooldown()<=0){
			setStunframes(0);
			setInvunerableframes(120);
    		setFreezeframes(120);
    		setCurrfreezecooldown(getFreezecooldown());
    	}
	}
	
	public int getCurrfreezecooldown() {
		return currfreezecooldown;
	}

	public void setCurrfreezecooldown(int currfreezecooldown) {
		this.currfreezecooldown = currfreezecooldown;
	}

	public int getFreezecooldown() {
		return freezecooldown;
	}

	public void setFreezecooldown(int freezecooldown) {
		this.freezecooldown = freezecooldown;
	}

	public boolean isPhasewalkupgrade() {
		return phasewalkupgrade;
	}

	public void setPhasewalkupgrade(boolean phasewalkupgrade) {
		this.phasewalkupgrade = phasewalkupgrade;
	}

	public int getCurrphasewalkcooldown() {
		return currphasewalkcooldown;
	}

	public void setCurrphasewalkcooldown(int currphasewalkcooldown) {
		this.currphasewalkcooldown = currphasewalkcooldown;
	}

	public int getPhasewalkcooldown() {
		return phasewalkcooldown;
	}

	public void setPhasewalkcooldown(int phasewalkcooldown) {
		this.phasewalkcooldown = phasewalkcooldown;
	}


    
}


