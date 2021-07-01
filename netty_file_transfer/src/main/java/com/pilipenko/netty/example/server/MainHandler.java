package com.pilipenko.netty.example.server;

import com.pilipenko.netty.example.common.FileMessage;
import com.pilipenko.netty.example.common.FileRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.file.Files;
import java.nio.file.Paths;

public class MainHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client: " + ctx.channel() + " connected");                                         // выводим данные о подключившимся клиенте в консоль
    }

    // ПОЛУЧЕНИЕ
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FileRequest) {                                                                // когда получаем от клиента "FileRequest"
            FileRequest fr = (FileRequest) msg;                                                          // делаем каст
            if (Files.exists(Paths.get("server_storage/" + fr.getFilename()))) {                    // если есть файл на сервере
                FileMessage fm = new FileMessage(Paths.get("server_storage/" + fr.getFilename()));  // формиуем "FileMessage" и отправляем клиенту
                System.out.println("Client download file: " + fr.getFilename());                         // вывод сообщения в консоль с именем скачаного файла
                ctx.writeAndFlush(fm);

            }
        }
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client: " + ctx.channel() + " disconnected");                                      // выводим данные о подключившимся клиенте в консоль
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
