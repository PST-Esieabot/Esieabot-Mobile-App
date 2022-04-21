# Sources :
# https://github.com/WiringPi/WiringPi-Python
# https://github.com/gaetanvetillard/ESIEABot-Keyboard

from wiringpi import *

# Constantes
DEFAULT_SPEED = 100
LEFT_ENABLE = 7  # GPIO  4, broche enable du moteur 1
LEFT_FORWARDS = 0  # GPIO 17, entree 1 du moteur 1
LEFT_BACKWARDS = 3  # GPIO 22, entree 2 du moteur 1
RIGHT_ENABLE = 5  # GPIO 24, broche enable du moteur 2
RIGHT_FORWARDS = 6  # GPIO 25, entree 1 du moteur 2
RIGHT_BACKWARDS = 4  # GPIO 23, entree 2 du moteur 2

class pinout(object):
    def __init__(self, *args, **kwargs):
        """ Constructeur : initialise les pins du robot """
        try:
            wiringPiSetup()

            # Initialisation roue gauche
            pinMode (LEFT_ENABLE, 1)
            pinMode (LEFT_FORWARDS, 1)
            pinMode (LEFT_BACKWARDS, 1)

            # Initialisation roue droite
            pinMode (RIGHT_ENABLE, 1)
            pinMode (RIGHT_FORWARDS, 1)
            pinMode (RIGHT_BACKWARDS, 1)

            # Activation de la modulation de la puissance fournie aux moteurs
            softPwmCreate (LEFT_ENABLE, 0, DEFAULT_SPEED)
            softPwmCreate (RIGHT_ENABLE, 0, DEFAULT_SPEED)

            print("Initialisation des pins : OK")
        except:
            print("ERREUR : L'initialisation des pins a echoue")
        pass
        return super().__init__(*args, **kwargs)


    def __del__(self):
        self.stop()

    def leftForwards(self, speed):
        softPwmWrite(LEFT_ENABLE, speed)
        digitalWrite(LEFT_FORWARDS, 1)
        digitalWrite(LEFT_BACKWARDS, 0)

    def leftBackwards(self, speed):
        softPwmWrite(LEFT_ENABLE, speed)
        digitalWrite(LEFT_FORWARDS, 0)
        digitalWrite(LEFT_BACKWARDS, 1)

    def rightForwards(self, speed):
        softPwmWrite(RIGHT_ENABLE, speed)
        digitalWrite(RIGHT_FORWARDS, 1)
        digitalWrite(RIGHT_BACKWARDS, 0)

    def rightBackwards(self, speed):
        softPwmWrite(RIGHT_ENABLE, speed)
        digitalWrite(RIGHT_FORWARDS, 0)
        digitalWrite(RIGHT_BACKWARDS, 1)

    def forwards(self, speed):
        self.leftForwards(speed)
        self.rightForwards(speed)

    def backwards(self, speed):
        self.leftBackwards(speed)
        self.rightBackwards(speed)

    def leftSpin(self, speed):
        self.rightForwards(speed)
        self.leftBackwards(speed)

    def rightSpin(self, speed):
        self.leftForwards(speed)
        self.rightBackwards(speed)

    def stop(self):
        softPwmWrite(LEFT_ENABLE, 0)
        digitalWrite(LEFT_FORWARDS, 0)
        digitalWrite(LEFT_BACKWARDS, 0)
        softPwmWrite(RIGHT_ENABLE, 0)
        digitalWrite(RIGHT_FORWARDS, 0)
        digitalWrite(RIGHT_BACKWARDS  , 0)