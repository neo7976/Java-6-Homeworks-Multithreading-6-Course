import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 1254;

        try (Socket client = new Socket(host, port);
             //скорее всего надо переписать откуда считывать и куда записывать данные
             PrintWriter out = new PrintWriter(client.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("Введи свое имя для знакомства с сервером");
            String name = scanner.nextLine();
            out.println(name);
            System.out.println(in.readLine());

            while (true) {
                System.out.println("Введите сообщение или \"end\" для завершения сеанса");
                String msg = scanner.nextLine();
                if (msg.toLowerCase().equals("end"))
                    break;
                out.println(msg);
            }
            System.out.println("Вы вышли из чата!");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
