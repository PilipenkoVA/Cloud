package client;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PanelController implements Initializable {
    @FXML
    TableView filesTable;               // соединяем "panel.fxml" файл с "контроллером"

    @FXML
    ComboBox<String> diskBox;           // при запуске будет производится (инициализация панели)

    @FXML
    TextField pathField;                // необходим для вывода информации о пути файла

    // ПЕРЕОПРЕДЕЛЯЕМ МЕТОД
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // СОЗДАЕМ: колонку
        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>();
        fileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeColumn.setPrefWidth(24);

        // СОЗДАЕМ: колонку "Имя"
        TableColumn<FileInfo, String> fileNameColumn = new TableColumn<>("Имя");
        fileNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFilename()));
        fileNameColumn.setPrefWidth(120);

        // СОЗДАЕМ: колонку "Размер"
        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Размер");
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumn.setPrefWidth(120);

        // ОТОБРАЖЕНИЕ ДАННЫХ: в колонке "Размер"
        fileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileInfo, Long>(){

                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty){                              // если данные = null или отсутствуют
                        setText(null);
                        setStyle("");
                    }else {
                        String text = String.format("%,d bytes", item);     // добавляем разделитель и слово "bytes" к тексту
                        if (item == -1L){                                   // если размер = -1L, то
                            text = "[DIR]";                                 // меняем на слово "[DIR]"
                        }
                        setText(text);
                    }
                }
            };
        });

        // СОЗДАЕМ: колонку "Дата изменения"

        // создаем собственный формат даты
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        TableColumn<FileInfo, String> fileDataColumn = new TableColumn<>("Дата изменения");
        fileDataColumn.setCellValueFactory(param -> new SimpleObjectProperty(param.getValue().getLastModified().format(dtf)));
        fileDataColumn.setPrefWidth(150);

        // ДОБАВЛЯЕМ НА ПАНЕЛЬ: созданые выше колонки
        filesTable.getColumns().addAll(fileTypeColumn, fileNameColumn, fileSizeColumn, fileDataColumn);

        // СОРТИРОВКА:
        filesTable.getSortOrder().add(fileTypeColumn);                                // для сортировке использовать первую колонку

        diskBox.getItems().clear();                                                   // очищаем "diskBox"
        // НЕОБХОДИМО ПЕРЕДЕЛАТЬ ПОД ПАПКИ "client" и "server"
        for (Path p : FileSystems.getDefault().getRootDirectories()){                 // используем корневую директорию
            diskBox.getItems().add(p.toString());
        }
        diskBox.getSelectionModel().select(0);

        // СОБЫТИЕ НА КЛИК МЫШКОЙ
//        filesTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                if (event.getClickCount() == 2){
//                    Path path = Paths.get(pathField.getText()).resolve(filesTable.getSelectionModel().getSelectedItem().getFilename());
//                    if (Files.isDirectory(path)){
//                        updateLis(path);
//                    }
//                }
//            }
//        });

        // ПУТЬ:
        updateLis(Paths.get("."));                                               // указываем путь к директории которая будет в таблице
    }

    // МЕТОД: позволяет собрать список файлов из либой директории и добавть из в таблицу
    public void updateLis(Path path){
        try {
            pathField.setText(path.normalize().toAbsolutePath().toString());          // выводим в поле "pathField" путь файла
            filesTable.getItems().clear();                                            // чистим список

            // берем любую папку и преобразуем к списку "FileInfo"
            filesTable.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList())); // получаем ссылку на список
            filesTable.sort();

        } catch (IOException e) {
            // если что-то пойдет не так при обновлении списка у нас появится окно с кнопкой "ОК" и предупреждением
            // для этого создаем объект типа "Alert"
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось обновить список файлов", ButtonType.OK);
            alert.showAndWait();                                            // показать окно
        }
    }
    // МЕТОД: вешаем действие на кнопку для перехода вверх
    public void btnUpPathAction(ActionEvent actionEvent) {
        Path upperPath = Paths.get(pathField.getText()).getParent();
        if (upperPath != null){
            updateLis(upperPath);
        }
    }
    // МЕТОД: для перехода на другой диск
    public void selectDiskAction(ActionEvent actionEvent) {
        ComboBox<String> element = (ComboBox<String>) actionEvent.getSource();
        updateLis(Paths.get(element.getSelectionModel().getSelectedItem()));
    }
    // получение имени файла ?????
//    public String getSelectFileName() {
//        if (!filesTable.isFocused()) {
//            return null;
//        }
//        return filesTable.getSelectionModel().getSelectedItem().getFilename;
//    }

    // куда указывает панель (в какую папку)
    public String getCurrentPath(){
        return pathField.getText();
    }
}
