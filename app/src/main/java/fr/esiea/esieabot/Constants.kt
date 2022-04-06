package fr.esiea.esieabot

interface Constants {
    companion object {

        const val MESSAGE_READ = 2
        const val MESSAGE_WRITE = 3
        const val MESSAGE_DEVICE_NAME = 4
        const val MESSAGE_TOAST = 5

        const val FORWARDS = "forwards"
        const val BACKWARDS = "backwards"
        const val LEFT = "left"
        const val RIGHT = "right"
        const val STOP = "stop"

        const val DEVICE_NAME = "device_name"
        const val DEVICE_IP = "000.000.0.00"
        const val TOAST = "toast"

        const val FIRSTTIME = "firstTime"

        const val ACTION = "action"
        const val DURATION = 0L
    }
}