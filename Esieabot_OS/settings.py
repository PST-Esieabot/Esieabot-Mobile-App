class settings(object):
    def __init__(self, *args, **kwargs):
        """ constructeur : initialise les options du robot """
        print("Initialisation des parametres du robot : OK")
        return super().__init__(*args, **kwargs)

    def createWifiConfig(self, SSID = "", password = ""):
        print("Changement de la wifi par %s - %s" % (SSID, password))
        config = (
                '\nctrl_interface=DIR=/var/run/wpa_supplicant GROUP=netdev' + 
                '\nupdate_config=1' + 
                '\ncountry=FR\n' +
                '\nnetwork={{\n' +
                '\tssid="{}"\n' +
                '\tpsk="{}"\n' + '}}').format(SSID, password)
        print(config)
        with open("/etc/wpa_supplicant/wpa_supplicant.conf", "r+") as wifi:
            wifi.write(config)
        wifi.close()
        print("Wifi config ajoute\n Redemarrez le robot")

    def getSSID(self, data):
        find  = data.find('*')
        return data[1:find]

    def getPassword(self, data):
        find  = data.find('*') + 1
        lenght = len(data)
        return data[find:lenght]