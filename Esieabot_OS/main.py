#! /usr/bin/env python3

import bluetoothServer as b
import webServer as w
import pinout as p
import autoPair as a
import picamera
import threading
import urllib.request as urllib2

OS_VERSION = 1.4

def main():

    robot = p.pinout()
    webServer = w.webServer()
    bluetoothServer = b.bluetoothServer(webServer.isWorking, webServer.deviceIP, robot)

    # Threads permettant de lancer le bluetooth et la camera en meme temps
    autoPairThread = threading.Thread(target = a.autopair)
    autoPairThread.start()

    bluetoothThread = threading.Thread(target = bluetoothServer.connect)
    bluetoothThread.start()

    waitForWifi(webServer)


def waitForWifi(webServer):
    wait = True
    while wait:
        try:
            urllib2.urlopen('https://www.google.fr/', timeout=5)
            print("Le robot est connecte a internet")
            wait = False
        except urllib2.URLError as err: 
            pass

    if(webServer.isWorking == True) :
        cameraThread = threading.Thread(target = webServer.startStreaming)
        cameraThread.start()


def readme():
    print("         ===== Esieabot OS =====  ")
    print("Systeme d'exploitation pour controler a distance le robot Esieabot")
    print("Github : https://github.com/PST-Esieabot/Esieabot-Mobile-App")
    print("Version :", OS_VERSION, "\n")


if __name__ == '__main__':
    readme()
    main()
