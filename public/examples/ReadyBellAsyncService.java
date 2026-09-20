package com.example.readybell;

import io.smallrye.mutiny.Uni;
import io.vertx.core.SocketAddress;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.core.datagram.DatagramSocket;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Logger;

@ApplicationScoped
public class ReadyBellAsyncService {

    private static final Logger log = Logger.getLogger(ReadyBellAsyncService.class.getName());
    private final SocketAddress remoteServer = SocketAddress.inetSocketAddress(3137, "ready-bell.com");

    @Inject
    Vertx vertx;

    private DatagramSocket socket;

    @PostConstruct
    public void init() {
        socket = vertx.createDatagramSocket();

        socket.handler(packet -> {
            String message = packet.data().toString(StandardCharsets.UTF_8).trim();
            log.info(() -> "Received UDP packet: " + message);

            if (message.startsWith("Ready ")) {
                onReady(UUID.fromString(message.substring(6).trim()));
            }
        });

        socket.listen(0, "0.0.0.0")
                .subscribe().with(
                        s -> log.info(() -> "ReadyBellAsyncService bound to port: " + s.localAddress().port()),
                        e -> log.severe("Failed to initialize UDP socket: " + e.getMessage())
                );
    }

    public void onReady(UUID uuid) {
        // Place custom business logic here
    }

    public Uni<Void> sendListen(UUID uuid, int seconds) {
        String msg = "Listen " + uuid + " " + seconds;
        return socket.send(Buffer.buffer(msg), remoteServer.port(), remoteServer.host()).replaceWithVoid();
    }

    public Uni<Void> sendNotify(UUID uuid) {
        String msg = "Notify " + uuid;
        return socket.send(Buffer.buffer(msg), remoteServer.port(), remoteServer.host()).replaceWithVoid();
    }

    @PreDestroy
    public void destroy() {
        if (socket != null) {
            socket.close().subscribe().with(
                    v -> log.info("ReadyBellAsyncService UDP socket closed."),
                    e -> log.severe("Error closing UDP socket: " + e.getMessage())
            );
        }
    }
}
