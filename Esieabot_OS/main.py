#! /usr/bin/python3

import bluetoothServer as b
import cameraServer as c
import motors as m
import picamera
import threading


OS_VERSION = 1.2
CAMERA_STATUS = False

def main():
    
    try:
        # Regarde si la camera est connecte au robot
        camera = picamera.PiCamera(resolution='640x480', framerate=24)
        CAMERA_STATUS = True
    except:
        CAMERA_STATUS = False

    robot = m.motors()
    cameraServer = c.cameraServer(camera)
    bluetoothServer = b.bluetoothServer(CAMERA_STATUS, cameraServer.deviceIP, robot)

    # Threads permettant de lancer le bluetooth et la camera en meme temps
    bluetoothThread = threading.Thread(target = bluetoothServer.connect)
    bluetoothThread.start()

    if(CAMERA_STATUS == True) :
        cameraThread = threading.Thread(target = cameraServer.startStreaming)
        cameraThread.start()


def readme():
    print("  ===== Esieabot OS =====  ")
    print("Serveur bluetooth pour controler a distance le robot Esieabot")
    print("Github : https://github.com/PST-Esieabot/Esieabot-Mobile-App")
    print("Version :", OS_VERSION, "\n")


if __name__ == '__main__':
    readme()
    main()
