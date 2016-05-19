package cn.chatroom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TestServerSocket {
	public static void main(String[] args) {
		Socket socket = null;
		ServerSocket s = null;
		Scanner input = new Scanner(System.in);
		try {
			s = new ServerSocket(8888);
			System.out.println("服务器已启动！");
			while (true) {
				socket = s.accept();
				new TestServerSocket().new Client(socket).start();
			}
		} catch (Exception e) {
			System.out.println("Server over!");
		}

	}

	class Client extends Thread {
		DataOutputStream dos = null;
		DataInputStream dis = null;
		Socket socket1 = null;

		public Client() {

		}

		public Client(Socket socket) {
			super();
			this.socket1 = socket;
		}

		@Override
		public void run() {
			try {
				while (true) {
					dos = new DataOutputStream(socket1.getOutputStream());
					dis = new DataInputStream(socket1.getInputStream());
					String str = null;
					if ((str = dis.readUTF()) != null) {
						System.out.println(socket1.getPort() + "说：" + str);
					}
					dos.writeUTF(socket1.getPort() + "说：" + str);
				}
			} catch (Exception e) {
				System.out.println("Server over!");
			} finally {
				try {
					dos.close();
					dis.close();
					// s.close();
					socket1.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		}
	}
}
