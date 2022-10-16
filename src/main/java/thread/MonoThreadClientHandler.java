package thread;

import log.MyLogger;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MonoThreadClientHandler implements Runnable {
    private final MyLogger myLogger;
    private static Socket clientDialog;
    Logger LOGGER;

    public MonoThreadClientHandler(Socket client, Logger LOGGER) {
        MonoThreadClientHandler.clientDialog = client;
        this.LOGGER = LOGGER;
        myLogger = MyLogger.getInstance();
    }

    @Override
    public void run() {
        try {
//            DataOutputStream out = new DataOutputStream(clientDialog.getOutputStream());
//            DataInputStream in = new DataInputStream(clientDialog.getInputStream());
            PrintWriter out = new PrintWriter(clientDialog.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientDialog.getInputStream()));
            System.out.println("Запись и чтение для приема и вывода создана");
            final String name = in.readLine() + "[" + clientDialog.getPort() + "]";
            System.out.printf(">>К чату подключился: >>%s\n", name);
            while (!clientDialog.isClosed()) {
//                String msg = in.readUTF();
                final String msg = in.readLine();
                System.out.println("Прочитали сообщение от " + name +": " + msg);

                if (msg.equalsIgnoreCase("/end")) {
                    System.out.println("Подключение разорвано...");
                    out.println("Сервер ожидает - " + msg + " - ОК");
//                    out.writeUTF("Сервер ожидает - " + msg + " - ОК");
                    Thread.sleep(3000);
                    break;
                }
                //не получили выход, значит работаем
                System.out.println("Сервер готов записывать....");
                String msgAndUser = name + ":" + msg;
                out.println(">>>" + msgAndUser + " - ОК");
//                LOGGER.log(Level.INFO, msgAndUser);
                myLogger.log(name, msg);
                System.out.println("Сервер записал сообщение");
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
