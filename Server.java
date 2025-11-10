import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        // Cổng mà server sẽ lắng nghe
        int port = 9999; 

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server da khoi dong tai cong " + port);
            System.out.println("Dang cho 2 nguoi choi ket noi...");

            while (true) {
                // Chap nhan nguoi choi 1
                Socket player1 = serverSocket.accept();
                System.out.println("Nguoi choi 1 da ket noi: " + player1.getInetAddress());
                PrintWriter out1 = new PrintWriter(player1.getOutputStream(), true);
                out1.println("CHO"); // Gui tin hieu cho P1 biet la phai cho

                // Chap nhan nguoi choi 2
                Socket player2 = serverSocket.accept();
                System.out.println("Nguoi choi 2 da ket noi: " + player2.getInetAddress());
                // PrintWriter out2 = new PrintWriter(player2.getOutputStream(), true); // Khong can
                
                // Khi du 2 nguoi, bat dau game trong 1 Thread moi
                System.out.println("Da du 2 nguoi choi. Bat dau GameHandler...");
                GameHandler gameHandler = new GameHandler(player1, player2);
                gameHandler.start(); // Chay Thread
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// Lớp này xử lý logic cho MỘT ván game giữa 2 người chơi
class GameHandler extends Thread {
    private Socket player1;
    private Socket player2;

    public GameHandler(Socket player1, Socket player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public void run() {
        try {
            // Tao luong doc/ghi cho 2 nguoi choi
            BufferedReader in1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
            PrintWriter out1 = new PrintWriter(player1.getOutputStream(), true);

            BufferedReader in2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
            PrintWriter out2 = new PrintWriter(player2.getOutputStream(), true);

            // Bao cho ca 2 biet game bat dau
            out1.println("START");
            out2.println("START");

            // Doc lua chon tu 2 nguoi choi
            String choice1 = in1.readLine();
            System.out.println("Nguoi choi 1 chon: " + choice1);

            String choice2 = in2.readLine();
            System.out.println("Nguoi choi 2 chon: " + choice2);

            // Xac dinh ket qua
            String result1 = "";
            String result2 = "";

            if (choice1.equals(choice2)) {
                result1 = "HOA";
                result2 = "HOA";
            } else if ((choice1.equals("KEO") && choice2.equals("BAO")) ||
                       (choice1.equals("BUA") && choice2.equals("KEO")) ||
                       (choice1.equals("BAO") && choice2.equals("BUA"))) {
                result1 = "THANG";
                result2 = "THUA";
            } else {
                result1 = "THUA";
                result2 = "THANG";
            }
            
            // Gui ket qua ve cho 2 nguoi choi
            System.out.println("-> Ket qua: P1 " + result1 + ", P2 " + result2);
            out1.println("KET QUA: BAN " + result1);
            out2.println("KET QUA: BAN " + result2);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Dong socket khi game ket thuc
                player1.close();
                player2.close();
                System.out.println("Da dong ket noi 1 game session.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}