# Sources :
# https://picamera.readthedocs.io/en/latest/recipes2.html#web-streaming


import socket
import picamera
import io
import socketserver
from threading import Condition
from http import server


class webServer(object):
    def __init__(self, *args, **kwargs):
        """ Constructeur : initialise le serveur camera """
        try:
            camera = picamera.PiCamera(resolution='640x480', framerate=24)
            self.isWorking = True
            self.camera = camera

        except:
            self.isWorking = False
            self.deviceIP = 0
            print("WARNING : Camera non connecte")
            return

        self.deviceIP = self.getIP()
        print("Initialisation du serveur camera : OK")
       
        return super().__init__(*args, **kwargs)

    def startStreaming(self):
        print("\nLancement du stream video a l'adresse http://{}" .format(self.deviceIP))

        output = StreamingOutput()
        self.camera.start_recording(output, format='mjpeg')
        try:
            address = ('', 8000)
            server = StreamingServer(address, StreamingHandler, output)
            server.serve_forever()
        finally:
            server.server_close()
            self.camera.stop_recording()


    def getIP(self):
        """ Renvoie l adresse IP du robot """
        try:
            s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            s.connect(("8.8.8.8.", 80))
            device_ip = s.getsockname()[0]
            s.close()
            device_ip += ":8000"
        except:
            device_ip = 0

        return device_ip


class StreamingOutput(object):
    def __init__(self):
        self.frame = None
        self.buffer = io.BytesIO()
        self.condition = Condition()

    def write(self, buf):
        if buf.startswith(b'\xff\xd8'):
            self.buffer.truncate()
            with self.condition:
                self.frame = self.buffer.getvalue()
                self.condition.notify_all()
            self.buffer.seek(0)
        return self.buffer.write(buf)


class StreamingHandler(server.BaseHTTPRequestHandler):
    output = None

    def do_GET(self):
        if self.path == '/' or self.path == '/index.html':
            self.path = '/index.html'
            try:
                file = open(self.path[1:]).read()
                self.send_response(200)
            except:
                file = "ERREUR : Index.html non trouve"
                self.send_response(404)

            self.end_headers()
            self.wfile.write(bytes(file, 'utf-8'))

        elif self.path == '/stream.mjpg':
            self.send_response(200)
            self.send_header('Age', 0)
            self.send_header('Cache-Control', 'no-cache, private')
            self.send_header('Pragma', 'no-cache')
            self.send_header('Content-Type', 'multipart/x-mixed-replace; boundary=FRAME')
            self.end_headers()
            try:
                while True:
                    with StreamingHandler.output.condition:
                        StreamingHandler.output.condition.wait()
                        frame = StreamingHandler.output.frame
                    self.wfile.write(b'--FRAME\r\n')
                    self.send_header('Content-Type', 'image/jpeg')
                    self.send_header('Content-Length', len(frame))
                    self.end_headers()
                    self.wfile.write(frame)
                    self.wfile.write(b'\r\n')
            except Exception as e:
                pass
        else:
            self.send_error(404)
            self.end_headers()


class StreamingServer(socketserver.ThreadingMixIn, server.HTTPServer):
    allow_reuse_address = True
    daemon_threads = True

    def __init__(self, address, handler, output):
        handler.output = output
        super().__init__(address, handler)