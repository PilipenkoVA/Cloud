package NioServer;

import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {
    public void run() throws Exception {
                   // Создаем пулы потоков
        EventLoopGroup bossGroup = new NioEventLoopGroup();                                 // созд. пул потоков для вход.подключений (подключ. клиенты)
        EventLoopGroup workerGroup = new NioEventLoopGroup();                               // созд. пул потоков для сетевой части (сетевая работа)

        try {   // Преднастройка работы сервера
            ServerBootstrap b = new ServerBootstrap();                                      // для этого создаем "Bootstrap"
            b.group(bossGroup, workerGroup)                                                 // для настройки отдаем ему два пул потока
                    .channel(NioServerSocketChannel.class)                                  // для того чтобы клиенты могли подключатся используем "NioServerSocketChannel"
                    .childHandler(new ChannelInitializer<SocketChannel>() {                 // при подключении клиента  мы его инициализируем и далее...
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {        // добавляем к нему "Handler"
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    });
//                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(8189).sync();                                   // задаем порт для сервера
            f.channel().closeFuture().sync();                                                // ожидаем завершение работы сервера
        } finally {
            workerGroup.shutdownGracefully();                                                // закрываем первый пул потоков "bossGroup"
            bossGroup.shutdownGracefully();                                                  // закрываем второй пул потоков "workerGroup"
        }
    }

    public static void main(String[] args) throws Exception {
        new DiscardServer().run();
    }
}
