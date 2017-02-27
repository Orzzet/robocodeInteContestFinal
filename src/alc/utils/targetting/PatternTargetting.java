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
	 * precision -> número de movimientos a tener en cuenta en el patrón
	 */
	public static Point2D.Double patternTargeting(Robot myRobot, double bulletVelocity, int precision,
			ScannedRobotEvent e, double enemyX, double enemyY, double[][] enemyPastMovements,
			int enemyPastMovementsIndex) {

		pastMovements = enemyPastMovements;
		pastMovementsIndex = enemyPastMovementsIndex;

		minDifferenceValue = 1000000;
		predictionBeginIndex = 0;

		recentMovements = saveRecentMovements(precision);

		predictionBeginIndex = fetchSimilarPatron(precision);

		Point2D.Double predictedPoint = predictPoint(myRobot, e, bulletVelocity, enemyX, enemyY);

		return predictedPoint;
	}

	/**
	 * Se comparan los últimos "precision" movimientos con la suma de las
	 * diferencias de todas las secuencias de "precision" movimientos en los
	 * movimientos anteriores. Acabo de aprender a usar labels. Mola.
	 * 
	 * @param precision
	 * @return
	 */
	private static int fetchSimilarPatron(int precision) {

		int predictionBeginIndex = 0;

		outerloop: for (int i = 0; i < pastMovements.length; i++) {

			int k = Util.normalizeArrayIndex(i, pastMovements.length);

			// Compara "precision" movimientos consecutivos si k no apunta al
			// movimiento reciente.
			if (!(k > pastMovementsIndex - precision && k < pastMovementsIndex)) {
				double currentDifference = 0;

				for (int j = 0; j < precision; j++) {
					int kjNormalized = Util.normalizeArrayIndex(k + j, pastMovements.length);
					currentDifference += Math.abs(pastMovements[kjNormalized][0] - recentMovements[j][0])
							+ Math.abs(pastMovements[kjNormalized][1] - recentMovements[j][1]);
				}

				if (currentDifference < minDifferenceValue) {
					minDifferenceValue = currentDifference;
					// Guarda el índice con la primera posición que se usará
					// para preveer el movimiento. También hay que normalizarlo
					// para evitar OOB exception
					predictionBeginIndex = Util.normalizeArrayIndex(k + precision, pastMovements.length);

					if (minDifferenceValue == 0) {
						break outerloop;
					}
				}
			}
		}
		return predictionBeginIndex;
	}

	/**
	 * Se guardan los últimos "precision" movimientos que el tanque ha realizado
	 * 
	 * @param precision
	 * @return
	 */

	private static double[][] saveRecentMovements(int precision) {

		double[][] recentMovements = new double[precision][2];

		for (int i = pastMovementsIndex - precision; i < pastMovementsIndex; i++) {

			// k -> [0,precision]
			int k = Util.normalizeArrayIndex(i, pastMovements.length);

			// Hay que volver a normalizar k al realizarle cualquier operación y
			// luego usarlo como índice.
			int k2 = Util.normalizeArrayIndex(k - (pastMovementsIndex - precision), pastMovements.length);

			recentMovements[k2][0] = pastMovements[k][0];
			recentMovements[k2][1] = pastMovements[k][1];
		}
		return recentMovements;
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