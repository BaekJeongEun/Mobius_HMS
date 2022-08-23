import RPi.GPIO as GPIO
import time
import threading as th

class jeongeun:
	def __init__(self):
		self.x = 0
		self.pin = 18
		self.mag = 27
		GPIO.setmode(GPIO.BCM)
		GPIO.setup(self.pin, GPIO.OUT)
		GPIO.setup(self.mag, GPIO.IN)
		GPIO.setwarnings(False)
		self.p = GPIO.PWM(self.pin,50)
		self.is_door_open = False
		self.door_state_changed = False

		self.jeongeun_handler = th.Thread(target=self.handle, args=())
		self.jeongeun_handler.start()

	def handle(self):
		while True:
			if self.x == 1 :
				print("문 열린다!")
				self.p.start(7.5)
				self.p.ChangeDutyCycle(7.5)
				self.p.ChangeDutyCycle(11.5)
				self.x = 0

			elif self.x == 2 :
				print("문 닫힌다!")
				self.p.ChangeDutyCycle(2.5)
				self.x = 0

			
			if GPIO.input(self.mag) == 1:
				print(GPIO.input(self.mag))
				self.is_door_open = False
				self.door_state_changed = True
				print("문 닫힘!")
				time.sleep(1)
			elif GPIO.input(self.mag)!=1:
				print(GPIO.input(self.mag))
				self.is_door_open = True
				self.door_state_changed = True
				print("문 열림!")
				time.sleep(1)
				
						

	def set_x(self,x):
		self.x = x

	def is_door_opened(self):
		return self.is_door_open

	def is_door_state_changed(self):
		ret_bool = self.door_state_changed
		self.door_state_changed = False
		return ret_bool

if __name__ == '__main__':
    jeongeun()
