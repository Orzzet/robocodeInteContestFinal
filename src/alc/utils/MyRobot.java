package alc.utils;

import robocode.AdvancedRobot;
import robocode.util.Utils;

public class MyRobot extends AdvancedRobot {
	
	
	
	protected void aimGunDegrees(double absoluteBearingDegrees) {
		// Apunta el cañón hacia el ángulo señalado en grados
		this.setTurnGunRight(Utils.normalRelativeAngleDegrees(absoluteBearingDegrees - this.getGunHeading()));
	}

	protected void aimGunRadians(double absoluteBearingRadians){
		this.setTurnGunRightRadians(Utils.normalRelativeAngle(absoluteBearingRadians - this.getGunHeadingRadians()));
	}
	
	protected void aimRadarDegrees(double absoluteBearingDegrees) {
		// Apunta el radar al ángulo señalado
		this.setTurnRadarRight(Utils.normalRelativeAngleDegrees(absoluteBearingDegrees - this.getRadarHeading()));
	}
	
	protected void aimRadarRadians(double absoluteBearingRadians) {
		// Apunta el radar al ángulo señalado
		this.setTurnRadarRightRadians(Utils.normalRelativeAngle(absoluteBearingRadians - this.getRadarHeadingRadians()));
	}
	
}
