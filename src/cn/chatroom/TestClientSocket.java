package cn.chatroom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class TestClientSocket {
	public static void main(String[] args) {
		DataInputStream dis = null;
		DataOutputStream dos = null;
		Socket socket1 = null;
		Scanner input = new Scanner(System.in);
		try {
			socket1 = new Socket("127.0.0.1", 8888);
			System.out.println("�ͻ�����������");
			String str = null;
			do {
				dis = new DataInputStream(socket1.getInputStream());
				dos = new DataOutputStream(socket1.getOutputStream());
				System.out.println("����������˵�Ļ���");
				str = input.nextLine();
				dos.writeUTF(str);
				System.out.println("�����������յ�˵��" + dis.readUTF());
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
