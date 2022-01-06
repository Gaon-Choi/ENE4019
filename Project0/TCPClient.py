import socket

HOST = "127.0.0.1"  # server IP
PORT = 10900       # server Port
BUFFER = 4096       # buffer size

# client socket type (protocol type)
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

# server connection
sock.connect((HOST, PORT))

# send message to Server
my_msg = "Hello, UDPServer!"

sock.send(my_msg.encode('utf-8'))

# save message from the server in buffer
recv = sock.recv(BUFFER)

# save context in buffer
print("[UDPServer said]: %s" % recv.decode("utf-8"))

sock.close()
