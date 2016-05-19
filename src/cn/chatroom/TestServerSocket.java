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
				while (true) {
					dos = new DataOutputStream(socket.getOutputStream());
					dis = new DataInputStream(socket.getInputStream());
					String str = null;
					if ((str = dis.readUTF()) != null) {
						System.out.println(socket.getPort() + "说：" + str);
					}
					for (Client client : clientList) {
						new DataOutputStream( client.socket.getOutputStream()).writeUTF(socket.getPort()+"说："+str);
					}
				}
			} catch (Exception e) {
				System.out.println("Server over!");
			} finally {
				try {
					dos.close();
					dis.close();
					// s.close();
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		}
	}
}
