
package com.pilipenko.netty.example.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class Server {
    public void run() throws Exception {
        // Пул потоков для обработки подключений клиентов
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        // Пул потоков для обработки сетевых сообщений
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // Создание настроек сервера
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(mainGroup, workerGroup)
                    // для подключения будем использовать "NioServerSocketChannel"
                    .channel(NioServerSocketChannel.class)
                    // каждый раз как только кто-то подключился мы получаем ссылку на это соединение "SocketChannel"
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // после подключения для каждого клиента будет построен конвеер "pipeline" (addLast-позволяет добавлять последовательно)
                            socketChannel.pipeline().addLast(
                                    new ObjectDecoder(50 * 1024 * 1024, ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new MainHandler()
                            );
                        }
                    });

            ChannelFuture future = bootstrap.bind(8189).sync();
            System.out.println("Server started");
            // ждем завершение работы сервера чтобы попасть в блок "finally "
            future.channel().closeFuture().sync();
            System.out.println("Server finished");
        } finally {
            // завершаем работу потоков
            mainGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new Server().run();
    }
}
