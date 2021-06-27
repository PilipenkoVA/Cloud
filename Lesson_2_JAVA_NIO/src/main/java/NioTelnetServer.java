import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class NioTelnetServer {
    private static final String LS_COMMAND = "\tls     view all files from current directory";
    private static final String MKDIR_COMMAND = "\tmkdir     view all files from current directory";
    private static final String TOUCH_COMMAND = "\ttouch     создание файла";                           // <-- добавил команду
    private static final String CD_COMMAND = "\tcd     изменение текущего положения";                   // <-- добавил команду
    private static final String RM_COMMAND = "\trm     удаление файла / директории";                    // <-- добавил команду
    private static final String COPY_COMMAND = "\tcopy     копирование файлов / директории";            // <-- добавил команду
    private static final String CAT_COMMAND = "\tcat     вывод содержимого текстового файла";           // <-- добавил команду
    private static final String CHANGENICK_COMMAND = "\t     изменение имени пользователя";             // <-- добавил команду
    private final String ROOT_DIR = "server";


    private final ByteBuffer buffer = ByteBuffer.allocate(512);                     // создаем рабочий буфер размером 512
    private int acceptedClientIndex = 1;                                            // <--  подлючившийся клиент будет иметь индекс равный 1

    private Map<SocketAddress, String> clients = new HashMap<>();                   // для созранения пдключаемых клиентов
    private Map<SocketAddress, Path> currentPaths = new HashMap<>();

    public NioTelnetServer() throws Exception {
        ServerSocketChannel server = ServerSocketChannel.open();                    // создаем сервер Socket канал
        server.bind(new InetSocketAddress(5681));                              // говорим что он должен работать на порту 5681 "bind" привязывает сервер к порту
        server.configureBlocking(false);                                            // должен работать в неблокирующем режиме
        Selector selector = Selector.open();                                        // создаем "Selector" это слушатель который будет прослушивать разные события

        server.register(selector, SelectionKey.OP_ACCEPT);                          // говорим Socket каналу зарегистрироватся на селекторе (чтобы события обрабатывались программой) а так же перехватывал события когда клиент подключается
        System.out.println("Server started");                                       // приветственное сообщение от "Server"

        // если нам придет 10 событий мыих все обработаем по очереди и далее будем ждать новых событий
        while (server.isOpen()) {
            selector.select();                                                      // блокирующая операция (ждем какого либо события (подключения, или пока кто либо не напишет))
            Set<SelectionKey> selectionKeys = selector.selectedKeys();              // информация о событии (формируем все события в список)
            Iterator<SelectionKey> iterator = selectionKeys.iterator();             // как только произошло подключение или пришло сообщение мы получаем "Iterator" по списку этих событий
            while (iterator.hasNext()) {                                            // перебираем по очереди все события
                SelectionKey key = iterator.next();                                 // вытаскиваем каждое событие
                if (key.isAcceptable()) {                                           // если событие "Acceptable" т.е. о подключившимся клиенте
                    handleAccept(key, selector);                                    // то обрабатывает событие как подключение
                } else if (key.isReadable()) {                                      // если событие "Readable" т.е. для чтения
                    handleRead(key, selector);                                      // то обрабатываем как чтение
                }
                iterator.remove();                                                  // выкидываем его из исписка
            }
        }
    }
    // если событие "Acceptable" т.е. к нам кто-то подключился то
    private void handleAccept(SelectionKey key, Selector selector) throws IOException {   // мы получаем ссылку на "SelectionKey" этого события
        SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();           // из события выдергиваем ссылку на "SocketChannel"
        String clientName = "Клиент #" + acceptedClientIndex;                             // <--  даем клиенту имя
        InetSocketAddress remoteAddress = (InetSocketAddress) channel.getRemoteAddress();
        String hostName = remoteAddress.getHostName();
        clients.put(remoteAddress, hostName);
        acceptedClientIndex++;                                                            // <-- каждому послежующему +1
        channel.configureBlocking(false);                                                 // говорим что будем работать в не блокируущем режиме
        System.out.println("Client connected. IP: " + remoteAddress.getAddress());
        channel.register(selector, SelectionKey.OP_READ, clientName);                     // этого клиента регистрируем на селекторе и перехватываем все события которые необходимо прочитать и в качечестве дополнительной информации отдаем имя клиента
        String helloMessage = "Hello user " + hostName + "!" + System.lineSeparator();
        channel.write(ByteBuffer.wrap(helloMessage.getBytes(StandardCharsets.UTF_8)));
        channel.write(ByteBuffer.wrap("Enter --help for support info".getBytes(StandardCharsets.UTF_8))); // приветственное сообщение №2
        Path userDir = Paths.get(ROOT_DIR + File.separator, hostName);
        if (!Files.exists(userDir)) {
            Files.createDirectory(userDir);
        }
        currentPaths.put(remoteAddress, userDir);
        printInviteString(channel);
    }

    private void printInviteString(SocketChannel channel) throws IOException {
        InetSocketAddress remoteAddress = (InetSocketAddress) channel.getRemoteAddress();
        String nickname = clients.get(remoteAddress);
        String inviteString = System.lineSeparator() + nickname + " " + pathStringForClient(remoteAddress) + ">";
        channel.write(ByteBuffer.wrap(inviteString.getBytes(StandardCharsets.UTF_8)));
    }

    private String pathStringForClient(InetSocketAddress remoteAddress) {
        StringBuilder sb = new StringBuilder();
        sb.append("~\\");
        Path currentPath = currentPaths.get(remoteAddress);
        Iterator<Path> iterator = currentPath.iterator();
        while(iterator.hasNext()){
            Path path = iterator.next();
            if ("server".equals(path.toString())) {
                continue;
            } else if (remoteAddress.getHostName().equals(path.toString())) {
                continue;
            } else {
                sb.append(path.toString() + "\\");
            }
        }
        return sb.toString();
    }

    // если событие "Readable" т.е. клиент что-то прислал для чтения
    private void handleRead(SelectionKey key, Selector selector) throws IOException {     // обрабатываем как сообщение для чтения
        SocketChannel channel = (SocketChannel) key.channel();                            // получаем ссылку о том кто нам это прислал
        SocketAddress client = channel.getRemoteAddress();
        int readBytes = channel.read(buffer);                                             // считываем все сообщения в буфер

        StringBuilder sb = new StringBuilder();                                           // создаем "StringBuilder"

        String msg;                                                                       // <-- созд. строку для сообщения
        if (readBytes < 0) {                                                              // если ни чего не прочитали то
            msg = key.attachment() + " disconnect\n";                                     // <-- начит клиент отвалился выводим сообщение
            channel.close();                                                              // закрываем канал
            return;
        } else  if (readBytes == 0) {
            return;
        }else {
            msg = key.attachment() + ": " + sb.toString();                                // <-- если прочитали что-то полезное по выводим текст сообщения
        }
        System.out.println(msg);                                                          // <-- выводим сообщение в консоль

        buffer.flip();                                                                    // запускаем режим чтения "buffer"
        while (buffer.hasRemaining()) {                                                   // считываем все из "buffer"
            sb.append((char) buffer.get());                                               // перегоняем сообщения любой длины в "StringBuilder"
        }
        buffer.clear();                                                                   // чистим "buffer"

        // TODO: 21.06.2021
        // touch (filename) - создание файла
        // mkdir (dirname) - создание директории
        // cd (path | ~ | ..) - изменение текущего положения
        // rm (filename / dirname) - удаление файла / директории
        // copy (src) (target) - копирование файлов / директории
        // cat (filename) - вывод содержимого текстового файла
        // changenick (nickname) - изменение имени пользователя

        // добавить имя клиента

        if (key.isValid()) {
            String[] command = sb.toString()
                    .replace("\n", "")                             // заменяем невидимые файлы которые может докидывать консоль
                    .replace("\r", "").split(" ");


            if ("--help".equals(command)) {
                sendMessage(LS_COMMAND, selector, client);
                sendMessage(MKDIR_COMMAND, selector, client);
                sendMessage(TOUCH_COMMAND, selector, client);
                sendMessage(CD_COMMAND, selector, client);
                sendMessage(RM_COMMAND, selector, client);
                sendMessage(COPY_COMMAND, selector, client);
                sendMessage(CAT_COMMAND, selector, client);
                sendMessage(CHANGENICK_COMMAND, selector, client);
                printInviteString(channel);
            } else if ("ls".equals(command[0])) {
                sendMessage(getFilesList(client).concat(System.lineSeparator()), selector, client);
                printInviteString(channel);
            } else if ("changenick".equals(command[0])) {
                if (command.length < 2) {
                    return;
                }
                clients.put(channel.getRemoteAddress(), command[1]);
                printInviteString(channel);
            } else if ("cd".equals(command[0])) {
                if (command.length < 2) {
                    return;
                }
                if (!changeCurrentDir(client, command[1])) {
                    sendMessage("Directory doesn't exist", selector, client);
                }
                printInviteString(channel);
            } else if ("touch".equals(command[0])) {
                if (command.length < 2) {
                    return;
                }
                if (!createFile(client, command[1], true)) {
                    sendMessage("File is already exist", selector, client);
                }
                printInviteString(channel);
            } else if ("mkdir".equals(command[0])) {
                if (command.length < 2) {
                    return;
                }
                if (!createFile(client, command[1], false)) {
                    sendMessage("Directory is already exist", selector, client);
                }
                printInviteString(channel);
            } else if ("rm".equals(command[0])) {
                if (command.length < 2) {
                    return;
                }
                deleteFileOrDirectory(client, command[1], selector);
                printInviteString(channel);
            } else if ("copy".equals(command[0])) {
                if (command.length < 3) {
                    return;
                }
                copyFileOrDirectory(client, command[1], command[2], selector);
                printInviteString(channel);
            } else if ("cat".equals(command[0])) {
                if (command.length < 2) {
                    return;
                }
                printTextFile(client, command[1], selector);
                printInviteString(channel);
            }
        }
    }

    private void printTextFile(SocketAddress client, String filename, Selector selector) throws IOException {
        Path path = getPathToFile(client, filename);
        if (Files.isDirectory(path)) {
            sendMessage("Can't read directory", selector, client);
            return;
        }
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        for (String line : lines) {
            sendMessage(line, selector, client);
            sendMessage(System.lineSeparator(), selector, client);
        }

    }

    private boolean createFile(SocketAddress client, String filename, boolean isFile) throws IOException {
        Path path = getPathToFile(client, filename);
        if (!Files.exists(path)) {
            if (isFile) {
                Files.createFile(path);
            } else {
                Files.createDirectories(path);
            }
            return true;
        }
        return false;
    }

    private void deleteFileOrDirectory(SocketAddress client, String filename, Selector selector) throws IOException {
        Path path = getPathToFile(client, filename);
        if (!Files.exists(path)) {
            sendMessage("File or directory doesn't exist", selector, client);
            return;
        }
        if (Files.isDirectory(path)) {
            String[] siblings = new File(path.toString()).list();
            if (siblings.length > 0) {
                sendMessage("Directory is not empty", selector, client);
                return;
            }
        }
        Files.delete(path);
    }

    private void copyFileOrDirectory(SocketAddress client, String source, String target, Selector selector) throws IOException {
        Path pathToSource = getPathToFile(client, source);
        if (!Files.exists(pathToSource)) {
            sendMessage("Source file doesn't exist", selector, client);
            return;
        }
        Path pathToTarget = getPathToFile(client, target);

        if (Files.isDirectory(pathToSource)) {
            if (!Files.exists(pathToTarget)) {
                Files.createDirectories(pathToTarget);
            }
            Files.walkFileTree(pathToSource, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Files.copy(dir, pathToTarget.resolve(dir.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.copy(file, pathToTarget.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            Files.copy(pathToSource, pathToTarget, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private boolean changeCurrentDir(SocketAddress client, String newDir) {
        StringBuilder sb = getDefaultPathForClient(client);
        if ("~".equals(newDir)) {
            currentPaths.put(client, Path.of(sb.toString()));
            return true;
        } else if ("..".equals(newDir)) {
            Path currentPath = currentPaths.get(client);
            if (!currentPath.equals(Path.of(sb.toString()))) {
                currentPaths.put(client, currentPath.getParent());
            }
            return true;
        } else {
            String[] dirs = newDir.split("\\\\");
            for (String dir : dirs) {
                sb.append(dir + "\\\\");
            }
        }
        Path newPath = Path.of(sb.toString());
        if (Files.exists(newPath)) {
            currentPaths.put(client, newPath);
            return true;
        } else {
            return false;
        }
    }

    private void sendMessage(String message, Selector selector, SocketAddress clientName) throws IOException {
        for (SelectionKey key : selector.keys()) {                                                                      // перебираем записи всех зарегистрированных на селекторе клиентов
            if (key.isValid() && key.channel() instanceof SocketChannel) {                                              // если зарег. каналы это "SocketChannel" т.е. обычные клиенты
                if (((SocketChannel) key.channel()).getRemoteAddress().equals(clientName)) {                            // проверяем что "SocketChannel" действительно принадлежит определенному клиенту
                    ((SocketChannel) key.channel()).write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));   // получаем ссылку на канал и отправляем сообщение (команду)
                }
            }
        }
    }

    private String getFilesList(SocketAddress client) {
        Path currentDir = currentPaths.get(client);
        String[] servers = new File(currentDir.toString()).list();
        return String.join(" ", servers);                                                                       // все файлы помещаем в лист через разделитель и отправляем их клиенту т.к. команда "ls"
    }
    private StringBuilder getDefaultPathForClient(SocketAddress client) {
        StringBuilder sb = new StringBuilder();
        sb.append("server\\" + ((InetSocketAddress)client).getHostName() + "\\\\");
        return sb;
    }

    private Path getPathToFile(SocketAddress client, String fileName) {
        Path path;
        String[] pathList = fileName.split("\\\\");
        if (pathList.length == 1) {
            path = Path.of(currentPaths.get(client).toString(), fileName);
        } else {
            StringBuilder sb = getDefaultPathForClient(client);
            for (String dir : pathList) {
                sb.append(dir + "\\\\");
            }
            path = Path.of(sb.toString());
        }
        return path;
    }

    public static void main(String[] args) throws Exception {
        new NioTelnetServer();
    }
}

