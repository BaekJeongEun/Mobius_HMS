import video_module as vm
import jeongeun_module as jm
import curtain_module as cm
#import alarm_module as am
import server_module as sm
import multiprocessing as mp

class module_communicator:
	def __init__(self):
		self.video_process = mp.Process(target=self.video_init, args=())
		self.video_process.start()
		self.server = sm.socket_server()
		self.jeongeun = jm.jeongeun()
		self.curtain = cm.curtain_module()
		self.command=""
		self.communicate()

	def video_init(self):
		vm.start_detecting_vid()

	def communicate(self):
		while True:
			if self.server.ch != None :
				self.command = self.server.ch.get_client_msg()
			if self.command != "" :
				self.command = int(self.command)

			if self.command != "" :
				print(self.command)
				if self.command == 1 or self.command == 2:
					self.jeongeun.set_x(self.command)
				elif self.command == 3 or self.command == 4 or self.command==5:
					self.curtain.set_cur(self.command)
				elif self.command >= 10000 :
					self.curtain.set_cur(self.command)
				else:
					print("main module : command ->", self.command)

			self.command=""
		
			if self.server.ch != None and self.jeongeun.is_door_state_changed() :
				self.server.ch.set_server_msg(("open" if self.jeongeun.is_door_open else "close"))

if __name__ == '__main__':
	module_communicator()
