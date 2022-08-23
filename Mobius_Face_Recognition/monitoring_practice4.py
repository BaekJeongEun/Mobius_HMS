from imutils.video import VideoStream
from flask import Flask, render_template, Response
from picamera.array import PiRGBArray
from picamera import PiCamera
import stream_module as sm
import cv2
import RPi.GPIO as GPIO
import time
#import keyboard as key
import sys
import threading
pin = 18
ang = 7.5
a=0.1
angle = 7.5
stop = False

#Thread names
leftMotorTh = threading.Thread()
rightMotorTh = threading.Thread()
faceTh=threading.Thread()


GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False) #사용되고 있는  GPIO핀을 setup할 경우 나는 에러를 나지 않게 해줍니다.
GPIO.setup(pin, GPIO.OUT)
p= GPIO.PWM(pin,50)

def leftControl():
	global ang,a,pin,angle, stop
	
	GPIO.setup(pin, GPIO.OUT)
	ang=angle
	angle=ang
	try : 
		while True:
			if(stop==False):
				print(angle)
				if angle>=12.5 or angle<=2.5:
					stop = True
					ang=angle
					angle=ang
			
				p.start(angle)
				angle = angle-a
				p.ChangeDutyCycle(angle)
				time.sleep(0.1)
			elif(stop==True):
				break;
			
	except KeyboardInterrupt :
		p.ChangeDutyCycle(7.5)
		time.sleep(0.1)
		p.stop()
		GPIO.cleanup()

		

def rightControl():
	global ang,a,pin,angle, stop

	GPIO.setup(pin, GPIO.OUT)
	ang=angle
	angle=ang
	try : 
		while True:
			if(stop==False):
				print(angle)
				if angle>=12.5 or angle<=2.5:
					stop = True
					ang=angle
					angle=ang
			
				p.start(angle)
				angle = angle+a
				p.ChangeDutyCycle(angle)
				time.sleep(0.1)
			elif(stop==True):
				break;
			
			
	
	except KeyboardInterrupt :
		p.ChangeDutyCycle(7.5)
		time.sleep(0.1)
		p.stop()
		GPIO.cleanup()

def detect(img, cascade):
    rects = cascade.detectMultiScale(img, scaleFactor=1.3, minNeighbors=4, minSize=(30, 30),
                                     flags=cv2.CASCADE_SCALE_IMAGE)
    if len(rects) == 0:
        return []
    rects[:,2:] += rects[:,:2]
    return rects

def draw_rects(img, rects, color):
    for x1, y1, x2, y2 in rects:
        cv2.rectangle(img, (x1, y1), (x2, y2), color, 2)

def face():
	# initialize the camera and grab a reference to the raw camera capture
	camera = PiCamera()
	camera.resolution = (640, 480)
	camera.framerate = 32
	rawCapture = PiRGBArray(camera, size=(640, 480))
	cascade =cv2.CascadeClassifier('/root/haarcascade_frontalface_default.xml')
	sm.run_app()
	
	# capture frames from the camera
	for frame in camera.capture_continuous(rawCapture, format="bgr", use_video_port=True):
		# grab the raw NumPy array representing the image, then initialize the timestamp
		# and occupied/unoccupied text
		img = frame.array
		gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
		gray = cv2.equalizeHist(gray)
    
		rects = detect(gray, cascade)
		vis = img.copy()
		draw_rects(vis, rects, (0, 255, 0))
    
		# show the frame
		cv2.imshow("Frame", vis)
		key = cv2.waitKey(1) & 0xFF
		sm.set_img(vis)
    
		# clear the stream in preparation for the next frame
		rawCapture.truncate(0)

        # if the `q` key was pressed, break from the loop
		if key == ord("q"):
			faceTh.release()
			break 

def main():
	global leftMotorTh, rightMotorTh,faceTh
	global ang,a,pin, angle, stop
	try:
		faceTh = threading.Thread(target= face)
		faceTh.start()
		while True : 
			print(ang)
			choice = input("choice : ")
			if choice == 'a':
				stop=False
				ang=angle
				angle=ang
				x='a'
				print('left')
				leftMotorTh = threading.Thread(target= leftControl)
				leftMotorTh.start()
			elif choice == 'd':
				stop=False
				ang=angle
				angle=ang
				x='d'
				print('right')
				rightMotorTh = threading.Thread(target=rightControl)
				rightMotorTh.start()
			elif choice == 's':
				stop=True
				a=0.1
				ang=angle
				angle=ang
				
				
			elif choice == 'q':
				break
			else :
				print('Invaild choice')
			
	except KeyboardInterrupt : 
		print('Interrupted!!')
		
main()

# allow the camera to warmup
time.sleep(0.1)
ang = 7.5
p.ChangeDutyCycle(7.5)
time.sleep(0.1)
p.stop()
GPIO.cleanup()
				
		
