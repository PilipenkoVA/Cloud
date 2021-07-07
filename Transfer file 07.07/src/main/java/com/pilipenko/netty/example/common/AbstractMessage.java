package com.pilipenko.netty.example.common;

import java.io.Serializable;

public abstract class AbstractMessage implements Serializable {
}
// Создан для того чтобы сервер и клиент могли перекидываться любыми сообщениями межу собой