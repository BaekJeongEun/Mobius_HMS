from imutils.video import VideoStream
from flask import Flask, render_template, Response
from picamera.array import PiRGBArray
from picamera import PiCamera
import stream_module as sm
import time
import cv2

def detect(img, cascade):
    rects = cascade.detectMultiScale(img, scaleFactor=1.3, minNeighbors=4, minSize=(30, 30),
                                     flags=cv2.CASCADE_SCALE_IMAGE)
    if len(rects) == 0:
        return []
    rects[:,2:] += rects[:,:2]
    return rects

def draw_rects(img, rects, color,gray):
    for (x,y,w,h) in rects:
        cv2.rectangle(img, (x,y), (w,h), color, 2)
        cv2.imwrite("/root/dataset/User." + str(face_id) + '.' + str(count) + ".jpg", gray[y:h,x:w])

# initialize the camera and grab a reference to the raw camera capture
camera = PiCamera()
camera.resolution = (640, 480)
camera.framerate = 32
rawCapture = PiRGBArray(camera, size=(640, 480))
cascade =cv2.CascadeClassifier('/root/haarcascade_frontalface_default.xml')
sm.run_app()
# For each person, enter one numeric face id
face_id = input('\n enter user id end press <return> ==>  ')
print("\n [INFO] Initializing face capture. Look the camera and wait ...")
# allow the camera to warmup
time.sleep(0.1)

# Initialize individual sampling face count
count = 0
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
    print(count)
    count += 1
    # Save the captured image into the datasets folder
    #cv2.imwrite("/root/dataset/User." + str(face_id) + '.' + str(count) + ".jpg", rects)
    #cv2.imshow('image', img)
    # show the frame
    cv2.imshow("Frame", vis)
    key = cv2.waitKey(1) & 0xFF
    sm.set_img(vis)


    # clear the stream in preparation for the next frame
    rawCapture.truncate(0)
    if count==50:
        break

        # if the `q` key was pressed, break from the loop
    elif key == ord("q"):
        break 


# Do a bit of cleanup
print("\n [INFO] Exiting Program and cleanup stuff")
cv2.destroyAllWindows()

