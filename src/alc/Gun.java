package alc;

import java.awt.geom.Point2D;

import alc.utils.MyRobot;
import alc.utils.Util;
import alc.utils.enemy.EnemyBot;
import alc.utils.enemy.PatternBot;
import alc.utils.targetting.PatternTargetting;
import alc.utils.targetting.SimpleTargetting;
import robocode.Rules;

public class Gun {

	private static Weapon weaponType = Weapon.PATTERN;
	private static double enemyClose = 0;
	private static int bulletsMissed = 0;
	private static long fireTime = 0;

	public static void main(MyRobot r, PatternBot enemy) {
		if (enemy.getDistance() <= 150) {
			enemyClose = 1;
		} else {
			enemyClose = 0;
		}

		double bulletFirepower = 2 + enemyClose;

		// Si se puede matar al enemigo con menos energ�a, mejor.
		if (enemy.getEnergy() < (4 * bulletFirepower + 2 * (bulletFirepower - 1))) {
			bulletFirepower = (enemy.getEnergy() + 2) / 6 + 0.1;
		}

		double bulletVelocity = Rules.getBulletSpeed(bulletFirepower);

		// En el motor del juego las balas se disparan un paso antes de mover el
		// ca��n, por lo que tenemos que apuntar un turno y disparar el
		// siguiente
		if (fireTime == r.getTime() && r.getGunTurnRemaining() == 0 && r.getGunHeat() == 0) {
			r.setFire(bulletFirepower);
			bulletsMissed++;
		}

		switch (weaponType) {
		case CIRCULAR:
			circular(r, enemy, bulletVelocity);
			break;
		case PATTERN:
			pattern(r, enemy, bulletVelocity, 10);
			break;
		}

		fireTime = r.getTime() + 1;

	}

	private static void circular(MyRobot r, EnemyBot enemy, double bulletVelocity) {
		Point2D.Double predictedPos = SimpleTargetting.circular(bulletVelocity, r.getX(), r.getY(),
				enemy.getScannedRobot(), enemy.getX(), enemy.getY(), enemy.getTurnRate());
		r.aimGunRadians(Util.getAbsoluteBearingToPointRadians(predictedPos, r.getX(), r.getY()));
		/*
		 * if(bulletsMissed > 3 && enemy.getHasEnoughData()){ weaponType =
		 * Weapon.PATTERN; }
		 */
	}

	private static void pattern(MyRobot r, PatternBot enemy, double bulletVelocity, int precision) {
		Point2D.Double predictedPos = PatternTargetting.pattern(r, bulletVelocity, precision, enemy.getScannedRobot(),
				enemy.getX(), enemy.getY(), enemy.getPastMovements(), enemy.getPastMovementsIndex(), 2);
		r.aimGunRadians(Util.getAbsoluteBearingToPointRadians(predictedPos, r.getX(), r.getY()));
		/*
		if (bulletsMissed > 4) {
			weaponType = Weapon.CIRCULAR;
		}
		*/
	}
	
	public void resetBulletsMissed(){
		bulletsMissed = 0;
	}
}