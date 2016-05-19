package cn.chatroom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class TestClientSocket {
	static DataInputStream dis = null;

	public static void main(String[] args) {
		DataOutputStream dos = null;
		Socket socket1 = null;
		Scanner input = new Scanner(System.in);
		try {
			socket1 = new Socket("127.0.0.1", 8888);
			System.out.println("客户端已启动！");
			String str = null;
			do {
				dis = new DataInputStream(socket1.getInputStream());
				dos = new DataOutputStream(socket1.getOutputStream());
				new Thread() {
					@Override
					public void run() {
						try {
							System.out.println(dis.readUTF());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();
				System.out.println("请输入你想说的话：");
				str = input.nextLine();
				dos.writeUTF(str);
			} while (!str.equals("88"));

		} catch (Exception e) {
			System.out.println("Client over!");
		} finally {
			try {
				dis.close();
				dos.close();
				socket1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
