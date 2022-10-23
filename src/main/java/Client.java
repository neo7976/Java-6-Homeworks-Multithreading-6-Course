import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static String userName = "Аноним";
    private static int count = -1;

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
            System.out.println("\nВведи свое имя для знакомства с сервером");
            setUserName(scanner.nextLine());
            out.println(getUserName());
            out.flush();
            Thread.sleep(1000);

            //Поток для чтения новых сообщений
            Thread send = new Thread(new Runnable() {
                @Override
                public void run() {
                    BufferedReader it = null;
                    try {
                        it = new BufferedReader(new FileReader("src/main/resources/myLog.log"));
                        int innerCount = 0;
                        String line;
                        while (true) {
                            while ((line = it.readLine()) != null) {
                                innerCount++;
                                //если читали и надо продолжить
                                if (count > -1) {
                                    if (innerCount > count) {
                                        System.out.println(line);
                                    }
                                } else {
                                    //читаем первый раз
                                    System.out.println(line);
                                }
                            }
                            count = innerCount;
                            Thread.sleep(1000);
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            send.start();

            while (true) {
                // ждём консоли клиента на предмет появления в ней данных
                System.out.println("Введите сообщение или /end для выхода из канала:");
                String msg = scanner.nextLine();
                Thread.sleep(1000);

                out.println(msg);
                out.flush();

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

// проверяем, что нам ответит сервер на сообщение(за предоставленное ему время в паузе он должен был успеть ответить)
                if (in.read() > -1) {
                    msgFromServer(in);
                }
            }
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
