package cn.chatroom;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.SystemColor;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;

public class UI extends JFrame {

	private JPanel contentPane;
	private JTextField sendText;
	JTextArea textArea;

	DataInputStream dis;
	DataOutputStream dos;
	private Socket socket;
	private JTextField nameText;
	JTextArea showName;
	JList<String> userList = new JList<String>();
	

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
			socket = new Socket("127.0.0.1", 8888);
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
							if (s.startsWith(",")) {
								updateList(s);
							} else {
								textArea.append(s + "\r\n");
							}
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
	public void updateList(String s){
		String userName[] = s.split(",");
		DefaultListModel<String> dml = new DefaultListModel<String>();
		for (int i = 1; i < userName.length-1; i++) {
			dml.addElement(userName[i]);
		}
		userList.setModel(dml);
	}

	/**
	 * Create the frame.
	 */
	public UI() {
		setBackground(new Color(255, 255, 255));
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
		setBounds(100, 100, 762, 501);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		sendText = new JTextField();
		sendText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendmsg(sendText);
			}
		});
		sendText.setBackground(SystemColor.textHighlightText);
		sendText.setColumns(10);
		JButton sendButton = new JButton("\u53D1\u9001");
		sendButton.setBackground(UIManager.getColor("Button.highlight"));
		sendButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				sendmsg(sendText);
			}
		});

		JScrollPane scrollPane = new JScrollPane();

		JLabel name = new JLabel("\u4FEE\u6539\u6635\u79F0\uFF1A");
		name.setBackground(new Color(240, 128, 128));

		nameText = new JTextField();
		nameText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showName.setText(nameText.getText());
				nameText.setText("#" + nameText.getText());
//				String myName = nameText.getText().substring(1);
				sendmsg(nameText);
//				nameText.setText(myName);

			}
		});
		nameText.setColumns(10);

		JButton sendNameButton = new JButton("\u63D0\u4EA4");
		sendNameButton.setBackground(UIManager.getColor("Button.highlight"));
		sendNameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showName.setText(name.getText());
				nameText.setText("#" + nameText.getText());
				sendmsg(nameText);
			}
		});
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		showName = new JTextArea();
		showName.setBackground(SystemColor.textHighlightText);
		showName.setEditable(false);
		
		JLabel myName = new JLabel("\u5F53\u524D\u6635\u79F0\uFF1A");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(49)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(myName)
							.addGap(18)
							.addComponent(showName, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
							.addComponent(sendText, Alignment.LEADING)
							.addComponent(scrollPane, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 316, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(name, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(nameText, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(sendNameButton)
							.addGap(21))
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 301, GroupLayout.PREFERRED_SIZE)
						.addComponent(sendButton, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(19)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(myName)
						.addComponent(showName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(name)
						.addComponent(nameText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(sendNameButton))
					.addGap(22)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 259, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(sendText, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(sendButton, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE))
					.addGap(81))
		);
				scrollPane_1.setViewportView(userList);
		
				
				
				userList.setBackground(SystemColor.info);
				
						JLabel lblNewLabel = new JLabel("\u5728\u7EBF\u7528\u6237:");
						scrollPane_1.setColumnHeaderView(lblNewLabel);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(SystemColor.textHighlightText);
		scrollPane.setViewportView(textArea);
		textArea.setLineWrap(true); // 激活自动换行功能
		textArea.setWrapStyleWord(true);
		contentPane.setLayout(gl_contentPane);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{sendText, textArea, myName, contentPane, lblNewLabel, showName, scrollPane_1, sendButton, userList, scrollPane, name, nameText, sendNameButton}));
		init(); // 连接服务器
	}
}
