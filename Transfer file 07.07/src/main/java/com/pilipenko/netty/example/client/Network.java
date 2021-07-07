package com.pilipenko.netty.example.client;

import com.pilipenko.netty.example.common.AbstractMessage;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.IOException;
import java.net.Socket;

public class Network {
    private static Socket socket;                   // "Socket" - соед. с сервером
    private static ObjectEncoderOutputStream out;   // исходящий поток для отправки объектов
    private static ObjectDecoderInputStream in;     // входящий поток для получения объектов

    // При старте
    public static void start() {
        try {
            socket = new Socket("localhost", 8188);                                           // подкл. к серверу
            out = new ObjectEncoderOutputStream(socket.getOutputStream());                              // созд. исходящий поток
            in = new ObjectDecoderInputStream(socket.getInputStream(), 50 * 1024 * 1024);  // созд. исходящий поток
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Метод для отправки любых сообщений на сервер
    public static boolean sendMsg(AbstractMessage msg) {
        try {
            out.writeObject(msg);                        // в исходящий поток отправляем "Message msg"
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    // Метод для получения объектов с сервера
    public static AbstractMessage readObject() throws ClassNotFoundException, IOException {
        Object obj = in.readObject();                     // блокирующая операция (постоянно находимся в ожидании)
        return (AbstractMessage) obj;
    }
}