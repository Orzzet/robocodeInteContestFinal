package alc.utils.targetting;

import java.awt.Color;
import java.awt.geom.Point2D;

import alc.utils.Util;
import robocode.Robot;
import robocode.ScannedRobotEvent;

public class PatternTargetting {

	private static double[][] pastMovements;
	private static int pastMovementsIndex;
	private static int topMovementIndex;

	private static double minDifferenceValue;
	private static int predictionBeginIndex;

	private static double[][] recentMovements;

	/**
	 * precision -> n�mero de movimientos a tener en cuenta en el patr�n factors
	 * -> n�mero de factores a tener en cuenta para el patr�n
	 */
	public static Point2D.Double pattern(Robot myRobot, double bulletVelocity, int precision, ScannedRobotEvent e,
			double enemyX, double enemyY, double[][] enemyPastMovements, int enemyPastMovementsIndex,
			int enemyTopMovementIndex, int factors) {

		if(pastMovementsIndex > 495){
			assert true;
		}
		
		pastMovements = enemyPastMovements;
		pastMovementsIndex = enemyPastMovementsIndex;
		topMovementIndex = enemyTopMovementIndex;

		minDifferenceValue = 1000000;
		predictionBeginIndex = 0;

		// Guarda los �ltimos movimientos que ha realizado el robot enemigo
		recentMovements = saveRecentMovements(precision, factors);

		// Compara los �ltimos movimientos con los realizados durante la partida
		// y devuelve el �ndice a partir del cual va a realizar los nuevos
		// movimientos
		predictionBeginIndex = fetchSimilarPatron(precision, factors);

		if (minDifferenceValue == 0) {
			myRobot.setBulletColor(Color.GREEN);
		} else {
			myRobot.setBulletColor(Color.YELLOW);
		}
		
		// Predice el punto en el que va a estar el enemigo teniendo en cuenta
		// la velocidad de la bala
		Point2D.Double predictedPoint = predictPoint(myRobot, e, bulletVelocity, enemyX, enemyY);

		return predictedPoint;
	}

	/**
	 * Se guardan los �ltimos "precision" movimientos que el tanque ha realizado
	 * 
	 * @param precision
	 * @return
	 */

	private static double[][] saveRecentMovements(int precision, int factors) {

		double[][] recentMovements = new double[precision][factors];

		for (int i = pastMovementsIndex - precision; i < pastMovementsIndex; i++) {

			// k -> [0,precision]
			int k = Util.normalizeArrayIndex(i, pastMovements.length);

			// Hay que volver a normalizar k al realizarle cualquier operaci�n y
			// luego usarlo como �ndice.
			int k2 = Util.normalizeArrayIndex(k - (pastMovementsIndex - precision), pastMovements.length);

			for (int j = 0; j < factors; j++) {
				recentMovements[k2][j] = pastMovements[k][j];
			}
		}
		return recentMovements;
	}

	/**
	 * Se comparan los �ltimos "precision" movimientos con la suma de las
	 * diferencias de todas las secuencias de "precision" movimientos en los
	 * movimientos anteriores. Acabo de aprender a usar labels. Mola.
	 * 
	 * @param precision
	 * @return
	 */
	private static int fetchSimilarPatron(int precision, int factors) {

		// Variable donde se guarda el �ndice de la primera posici�n predicha del enemigo.
		int predictionBeginIndex = 0;

		// Recorre todas las posiciones de pastMovements (En paquetes de
		// [precision] movimientos)
		outerloop: for (int i = 0; i < pastMovements.length; i++) {

			// Compara [precision] movimientos consecutivos siempre que i no
			// apunte a los movimientos recientes.
			if (!(i + precision >= (pastMovementsIndex - precision) && i < pastMovementsIndex)) {
				double currentDifference = 0;
				for (int j = 0; j < precision; j++) {

					// Normaliza el �ndice resultado de la suma i + j
					int ijNormalized = Util.normalizeArrayIndex(i + j, pastMovements.length);

					if (ijNormalized > topMovementIndex) {
						break outerloop;
					}

					// La diferencia entre el patr�n reciente y el obtenido se
					// calcula teniendo en cuenta todos los [factors]
					for (int k = 1; k < factors; k++) {
						currentDifference += Math.abs(pastMovements[ijNormalized][k] - recentMovements[j][k]);
					}
				}

				if (currentDifference < minDifferenceValue) {
					minDifferenceValue = currentDifference;
					predictionBeginIndex = Util.normalizeArrayIndex(i + precision, pastMovements.length);

					if (minDifferenceValue == 0) {
						break outerloop;
					}
				}
			}
		}
		return predictionBeginIndex;
	}

	/**
	 * Comprueba la posici�n en la que se encontrar� el tanque enemigo si
	 * siguiera el patr�n encontrado y se calcula si se le puede acertar con una
	 * bala de velocidad bulletVelocity, de forma similar a lo usado en
	 * circularTargeting.
	 * 
	 * @param myRobot
	 * @param e
	 * @param bulletVelocity
	 * @param enemyX
	 * @param enemyY
	 * @return Punto donde se va a encontrar el robot enemigo teniendo en cuenta que nuestra bala puede llegarle
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
			heading += pastMovements[normalizedIndex][0];
			x += pastMovements[normalizedIndex][1] * Math.sin(heading);
			y += pastMovements[normalizedIndex][1] * Math.cos(heading);
			newDistance = Point2D.distance(x0, y0, x, y);
			predictionBeginIndex++;
		}

		return new Point2D.Double(x, y);

	}

}