from imutils.video import VideoStream
from flask import Flask, render_template, Response
import cv2
import sys
import imutils
import numpy
import time
import threading

app = Flask(__name__)

global im
im = None

global clear
clear = False

@app.route('/')
def index():
	return render_template('index.html')

def set_img(img):
	global im
	im = img
	
def get_img():
	global im
	return im
	
def get_frame():
	global im
	global clear
	
	#camera_port=0
	#camera=cv2.VideoCapture(camera_port)
	
	while True:
		try :
			retval = 0
			#im = imutils.resize(im, width=400)
			imgencode=cv2.imencode('.jpg',im)[1]
			stringData=imgencode.tostring()
			yield (b'--frame\r\n'
				b'Content-Type: text/plain\r\n\r\n' + stringData + b'\r\n')
				
			if clear :
				break
		except SystemError :
			pass 
		else :
			pass
			
	#del(camera)
	#del(vs)
	
@app.route('/calc')
def calc():
	return Response(get_frame(),mimetype='multipart/x-mixed-replace; boundary=frame')

def run_app():
	th = threading.Thread(target=run_server , args=())
	th.start()

def run_server():
	host = '192.168.1.14'
	port = '7003'
	app.run(host=host,port=port,debug=False, threaded=True)
	
def clear_app():
	global clear
	clear = True
	
if __name__=='__main__':
	#init()
	app.run(host='192.168.1.14',port='7003',debug=True, threaded=True)
