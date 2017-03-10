package alc.utils.enemy;

import alc.utils.MyRobot;
import robocode.ScannedRobotEvent;

public class PatternBot extends EnemyBot {

	// Se guarda el turnRate, la velocidad y la velocidad perpendicular de los
	// últimos 5000 ticks del robot en pastMovements,
	// pastMovementsIndex es un contador que señala el índice en el que hay que
	// añadir el valor nuevo.
	private static double[][] pastMovements = new double[5000][3];
	private static int pastMovementsIndex = 0;
	// Mayor índice de pastMovements con información
	private static int topMovementIndex = -1;
	private static boolean hasEnoughData = false;

	public void setEverything(ScannedRobotEvent e, MyRobot r) {
		super.setEverything(e, r);

		pastMovements[pastMovementsIndex][0] = getTurnRate();
		pastMovements[pastMovementsIndex][1] = getVelocity();
		// Velocidad lateral (respecto a tu robot)
		pastMovements[pastMovementsIndex++][2] = e.getVelocity()
				* Math.sin(e.getHeadingRadians() - getAbsoluteBearingRadians());

		if (topMovementIndex < pastMovements.length) {
			topMovementIndex++;
		}

		if (pastMovementsIndex == 100) {
			hasEnoughData = true;
		}
		if (pastMovementsIndex == 5000) {
			pastMovementsIndex = 0;
		}

	}

	public int getPastMovementsIndex() {
		return pastMovementsIndex;
	}

	public double[][] getPastMovements() {
		return pastMovements;
	}

	public boolean getHasEnoughData() {
		return hasEnoughData;
	}

	public int getTopMovementIndex() {
		return topMovementIndex;
	}

}
