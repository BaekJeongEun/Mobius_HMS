import RPi.GPIO as GPIO
import time
import threading as th
from datetime import datetime
import math

class curtain_module:
	def __init__(self):
		self.servo=21
		#GPIO.setmode(GPIO.BCM)
		GPIO.setup(self.servo, GPIO.OUT)
		self.sv = GPIO.PWM(self.servo,50)
		self.sv.start(0)
		self.cur = 0
		self.alarm_waiter = None
		self.handler = th.Thread(target=self.handle, args=())
		self.handler.start()

	def handle(self):
		while True:
			if(self.cur == 3):
				self.sv.ChangeDutyCycle(2.5)
				time.sleep(0.8)
				self.sv.ChangeDutyCycle(0)
				self.cur = 0
			elif(self.cur == 4):
				self.sv.ChangeDutyCycle(10)
				time.sleep(0.8)
				self.sv.ChangeDutyCycle(0)
				self.cur = 0
			elif(self.cur == 5):
				self.sv.ChangeDutyCycle(0)
				self.cur = 0
			elif(self.cur >= 10000):
				self.set_alarm()
				self.alarm_waiter = th.Thread(target=self.wait_alarm, args=())
				self.alarm_waiter.start()

	def set_alarm(self):
		self.alarm = self.cur
		self.s = self.alarm % 100
		self.alarm = self.alarm/100
		self.m = self.alarm % 100
		self.alarm = self.alarm/100
		self.h = self.alarm
		
		if self.s >=20 :
			s = self.s
			s = s - 10
			self.al1 = self.h*10000 + self.m*100 + s
			s = s - 10
			self.al2 = self.h*10000 + self.m*100 + s
			
		else :
			m = self.m - 1
			s = (self.s+60)
			s = s - 10
			self.al1 = self.h*10000 + m*100 + s
			s = s - 10
			self.al2 = self.h*10000 + m*100 + s
		
	def wait_alarm(self):
                while True :
                	present= datetime.now()
                	present=present.strftime('%H%M%S')
                	int(present)
                	time.sleep(1)
			
                	if(self.al1==present):
                		self.sv.ChangeDutyCycle(10)
                		time.sleep(0.3)
                		self.sv.ChangeDutyCycle(0)
                	elif(self.al2==present):
                		self.sv.ChangeDutyCycle(10)
                		time.sleep(0.3)
                		self.sv.ChangeDutyCycle(0)
                	elif(self.alarm==present):
                		self.sv.ChangeDutyCycle(10)
                		time.sleep(0.3)
                		self.sv.ChangeDutyCycle(0)
		
	def set_cur(self,cur):
		self.cur = cur

if __name__ == '__main__':
	curtain_module()
