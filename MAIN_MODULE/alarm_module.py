import RPi.GPIO as GPIO
import time
from datetime import datetime
import math

servo=22
GPIO.setmode(GPIO.BCM)
GPIO.setup(servo, GPIO.OUT)
sv = GPIO.PWM(servo,50)
sv.start(0)
alarm = input("What do you want? : ")
#h=int(alarm[0:2])
h=alarm[0:2]
m=int(alarm[3:5])
s=int(alarm[6:8])

"10:34:44"

alarm=str(h)+':'+str(m)+':'+str(s)
print(len(alarm))
print(h)
print(m)
print(s)
if(s>=20):
	s=s-10
	al=str(h)+':'+str(m)+':'+str(s)
	s=s-10
	al2=str(h)+':'+str(m)+':'+str(s)
else :
	m=m-1
	s=(60+s)-10
	al=str(h)+':'+str(m)+':'+str(s)
	s=(60+s)-10
	al2=str(h)+':'+str(m)+':'+str(s)
while True :
	try :
		present= datetime.now()
		present=present.strftime('%H:%M:%S')
		print(present)
		time.sleep(1)
		if(al==present):
			sv.ChangeDutyCycle(10)
			time.sleep(1)
			sv.ChangeDutyCycle(0)
		if(al2==present):
			sv.ChangeDutyCycle(10)
			time.sleep(1)
			sv.ChangeDutyCycle(0)
		if(alarm==present):
			sv.ChangeDutyCycle(10)
			time.sleep(1)
			sv.ChangeDutyCycle(0)

	except KeyboardInterrupt:
		sv.ChangeDutyCycle(0)
		
		break
	except (IOError,TypeError) as e:
		print("Error")

