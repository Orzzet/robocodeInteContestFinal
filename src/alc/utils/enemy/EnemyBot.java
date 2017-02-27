package alc.utils.enemy;

import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class EnemyBot {

	private double myRobotX;
	private double myRobotY;
	
	private double distance;
	private double velocity = 0;
	private double energy = 0;
	double time;
	private double headingRadians;
	private double bearingRadians;

	private double absoluteBearingRadians;
	private double energyChange;
	private double turnRate;

	private double x;
	private double y;

	public void setEverything(ScannedRobotEvent e, AdvancedRobot r) {

		// Tengo que cambiar todo a radianes, me voy a ahorrar muchas cosas
		// /(e.getTime() - time) para la media
		turnRate = e.getHeadingRadians() - headingRadians;
		energyChange = e.getEnergy() - energy;

		myRobotX = r.getX();
		myRobotY = r.getY();
		
		bearingRadians = e.getBearingRadians();
		headingRadians = e.getHeadingRadians();
		distance = e.getDistance();
		energy = e.getEnergy();
		velocity = e.getVelocity();
		time = e.getTime();
		setAbsoluteBearingRadians(r.getHeadingRadians(), e.getBearingRadians());
		setPosition();

	}

	public void setAbsoluteBearingRadians(double heading0, double bearing) {
		absoluteBearingRadians = Utils.normalAbsoluteAngle(bearing + heading0);
	}

	public void setPosition() {

		x = myRobotX + (distance * Math.sin(absoluteBearingRadians));
		y = myRobotY + (distance * Math.cos(absoluteBearingRadians));
		
	}

	public Point2D.Double getPosition(){
		return new Point2D.Double(getX(), getY());
	}
	
	public Point2D.Double getNextPosition() {
		double nextX = x + velocity * Math.sin(headingRadians + turnRate);
		double nextY = y + velocity * Math.cos(headingRadians + turnRate);

		return new Point2D.Double(nextX, nextY);
	}
	
	public double getBearingRadians() {
		return bearingRadians;
	}

	public double getEnergyChange() {
		return energyChange;
	}

	public double getDistance() {
		return distance;
	}

	public double getHeadingRadians() {
		return headingRadians;
	}

	public double getVelocity() {
		return velocity;
	}

	public double getTurnRate() {
		return turnRate;
	}

	public double getEnergy() {
		return energy;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
}
