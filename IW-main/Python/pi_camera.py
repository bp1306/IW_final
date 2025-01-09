from picamera2 import Picamera2, Preview
import time

def Camera(width, height, lores_width,lores_height):
    camera = Picamera2()
    config = camera.create_still_configuration(main={"size": (width,height)}, lores ={"size":(lores_width,lores_height)},display ="lores")
    camera.configure(config)
    return camera

def start_preview(camera):
    camera.start_preview(Preview.QTGL)

def start(camera):
    camera.start()

def delay(seconds):
    time.sleep(seconds)

def capture(camera):
    return camera.capture_array("main")

def stop_preview(camera):
    camera.stop_preview()

def stop(camera):
    camera.stop()
