package cn.chatroom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class TestServerSocket {
	public static void main(String[] args) {
		DataOutputStream dos = null;
		DataInputStream dis = null;
		ServerSocket s = null;
		Socket socket1 = null;
		Scanner input = new Scanner(System.in);
		try {
			s = new ServerSocket(8888);
			System.out.println("服务器已启动！");
			socket1 = s.accept();
			while (true) {
				dos = new DataOutputStream(socket1.getOutputStream());
				dis = new DataInputStream(socket1.getInputStream());
				String str = null;
				if ((str = dis.readUTF()) != null) {
					System.out.println("客户端说：" + str);
					// System.out.println("请说话：");
					// str = input.next();
				}
				dos.writeUTF(str);
			}
		} catch (Exception e) {
			System.out.println("Server over!");
		} finally {
			try {
				dos.close();
				dis.close();
				s.close();
				socket1.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
	}
}
