package cn.chatroom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestServerSocket {
	static List<Client> clientList = new ArrayList<Client>();

	public static void main(String[] args) {
		Socket socket = null;
		ServerSocket s = null;
		Scanner input = new Scanner(System.in);
		try {
			s = new ServerSocket(8888);
			System.out.println("服务器已启动！");
			while (true) {
				socket = s.accept();
				Client c = new TestServerSocket().new Client(socket);
				clientList.add(c);
				c.start();
			}
		} catch (Exception e) {
			System.out.println("Server over!");
		}
		input.close();
	}

	class Client extends Thread {
		DataOutputStream dos = null;
		DataInputStream dis = null;
		Socket socket = null;

		public Client() {

		}

		public Client(Socket socket) {
			super();
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				String str = null;
				do {
					dos = new DataOutputStream(socket.getOutputStream());
					dis = new DataInputStream(socket.getInputStream());
					if ((str = dis.readUTF()) != null) {
						System.out.println(socket.getPort() + "说：" + str);
					}
					for (Client client : clientList) {
						new DataOutputStream(client.socket.getOutputStream()).writeUTF(socket.getPort() + "说：" + str);
					}
				} while (!str.equals("88"));
			} catch (Exception e) {
				System.out.println("客户端" + socket.getPort() + "已退出!");
				clientList.remove(this);
			} finally {
				try {
					dos.close();
					socket.close();
					dis.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		}
	}
}
