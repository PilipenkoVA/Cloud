package client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {

    @FXML
    ListView<FileInfo> filesList;

    Path root;                                                                                              // ВВЕРХ: необходим для запоминания папки где мы находимся

    Path selectCopyFile;         // КОПИРОВАНИЕ
    Path selectMoveFile;         // ПЕРЕМЕЩЕНИЕ

    @FXML
    TextField pathField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        filesList.setCellFactory(new Callback<ListView<FileInfo>, ListCell<FileInfo>>() {
            @Override
            public ListCell<FileInfo> call(ListView<FileInfo> param) {
                return new ListCell<FileInfo>(){
                    // редактируем наш лист
                    @Override
                    protected void updateItem(FileInfo item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty){                                                         // "если ДАННЫХ НЕТ" и "item" пустой
                            setText(null);
                            setStyle("");
                        }else {                                                                             // "если ДАННЫЕ ЕСТЬ"
                            String formattedFilename = String.format("%-30s", item.getFileName());          // запрашиваем имя файла (ограничиваем его по количеству символов)
                            String formattedFilenght = String.format("%,d bytes", item.getLength());        // запрашиваем размер файла (дабавляем к нему пробел и слово "bytes")
                            if (item.getLength() == -1L){                                                   // если "файл равен -1L", то
                                formattedFilenght = String.format("%s", "[DIR]");                           // заменяем "-1L" на "[DIR]"
                            }
                            if (item.getLength() == -2L) {                                                   // ВВЕРХ: если "файл равен -2L", то
                                formattedFilenght = "";                                                      // ВВЕРХ: ничего не печатаем в лист
                            }
                            String text = String.format("%s %-20s", formattedFilename, formattedFilenght);  // созд "text" который включает в себя имя файла и размер
                            setText(text);                                                                  // выводим "text" в наш лист
                        }
                    }
                };
            }
        });
        // при запуске програмы сканируем папку "client.box"
        goToPath(Paths.get("client.box"));
    }
    // МЕТОД: Выход из программы
    public void btnExit(ActionEvent actionEvent) {
        Platform.exit();
    }
    // МЕТОД: перехода
    public void goToPath(Path path){                                                                        // когда мы переходим куда-то
        root = path;                                                                                        // "root" запоминает путь
        pathField.setText(root.toAbsolutePath().toString());                                                // в "pathField" прописывается полный путь
        // ДАЛЕЕ список файлов обновляется на соответствующую папку
        filesList.getItems().clear();                                                                       // перед заполнением чистим коллекцию
        filesList.getItems().add(new FileInfo(FileInfo.UP_TOKEN, -2L));                              // ВВЕРХ: переход на уровень вверх
        filesList.getItems().addAll(scanFiles(path));                                                       // когда перешли (заполняем колекцию файлов)
    }

    // МЕТОД: возвращаяющий из корневого каталога лист "FileInfo" после чего мы его добавим в "ListView"
    public List<FileInfo> scanFiles(Path root){
        try {
            return Files.list(root).map(FileInfo::new).collect(Collectors.toList());
        }catch (IOException e){
            throw new RuntimeException("Files scan exception: "+root);
        }

    }
    // МЕТОД: сортировки по размеру
    public void sortSizeFiles(Path path){
        filesList.getItems().sort(new Comparator<FileInfo>() {
            @Override
            public int compare(FileInfo o1, FileInfo o2) {
                if (o1.getFileName().equals(FileInfo.UP_TOKEN)){                                             // ВВЕРХ: сортровка
                    return -1;
                }
                if ((int) Math.signum(o1.getLength()) == (int)Math.signum(o2.getLength())){
                    return o1.getFileName().compareTo(o2.getFileName());
                }
                return new Long(o1.getLength() - o2.getLength()).intValue();
            }
        });
    }

    // МЕТОД: сортировка по имени файла
    public void sortNameFiles(Path path){
        filesList.getItems().sort(new Comparator<FileInfo>() {

            @Override
            public int compare(FileInfo o1, FileInfo o2) {
                return o1.getFileName().compareTo(o2.getFileName());
            }
        });
    }

    // МЕТОД: отслеживание событий "ДВОЙНОЙ КЛИК" по "ListView"
    public void filesListClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2){
            FileInfo fileInfo = filesList.getSelectionModel().getSelectedItem();
            if (fileInfo != null){
                if (fileInfo.isDirectory()){                        // если файл является "Directory"
                    Path pathTo = root.resolve(fileInfo.getFileName());
                    goToPath(pathTo);
                }
                if (fileInfo.isUpElement()){                        // ВВЕРХ: если файл является "элементом для перехода вверх"
                    Path pathTo = root.toAbsolutePath().getParent();
                    goToPath(pathTo);
                }
            }
        }
    }
    // МЕТОД: обновляем список файлов в текущем каталоге
    public void refresh(){
        goToPath(root);
    }

    // МЕТОД: копирование
    public void copyAction(ActionEvent actionEvent) {                                                                            // КОПИРОВАНИЕ
        FileInfo fileInfo = filesList.getSelectionModel().getSelectedItem();                                                    // получаем "FileInfo"
        if (selectCopyFile == null && (fileInfo == null || fileInfo.isDirectory() || fileInfo.isUpElement())){
            return;
        }
        if (selectCopyFile == null){                                                                                            // начало копирования
            selectCopyFile = root.resolve(fileInfo.getFileName());                                                              // копируем файл
            ((Button) actionEvent.getSource()).setText("Копируется: "+ fileInfo.getFileName());                                 // меняем название кнопки
            return;
        }
        if (selectCopyFile != null){                                                                                            // если файл уже скопировали
            try {
                Files.copy(selectCopyFile, root.resolve(selectCopyFile.getFileName()), StandardCopyOption.REPLACE_EXISTING);    // добавляем файл в место назначение
                selectCopyFile = null;                                                                                          // обнуляемся
                ((Button) actionEvent.getSource()).setText("Копирование");                                                      // возвращаем название кнопке
                refresh();                                                                                                      // обновляем наш лист
            } catch (IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Невозможно скопировать файл", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    // МЕТОД: перемещение
    public void moveAction(ActionEvent actionEvent) {                                                                            // КОПИРОВАНИЕ
        FileInfo fileInfo = filesList.getSelectionModel().getSelectedItem();                                                    // получаем "FileInfo"
        if (selectMoveFile == null && (fileInfo == null || fileInfo.isDirectory() || fileInfo.isUpElement())){
            return;
        }
        if (selectMoveFile == null){                                                                                            // начало копирования
            selectMoveFile = root.resolve(fileInfo.getFileName());                                                              // копируем файл
            ((Button) actionEvent.getSource()).setText("Перемещается: "+ fileInfo.getFileName());                                 // меняем название кнопки
            return;
        }
        if (selectMoveFile != null){                                                                                            // если файл уже скопировали
            try {
                Files.move(selectMoveFile, root.resolve(selectMoveFile.getFileName()), StandardCopyOption.REPLACE_EXISTING);    // добавляем файл в место назначение
                selectMoveFile = null;                                                                                          // обнуляемся
                ((Button) actionEvent.getSource()).setText("Перемещение");                                                      // возвращаем название кнопке
                refresh();                                                                                                      // обновляем наш лист
            } catch (IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Невозможно переместить файл", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }
    // МЕТОД: удаление
    public void deleteAction(ActionEvent actionEvent) {
        FileInfo fileInfo = filesList.getSelectionModel().getSelectedItem();                                                    // получаем "FileInfo"
        if (fileInfo == null || fileInfo.isDirectory() || fileInfo.isUpElement()){
            return;
        }
        try {
            Files.delete(root.resolve(fileInfo.getFileName()));                                                                 // удаляем файл
            refresh();                                                                                                          // обновляем наш лист
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Невозможно удалить выбранный файл", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
