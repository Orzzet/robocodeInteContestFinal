package alc;

import alc.utils.MyRobot;
import alc.utils.Util;
import alc.utils.enemy.PatternBot;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;

public class AlcOne extends MyRobot {

	private Game game = new Game();
	PatternBot enemy = new PatternBot();
	long lastEnemySeen = 0;
	double FORWARD = 1;

	public void run() {

		firstRun(game);

		Move.startRound();

		this.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);

		// Siempre scanea el robot enemigo, no se puede escapar con esto.
		while (true) {
			// Si pierde al enemigo de vista
			if ((getTime() - lastEnemySeen) >= 5) {
				this.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
			}
			scan();
		}
	}

	// Cada vez que ha escaneado con exito al robot enemigo (todos los turnos en 1v1), realiza todas las acciones.
	public void onScannedRobot(ScannedRobotEvent e) {

		enemy.setEverything(e, this);

		lastEnemySeen = this.getTime();

		this.aimRadarRadians(
				Util.getAbsoluteBearingToPointRadians(enemy.getX(), enemy.getY(), this.getX(), this.getY()));

		Gun.main(this, enemy);
		Move.main(this, enemy);

	}

	@Override
	public void onHitWall(HitWallEvent event) {
		Move.hitByWall(event);
	}

	@Override
	public void onBulletHit(BulletHitEvent event) {
		Gun.onBulletHit(event);
	}

	@Override
	public void onBulletHitBullet(BulletHitBulletEvent event) {
		Gun.onBulletHitBullet(event);
	}

	@Override
	public void onBulletMissed(BulletMissedEvent event) {
		Gun.onBulletMissed(event);
	}

	public Game getGame() {
		return game;
	}

}