import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.enterprise.context.ApplicationScoped;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Logger;

@ApplicationScoped
public class ReadyBellService {

    private static final Logger log = Logger.getLogger(ReadyBellService.class.getName());
    private final InetSocketAddress remoteServer = new InetSocketAddress("ready-bell.com", 3137);

    private DatagramSocket socket;

    @Resource
    private ManagedExecutorService executor;

    @PostConstruct
    public void init() {
        try {
            socket = new DatagramSocket();
            executor.submit(this::listenLoop);
            log.info(() -> "ReadyBellService bound to port: " + socket.getLocalPort());
        } catch (SocketException e) {
            throw new RuntimeException("Failed to initialize UDP socket", e);
        }
    }

    private void listenLoop() {
        var buffer = new byte[512];
        var packet = new DatagramPacket(buffer, buffer.length);

        while (!socket.isClosed() && !Thread.currentThread().isInterrupted()) {
            try {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8).trim();
                log.info(() -> "Received UDP packet: " + message);

                if (message.startsWith("Ready ")) {
                    onReady(UUID.fromString(message.substring(6).trim()));
                }
            } catch (SocketException e) {
                log.info("UDP socket closed, stopping listener.");
                break;
            } catch (Exception e) {
                log.severe("UDP reader error: " + e.getMessage());
            }
        }
    }

    public void onReady(UUID uuid) {
        // Place custom business logic here
    }

    public void sendListen(UUID uuid, int seconds) throws Exception {
        var bytes = ("Listen " + uuid + " " + seconds).getBytes(StandardCharsets.UTF_8);
        socket.send(new DatagramPacket(bytes, bytes.length, remoteServer));
    }

    public void sendNotify(UUID uuid) throws Exception {
        var bytes = ("Notify " + uuid).getBytes(StandardCharsets.UTF_8);
        socket.send(new DatagramPacket(bytes, bytes.length, remoteServer));
    }

    @PreDestroy
    public void destroy() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            log.info("ReadyBellService UDP socket closed.");
        }
    }
}
