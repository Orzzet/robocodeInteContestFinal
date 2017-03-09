package alc.utils;

import java.awt.geom.Rectangle2D;

import robocode.AdvancedRobot;
import robocode.RoundEndedEvent;
import robocode.SkippedTurnEvent;
import robocode.WinEvent;
import robocode.util.Utils;

public class MyRobot extends AdvancedRobot {
	
	protected int hasShoot = 0;
	protected int enemyReceivedShot = 0;
	protected static int victories = 0;
	protected static int defeats = 0;
	
	protected void firstRun(MyGame game) {
		game.setBattlefield(new Rectangle2D.Double(18, 18, getBattleFieldWidth() - 36, getBattleFieldHeight() - 36));
		game.setInnerBattlefield(new Rectangle2D.Double(getBattleFieldWidth() * 1 / 10, getBattleFieldHeight() * 1 / 10,
				getBattleFieldWidth() * 1 / 10, getBattleFieldHeight() * 1 / 10));

		this.setAdjustRadarForGunTurn(true);
		this.setAdjustGunForRobotTurn(true);
		this.setAdjustRadarForRobotTurn(true);
		this.setTurnRadarRight(Double.POSITIVE_INFINITY);
	}

	public void aimGunDegrees(double absoluteBearingDegrees) {
		// Apunta el cañón hacia el ángulo señalado en grados
		this.setTurnGunRight(Utils.normalRelativeAngleDegrees(absoluteBearingDegrees - this.getGunHeading()));
	}

	public void aimGunRadians(double absoluteBearingRadians) {
		this.setTurnGunRightRadians(Utils.normalRelativeAngle(absoluteBearingRadians - this.getGunHeadingRadians()));
	}

	public void aimRadarDegrees(double absoluteBearingDegrees) {
		// Apunta el radar al ángulo señalado
		this.setTurnRadarRight(Utils.normalRelativeAngleDegrees(absoluteBearingDegrees - this.getRadarHeading()));
	}

	public void aimRadarRadians(double absoluteBearingRadians) {
		// Apunta el radar al ángulo señalado
		this.setTurnRadarRightRadians(
				Utils.normalRelativeAngle(absoluteBearingRadians - this.getRadarHeadingRadians()));
	}
	
	@Override
	public void onSkippedTurn(SkippedTurnEvent event) {
		System.out.println("Se ha saltado el turno: " + event.getTime());
	}
	
	
	
	@Override
	public void onRoundEnded(RoundEndedEvent event) {
		defeats++;
	}

	@Override
	public void onWin(WinEvent event) {
		defeats--;
		victories++;
	}

	public int getVictories(){
		return victories;
	}
	
	public int getDefeats(){
		return defeats;
	}
	
}
