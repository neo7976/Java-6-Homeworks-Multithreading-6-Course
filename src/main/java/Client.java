import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
//        String host = "127.0.0.1";
//        int port = 1254;
        String host1 = null;
        int port1 = 0;

        try (BufferedReader bf = new BufferedReader(new FileReader("src/main/resources/settings.txt"))) {
            String c;
            while ((c = bf.readLine()) != null) {
                if (c.contains("host")) {
                    String[] s = c.split(" ");
                    host1 = s[1];
                } else if (c.contains("port")) {
                    String[] s = c.split(" ");
                    port1 = Integer.parseInt(s[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("host подключения: " + host1);
        System.out.println("port подключения: " + port1);

        //при каждом подключении почему добавляется номер хоста и порта в настройки
        try (Socket client = new Socket(host1, port1);
             PrintWriter out = new PrintWriter(client.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("\nВведи свое имя для знакомства с сервером");
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


        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}
