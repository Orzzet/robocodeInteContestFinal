package alc.utils;

import java.awt.geom.Rectangle2D;

import robocode.AdvancedRobot;
import robocode.util.Utils;

public class MyRobot extends AdvancedRobot {

	protected void firstRun(MyGame game) {
		game.setBattlefield(new Rectangle2D.Double(18, 18, getBattleFieldWidth() - 36, getBattleFieldHeight() - 36));
		game.setInnerBattlefield(new Rectangle2D.Double(getBattleFieldWidth() / 3, getBattleFieldHeight() / 3,
				getBattleFieldWidth() * 2 / 3, getBattleFieldHeight() * 2 / 3));

		this.setAdjustRadarForGunTurn(true);
		this.setAdjustGunForRobotTurn(true);
		this.setAdjustRadarForRobotTurn(true);
		this.setTurnRadarRight(Double.POSITIVE_INFINITY);
	}

	public void aimGunDegrees(double absoluteBearingDegrees) {
		// Apunta el ca��n hacia el �ngulo se�alado en grados
		this.setTurnGunRight(Utils.normalRelativeAngleDegrees(absoluteBearingDegrees - this.getGunHeading()));
	}

	public void aimGunRadians(double absoluteBearingRadians) {
		this.setTurnGunRightRadians(Utils.normalRelativeAngle(absoluteBearingRadians - this.getGunHeadingRadians()));
	}

	public void aimRadarDegrees(double absoluteBearingDegrees) {
		// Apunta el radar al �ngulo se�alado
		this.setTurnRadarRight(Utils.normalRelativeAngleDegrees(absoluteBearingDegrees - this.getRadarHeading()));
	}

	public void aimRadarRadians(double absoluteBearingRadians) {
		// Apunta el radar al �ngulo se�alado
		this.setTurnRadarRightRadians(
				Utils.normalRelativeAngle(absoluteBearingRadians - this.getRadarHeadingRadians()));
	}

}
