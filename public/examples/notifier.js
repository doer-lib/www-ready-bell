import dgram from "dgram";

const socket = dgram.createSocket("udp4");
const uuid = "3fa85f64-5717-4562-b3fc-2c963f66afa6";

socket.once("message", msg => (console.log(msg.toString().trim()), socket.close()));
socket.send(`Notify ${uuid}`, 3137, "ready-bell.com");
