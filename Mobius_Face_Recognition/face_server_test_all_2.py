import socket

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
    rects = cascade.detectMultiScale(img, scaleFactor=1.3, minNeighbors=4, minSize=(30, 30),flags=cv2.CASCADE_SCALE_IMAGE)
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
	


HOST = ""
PORT = 5000
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print ('Socket created')
s.bind((HOST, PORT))
print ('Socket bind complete')
s.listen(1)
print ('Socket now listening')


#파이 컨트롤 함수
def do_some_stuffs_with_input(input_string):
	global ang,a,pin,angle, stop
	#라즈베리파이를 컨트롤할 명령어 설정
	if input_string == "1":
		input_string = "서보모터 좌회전 합니다."
		stop=False
		ang=angle
		angle=ang
		leftMotorTh = threading.Thread(target= leftControl)
		leftMotorTh.start()
		#파이 동작 명령 추가할것
	elif input_string == "3":
		input_string = "서보모터 우회전 합니다."
		stop=False
		ang=angle
		angle=ang
		rightMotorTh = threading.Thread(target=rightControl)
		rightMotorTh.start()
	elif input_string == "2":
		input_string ="서보모터 정지 합니다."
		stop=True
		a=0.1
		ang=angle
		angle=ang
	else :
		input_string = input_string + " 없는 명령어 입니다."
	return input_string

faceTh = threading.Thread(target= face)
faceTh.start()
while True:
	#접속 승인
	conn, addr = s.accept()
	print("Connected by ", addr)

	#데이터 수신
	data = conn.recv(1024)
	data = data.decode("utf8").strip()
	if not data: break
	print("Received: " + data)

	#수신한 데이터로 파이를 컨트롤 
	res = do_some_stuffs_with_input(data)
	print("파이 동작 :" + res)

	#클라이언트에게 답을 보냄
	conn.sendall(res.encode("utf-8"))
	#연결 닫기
	conn.close()
s.close()
