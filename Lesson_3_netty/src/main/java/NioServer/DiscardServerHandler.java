package NioServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {        // наследуемся от входящего "InboundHandler"
                                                                                // в "Adapter" мы можем переопределить все что нам необходимо т.к. в нем есть стандартная реализация всех методов
      // ВАРИАНТ №1: это метод котрый срабатывает когда приходит хоть какое сообщение
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {         // "HandlerContext" дает всю информацию о соединении,а "Object" это само сообщение
//        System.out.println("Сообщение получил и освободил ресурсы");
//        ((ByteBuf) msg).release();                                            // делаем "release" т.е. освобождаем "Буфер"
//    }

    // НЕЗАБЫВАЕМ переопределять данный метод т.к. если сообщение не придет мы не сможем по другому узнать причину
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {   // если в процессе возникла ошибка
        cause.printStackTrace();                                                // мы печатаем причину ошибки
        ctx.close();                                                            // закрываем контекст, если не хотим общаться с клиентом
    }
    // ВАРИАНТ №2: это метод котрый срабатывает когда приходит хоть какое сообщение
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;                                             // т.к. этот "Handler" первый мы делаем каст к "ByteBuf"
        try {
            while (in.isReadable()) {                                           // до тех пор пака в "ByteBuf" есть данные
                System.out.print((char) in.readByte());                         // мы их вычитываем по байтово и печатаем в консоль
            }
        } finally {
            in.release();                                                       // после чтения всегда чистить "ByteBuf" т.к. после записи он чистится сам
        }
    }
}

// channelActive - событие когда клиент подключился
// channelInactive - событие когда клиент отвалился
// channelReadComplete - событие которое дает гаррантию что посылка прочитана до конца