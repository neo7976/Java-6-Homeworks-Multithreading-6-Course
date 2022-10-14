package thread;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MonoThreadClientHandler implements Runnable {
    private static Socket clientDialog;
    Logger LOGGER;
//
//    static {
//        try (FileInputStream ins = new FileInputStream("src/main/resources/log.config")) {
//            LogManager.getLogManager().readConfiguration(ins);
//            LOGGER = Logger.getLogger(MonoThreadClientHandler.class.getName());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public MonoThreadClientHandler(Socket client, Logger LOGGER) {
        MonoThreadClientHandler.clientDialog = client;
        this.LOGGER = LOGGER;
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

                //todo доработать выход, так как цепляется имя,
                // попробовать перебить и хранить здесь порт и имя пользователя

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
                out.println(msg + " - ОК");
                LOGGER.log(Level.INFO, msg);
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
