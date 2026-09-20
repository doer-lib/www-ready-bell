import socket

uuid = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
addr = ("ready-bell.com", 3137)

with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
    s.sendto(f"Listen {uuid} 30".encode(), addr)
    while True:
        data, _ = s.recvfrom(512)
        print(data.decode().strip())
