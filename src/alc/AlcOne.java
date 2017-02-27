package alc;

import alc.utils.MyRobot;
import alc.utils.Util;
import alc.utils.enemy.PatternBot;
import robocode.ScannedRobotEvent;

public class AlcOne extends MyRobot {

	PatternBot enemy = new PatternBot();
	long lastEnemySeen = 0;
	
	
	public void run() {

		this.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);

		// Si pierde al enemigo de vista
		if ((getTime() - lastEnemySeen) >= 5) {
			this.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
		}

		while (true) {
			scan();
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {

		enemy.setEverything(e, this);

		lastEnemySeen = this.getTime();
		
		this.aimRadarRadians(Util.getAbsoluteBearingToPointRadians(enemy.getX(), enemy.getY(), this.getX(), this.getY()));
		// this.aimRadarDegrees(Util.getAbsoluteBearingToPointDegree(enemy.getX(), enemy.getY(), this.getX(), this.getY()));

	}

}