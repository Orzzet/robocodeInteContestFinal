package alc.utils;

import java.awt.geom.Point2D;

public class Util {
	/**
	 * Se le pasa un punto y el objeto de tu robot -> Devuelve el ángulo
	 * absoluto en radianes de ese punto.
	 * 
	 * @param targetX
	 * @param targetY
	 * @param myRobot
	 * @return
	 */
	public static double getAbsoluteBearingToPointRadians(double targetX, double targetY, double myRobotX, double myRobotY) {
		/*
		 * Para pasar de radianes cartesianos estándar a radiantes del juego (0
		 * en norte), uso un método raro donde cojo el ángulo en negativo, le
		 * resto PI/2 y le cambio el signo. Seguro que hay otro método (cambiar
		 * el punto que se le pasa a atan2, p.e) pero este es el que me ha
		 * funcionado hasta ahora.
		 */

		double gameAngle;
		double standardAngle = Math.atan2(targetY - myRobotY, targetX - myRobotX);

		if (standardAngle > 0) {
			standardAngle = standardAngle - 2 * Math.PI;
		}

		gameAngle = -1 * (standardAngle - Math.PI/2);

		if (gameAngle > 2 * Math.PI) {
			gameAngle -= 2 * Math.PI;
		}

		return gameAngle;
	}
	
	public static double getAbsoluteBearingToPointRadians(Point2D enemyPosition, double myRobotX, double myRobotY) {
		return getAbsoluteBearingToPointRadians(enemyPosition.getX(), enemyPosition.getY(), myRobotX, myRobotY);
	}
	
	public static double getAbsoluteBearingToPointDegree(double targetX, double targetY, double myRobotX, double myRobotY) {
		/*
		 * Para pasar de grados cartesianos estándar a grados del juego (0 en
		 * norte), me he inventado un método raro donde cojo el ángulo en
		 * negativo, le resto 90 y le cambio el signo. Seguro que hay otro
		 * método (cambiar el punto que se le pasa a atan2, p.e) pero este es el
		 * que me ha funcionado hasta ahora.
		 */

		double gameDegrees;
		double standardDegrees = Math
				.toDegrees(Math.atan2(targetY - myRobotY, targetX - myRobotX));

		if (standardDegrees > 0) {
			standardDegrees = standardDegrees - 360;
		}

		gameDegrees = -1 * (standardDegrees - 90);

		if (gameDegrees > 360) {
			gameDegrees -= 360;
		}

		return gameDegrees;

	}
	
	public static double getAbsoluteBearingToPointDegree(Point2D enemyPosition, double myRobotX, double myRobotY) {
		
		return getAbsoluteBearingToPointDegree(enemyPosition.getX(), enemyPosition.getY(), myRobotX, myRobotY);
		
	}

	public static boolean enemyFired(double energyChange) {

		boolean fired = false;

		if (energyChange < 0 && energyChange >= -3) {
			fired = true;
		}

		return fired;
	}

	/**
	 * 
	 * @param index
	 *            - Índice sin normalizar (p.e: arr[-1])
	 * @param arrayLength
	 *            - Longitud del array
	 * @return - int índice normalizado del array
	 */
	public static int normalizeArrayIndex(int index, int arrayLength) {

		int k = index;

		if (k > arrayLength - 1) {
			k = k - arrayLength;
		}
		if (k < 0) {
			k = k + arrayLength;
		}

		return k;
	}

}
