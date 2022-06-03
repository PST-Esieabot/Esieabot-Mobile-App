////////// GUIDE D'INSTALATION OS //////////

- ping esieabot
- ssh esieabot@[adresse ip du robot]	// Mot de passe: esieabot
- sudo apt update && sudo apt upgrade -y
- sudo apt-get install python3
- sudo apt-get install python3-pip
- sudo apt-get install bluetooth bluez blueman
- sudo pip install picamera
- sudo pip install pybluez
- sudo pip install WiringPi
- sudo pip install dbus-python

- sudo nano /etc/systemd/system/dbus-org.bluez.service	// Ajouter -C a la fin de la ligne: ExecStart=/usr/lib/bluetooth/bluetoothd -C
- sudo systemctl daemon-reload
- sudo systemctl restart dbus-org.bluez.service
- sudo raspi config		// Aller a Configuration option, interface, camera legacy, enable

- mkdir esieabot
- cd /home/esieabot/esieabot
- cat > main.py
- Faire ça pour tous les fichiers

- sudo chmod +x /home/esieabot/esieabot/main.py
- sudo nano /etc/systemd/system/esieabot.service	// Copier le contenue de esieabot.service
- sudo systemctl daemon-reload && sudo systemctl enable esieabot.service
- sudo systemctl start esieabot.service