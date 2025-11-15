import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Lớp Client cho trò chơi Kéo-Búa-Bao đơn giản qua mạng.
 * Giao tiếp với Server bằng các chuỗi lệnh.
 */
public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 9999;

    public static void main(String[] args) {
        System.out.println("Dang ket noi toi server...");

        try (
                // 1. Khoi tao Socket va cac luong I/O
                Socket socket = new Socket(SERVER_ADDRESS, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner consoleInput = new Scanner(System.in)
        ) {
            System.out.println(">>> Da ket noi toi server thanh cong! <<<");
            
            String serverMessage;
            // 2. Vòng lặp chính để nhận tin nhắn từ Server
            while ((serverMessage = in.readLine()) != null) {
                // In ra tin nhắn gốc từ Server (trừ khi nó là tin nhắn xử lý đặc biệt)
                // System.out.println("[RAW Server]: " + serverMessage);

                if (serverMessage.equals("CHO")) {
                    // Tin nhắn chờ người chơi khác
                    System.out.println("Ban la nguoi choi 1. Dang cho doi nguoi choi 2...");
                    continue;
                }

                if (serverMessage.equals("START")) {
                    // Tin nhắn bắt đầu ván chơi
                    System.out.println("----------------------------------------");
                    System.out.println("!!! Game bat dau !!! Moi ban chon (KEO, BUA, BAO):");
                    
                    String userChoice;
                    while (true) {
                        System.out.print("> Nhap lua chon: ");
                        userChoice = consoleInput.nextLine().toUpperCase().trim();
                        // Kiem tra tinh hop le cua lua chon
                        if (userChoice.equals("KEO") || userChoice.equals("BUA") || userChoice.equals("BAO")) {
                            break;
                        }
                        System.out.println("(!) Lua chon khong hop le. Vui long chi nhap KEO, BUA, hoac BAO.");
                    }

                    out.println(userChoice); // Gui lua chon den Server
                    System.out.println("Ban da chon: **" + userChoice + "**.");
                    System.out.println("Dang cho Server va doi thu xu ly...");

                } else if (serverMessage.equals("CHOI_TIEP?")) {
                    // Tin nhắn hỏi chơi tiếp
                    System.out.println("----------------------------------------");
                    System.out.print("Ban co muon choi tiep khong? (Y/N): ");
                    String playAgain = consoleInput.nextLine().toUpperCase().trim();
                    out.println(playAgain); // Gui lua chon choi tiep den Server

                    if (!playAgain.equals("Y")) {
                        System.out.println("Ban da chon thoat. Tam biet va cam on da choi!");
                        break;
                    }
                    System.out.println("Ban da chon choi tiep. Dang cho doi thu xac nhan...");

                } else if (serverMessage.equals("QUIT_DOI_THU")) {
                    // Tin nhắn đối thủ ngắt kết nối
                    System.out.println("----------------------------------------");
                    System.out.println("!!! DOI THU DA NGAT KET NOI. GAME KET THUC. !!!");
                    break;

                } else if (serverMessage.equals("QUIT_GAME")) {
                    // Tin nhắn Server báo game kết thúc do người chơi chọn thoát
                    System.out.println("----------------------------------------");
                    System.out.println("Mot trong hai nguoi da chon thoat. Game ket thuc!");
                    break;
                } else {
                    // Xử lý kết quả ván chơi: "KET_QUA:LUA_CHON_TOI:LUA_CHON_DOI_THU"
                    String[] parts = serverMessage.split(":");
                    if (parts.length == 3) {
                        String result = parts[0]; // THANG, THUA, HOA
                        String myChoice = parts[1];
                        String opponentChoice = parts[2];

                        System.out.println("=========================================");
                        System.out.println("KET QUA VAN CHOI: BAN **" + result + "**");
                        System.out.println("Ban chon: " + myChoice + " | Doi thu chon: " + opponentChoice);
                        System.out.println("=========================================");
                    } else {
                        // Tin nhắn thông báo chung từ Server (nếu có)
                        System.out.println("[SERVER]: " + serverMessage);
                    }
                }
            }
        } catch (Exception e) {
            // Xử lý khi mất kết nối hoặc lỗi khác
            System.err.println("!!! LOI: Mat ket noi toi server hoac xay ra loi khac: " + e.getMessage() + " !!!");
        }

        System.out.println("--- Chuong trinh Client ket thuc. Da ngat ket noi. ---");
    }
}
