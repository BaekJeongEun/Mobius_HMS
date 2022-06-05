from imutils.video import VideoStream
from flask import Flask, render_template, Response
from picamera.array import PiRGBArray
from picamera import PiCamera
import stream_module as sm
import time
import cv2

recognizer = cv2.face.LBPHFaceRecognizer_create()
recognizer.read('/root/trainer/trainer.yml')
font = cv2.FONT_HERSHEY_SIMPLEX

#iniciate id counter
id = 0

# names related to ids: example ==> loze: id=1,  etc
# 이런식으로 사용자의 이름을 사용자 수만큼 추가해준다.
names = ['None', 'taeyeoni', 'jeongeuni','dayeoni']

def detect(img, cascade):
    rects = cascade.detectMultiScale(img, scaleFactor=1.3, minNeighbors=4, minSize=(30, 30),
                                     flags=cv2.CASCADE_SCALE_IMAGE)
    if len(rects) == 0:
        return []
    rects[:,2:] += rects[:,:2]
    return rects

def draw_rects(img, rects, color,gray):
	for (x,y,w,h)  in rects:
		cv2.rectangle(img,(x,y), (w,h), color, 2)
		id, confidence = recognizer.predict(gray[y:h,x:w])
		# Check if confidence is less them 100 ==> "0" is perfect match
		if (confidence < 100):
			id = names[id]
			confidence = "  {0}%".format(round(100 - confidence))
		else:
			id = "unknown"
			confidence = "  {0}%".format(round(100 - confidence))
		cv2.putText(img, str(id), (x+5,y-5), font, 1, (255,255,255), 2)
		cv2.putText(img, str(confidence), (x+5,y+h-5), font, 1, (255,255,0), 1)

# initialize the camera and grab a reference to the raw camera capture
camera = PiCamera()
camera.resolution = (640, 480)
camera.framerate = 32
rawCapture = PiRGBArray(camera, size=(640, 480))
cascade =cv2.CascadeClassifier('/root/haarcascade_frontalface_default.xml')
sm.run_app()



# allow the camera to warmup
time.sleep(0.1)

# capture frames from the camera
for frame in camera.capture_continuous(rawCapture, format="bgr", use_video_port=True):
    # grab the raw NumPy array representing the image, then initialize the timestamp
    # and occupied/unoccupied text
    img = frame.array
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    gray = cv2.equalizeHist(gray)
    
    rects = detect(gray, cascade)
    vis = img.copy()
    draw_rects(vis, rects, (0, 255, 0),gray)
    
    # show the frame
    cv2.imshow("Frame", vis)
    key = cv2.waitKey(1) & 0xFF
    sm.set_img(vis)
    
    # clear the stream in preparation for the next frame
    rawCapture.truncate(0)

        # if the `q` key was pressed, break from the loop
    if key == ord("q"):
        break 
