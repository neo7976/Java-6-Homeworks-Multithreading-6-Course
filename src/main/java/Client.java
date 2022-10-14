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
//             BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()))) {

            System.out.println("Клиент подключился to socketClient.");
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nВведи свое имя для знакомства с сервером");
            setUserName(scanner.nextLine());
            out.println(">>К чату подключился: >>\"" + getUserName() + "\" [" + socketClient.getPort() + "]");
            out.flush();
            Thread.sleep(1000);
            if (in.read() > -1) {
                msgFromServer(in);
            }
            while (true) {
                // ждём консоли клиента на предмет появления в ней данных
                System.out.println("Введите сообщение:");
                String msg = scanner.nextLine();
                Thread.sleep(1000);

                // пишем данные с консоли в канал сокета для сервера
                out.printf(">>>%s[%s]: %s\n", getUserName(), socketClient.getPort(), msg);
                out.flush();
                System.out.println("Вы:" + msg);
                Thread.sleep(1000);

// ждём чтобы сервер успел прочесть сообщение из сокета и ответить
// проверяем условие выхода из соединения
                if (msg.equalsIgnoreCase("/end")) {
                    System.out.println("Client kill connections");
                    Thread.sleep(2000);
// смотрим что нам ответил сервер на последок перед закрытием ресурсов)
                    if (in.read() > -1) {
                        msgFromServer(in);
                    }
                    break;
                }

// если условие разъединения не достигнуто продолжаем работу
                System.out.println("Ждём ответа от сервера...");
                Thread.sleep(2000);
// проверяем, что нам ответит сервер на сообщение(за предоставленное ему время в паузе он должен был успеть ответить)
                if (in.read() > -1) {
                    msgFromServer(in);
                }
            }
// на выходе из цикла общения закрываем свои ресурсы
            System.out.println("Закрытие канала соединения - ВЫПОЛНЕНО.");

        } catch (
                IOException |
                        InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void msgFromServer(BufferedReader in) throws IOException {
        System.out.println("ожидание...");
        String msgServ = in.readLine();
        System.out.println(msgServ);
    }
}
