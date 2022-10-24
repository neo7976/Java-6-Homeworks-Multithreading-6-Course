import thread.ThreadReadMessage;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static String userName = "Аноним";

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        if (!userName.trim().isEmpty())
            Client.userName = userName;
    }

    public static void main(String[] args) {
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

        try (Socket socketClient = new Socket(host1, port1);
             PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()))) {
            System.out.println("Клиент подключился to socketClient.");

            Scanner scanner = new Scanner(System.in);
            System.out.println("\nВведите свое имя для знакомства с сервером!\n" +
                    "После вводите сообщения для отправки пользователям или /end для выхода из канала:");
            setUserName(scanner.nextLine());
            out.println(getUserName());
            out.flush();
            Thread.sleep(1000);

            //Поток для чтения новых сообщений
            ThreadReadMessage send = new ThreadReadMessage();
            send.start();

            while (true) {
                // ждём консоли клиента на предмет появления в ней данных
                String msg = scanner.nextLine();
                out.println(msg);
                out.flush();

                if (msg.equalsIgnoreCase("/end")) {
                    if (in.read() > -1) {
                        msgFromServer(in);
                    }
                    break;
                }
                if (in.read() > -1) {
                    msgFromServer(in);
                }
            }
            send.interrupt();
            System.out.println("Закрытие канала соединения - ВЫПОЛНЕНО.");

        } catch (
                IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void msgFromServer(BufferedReader in) throws IOException {
        String msgServ = in.readLine();
    }
}
