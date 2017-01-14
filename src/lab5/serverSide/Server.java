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
            System.out.println("____________________________\n" +
                    "Welcome, dear %username%!\n" +
                    "\tThe server is ready...\n" +
                    "____________________________\n");
            while (true) {
                DatagramPacket packet = new DatagramPacket(new byte[sizeOfPackage], sizeOfPackage);
                socket.receive(packet);
                new Thread(new ServerThread(socket, packet)).start();
            }
        } catch (UnknownHostException | SocketException e) {
            System.out.println("Error: can't create datagramSocket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: can't send datagramPacket: " + e.getMessage());
        }
    }

    private class ServerThread extends Thread {
        private DatagramPacket datagramPacket;
        private DatagramSocket datagramSocket;

        public ServerThread(DatagramSocket datagramSocket, DatagramPacket datagramPacket) {
            this.datagramSocket = datagramSocket;
            this.datagramPacket = datagramPacket;
        }

        public void run() {
            try {
                final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(datagramPacket.getData());
                final DataInputStream dais = new DataInputStream(byteArrayInputStream);
                final int pointCounter = dais.readInt();
                final float radius = dais.readFloat();
                final float _x = dais.readFloat();
                final float _y = dais.readFloat();
                CheckPointInArea checkPointInArea = new CheckPointInArea(radius);
                int answer = checkPointInArea.pointBelongToTheArea(new Point(_x, _y));

                System.out.println("point №" + pointCounter + " (x = " + _x + "; y = " + _y + ") radius = " + radius +
                        ((answer == 1) ? "; the point belong to the graph; " : "; the point doesn't belong to the graph;") + " " +
                        datagramPacket.getAddress() + " " + datagramPacket.getPort()
                );

                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

                dataOutputStream.writeInt(pointCounter);
                dataOutputStream.writeBoolean(answer == 1);
                dataOutputStream.close();

                final byte[] bytes = byteArrayOutputStream.toByteArray();

                datagramPacket.setData(bytes);
                datagramPacket.setLength(dataOutputStream.size());
                datagramPacket.setPort(datagramPacket.getPort());
                datagramSocket.send(datagramPacket);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
