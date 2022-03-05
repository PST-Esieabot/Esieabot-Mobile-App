# Sources :
# https://github.com/karulis/pybluez

from bluetooth import *
import os
import time

DEFAULT_SPEED = 100

STATE_NONE = 0
STATE_CONNECTING = 1
STATE_CONNECTED = 2
STATE_CONNECTION_LOST = 3

class bluetoothServer(object):
    def __init__(self, isCamera, deviceIP, robot, *args, **kwargs):
        """ Constructeur : initialise le serveur bluetooth """
        try:
            self.isCamera = isCamera
            self.deviceIP = deviceIP
            self.robot = robot
            self.connectionState = STATE_NONE

            print("Initialisation du serveur bluetooth : OK")
        except:
            print("ERREUR : L'initialisation du serveur bluetooth a echoue")
        pass
        return super().__init__(*args, **kwargs)


    def connect(self):
        """ Entame la connexion bluetooth """
        self.connectionState = STATE_CONNECTING

        time.sleep(4) # Permet de laisser le temps au bluetooth de s'activer
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
        time.sleep(0.05)
        self.robot.stop()

        print("En attente de connexion sur le port RFCOMM %d" % port)

        client_sock, client_info = server_sock.accept()
        print("Connexion acceptee de ", client_info, "\n")

        if(self.isCamera == True):
            client_sock.send(self.deviceIP)

        self.connected(client_sock, server_sock)


    def connected(self, client_sock, server_sock):
        """ Gere la connexion bluetooth """
        self.connectionState = STATE_CONNECTED
        try:
            while True:
                bytedata = client_sock.recv(1024)
                if len(bytedata) == 0: break

                data = str(bytedata.decode())   # Sert a transformer les bytes en strings
                data = data.rstrip("\n").rstrip("\r")
                print("Donnees recus : [%s]" % data)

                if data == "forwards":
                    self.robot.forwards(DEFAULT_SPEED)
                    print("Avancer")
                if data == "backwards":
                    self.robot.backwards(DEFAULT_SPEED)
                    print("Reculer")
                if data == "left":
                    self.robot.leftSpin(DEFAULT_SPEED)
                    print("Gauche")
                if data == "right":
                    self.robot.rightSpin(DEFAULT_SPEED)
                    print("Droite")
                if data == "stop":
                    self.robot.stop()
                    print("Stop")

        except IOError:
            self.connectionLost(client_sock, server_sock)


    def connectionLost(self, client_sock, server_sock):
        """ Permet de se deconnecter du serveur bluetooth en cas d'erreur """
        self.connectionState = STATE_CONNECTION_LOST
        print("\nConnexion perdu")
        self.robot.stop()

        client_sock.close()
        server_sock.close()

        print("Fin du programme\n")