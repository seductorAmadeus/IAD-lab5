package lab5.clientSide;

import java.net.*;
import java.io.*;

class Client {
    private final static int sizeOfPackage = 100;
    private DatagramSocket socket;

    Client() {
        try {
            socket = new DatagramSocket();
            System.out.println("____________________________\n" +
                    "Welcome, dear %username%!\n" +
                    "\tThe client-side is ready...\n" +
                    "\tLocal port = " + socket.getLocalPort() + "\n" +
                    "____________________________\n");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ServerAnswer getFromTheServer() {
        ServerAnswer serverAnswer = new ServerAnswer();
        try {
            while (true) {
                DatagramPacket packet = new DatagramPacket(new byte[sizeOfPackage], sizeOfPackage);
                socket.receive(packet);

                final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
                final DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

                serverAnswer.pointCounter = dataInputStream.readInt();
                serverAnswer.stateOfPointsIs = dataInputStream.readBoolean();

                System.out.println("Received from " + packet.getAddress() + ":" + 4718 + "\nAnswer = " + serverAnswer.stateOfPointsIs + "\n");

                return serverAnswer;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void sendPacket(StateOfPoints stateOfPoints, float _radius) {
        float _x = stateOfPoints.point.getX();
        float _y = stateOfPoints.point.getY();
        try {

            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

            dataOutputStream.writeInt(stateOfPoints.getId());
            dataOutputStream.writeFloat(_radius);
            dataOutputStream.writeFloat(_x);
            dataOutputStream.writeFloat(_y);
            dataOutputStream.close();
            final byte[] bytes = byteArrayOutputStream.toByteArray();

            DatagramPacket packet = new DatagramPacket(bytes, byteArrayOutputStream.size(), InetAddress.getByName("localhost"), 4718);
            socket.send(packet);

            System.out.println("Send to " + packet.getAddress() + "\nx = " + _x + " y = " + _y + " radius = " + _radius + "\n");
        } catch (Exception e) {
            e.getMessage();
        }
    }
}

