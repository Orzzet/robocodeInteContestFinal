package alc.utils.targetting;

import java.awt.geom.Point2D;

import alc.utils.Util;
import robocode.Robot;
import robocode.ScannedRobotEvent;

public class PatternTargetting {

	private static double[][] pastMovements;
	private static int pastMovementsIndex;

	private static double minDifferenceValue;
	private static int predictionBeginIndex;

	private static double[][] recentMovements;

	/**
	 * precision -> número de movimientos a tener en cuenta en el patrón factors
	 * -> número de factores a tener en cuenta para el patrón
	 */
	public static Point2D.Double pattern(Robot myRobot, double bulletVelocity, int precision, ScannedRobotEvent e,
			double enemyX, double enemyY, double[][] enemyPastMovements, int enemyPastMovementsIndex, int factors) {

		pastMovements = enemyPastMovements;
		pastMovementsIndex = enemyPastMovementsIndex;

		minDifferenceValue = 1000000;
		predictionBeginIndex = 0;

		// Guarda los últimos movimientos que ha realizado el robot enemigo
		recentMovements = saveRecentMovements(precision, factors);

		// Compara los últimos movimientos con los realizados durante la partida
		// y devuelve el índice a partir del cual va a realizar los nuevos
		// movimientos
		predictionBeginIndex = fetchSimilarPatron(precision, factors);

		// Predice el punto en el que va a estar el enemigo teniendo en cuenta
		// la velocidad de la bala
		Point2D.Double predictedPoint = predictPoint(myRobot, e, bulletVelocity, enemyX, enemyY);

		return predictedPoint;
	}

	/**
	 * Se guardan los últimos "precision" movimientos que el tanque ha realizado
	 * 
	 * @param precision
	 * @return
	 */

	private static double[][] saveRecentMovements(int precision, int factors) {

		double[][] recentMovements = new double[precision][factors];

		for (int i = pastMovementsIndex - precision; i < pastMovementsIndex; i++) {

			// k -> [0,precision]
			int k = Util.normalizeArrayIndex(i, pastMovements.length);

			// Hay que volver a normalizar k al realizarle cualquier operación y
			// luego usarlo como índice.
			int k2 = Util.normalizeArrayIndex(k - (pastMovementsIndex - precision), pastMovements.length);

			for (int j = 0; j < factors; j++) {
				recentMovements[k2][j] = pastMovements[k][j];
			}
		}
		return recentMovements;
	}

	/**
	 * Se comparan los últimos "precision" movimientos con la suma de las
	 * diferencias de todas las secuencias de "precision" movimientos en los
	 * movimientos anteriores. Acabo de aprender a usar labels. Mola.
	 * 
	 * @param precision
	 * @return
	 */
	private static int fetchSimilarPatron(int precision, int factors) {

		int predictionBeginIndex = 0;

		// Recorre todas las posiciones de pastMovements (En paquetes de
		// [precision] movimientos
		outerloop: for (int i = 0; i < pastMovements.length; i++) {

			int iNorm = Util.normalizeArrayIndex(i, pastMovements.length);

			// Compara [precision] movimientos consecutivos siempre que iNorm no
			// apunte a los movimientos recientes.
			if (!(iNorm > pastMovementsIndex - precision && iNorm < pastMovementsIndex)) {
				double currentDifference = 0;
				for (int j = 0; j < precision; j++) {

					// Normaliza el índice resultado de la suma iNorm + j
					int ijNormalized = Util.normalizeArrayIndex(iNorm + j, pastMovements.length);

					// La diferencia entre el patrón reciente y el obtenido se calcula teniendo en cuenta todos los
					// [factors]
					for (int k = 0; k < factors; k++) {
						currentDifference += Math.abs(pastMovements[ijNormalized][k] - recentMovements[j][k]);
					}
				}

				if (currentDifference < minDifferenceValue) {
					minDifferenceValue = currentDifference;
					predictionBeginIndex = Util.normalizeArrayIndex(iNorm + precision, pastMovements.length);

					if (minDifferenceValue == 0) {
						break outerloop;
					}
				}
			}
		}
		return predictionBeginIndex;
	}

	/**
	 * Comprueba la posición en la que se encontrará el tanque enemigo si
	 * siguiera el patrón encontrado y se calcula si se le puede acertar con una
	 * bala de velocidad bulletVelocity, de forma similar a lo usado en
	 * circularTargeting.
	 * 
	 * @param myRobot
	 * @param e
	 * @param bulletVelocity
	 * @param enemyX
	 * @param enemyY
	 * @return
	 */
	private static Point2D.Double predictPoint(Robot myRobot, ScannedRobotEvent e, double bulletVelocity, double enemyX,
			double enemyY) {

		double x0 = myRobot.getX();
		double y0 = myRobot.getY();
		double x = enemyX;
		double y = enemyY;
		double newDistance = e.getDistance();
		double heading = e.getHeadingRadians();

		for (int i = 0; bulletVelocity * i < newDistance; i++) {
			int normalizedIndex = Util.normalizeArrayIndex(predictionBeginIndex, pastMovements.length);
			x += pastMovements[normalizedIndex][1] * Math.sin(heading);
			y += pastMovements[normalizedIndex][1] * Math.cos(heading);
			heading += pastMovements[normalizedIndex][0];
			newDistance = Point2D.distance(x0, y0, x, y);
			predictionBeginIndex++;
		}

		return new Point2D.Double(x, y);

	}

}