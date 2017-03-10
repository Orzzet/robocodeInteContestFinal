package alc;

import java.awt.Color;

import alc.utils.MyGame;
import alc.utils.MyRobot;
import alc.utils.Util;
import alc.utils.enemy.EnemyBot;
import robocode.HitWallEvent;
import robocode.util.Utils;

public class Move {

	private static double orientation = 1;
	private static long ticksLastWallHit;
	private static Movement movementType = Movement.ORBIT2;
	private static int recentWallHits = 0;

	public static void main(AlcOne r, EnemyBot enemy) {

		if (r.getTime() - ticksLastWallHit > 30) {
			recentWallHits = 0;
		}

		if (recentWallHits >= 4) {
			System.out.println(recentWallHits);
			movementType = Movement.TO_MIDDLE;
		}

		switch (movementType) {
		case ORBIT1:
			orbit1(r, enemy, 250);
			break;
		case ORBIT2:
			orbit2(r, enemy, 250);
			break;
		case TO_MIDDLE:
			toMiddle(r, r.getGame());
			break;
		}
	}

	private static void toMiddle(MyRobot r, MyGame game) {
		r.setBodyColor(Color.BLACK);
		goToPoint(r, r.getBattleFieldWidth() / 2, r.getBattleFieldHeight() / 2);
		if (game.getInnerBattlefield().contains(r.getX(), r.getY())) {
			movementType = Movement.ORBIT2;
		}
	}

	private static void goToPoint(MyRobot r, double x, double y) {
		r.setTurnRightRadians(Utils.normalRelativeAngle(Util.getAbsoluteBearingToPointRadians(x, y, r.getX(), r.getY())
				- r.getHeadingRadians()));
		if (r.getTurnRemainingRadians() == 0) {
			r.setAhead(100);
		}
	}

	private static void orbit1(MyRobot r, EnemyBot enemy, double distance) {
		r.setBodyColor(Color.CYAN);
		int enemyFar = 1;
		if (r.getDistanceRemaining() == 0) {
			orientation = -orientation;
			r.setAhead(250 * orientation);
		}

		if (enemy.getDistance() <= distance) {
			enemyFar = -1;
		}
		r.setTurnRightRadians(enemy.getBearingRadians() + Math.PI / 2 - 0.52 * orientation * enemyFar);
	}

	private static void orbit2(MyRobot r, EnemyBot enemy, double distance) {

		r.setBodyColor(Color.ORANGE);
		long time = r.getTime();
		int enemyFar = 1;
		if (r.getDistanceRemaining() == 0) {
			orientation = -orientation;
			r.setAhead(250 * orientation * Math.abs(Math.sin(time / 17 * Math.cos(time / 13))));
		}

		if (enemy.getDistance() <= distance) {
			enemyFar = -1;
		}
		r.setTurnRightRadians(enemy.getBearingRadians() + Math.PI / 2 - 0.6 * orientation * enemyFar);

	}

	public static void hitByWall(HitWallEvent event) {

		if (event.getTime() - ticksLastWallHit > 5) {
			recentWallHits++;
			orientation = -orientation;
			ticksLastWallHit = event.getTime();
		}
	}

	public static void startRound() {
		ticksLastWallHit = 0;
		recentWallHits = 0;
	}
}
