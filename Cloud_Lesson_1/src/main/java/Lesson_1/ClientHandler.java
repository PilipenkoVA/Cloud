package Lesson_1;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        try (
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream())
        ) {
            System.out.printf("Lesson_1.Client %s connected\n", socket.getInetAddress());
            while (true) {
                String command = in.readUTF();
                if ("upload".equals(command)) {
                    try {
                        File file = new File("server"  + File.separator + in.readUTF());
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        FileOutputStream fos = new FileOutputStream(file);

                        long size = in.readLong();

                        byte[] buffer = new byte[8 * 1024];

                        // так сделано чтобы в цикл можно было зайти единственный раз,когда получаемый файл меньше размера буфера
                        for (int i = 0; i < (size + (buffer.length - 1)) / (buffer.length); i++) {
                            int read = in.read(buffer);
                            fos.write(buffer, 0, read);
                        }
                        System.out.println(command+" " + file.getName() + " completed");
                        out.writeUTF("Uploading " + file.getName() + " completed");
                        fos.close();
                        out.writeUTF("OK");
                    } catch (Exception e) {
                        out.writeUTF("FATAL ERROR");
                    }
                }

                if ("download".equals(command)) {
                    // TODO: 14.06.2021
                    try {
                        File file = new File("server"  + File.separator + in.readUTF());
                        if (file.exists()) {
                            out.writeUTF("exists");
                            long fileLength = file.length();
                            FileInputStream fis = new FileInputStream(file);
                            out.writeLong(fileLength);
                            int read = 0;
                            byte[] buffer = new byte[8 * 1024];
                            while ((read = fis.read(buffer)) != -1) {
                                out.write(buffer, 0, read);
                            }
                            out.flush();
                            String status = in.readUTF();
                            fis.close();
                            System.out.println("Downloading status: " + status);
                        }
                        else{
                            out.writeUTF("File " + file.getName() + " not found on a server");
                            System.out.println("File " + file.getName() + " not found");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if ("exit".equals(command)) {
                    System.out.printf("Lesson_1.Client %s disconnected correctly\n", socket.getInetAddress());
                    break;
                }

                System.out.println(command);
                out.writeUTF(command);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}