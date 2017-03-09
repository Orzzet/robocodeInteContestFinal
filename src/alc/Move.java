package alc;

import alc.utils.MyRobot;
import alc.utils.enemy.EnemyBot;
import robocode.HitByBulletEvent;
import robocode.HitWallEvent;

public class Move {

	private static double orientation = 1;
	private static long ticksLastReceivedHit = 0;
	private static long ticksOrientationChanged;

	public static void main(MyRobot r, EnemyBot enemy) {

		orbit(r, enemy, 250);

	}

	public static void hitByBullet(HitByBulletEvent e) {
		
		ticksLastReceivedHit = e.getTime();

	}

	public static void orbit(MyRobot r, EnemyBot enemy, double distance) {
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

	public static void hitByWall(HitWallEvent event) {
		if (event.getTime() - ticksOrientationChanged > 5) {
			orientation = -orientation;
			ticksOrientationChanged = event.getTime();
		}

	}

}
