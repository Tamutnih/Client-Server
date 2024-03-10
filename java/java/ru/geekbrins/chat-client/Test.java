package ru.geekbrains.chat.client;

public class Test {
    public static void main(String[] args) {
        String name = "Ford";
        String message = "@Mickle Hello, friend!";
        p(message, name);


    }

    public static void p(String message, String name) {
        int i = message.indexOf(' ');
        String recipient = message.substring(0, i);
        String text = message.substring(i);
        System.out.println(recipient + " " + name + ": " + text);

    }
}