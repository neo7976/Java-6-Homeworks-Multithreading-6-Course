import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Server {

    public static final DateTimeFormatter dfm = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static void main(String[] args) {
        Logger logger = Logger.getInstance();

        System.out.println("Hello! Server start!");
        String host = "127.0.0.1\n";
        int port = 1254;

        try (FileWriter writer = new FileWriter("src/main/resources/settings.txt", false)) {
            writer.write("host: " + host);
            writer.write("port: " + String.valueOf(port) + "\n");
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //todo будет как отдельный поток в будущем для новых пользователей
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {

                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                System.out.println("Новый пользователь! Порт подключения >> " + clientSocket.getPort());
                final String name = in.readLine();
//                out.println(String.format("[%s] Привет %s! Твой порт подключения: [%d]",
//                        dfm.format(LocalDateTime.now()),
//                        name,
//                        clientSocket.getPort()));

                out.println(logger.log(String.format("В чат вошёл(ла) %s! Твой порт подключения: [%d]",
                        name,
                        clientSocket.getPort())));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //todo следует сделать реализацию для прочтения сообщения и записи в файл


    }
}
