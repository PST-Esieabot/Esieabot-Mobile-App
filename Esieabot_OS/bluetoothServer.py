# Sources :
# https://github.com/karulis/pybluez

from bluetooth import *
import settings as s
import os
import time
import settings
import threading

DEFAULT_SPEED = 100

STATE_NONE = 0
STATE_CONNECTING = 1
STATE_CONNECTED = 2
STATE_CONNECTION_LOST = 3

class bluetoothServer(object):
    def __init__(self, isCamera, deviceIP = None, robot = None, *args, **kwargs):
        """ constructeur : initialise le serveur bluetooth """
        try:
            self.isCamera = isCamera
            self.deviceIP = deviceIP
            self.robot = robot
            self.connectionState = STATE_NONE

            self.settings = s.settings()

            print("Initialisation du serveur bluetooth : OK")
        except:
            print("ERREUR : L'initialisation du serveur bluetooth a echoue")
        pass
        return super().__init__(*args, **kwargs)


    def connect(self):
        """ Entame la connexion bluetooth """
        self.connectionState = STATE_CONNECTING
        os.system("sudo hciconfig hci0 piscan")  # Rend le robot visible

        server_sock = BluetoothSocket( RFCOMM )
        server_sock.bind(("",PORT_ANY))
        server_sock.listen(1)

        port = server_sock.getsockname()[1]

        uuid = "00001101-0000-1000-8000-00805F9B34FB"   # UUID de connexion, le meme que sur l'appli

        advertise_service( server_sock, "Esieabot Bluetooth Server",
                            service_id = uuid,
                            service_classes = [ uuid, SERIAL_PORT_CLASS ],
                            profiles = [ SERIAL_PORT_PROFILE ]
                            )

        # Permet d'indiquer que le robot est pret pour une connexion
        self.robot.forwards(DEFAULT_SPEED)
        time.sleep(0.07)
        self.robot.stop()

        print("\nEn attente de connexion sur le port RFCOMM %d" % port)

        client_sock, client_info = server_sock.accept()
        print("Connexion acceptee de ", client_info)

        # Envoie l'adresse IP du robot a l'appareil connecte
        if(self.isCamera == True):
            try:
                client_sock.send(self.deviceIP.encode())
            except:
                pass

        self.connected(client_sock, server_sock)


    def connected(self, client_sock, server_sock):
        """ Gere la connexion bluetooth """
        self.connectionState = STATE_CONNECTED
        sensorActivated = False

        try:
            while True:
                bytedata = client_sock.recv(1024)
                if len(bytedata) == 0: break

                data = str(bytedata.decode())   # Sert a transformer les bytes en strings
                data = data.rstrip("\n").rstrip("\r")
                print("Donnees recus : [%s]" % data)

                if data == "forwards":
                    self.robot.forwards(DEFAULT_SPEED)

                if data == "backwards":
                    self.robot.backwards(DEFAULT_SPEED)

                if data == "left":
                    self.robot.leftSpin(DEFAULT_SPEED)

                if data == "right":
                    self.robot.rightSpin(DEFAULT_SPEED)

                if data == "stop":
                    self.robot.stop()

                if data[0] == 'w':
                    SSID = self.settings.getSSID(data)
                    password = self.settings.getPassword(data)
                    self.settings.createWifiConfig(SSID, password)

                if data == "ultrasound_ON" and self.robot.activated == False:
                    self.robot.activated = True
                    self.ultrasonicSensorThread = threading.Thread(target = self.robot.calculateDistance)
                    self.ultrasonicSensorThread.start()

                if data == "ultrasound_OFF" and self.robot.activated == True:
                    self.robot.activated = False
                    self.robot.distance = 100
                    self.ultrasonicSensorThread.join()

                if data == "scan" and self.robot.activated == True:
                    self.robot.forward(DEFAULT_SPEED)


        except IOError:
            self.connectionLost(client_sock, server_sock)


    def connectionLost(self, client_sock, server_sock):
        """ Permet de se deconnecter du serveur bluetooth en cas d'erreur """
        self.connectionState = STATE_CONNECTION_LOST

        print("\nConnexion perdu")

        self.robot.stop()

        try:
            self.ultrasonicSensorThread.join()
        except:
            pass

        client_sock.close()
        server_sock.close()

        print("Fin du programme\n")