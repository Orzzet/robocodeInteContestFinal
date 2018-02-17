package alc;

import java.awt.Color;
import java.awt.geom.Point2D;

import alc.utils.MyRobot;
import alc.utils.Util;
import alc.utils.enemy.PatternBot;
import alc.utils.targetting.PatternTargetting;
import alc.utils.targetting.SimpleTargetting;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.Rules;

public class Gun {

	private static Weapon weaponType = Weapon.CIRCULAR;
	private static double enemyClose = 0;
	private static int bulletsMissed = 0;
	private static long fireTime = 0;

	public static void main(MyRobot r, PatternBot enemy) {

		if (enemy.getDistance() <= 100) {
			enemyClose = 1;

			if (enemy.getDistance() <= 50) {
				weaponType = Weapon.HEAD;
			} else {
				weaponType = Weapon.CIRCULAR;
			}

		} else {
			enemyClose = 0;
		}

		double bulletFirepower = 2 + enemyClose;

		// Si se puede matar al enemigo con menos energía, mejor.
		if (enemy.getEnergy() < (4 * bulletFirepower + 2 * (bulletFirepower - 1))) {
			bulletFirepower = (enemy.getEnergy() + 2) / 6 + 0.1;
		}

		// Si queda poca energía, se usa menos para los disparos.
		if (r.getEnergy() <= 3) {
			bulletFirepower = r.getEnergy() / 4;
		}

		double bulletVelocity = Rules.getBulletSpeed(bulletFirepower);

		// En el motor del juego las balas se disparan antes de mover el
		// cañón, por lo que tenemos que apuntar un turno y disparar el
		// siguiente
		if (fireTime == r.getTime() && r.getGunTurnRemaining() == 0 && r.getGunHeat() == 0) {
			r.setFire(bulletFirepower);
		}

		switch (weaponType) {
		case CIRCULAR:
			circular(r, enemy, bulletVelocity);
			break;
		case HEAD:
			head(r, enemy);
			break;
		case PATTERN:
			pattern(r, enemy, bulletVelocity, 10);
			break;
		}

		// Listo para disparar en el tick siguiente
		fireTime = r.getTime() + 1;

	}

	// Dispara a la posición actual del robot. Solo valido para cuando el robot esta muy cerca.
	private static void head(MyRobot r, PatternBot enemy) {
		r.setGunColor(Color.PINK);
		r.setBulletColor(Color.PINK);
		Point2D.Double predictedPos = SimpleTargetting.head(enemy.getX(), enemy.getY());
		r.aimGunRadians(Util.getAbsoluteBearingToPointRadians(predictedPos, r.getX(), r.getY()));
		if (bulletsMissed >= 4 || enemy.getHasEnoughData()) {
			weaponType = Weapon.PATTERN;
			bulletsMissed = 0;
		}

	}

	// Dispara a una posicion futura del robot. Supone que mantiene su velocidad angular.
	private static void circular(MyRobot r, PatternBot enemy, double bulletVelocity) {
		r.setGunColor(Color.RED);
		r.setBulletColor(Color.RED);
		
		Point2D.Double predictedPos = SimpleTargetting.circular(bulletVelocity, r.getX(), r.getY(),
				enemy.getScannedRobot(), enemy.getX(), enemy.getY(), enemy.getTurnRate());
		
		r.aimGunRadians(Util.getAbsoluteBearingToPointRadians(predictedPos, r.getX(), r.getY()));

		if (bulletsMissed >= 3 && enemy.getHasEnoughData()) {
			weaponType = Weapon.PATTERN;
			bulletsMissed = 0;
		}

	}
	
	// Dispara a una posición futura del robot. Se basa en datos recolectados durante la partida.
	private static void pattern(MyRobot r, PatternBot enemy, double bulletVelocity, int precision) {
		r.setGunColor(Color.GREEN);
		r.setBulletColor(Color.GREEN);
		
		Point2D.Double predictedPos = PatternTargetting.pattern(r, bulletVelocity, precision, enemy.getScannedRobot(),
				enemy.getX(), enemy.getY(), enemy.getPastMovements(), enemy.getPastMovementsIndex(),
				enemy.getTopMovementIndex(), 3);
		
		r.aimGunRadians(Util.getAbsoluteBearingToPointRadians(predictedPos, r.getX(), r.getY()));
		
	}

	public static void onBulletMissed(BulletMissedEvent event){
		bulletsMissed += 1;
	}
	
	public static void onBulletHit(BulletHitEvent event) {
		bulletsMissed = 0;
	}

	public static void onBulletHitBullet(BulletHitBulletEvent event) {
		bulletsMissed -= 1;
	}
}