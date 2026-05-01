    package com.server.network;

    import java.net.ServerSocket;
    import java.net.Socket;
    import java.util.ResourceBundle;

    public class Server {
        public static void main(String[] args) {

            ResourceBundle bundle = ResourceBundle.getBundle("server");
            int serverPort = Integer.parseInt(bundle.getString("SERVER_PORT"));

            System.out.println("Сервер запускается....");

            try (ServerSocket serverSocket = new ServerSocket(serverPort)) {

                System.out.println("Сервер запущен на порте " + serverPort + "!");

                while (true) {
                    System.out.println("Ожидание подключения....");
                    Socket clientAccepted = serverSocket.accept();
                    System.out.println("Соединение установлено...");

                    // Запускаем обработку клиента в отдельном потоке
                    new Thread(() -> new ClientThread(clientAccepted).run()).start();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
