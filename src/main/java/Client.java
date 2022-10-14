import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static String userName = "Аноним";

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        Client.userName = userName;
    }

    public static void main(String[] args) {
        MyLogger myLogger = MyLogger.getInstance();
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
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//             DataOutputStream oos = new DataOutputStream(socketClient.getOutputStream());
             PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
//             DataInputStream ois = new DataInputStream(socketClient.getInputStream()))
             BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()))) {

            System.out.println("Клиент подключился to socketClient.");
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nВведи свое имя для знакомства с сервером");
            String name = scanner.nextLine();

            out.println(name);
            out.flush();
            Thread.sleep(1000);
            if (in.read() > -1) {
                System.out.println("reading...");
//                            String msgServ= ois.readUTF();
                String msgServ = in.readLine();
                System.out.println(msgServ);
            }
            // проверяем живой ли канал и работаем если живой
//            while (!socketClient.isOutputShutdown()) {
            while (true) {
                // ждём консоли клиента на предмет появления в ней данных
                System.out.println("Введите команду");
                String msg = scanner.nextLine();
                // данные появились - работаем
                System.out.println("Client start writing in channel...");
                Thread.sleep(1000);

                // пишем данные с консоли в канал сокета для сервера
//                    oos.writeUTF(clientCommand);
//                    oos.flush();
                out.println(msg);
                out.flush();
                System.out.println("Clien sent message " + msg + " to server.");
                Thread.sleep(1000);


// ждём чтобы сервер успел прочесть сообщение из сокета и ответить

// проверяем условие выхода из соединения
            if (msg.equalsIgnoreCase("/end")) {

// если условие выхода достигнуто разъединяемся
                System.out.println("Client kill connections");
                Thread.sleep(2000);

// смотрим что нам ответил сервер на последок перед закрытием ресурсов)
                if (in.read() > -1) {
                    System.out.println("reading...");
//                            String msgServ= ois.readUTF();
                    String msgServ = in.readLine();
                    System.out.println(msgServ);
                }
// после предварительных приготовлений выходим из цикла записи чтения
                break;
            }

// если условие разъединения не достигнуто продолжаем работу
            System.out.println("Client sent message & start waiting for data from server...");
            Thread.sleep(2000);

            // проверяем, что нам ответит сервер на сообщение(за предоставленное ему время в паузе он должен был успеть ответить)
            if (in.read() > -1) {
                System.out.println("reading...");
//                            String msgServ= ois.readUTF();
                String msgServ = in.readLine();
                System.out.println(msgServ);
            }
        }
// на выходе из цикла общения закрываем свои ресурсы
        System.out.println("Closing connections & channels on clentSide - DONE.");


//        try (Socket client = new Socket(host1, port1);
//             PrintWriter out = new PrintWriter(client.getOutputStream(), true);
//             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
//
//            Scanner scanner = new Scanner(System.in);
//            System.out.println("\nВведи свое имя для знакомства с сервером");
//            setUserName(scanner.nextLine());
//            out.println(getUserName());
//            System.out.println(in.readLine());
//
//            while (true) {
//                System.out.println("Введите сообщение или \"end\" для завершения сеанса");
//                String msg = scanner.nextLine();
//                if (msg.toLowerCase().equals("end"))
//                    break;
//                out.println(msg);
//            }
//            System.out.println("Вы вышли из чата!");
//
//            //логер начинает с 1
////            System.out.println(logger.log("Вы вышли из чата!"));


    } catch(
    IOException |
    InterruptedException e)

    {
        e.printStackTrace();
    }
}
}
