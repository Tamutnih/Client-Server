package ru.geekbrains.chat.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private final Socket socket;    // Сокет - дескриптор для связи между клиентом и сервером по протоколу IP.
    private final String name;      // Имя клиента.

    /**
     * BufferedWriter — это класс из библиотеки Java, который используется для записи данных в поток байтов.
     * Он работает как буфер между приложением и физическим устройством, улучшая производительность системы.
     */
    private BufferedWriter bufferedWriter;

    /**
     * BufferedReader — это класс из библиотеки Java, предназначенный для чтения данных из потока байтов.
     */
    private BufferedReader bufferedReader;

    /**
     * Конструктор, создающий нового клиента, который подключается к серверу через указанный сокет (socket).
     * При этом создается поток чтения и поток записи для обмена данными между клиентом и сервером.
     * Если при создании потоков возникает ошибка, то закрываются все открытые ресурсы (closeEverything).
     *
     * @param socket   — дескриптор для связи между клиентом и сервером по протоколу IP.
     * @param userName — имя клиента.
     */
    public Client(Socket socket, String userName) {
        this.socket = socket;
        name = userName;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    /**
     * Слушатель для входящих сообщений
     */
    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;
                while (socket.isConnected()) {
                    try {
                        message = bufferedReader.readLine();
                        System.out.println(message);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    /**
     * Отправить сообщение
     */
    public void sendMessage() {
        try {
            bufferedWriter.write(name);                            // Отправка своего имени на сервер.
            bufferedWriter.newLine();                              // Отправка перехода на следующую строку.
            bufferedWriter.flush();                                // Вызов метода немедленной отправки сообщения.

            Scanner scanner = new Scanner(System.in);              // Создание объекта сканер.
            while (socket.isConnected()) {                         // Цикл ожидания и немедленной отправки сообщения,
                String message = scanner.nextLine();               // с указанием своего имени.

                if (message.startsWith("@")) {
                    int i = message.indexOf(' ');                  // Распарсим текст по пробелу.
                    String recipient = message.substring(0, i);    // Выделим адресат.
                    String text = message.substring(i);            // Выделим текст сообщения.
                    bufferedWriter.write(recipient + " " + name + ": " + text);
                } else {
                    bufferedWriter.write(name + ": " + message);
                }
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }



    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}