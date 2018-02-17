package alc.utils.targetting;

import java.awt.geom.Point2D;

import robocode.ScannedRobotEvent;

public class SimpleTargetting {
	/**
	 * 
	 * @param bulletVelocity
	 * @param myRobotX
	 * @param myRobotY
	 * @param e
	 * @param enemyInitialPosition
	 *            - Posición inicial del enemigo
	 * @param enemyTurnRate
	 *            - La diferencia de heading entre este tick y el anterior del
	 *            robot enemigo
	 * @return - Point2D.Double con la posición predicha del enemigo
	 */
	public static Point2D.Double circular(double bulletVelocity, double myRobotX, double myRobotY, ScannedRobotEvent e,
			double enemyX, double enemyY, double enemyTurnRate) {

		double headingRadians = e.getHeadingRadians();
		double newDistance = e.getDistance();

		double x = enemyX;
		double y = enemyY;

		double velocity = e.getVelocity();

		// Con esto calculo la posición en la que se tiene que encontrar
		// el enemigo para acertarle si le disparo una bala con una velocidad
		// determinada y el enemigo sigue con velocidad y giro constantes.

		for (int i = 0; bulletVelocity * i < newDistance; i++) {

			x = x + velocity * Math.sin(headingRadians + enemyTurnRate * i);
			y = y + velocity * Math.cos(headingRadians + enemyTurnRate * i);

			newDistance = Point2D.distance(myRobotX, myRobotY, x, y);
		}

		return new Point2D.Double(x, y);

	}
	
	
	public static Point2D.Double head(double enemyX, double enemyY){
		
		return new Point2D.Double(enemyX, enemyY);
		
	}

}
