package com.pilipenko.netty.example.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

// СОЗДАН: для чтения с сервера

public class UnlimitedHandler extends ChannelInboundHandlerAdapter {
    private ByteBuf accumulator;                                                          // создаем "ByteBuf" аккумулятор

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBufAllocator allocator = ctx.alloc();
        accumulator = allocator.directBuffer(1024 * 1024 * 1, 5 * 1024 * 1024);     // выделяем память под аккумулятор
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {                      // когда к нам приходит сообщение
        ByteBuf input = (ByteBuf) msg;                                                    // мы получаем "ByteBuf"
        accumulator.writeBytes(input);                                                    // переливаем все байты в наш аккумулятор
        input.release();                                                                  // чистим буфер

        // ...
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("1.txt", true))) {// открываем "OutputStream"
            while (accumulator.readableBytes() > 0) {
                out.write(accumulator.readByte());                                         // сливаем данные в файл
            }
            accumulator.clear();                                                           // чистим аккумулятор

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
