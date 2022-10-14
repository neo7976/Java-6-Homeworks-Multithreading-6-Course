package thread;

import java.io.*;
import java.net.Socket;

public class MonoThreadClientHandler implements Runnable {
    private static Socket clientDialog;

    public MonoThreadClientHandler(Socket client) {
        MonoThreadClientHandler.clientDialog = client;
    }

    @Override
    public void run() {
        try {
//            DataOutputStream out = new DataOutputStream(clientDialog.getOutputStream());
//            DataInputStream in = new DataInputStream(clientDialog.getInputStream());
            PrintWriter out = new PrintWriter(clientDialog.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientDialog.getInputStream()));
            System.out.println("Data для приема и вывода создана");
            while (!clientDialog.isClosed()) {
//                String msg = in.readUTF();
                final String msg = in.readLine();
                System.out.println("Прочитали сообщение: " + msg);

                if (msg.equalsIgnoreCase("/end")) {
                    System.out.println("Подключение разорвано...");
                    out.println("Сервер ожидает - " + msg + " - ОК");
//                    out.writeUTF("Сервер ожидает - " + msg + " - ОК");
                    Thread.sleep(3000);
                    break;
                }
                //не получили выход, значит работаем
                System.out.println("Сервер готов записывать....");
//                out.writeUTF("Сервер ожидает - " + msg + " - ОК");
                out.println("Сервер ожидает - " + msg + " - ОК");
                System.out.println("Сервер записал сообщение");
                //освобождаем буфер
                out.flush();
            }
            in.close();
            out.close();
            clientDialog.close();
            System.out.println("Подключение закрыто - Выполнено");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
