import log.ServLogger;
import thread.MonoThreadClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static ServLogger LOGGER = ServLogger.getInstance();
    public static ExecutorService executeIt = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) {
        String host = "127.0.0.1\n";
        int port = 1254;

        try (FileWriter writer = new FileWriter("src/main/resources/settings.txt", false)) {
            writer.write("host: " + host);
            writer.write("port: " + String.valueOf(port));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ServerSocket serverSocket = new ServerSocket(port);
             Scanner sc = new Scanner(System.in)) {
            LOGGER.log("Инфо", LOGGER.getAnsiYellow(), "Привет! Сервер активен," +
                    " ждёт консольных команд или подключение пользователей." +
                    "Для завершения работы сервера наберите \"end\"");
            Thread readThread = new Thread(() -> {
                while (!serverSocket.isClosed()) {
                    try {
                        Thread.sleep(1000);
                        if (sc.hasNextLine()) {
                            System.out.println("Сервер нашёл команды!");
                            Thread.sleep(1000);
                            String serverCommand = sc.nextLine();
                            if (serverCommand.equalsIgnoreCase("end")) {
                                System.out.println("Сервер инициализирует выход");
                                executeIt.shutdown();
                                serverSocket.close();
                                break;
                            }
                        }
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            readThread.start();

            while (!serverSocket.isClosed()) {
                //ждём подключения
                Socket clientSocket = serverSocket.accept();
                System.out.println("Есть подключение");
                // после получения запроса на подключение сервер создаёт сокет
                // для общения с клиентом и отправляет его в отдельную нить
                // в Runnable(при необходимости можно создать Callable)
                // монопоточную нить = сервер - MonoThreadClientHandler и тот
                // продолжает общение от лица сервера
                executeIt.execute(new MonoThreadClientHandler(clientSocket, LOGGER));
                System.out.println("Подключение установлено");
            }
            System.out.println("Пытаемся выйти");
            //завершаем пол нитей после завершения всех нитей
            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
