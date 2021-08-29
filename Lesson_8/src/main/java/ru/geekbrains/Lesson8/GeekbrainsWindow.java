package ru.geekbrains.Lesson8;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GeekbrainsWindow extends JFrame {
    private int randomNumber;
    private JTextField textFieldOne;
    private JTextField textFieldTwo;
    private int game = 3;

    public GeekbrainsWindow() {

        // Создаем окно
        setTitle("Игра: Угадай число");
        setBounds(600, 300, 600, 160);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Запрет на изменение размера окна
        setResizable(false);

        // Создаем основное однострочное текстовое поле
        textFieldOne = new JTextField();
        // Добавляем его в наше окно
        add(textFieldOne, BorderLayout.NORTH);

        // Создаем тип и размер шрифта
        Font font = new Font("Arial", Font.PLAIN, 20);
        // Добавляем его в наше текстовое поле "textField"
        textFieldOne.setFont(font);

        // Создаем панель для дополнительного поля и кнопки пеезапуска игры
        JPanel buttonsPanel1 = new JPanel(new GridLayout(1, 2));
        buttonsPanel1.setPreferredSize(new Dimension(1, 30));
        add(buttonsPanel1, BorderLayout.SOUTH);

        // Создаем дополнительное поле
        textFieldTwo = new JTextField();
        textFieldTwo.setFont(font);
        buttonsPanel1.add(textFieldTwo);

        // Создаем кнопку перезапуска игры
        JButton buttonRestart = new JButton("Перезапуск игры");
        buttonRestart.setFont(font);
        buttonsPanel1.add(buttonRestart);
        buttonRestart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });

        this.randomNumber = (int) (Math.random() * 10) + 1; // [1, 10]
        textFieldOne.setText("Программа загадала число от 1 до 10");
        textFieldOne.setEditable(false);
        // Выравниваем текст по центру
        textFieldOne.setHorizontalAlignment(SwingConstants.CENTER);
        textFieldTwo.setText("У вас " + game + " попытки");


        // Создаем "buttonsPanel" с 10 кнопками в 1 ряд
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 10));
        // Выделяем ее синим цветом
        buttonsPanel.setBackground(Color.BLUE);
        // Добавляем ее в наше окно
        add(buttonsPanel, BorderLayout.CENTER);

        // Создаем и добавляем при помощи цикла кнопки на "buttonsPanel"
        for (int i = 1; i <= 10; i++) {
            JButton button = new JButton(String.valueOf(i));
            button.setFont(font);
            buttonsPanel.add(button);
            int buttonIndex = i;
            // Вешаем действие на кнопки (где каждая кнопка соответствует своему индексу)
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tryToAnswer(buttonIndex);
                    if (game  <= 0){
                        textFieldTwo.setText("Игра окончена.");
                        return;
                    }
                    textFieldTwo.setText("У вас осталось " + game + " попытки.");
                    return;
                }
            });
        }
        setVisible(true);
    }

    public void tryToAnswer(int answer) {
        // С каждым ответом количество попыток будет уменьшаться
        while (game > 0){
            if(answer < randomNumber) {
                textFieldOne.setText("Не угадали! Загаданное число больше!");
                game --;
                return;
            }
            if(answer > randomNumber) {
                textFieldOne.setText("Не угадали! Загаданное число меньше.");
                game --;
                return;
            }
            textFieldOne.setText("Вы угадали!!! Ответ: " + randomNumber);
            game = 0;
            return;
        }
    }

    // МЕТОД: для перезапуска игры и обнуления счетчика попыток
    public void restartGame(){
        game = 3;
        new  GeekbrainsWindow();
    }
}

