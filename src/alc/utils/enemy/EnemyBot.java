package alc.utils.enemy;

import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class EnemyBot {

	private double myRobotX;
	private double myRobotY;

	private ScannedRobotEvent scannedRobot = null;

	private double absoluteBearingRadians;
	private double energyChange;
	private double turnRate;

	private double x;
	private double y;

	public void setEverything(ScannedRobotEvent e, AdvancedRobot r) {

		if (scannedRobot != null) {
			turnRate = e.getHeadingRadians() - getHeadingRadians();
			energyChange = e.getEnergy() - getEnergy();
		}
		scannedRobot = e;

		myRobotX = r.getX();
		myRobotY = r.getY();

		setAbsoluteBearingRadians(r.getHeadingRadians(), e.getBearingRadians());
		setPosition();

	}

	public void setAbsoluteBearingRadians(double heading0, double bearing) {
		absoluteBearingRadians = Utils.normalAbsoluteAngle(bearing + heading0);
	}

	public void setPosition() {

		x = myRobotX + (getDistance() * Math.sin(absoluteBearingRadians));
		y = myRobotY + (getDistance() * Math.cos(absoluteBearingRadians));

	}

	public Point2D.Double getPosition() {
		return new Point2D.Double(getX(), getY());
	}

	public Point2D.Double getNextPosition() {
		double nextX = x + getVelocity() * Math.sin(getHeadingRadians() + turnRate);
		double nextY = y + getVelocity() * Math.cos(getHeadingRadians() + turnRate);

		return new Point2D.Double(nextX, nextY);
	}

	public double getBearingRadians() {
		return scannedRobot.getBearingRadians();
	}

	public double getEnergyChange() {
		return energyChange;
	}

	public double getDistance() {
		return scannedRobot.getDistance();
	}

	public double getHeadingRadians() {
		return scannedRobot.getHeadingRadians();
	}

	public double getVelocity() {
		return scannedRobot.getVelocity();
	}

	public double getTurnRate() {
		return turnRate;
	}

	public double getEnergy() {
		return scannedRobot.getEnergy();
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public ScannedRobotEvent getScannedRobot() {
		return scannedRobot;
	}
	
	public double getAbsoluteBearingRadians(){
		return absoluteBearingRadians;
	}
}
