import java.net.*;

void main() throws Exception {
    var uuid = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
    var address = new InetSocketAddress("ready-bell.com", 3137);

    try (var socket = new DatagramSocket()) {
        var request = ("Listen " + uuid + " 30").getBytes();
        socket.send(new DatagramPacket(request, request.length, address));

        var packet = new DatagramPacket(new byte[512], 512);
        while (true) {
            socket.receive(packet);
            System.out.println(new String(packet.getData(), 0, packet.getLength()).trim());
        }
    }
}
