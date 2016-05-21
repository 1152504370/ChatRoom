package cn.chatroom;

import java.awt.EventQueue;
import java.awt.TextArea;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class UI extends JFrame {

	private JPanel contentPane;
	private JTextField sendText;
	JTextArea textArea;

	DataInputStream dis;
	DataOutputStream dos;
	private Socket socket;
	private JTextField sendName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI frame = new UI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void init() {
		try {
			socket = new Socket("192.168.46.28", 8888);
			System.out.println("客户端已连接！");
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread() {
			@Override
			public void run() {
				String s = null;
				try {
					while (true) {
						if ((s = dis.readUTF()) != null)
							textArea.append(s + "\r\n");
					}
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
		}.start();
	}

	public void sendmsg(JTextField sendText) {
		// 向服务器发送数据
		try {
			if (sendText.getText() != null) {
				dos.writeUTF(sendText.getText());
				sendText.setText("");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 */
	public UI() {
		setBackground(SystemColor.inactiveCaption);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					dos.close();
					socket.close();
					dis.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		setTitle("\u804A\u5929\u5BA4");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 486, 358);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		sendText = new JTextField();
		sendText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendmsg(sendText);
			}
		});
		sendText.setBackground(Color.WHITE);
		sendText.setColumns(10);
		JButton sendButton = new JButton("\u53D1\u9001");
		sendButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				sendmsg(sendText);
			}
		});

		JScrollPane scrollPane = new JScrollPane();

		JLabel name = new JLabel("\u6635\u79F0\uFF1A");

		sendName = new JTextField();
		sendName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendName.setText(sendName.getText() + "#");
				sendmsg(sendName);
			}
		});
		sendName.setColumns(10);

		JButton sendNameButton = new JButton("\u63D0\u4EA4");
		sendNameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendName.setText("#"+sendName.getText());
				sendmsg(sendName);
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 281, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
												.addComponent(sendText, GroupLayout.PREFERRED_SIZE, 218,
														GroupLayout.PREFERRED_SIZE)
												.addGap(3).addComponent(sendButton))
										.addGroup(gl_contentPane.createSequentialGroup().addGap(2)
												.addComponent(name, GroupLayout.PREFERRED_SIZE, 41,
														GroupLayout.PREFERRED_SIZE)
												.addGap(10)
												.addComponent(sendName, GroupLayout.PREFERRED_SIZE, 163,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(sendNameButton))))
						.addGap(169)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addGroup(gl_contentPane
				.createSequentialGroup()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup().addGap(19).addComponent(sendNameButton))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(21).addComponent(sendName,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(23).addComponent(name)))
				.addGap(17).addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup().addGap(16).addComponent(sendButton))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(18).addComponent(sendText,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
				.addGap(21)));

		textArea = new JTextArea();
		textArea.setBackground(Color.WHITE);
		scrollPane.setViewportView(textArea);
		textArea.setLineWrap(true); // 激活自动换行功能
		textArea.setWrapStyleWord(true);
		contentPane.setLayout(gl_contentPane);
		init(); // 连接服务器
	}
}
