import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Server {

    static Logger LOGGER;
    static {
        try(FileInputStream ins = new FileInputStream("src/main/resources/log.config")) {
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(Server.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static final DateTimeFormatter dfm = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static void main(String[] args) {
        MyLogger myLogger = MyLogger.getInstance();
        LOGGER.log(Level.INFO, "Hello! Server start!");

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

                final String name = in.readLine();
                String msg = "К чату подключился пользователь >> " + name + "! Порт подключения >> " + clientSocket.getPort();
                LOGGER.log(Level.INFO, msg);
                out.println(myLogger.log(String.format("В чат вошёл(ла) %s! Твой порт подключения: [%d]",
                        name,
                        clientSocket.getPort())));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //todo следует сделать реализацию для прочтения сообщения и записи в файл


    }
}
