# AlcOne - Intel Coding Contest - Final

## Descripción

Bot ganador del [Intel Coding Contest](http://institucionales.us.es/intelcontest/), patrocinado por Intel y la ETSII de la Universidad de Sevilla. 

En la competición había que programar un bot de [Robocode](http://robocode.sourceforge.net) para que se enfrentara en combates 1v1.

[Resultados de la final](https://challonge.com/es/icc1_final)

## Robocode

Robocode es un juego de programación en el que tienes que programar el comportamiento de un tanque por medio de una [API](http://robocode.sourceforge.net/docs/robocode/).

![robocode 1v1](https://i.ytimg.com/vi/Hp6bhARBGc4/hqdefault.jpg)

El robot está formado por tres partes. La capa inferior con la base, la capa intermedia con la torreta y la capa superior con el escáner.

Todas las capas se pueden girar y, al hacerlo, se giran las capas superiores en la misma cantidad.

* **Base**: controla la aceleración y el giro de la base (más la torreta y el escáner).

* **Torreta**: controla el disparo y el giro de la torreta (más el escáner) 

* **Escáner**: obtiene información de un tanque rival si está siendo apuntado por el escáner (escanea en una línea, no en una zona). No se obtiene información
         del tanque rival si no está siendo escaneado.

Se pueden encontrar tutoriales y diversas estrategias en [esta wiki](http://robowiki.net/wiki/Main_Page).

## Estructura de archivos

Bot compilado: [alc.AlcOne_1.03.jar](https://github.com/Orzzet/robocodeInteContestFinal/blob/master/alc.AlcOne_1.03.jar)

* [AlcOne](https://github.com/Orzzet/robocodeInteContestFinal/blob/master/src/alc/AlcOne.java) es la clase principal, usa métodos definidos por la clase AdvancedRobot de la API, para interactuar
con el juego y comprobar eventos. También usa los métodos definidos por la clase MyRobot, que se encuentra en la carpeta Utils. El escáner se controla aquí de una forma sencilla.

* [Move](https://github.com/Orzzet/robocodeInteContestFinal/blob/master/src/alc/Move.java) controla el movimiento.

* [Gun](https://github.com/Orzzet/robocodeInteContestFinal/blob/master/src/alc/Gun.java) controla cómo se apunta y se dispara.

* [Utils](https://github.com/Orzzet/robocodeInteContestFinal/tree/master/src/alc/utils). Aquí he metido lo que puede servir para otros bots y reutilizarse con facilidad.
Dentro de esta carpeta se puede destacar:
  * [EnemyBot](https://github.com/Orzzet/robocodeInteContestFinal/blob/master/src/alc/utils/enemy/EnemyBot.java) Obtiene información 
sobre el bot enemigo y define métodos para leerla de forma sencilla.
  * [PatternBot](https://github.com/Orzzet/robocodeInteContestFinal/blob/master/src/alc/utils/enemy/PatternBot.java) Caso concreto para
cuando se usa la estrategia de [reconocimiento de patrón](http://robowiki.net/wiki/Pattern_Matching) (que es la usada por mi bot). Extiende la clase EnemyBot para además almacenar cada turno el giro, la velocidad lineal y [la velocidad lateral](http://robowiki.net/wiki/Lateral_Velocity) 
(aunque al final no la he usado).
  * [SimpleTargetting](https://github.com/Orzzet/robocodeInteContestFinal/blob/master/src/alc/utils/targetting/SimpleTargetting.java) es un método simple para apuntar que supone que el bot enemigo va a 
  seguir su velocidad y su giro actual. También llamado [CircularTargeting](http://robowiki.net/wiki/Circular_Targeting/Walkthrough)
  * [PatternTargeting](https://github.com/Orzzet/robocodeInteContestFinal/blob/master/src/alc/utils/targetting/PatternTargetting.java) es un método más complejo para apuntar que tiene en cuenta información obtenida a lo largo de la partida. En detalle [aquí](http://robowiki.net/wiki/Pattern_Matching)


## Escáner

La estrategia del escaner es muy simple.

Si no tiene objetivo, se gira el escáner hasta que encuentra un objetivo.

Si tiene objetivo (tiene forma rectangular), se gira el escaner para acercarlo más al centro del objetivo.

Debido a la velocidad de giro del escáner en comparación a la velocidad de movimiento y al tamaño del tanque, esto provoca un [infinity lock](http://robowiki.net/wiki/One_on_One_Radar#The_Infinity_Lock).

## Movimiento

En el bot original la estrategia de movimiento era más compleja, usaba [wall smoothing](http://robowiki.net/wiki/Wall_Smoothing) para evitar choques con las
paredes y así no perder vida y tener un movimiento más fluido. Conseguía no chocar, pero al coste de aminorar mucho la velocidad por los bordes y ser un blanco fácil.

También intentaba esquivar balas basandome en el decremento de energía del bot rival ([para saber la velocidad de la bala](http://robowiki.net/wiki/Bullet)). La información
de hacia dónde se dirige la bala no se puede obtener del juego, por lo que yo suponía que iba directo a mi bot cuando no siempre era así. Además,
aunque me colocase en una posición correcta para esquivar la bala, al intentar esquivar una segunda bala podía ponerme en la trayectoria de una bala disparada con anterioridad.

En general tomaba muchos datos para intentar tomar la mejor decision en cada momento y la realidad era que acababa haciendo movimientos mediocres sin llegar a evitar el peligro.

El mejor resultado lo obtuve cuando quité la mayor parte del movimiento y lo simplifiqué a dos partes:
1. En el comienzo de la ronda, dirige el bot a una zona más central del mapa si está muy cerca de los bordes.
2. Orbita alrededor del bot rival siguiendo una [trayectoria sinusoidal](http://robowiki.net/wiki/Oscillator_Movement). Si se choca contra una pared, se invierte el sentido.

## Apuntado

Nada más empezar la ronda, comienzo a recolectar datos del bot rival. Una vez tiene 100 o más turnos almacenados, se habilita el apuntado por reconocimiento de patrones.

Para el apuntado uso 3 estrategias distintas:
1. [Apuntado directo](http://robowiki.net/wiki/Head-On_Targeting): Si el bot enemigo está muy cerca -> Apunto en la posición exacta del bot rival.
2. [Apuntado circular](http://robowiki.net/wiki/Circular_Targeting/Walkthrough): Si el bot enemigo no está cerca y no tengo datos suficientes almacenados -> Apunto en la posición que va a estar el rival suponiendo que sigue su curso actual. 
3. [Apuntado por reconocimiento de patrones](http://robowiki.net/wiki/Pattern_Matching): Si el bot enemigo no está cerca y tengo datos suficientes almacenados -> Apunto en la posición que va a estar el rival teniendo en cuenta sus últimos movimientos
y comparándolos con los movimientos almacenados de la ronda.

Además, si fallo más de 3 balas seguidas y estoy en 2 o en 3, cambio a la otra estrategia.

Durante el torneo la estrategia del reconocimiento de patrones resultó ser muy efectiva ya que nadie jugó alrededor de esa posibilidad, lo que
hace que esta estrategia tenga una efectividad muy superior a cualquier método de apuntado simple.
