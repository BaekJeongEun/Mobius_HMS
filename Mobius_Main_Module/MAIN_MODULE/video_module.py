from imutils.video import VideoStream
from flask import Flask, render_template, Response
import stream_module as sm
import numpy
import RPi.GPIO as GPIO
import threading
import imutils
import cv2
import time
import os
import sys
from picamera import PiCamera
import picamera

class video_module():
   def __init__(self):
      self.get_gpio = 0
      self.video_stream = self.vs_init()
      self.video_thread = threading.Thread(target=self.video_handle, args=())
      self.video_thread.start()
      sm.run_app()

   def get_get(self, num):
      self.get_gpio = num

   def vs_init(self):
      print("starting video stream...")
      vs = VideoStream(usePiCamera=True).start()
      time.sleep(2.0)
      print("done: video stream init")
      return vs

   def video_handle(self):
      while True :
         frame = self.video_stream.read()
         frame = imutils.resize(frame, width=500)

         sm.set_img(frame)
         #cv2.imshow("lee yewon is a genius", frame)
         if self.get_gpio == 1 :
            cv2.imwrite('/home/pi/img.jpg',frame,params=[cv2.IMWRITE_PNG_COMPRESSION,0])
            self.get_gpio = 0
            print("capture")
         k = cv2.waitKey(1)
         if k== 27 :
            break;
      #end of while
   #end of func
#end of class

def capture():
   cam =cv2.VideoCapture(0)
   cam.set(3,360)
   cam.set(4,240)
   if cam.isOpened() == False:
      print('camt open the cam (%d)' % 0)
      return None

   ret, frame =cam.read()
   if frame is None:
      print('frame is not exist')
      return None
   
   #cv2.imshow("lee yewon is a genius", frame)
   cv2.imwrite('/img.jpg',frame,params=[cv2.IMWRITE_JPEG_QUALITY,100])
   print ("capture")
   cam.release()

def start_detecting_vid():
    GPIO.setmode(GPIO.BCM)

    pirPin=26
    GPIO.setup(pirPin,GPIO.IN,GPIO.PUD_UP)

    CAM_ID=0
    vm = video_module()
    while True:
       if GPIO.input(pirPin):
          try:
             vm.get_get(1)
             time.sleep(1)
             th = threading.Thread(target=send_email, args=())
             th.start()
          except:
             print("Error")
       time.sleep(2)

def send_email():
   os.system("mpack -s 'motion' /home/pi/img.jpg degam47@naver.com")

if __name__ == '__main__':
    start_detecting_vid()
