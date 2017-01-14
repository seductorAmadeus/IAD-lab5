package lab5.serverSide;

import lab5.clientSide.Point;

import java.net.*;
import java.io.*;

public class Server {
    private final static int sizeOfPackage = 100;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    private void start() {
        try {
            DatagramSocket socket = new DatagramSocket(3456, InetAddress.getByName("localhost"));
            System.out.println("The server is ready...");

            while (true) {
                DatagramPacket packet = new DatagramPacket(new byte[sizeOfPackage], sizeOfPackage);
                socket.receive(packet);
                new Thread(new ServerThread(socket, packet)).start();
            }
        } catch (UnknownHostException | SocketException e) {
            System.out.println("Error: can not create socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: can not sendPacket packet: " + e.getMessage());
        }
    }

    class ServerThread extends Thread {
        InputStream in = null;
        OutputStream out = null;

        private DatagramPacket packet;
        private DatagramSocket socket;

        public ServerThread(DatagramSocket socket, DatagramPacket packet) {
            this.socket = socket;
            this.packet = packet;
        }

        public void run() {
            try {
                final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
                final DataInputStream dais = new DataInputStream(byteArrayInputStream);
                final int ID = dais.readInt();
                final float radius = dais.readFloat();
                final float _x = dais.readFloat();
                final float _y = dais.readFloat();
                CheckPointInArea test = new CheckPointInArea(radius);
                int ans = test.pointBelongToTheArea(new Point(_x, _y));

                System.out.println(packet.getAddress() + " " + packet.getPort() + ": Point #" + "(" + _x + "; " + _y + ") with R = " + radius + ", Answer: " + ans);

                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                dataOutputStream.writeInt(ID);
                dataOutputStream.writeBoolean(ans == 1);
                dataOutputStream.close();
                final byte[] bytes = byteArrayOutputStream.toByteArray();

                packet.setData(bytes);
                packet.setLength(dataOutputStream.size());
                packet.setPort(packet.getPort());
                socket.send(packet);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
