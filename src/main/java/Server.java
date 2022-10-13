import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
    System.out.println("Hello! Server start!");
        int port = 1254;

        try(ServerSocket serverSocket = new ServerSocket(port)) {
           while (true) {
               Socket clientSocket = serverSocket.accept();
               PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
               BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

               System.out.println("Новый пользователь! Порт подключения >> " + clientSocket.getPort());
               final String name = in.readLine();
               out.println(String.format("Привет %s! Твой порт подключения: [%d]", name, clientSocket.getPort()));
           }




        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
