import socket

HOST = "127.0.0.1"  # server IP
PORT = 50001        # server Port
BUFFER = 4096       # buffer size

# server socket type (protocol type)
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

# bind server socket IP/Port
sock.bind((HOST, PORT))

# do not need to real-time listen
'''
sock.listen(0)
'''

print("UDPServer listen at: %s:%s\n\r" % (HOST, PORT))

while True:
    # client_sock, client_addr = socket.accept()
    # print("%s:%s connect" % client_addr)

    while True:
        recv, client_addr = sock.recvfrom(BUFFER)

        if not recv:
            break

        # print the context from Client
        print("[Client %s:%s said]:%s" % (client_addr[0], client_addr[1], recv.decode("utf-8")))

        # reply to Client
        msg2 = "UDPServer has received your message"
        sock.sendto(msg2.encode("utf-8"), client_addr)

sock.close()