# Sources :
# https://picamera.readthedocs.io/en/latest/recipes2.html#web-streaming

import socket
import picamera
import io
import logging
import socketserver
from threading import Condition
from http import server


PAGE="""\
<html>
<head>
    <title>Esieabot Streaming server</title>
</head>
<body>
    <img src="stream.mjpg" width="640" height="480" />
</body>
</html>
"""

class cameraServer(object):
    def __init__(self, camera, *args, **kwargs):
        """ Constructeur : initialise le serveur camera """
        try:
            self.deviceIP = self.getIP()
            self.camera = camera
            print("Initialisation du serveur camera : OK")
        except:
            print("ERREUR : L'initialisation du serveur camera a echoue")
        pass
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
            self.camera.stop_recording()


    def getIP(self):
        """ Renvoie l adresse IP du robot """
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(("8.8.8.8.", 80))
        device_ip = s.getsockname()[0]
        s.close()
        device_ip += ":8000"

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
        if self.path == '/':
            self.send_response(301)
            self.send_header('Location', '/index.html')
            self.end_headers()
        elif self.path == '/index.html':
            content = PAGE.encode('utf-8')
            self.send_response(200)
            self.send_header('Content-Type', 'text/html')
            self.send_header('Content-Length', len(content))
            self.end_headers()
            self.wfile.write(content)
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
                logging.warning(
                    'Removed streaming client %s: %s',
                    self.client_address, str(e))
        else:
            self.send_error(404)
            self.end_headers()


class StreamingServer(socketserver.ThreadingMixIn, server.HTTPServer):
    allow_reuse_address = True
    daemon_threads = True

    def __init__(self, address, handler, output):
        handler.output = output
        super().__init__(address, handler)