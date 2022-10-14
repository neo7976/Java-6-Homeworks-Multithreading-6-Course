import thread.MonoThreadClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Server {

    //todo потом подправить на все действующие потоки
    public static final DateTimeFormatter dfm = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    public static ExecutorService executeIt = Executors.newFixedThreadPool(10);

    static Logger LOGGER;

    static {
        try (FileInputStream ins = new FileInputStream("src/main/resources/log.config")) {
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(Server.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MyLogger myLogger = MyLogger.getInstance();
        LOGGER.log(Level.INFO, "Hello! Server start!");

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
             BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))) {
//             Scanner sc = new Scanner(System.in)) {
            System.out.println("Server socket created, command console reader for listen to server commands");
            while (!serverSocket.isClosed()) {
                //todo не работает завершение сервера
                if (bf.ready()) {
//                if (sc.hasNextLine()) {
                    System.out.println("Сервер нашёл команды!");
                    Thread.sleep(1000);
                    String serverCommand = bf.readLine();
//                    String serverCommand = sc.nextLine();
                    if (serverCommand.equalsIgnoreCase("end")) {
                        System.out.println("Сервер инициализирует выход");
                        serverSocket.close();
                        break;
                    }
                }
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
            //завершаем пол нитей после завершения всех нитей
            executeIt.shutdown();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
