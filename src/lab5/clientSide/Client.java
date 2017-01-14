package lab5.clientSide;

import java.net.*;
import java.io.*;

class Client {
    private final static int sizeOfPackage = 100;
    private DatagramSocket socket;

    Client() {
        try {
            socket = new DatagramSocket();
            System.out.println("The client-side is ready..." + socket.getLocalPort());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ServerAnswer getFromTheServer() {
        ServerAnswer serverAnswer = new ServerAnswer();
        try {
            for (; ; ) {
                DatagramPacket packet = new DatagramPacket(new byte[sizeOfPackage], sizeOfPackage);
                socket.receive(packet);

                final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
                final DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

                serverAnswer.pointCounter = dataInputStream.readInt();
                serverAnswer.stateOfPointsIs = dataInputStream.readBoolean();

                System.out.println("Received from: " + packet.getAddress() + ":" + 3456 + " answer: " + serverAnswer.stateOfPointsIs);

                return serverAnswer;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void sendPacket(StateOfPoints mw, float _radius) {
        float _x = mw.point.getX();
        float _y = mw.point.getY();
        try {

            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

            dataOutputStream.writeInt(mw.getId());
            dataOutputStream.writeFloat(_radius);
            dataOutputStream.writeFloat(_x);
            dataOutputStream.writeFloat(_y);
            dataOutputStream.close();
            final byte[] bytes = byteArrayOutputStream.toByteArray();

            DatagramPacket packet = new DatagramPacket(bytes, byteArrayOutputStream.size(), InetAddress.getByName("localhost"), 3456);
            socket.send(packet);

            System.out.println("Send to " + packet.getAddress() + " data: " + _x + " " + _y + " " + _radius);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}

