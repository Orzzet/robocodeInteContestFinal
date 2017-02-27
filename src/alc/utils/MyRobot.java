package alc.utils;

import robocode.AdvancedRobot;
import robocode.util.Utils;

public class MyRobot extends AdvancedRobot {
	
	
	
	protected void aimGunDegrees(double absoluteBearingDegrees) {
		// Apunta el ca��n hacia el �ngulo se�alado en grados
		this.setTurnGunRight(Utils.normalRelativeAngleDegrees(absoluteBearingDegrees - this.getGunHeading()));
	}

	protected void aimGunRadians(double absoluteBearingRadians){
		this.setTurnGunRightRadians(Utils.normalRelativeAngle(absoluteBearingRadians - this.getGunHeadingRadians()));
	}
	
	protected void aimRadarDegrees(double absoluteBearingDegrees) {
		// Apunta el radar al �ngulo se�alado
		this.setTurnRadarRight(Utils.normalRelativeAngleDegrees(absoluteBearingDegrees - this.getRadarHeading()));
	}
	
	protected void aimRadarRadians(double absoluteBearingRadians) {
		// Apunta el radar al �ngulo se�alado
		this.setTurnRadarRightRadians(Utils.normalRelativeAngle(absoluteBearingRadians - this.getRadarHeadingRadians()));
	}
	
}
