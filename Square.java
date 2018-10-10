package ARPG;

import java.awt.Rectangle;
import java.util.Random;

public class Square extends Object
{
	
    private int width;
    private int height;
    private int health;private int displayhealth;
    private int maxhealth;
    private int skillpoints;
    private int dodge;
    private boolean fireupgrade;
    private boolean canfreeze; private int freezeframes; private int currfreezecooldown; private int freezecooldown;
    private boolean phasewalkupgrade; private int currphasewalkcooldown; private int phasewalkcooldown; private boolean phasewalking;
    private boolean comboupgrade;
    private boolean stundashupgrade; private boolean dashstunned;
    private boolean spinupgrade; private int attacklimit; private int spincooldown; private int currspincooldown;
    
    private int attackdamage;
    private boolean attacking;
    private double attackangle;
    private int attackcooldown;
    private int currattackcooldown;
    private int attackamount;
    private int attackcombo;
    private int comboattacks;
    private int attackarc;
    private int lastattackradius;
    private int lastattackangle;
    private int maxattackamount;
    private int attackinterval;
    private int healthonhit;
    private boolean canlifesteal;
    private boolean chargingattack;
    private int attackchargeframes;
    private int maxattackchargeframes;
    private int totalattackchargeframes;
    private int tickframes;
    
    private double dvelx; private double dvely;
    private double camdisplacex; private double camdisplacey;
    private int dizzylimit; 
    private int dizzymaxlimit;
    private double shakex; private double shakey;
    private int shakelimit;
    
    private int shotcharges;
    private boolean shooting;
    private int projectileshots;
    private double angle;
    private int chargeframes;
    private int maxchargeframes;
    private int totalchargeframes;
    
    private boolean isDashing;//Boolean to determine whether or not the player is dashing.
    private int dashcooldown;
    private int currdashcooldown;
    private int parryframes;
    private int maxparry;
    private int parrycooldown;
    private int currparrycooldown;
    /**
     * Constructor for objects of class Square
     */
    public Square()
    {
    	width=30;
    	height=30;
        setX(700);//4150
        setY(700);
        Rectangle rect= new Rectangle(getX(),getY(),width,height);
        setRect(rect);
        setSkillpoints(0);
        
        setMaxspeed(4);
        setAcceleration(2);
        setMaxhealth(100);
        setHealth(getMaxhealth());//Base 100, can change
        setDisplayhealth(getHealth());
        setAttacking(false);
        setAttackdamage(50);
        setAttackcombo(1);
        setComboattacks(1);
        setMaxattackamount(180);
        setAttackinterval(30);
        setAttackarc(getMaxattackamount());
        setLastattackangle(0);
        setLastattackradius(0);
        setChargingattack(false);
        setAttackchargeframes(0);
        setMaxattackchargeframes(30);
        setTotalattackchargeframes(0);
        setTickframes(0);
        
        setDvelx(0); setDvely(0);
        setCamdisplacex(0);setCamdisplacey(0);
        setDizzylimit(0);
        setDizzymaxlimit(60);
        
        setAttackcooldown(20);
        setHealthonhit(0);
        setShotcharges(3);
        setProjectileshots(1);
        setShooting(false);
        setChargeframes(0);
        setMaxchargeframes(30);
        setTotalchargeframes(0);
        
        setInvunerable(false);
        isDashing=false;
        setDashcooldown(60);//60 frames
        setParrycooldown(120);//120
        setSpeedboostduration(75);
        setDodge(5);
        
        fireupgrade=false;
        canfreeze=false; setFreezeframes(0); setCurrfreezecooldown(0); setFreezecooldown(780);
        phasewalkupgrade=false; setCurrphasewalkcooldown(0); setPhasewalkcooldown(600); phasewalking=false;
        comboupgrade=false;
        stundashupgrade=true; setDashstunned(false);
        spinupgrade=false; setAttacklimit(0); setCurrspincooldown(0); setSpincooldown(getParrycooldown());
        
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
		if(Math.abs(this.parryframes-parryframes)>10){
			this.maxparry=parryframes;
		}
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
	
	public void freezeTime(Play p) throws Exception{
		if(isCanfreeze()==true&&getCurrfreezecooldown()<=0){
			p.sound.freeze();
			p.sound.playeffect("Freezetimestart");
			setStunframes(0);
			setInvunerableframes(180);
    		setFreezeframes(180);
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

	public boolean isCanlifesteal() {
		return canlifesteal;
	}

	public void setCanlifesteal(boolean canlifesteal) {
		this.canlifesteal = canlifesteal;
	}

	public boolean isPhasewalking() {
		return phasewalking;
	}

	public void setPhasewalking(boolean phasewalking) {
		this.phasewalking = phasewalking;
	}

	public int getAttackcombo() {
		return attackcombo;
	}

	public void setAttackcombo(int attackcombo) {
		this.attackcombo = attackcombo;
	}

	public int getComboattacks() {
		return comboattacks;
	}

	public void setComboattacks(int comboattacks) {
		this.comboattacks = comboattacks;
	}

	public boolean isComboupgrade() {
		return comboupgrade;
	}

	public void setComboupgrade(boolean comboupgrade) {
		this.comboupgrade = comboupgrade;
	}

	public boolean isStundashupgrade() {
		return stundashupgrade;
	}

	public void setStundashupgrade(boolean stundashupgrade) {
		this.stundashupgrade = stundashupgrade;
	}

	public boolean isDashstunned() {
		return dashstunned;
	}

	public void setDashstunned(boolean dashstunned) {
		this.dashstunned = dashstunned;
	}

	public int getMaxparry() {
		return maxparry;
	}

	public void setMaxparry(int maxparry) {
		this.maxparry = maxparry;
	}

	public int getAttacklimit() {
		return attacklimit;
	}

	public void setAttacklimit(int attacklimit) {
		this.attacklimit = attacklimit;
	}

	public boolean isSpinupgrade() {
		return spinupgrade;
	}

	public void setSpinupgrade(boolean spinupgrade) {
		this.spinupgrade = spinupgrade;
	}

	public int getSpincooldown() {
		return spincooldown;
	}

	public void setSpincooldown(int spincooldown) {
		this.spincooldown = spincooldown;
	}

	public int getCurrspincooldown() {
		return currspincooldown;
	}

	public void setCurrspincooldown(int currspincooldown) {
		this.currspincooldown = currspincooldown;
	}
	
	public void spin(){
		if(isSpinupgrade()){
			if(getCurrspincooldown()<=0){
				setCurrspincooldown(getSpincooldown());
			}
		}
		else{
			setCurrspincooldown(14);
		}
	}

	public int getDizzylimit() {
		return dizzylimit;
	}

	public void setDizzylimit(int dizzylimit) {
		this.dizzylimit = dizzylimit;
	}

	public int getDizzymaxlimit() {
		return dizzymaxlimit;
	}

	public void setDizzymaxlimit(int dizzymaxlimit) {
		this.dizzymaxlimit = dizzymaxlimit;
	}

	public double getCamdisplacex() {
		return camdisplacex;
	}

	public void setCamdisplacex(double camdisplacex) {
		this.camdisplacex = camdisplacex;
	}

	public double getCamdisplacey() {
		return camdisplacey;
	}

	public void setCamdisplacey(double camdisplacey) {
		this.camdisplacey = camdisplacey;
	}

	public void shakeCam(){
		Random RN= new Random();
		if(Math.abs(getShakex())<getShakelimit()){
			setCamdisplacex(getCamdisplacex()+(Math.pow(-1, RN.nextInt(2))*1));
		}
		else if(Math.abs(getShakex())>getShakelimit()){
			if(Math.abs(getShakex())<=1&&getShakelimit()<=0){
				setShakex(0);
			}
			if(getShakex()>0){
				setShakex(getShakex()-1);
			}
			else if(getShakex()<0){
				setShakex(getShakex()+1);
			}
		}
		if(Math.abs(getShakey())<getShakelimit()){
			setShakey(getShakey()+(Math.pow(-1, RN.nextInt(2))*1));
		}
		else if(Math.abs(getShakey())>getShakelimit()){
			if(Math.abs(getShakey())<=1&&getShakelimit()<=0){
				setShakey(0);
			}
			if(getShakey()>0){
				setShakey(getShakey()-1);
			}
			else if(getShakey()<0){
				setShakey(getShakey()+1);
			}
		}
	}
	
	public void displaceCam(){
		//Random RN= new Random();
		//System.out.println(getCamdisplacex());
		//System.out.println(getCamdisplacey());
		if(Math.abs(getCamdisplacex())<getDizzylimit()){
			setCamdisplacex(getCamdisplacex()+getDvelx());
		}
		else if(Math.abs(getCamdisplacex())>getDizzylimit()){
			//System.out.println("X");
			if(getCamdisplacex()>0){
				setDvelx(getDvelx()-.3);
			}
			else if(getCamdisplacex()<0){
				setDvelx(getDvelx()+.3);
			}
			setCamdisplacex(getCamdisplacex()+getDvelx());
		}
		if(Math.abs(getCamdisplacey())<getDizzylimit()){
			setCamdisplacey(getCamdisplacey()+getDvely());
		}
		else if(Math.abs(getCamdisplacey())>getDizzylimit()){
			//System.out.println("Y");
			if(getCamdisplacey()>0){
				setDvely(getDvely()-.3);
			}
			else if(getCamdisplacey()<0){
				setDvely(getDvely()+.3);
			}
			setCamdisplacey(getCamdisplacey()+getDvely());
		}
		
		if(getDizzylimit()==0){
			setDvelx(0);
			setDvely(0);
			if(getCamdisplacex()>0){
				setCamdisplacex(getCamdisplacex()-.2);
			}
			else if(getCamdisplacex()<0){
				setCamdisplacex(getCamdisplacex()+.2);
			}
			if(getCamdisplacey()>0){
				setCamdisplacey(getCamdisplacey()-.2);
			}
			else if(getCamdisplacey()<0){
				setCamdisplacey(getCamdisplacey()+.2);
			}
		}
	}
	
	public void displaceVel(){
		Random RN= new Random();
		if(Math.abs(getDvelx())<5){
			setDvelx(getDvelx()+(Math.pow(-1, RN.nextInt(2))*.2));			
		}
		else{
			if(getDvelx()>5){
				setDvelx(getDvelx()-.2);
			}
			else if(getDvelx()<-5){
				setDvelx(getDvelx()+.2);
			}
		}
		if(Math.abs(getDvely())<5){
			setDvely(getDvely()+(Math.pow(-1, RN.nextInt(2))*.2));
		}
		else{
			if(getDvely()>5){
				setDvely(getDvely()-.2);
			}
			else if(getDvely()<-5){
				setDvely(getDvely()+.2);
			}
		}
	}

	public double getDvely() {
		return dvely;
	}

	public void setDvely(double dvely) {
		this.dvely = dvely;
	}

	public double getDvelx() {
		return dvelx;
	}

	public void setDvelx(double dvelx) {
		this.dvelx = dvelx;
	}

	public double getShakey() {
		return shakey;
	}

	public void setShakey(double shakey) {
		this.shakey = shakey;
	}

	public double getShakex() {
		return shakex;
	}

	public void setShakex(double shakex) {
		this.shakex = shakex;
	}

	public int getShakelimit() {
		return shakelimit;
	}

	public void setShakelimit(int shakelimit) {
		this.shakelimit = shakelimit;
	}

	public boolean isShooting() {
		return shooting;
	}

	public void setShooting(boolean shooting) {
		this.shooting = shooting;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public int getChargeframes() {
		return chargeframes;
	}

	public void setChargeframes(int chargeframes) {
		this.chargeframes = chargeframes;
	}

	public int getMaxchargeframes() {
		return maxchargeframes;
	}

	public void setMaxchargeframes(int maxchargeframes) {
		this.maxchargeframes = maxchargeframes;
	}

	public int getTotalchargeframes() {
		return totalchargeframes;
	}

	public void setTotalchargeframes(int totalchargeframes) {
		this.totalchargeframes = totalchargeframes;
	}

	public int getDisplayhealth() {
		return displayhealth;
	}

	public void setDisplayhealth(int displayhealth) {
		this.displayhealth = displayhealth;
	}

	public int getAttackchargeframes() {
		return attackchargeframes;
	}

	public void setAttackchargeframes(int attackchargeframes) {
		this.attackchargeframes = attackchargeframes;
	}

	public int getMaxattackchargeframes() {
		return maxattackchargeframes;
	}

	public void setMaxattackchargeframes(int maxattackchargeframes) {
		this.maxattackchargeframes = maxattackchargeframes;
	}

	public int getTotalattackchargeframes() {
		return totalattackchargeframes;
	}

	public void setTotalattackchargeframes(int totalattackchargeframes) {
		this.totalattackchargeframes = totalattackchargeframes;
	}

	/**
	 * @return the chargingattack
	 */
	public boolean isChargingattack() {
		return chargingattack;
	}

	/**
	 * @param chargingattack the chargingattack to set
	 */
	public void setChargingattack(boolean chargingattack) {
		this.chargingattack = chargingattack;
	}

	/**
	 * @return the maxattackamount
	 */
	public int getMaxattackamount() {
		return maxattackamount;
	}

	/**
	 * @param maxattackamount the maxattackamount to set
	 */
	public void setMaxattackamount(int maxattackamount) {
		this.maxattackamount = maxattackamount;
	}

	/**
	 * @return the attackinterval
	 */
	public int getAttackinterval() {
		return attackinterval;
	}

	/**
	 * @param attackinterval the attackinterval to set
	 */
	public void setAttackinterval(int attackinterval) {
		this.attackinterval = attackinterval;
	}

	/**
	 * @return the attackarc
	 */
	public int getAttackarc() {
		return attackarc;
	}

	/**
	 * @param attackarc the attackarc to set
	 */
	public void setAttackarc(int attackarc) {
		this.attackarc = attackarc;
	}

	/**
	 * @return the lastattackangle
	 */
	public int getLastattackangle() {
		return lastattackangle;
	}

	/**
	 * @param lastattackangle the lastattackangle to set
	 */
	public void setLastattackangle(int lastattackangle) {
		this.lastattackangle = lastattackangle;
	}

	/**
	 * @return the lastattackradius
	 */
	public int getLastattackradius() {
		return lastattackradius;
	}

	/**
	 * @param lastattackradius the lastattackradius to set
	 */
	public void setLastattackradius(int lastattackradius) {
		this.lastattackradius = lastattackradius;
	}

	/**
	 * @return the tickframes
	 */
	public int getTickframes() {
		return tickframes;
	}

	/**
	 * @param tickframes the tickframes to set
	 */
	public void setTickframes(int tickframes) {
		this.tickframes = tickframes;
	}

	/**
	 * @return the dodge
	 */
	public int getDodge() {
		return dodge;
	}

	/**
	 * @param dodge the dodge to set
	 */
	public void setDodge(int dodge) {
		this.dodge = dodge;
	}

    
}


