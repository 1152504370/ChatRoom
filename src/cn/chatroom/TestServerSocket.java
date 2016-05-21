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
	List<Client> clientList = new ArrayList<Client>();

	public void niit() {
		Socket socket = null;
		ServerSocket s = null;
		Scanner input = new Scanner(System.in);
		try {
			s = new ServerSocket(8888);
			System.out.println("��������������");
			while (true) {
				socket = s.accept();
				Client c = new Client(socket);
				clientList.add(c);
				c.start();
			}
		} catch (Exception e) {
			System.out.println("Server over!");
		}
		input.close();
	}

	class Client extends Thread {
		DataOutputStream dos;
		DataInputStream dis;
		Socket socket;
		String name;

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
						if (str.endsWith("#")) {
							this.name = str.substring(0, str.indexOf("#"));
							str = "��Һã��ҽ�" + str.substring(0, str.indexOf("#"));
						}
						if (this.name != null) {
							System.out.println(name + "��" + str);
						} else {
							System.out.println(socket.getPort() + "��" + str);
						}
					}
					for (Client client : clientList) {
						if (this.name != null) {
							new DataOutputStream(client.socket.getOutputStream()).writeUTF(this.name + "��" + str);
						} else {
							System.out.println(socket.getPort() + "��" + str);
						}
					}

				} while (!str.equals("88"));
			} catch (Exception e) {
			} finally {
				try {
					System.out.println("�ͻ���" + socket.getPort() + "���˳�!");
					clientList.remove(this);
					dos.close();
					socket.close();
					dis.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		}
	}

	public static void main(String[] args) {
		TestServerSocket t = new TestServerSocket();
		t.niit();
	}
}
