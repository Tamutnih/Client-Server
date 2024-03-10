package ru.geekbrains.chat.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Введите своё имя: ");
            // Укажем свое имя
            String name = scanner.nextLine();
            Socket socket = new Socket("localhost", 1400);  // Установка соединения с сервером.
            Client client = new Client(socket, name);                 // Создание "обертки" для клиента.
            InetAddress inetAddress = socket.getInetAddress();
            System.out.println("InetAddress: " + inetAddress);
            String remoteIp = inetAddress.getHostAddress();
            System.out.println("Remote IP: " + remoteIp);
            System.out.println("LocalPort: " + socket.getLocalPort());
            System.out.println("Если Вы желаете написать личное сообщение участнику беседы, " +
                    "введите @имя и далее через пробел с большой буквы нужный текст. ");
            System.out.println("Например, @Станислав Здравствуйте, Станислав!\n");


            client.listenForMessage();                                 // Инициализация потока на чтение данных.
            client.sendMessage();                                      // Инициализация процедуры, позволяющей
        }                                                              // осуществлять запись данных в поток сокета.
        catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}