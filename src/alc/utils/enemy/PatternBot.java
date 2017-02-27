package alc.utils.enemy;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class PatternBot extends EnemyBot {

	// Se guarda el turnRate y la velocidad de los últimos 500 ticks del robot,
	// history index es un contador que señala el índice en el que hay que
	// añadir el valor nuevo.
	private static double[][] pastMovements = new double[1000][2];
	private static int pastMovementsIndex = 0;
	private static boolean hasEnoughData = false;

	@Override
	public void setEverything(ScannedRobotEvent e, AdvancedRobot r) {
		super.setEverything(e, r);

		pastMovements[pastMovementsIndex][0] = getTurnRate();
		pastMovements[pastMovementsIndex++][1] = getVelocity();
		if (pastMovementsIndex == 100) {
			hasEnoughData = true;
		}
		if (pastMovementsIndex == 499) {
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

}
