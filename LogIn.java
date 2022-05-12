package tw.myproject;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class LogIn extends JFrame implements ActionListener {
		private JPanel cp1 ,cp2,cp3;
		private JLabel jl1,jl2 ;
		private JButton jb1,jb2;
		private JTextField jtf;
		private JPasswordField jpf;
		private String id ;
		private ResultSet rs;
		
		Image img = Toolkit.getDefaultToolkit().getImage("iii/icon/volleyball.png");
		URL imgUrl = LogIn.class.getClassLoader().getResource("volleyball.png");
		

		
	public static void main(String[] args) {
		new LogIn();
	}
	public LogIn () {
		super("log in");
		Image deskImg = Toolkit.getDefaultToolkit().getImage(imgUrl);
		this.setIconImage(deskImg);
		setLayout(null);
		cp1 =new JPanel(new FlowLayout());
		cp2 =new JPanel(new FlowLayout());
		cp3 =new JPanel(new FlowLayout()); 
		
		cp1.setBounds(5,50,395,30);
		cp2.setBounds(5,80,395,30);
		cp3.setBounds(100,120,200,30);
		
		jl1 = new JLabel("帳號 ：");
		jl2 = new JLabel("密碼 ：");
		
		jtf = new JTextField(20);
		jtf.addFocusListener(new HintText(jtf,"請輸入註冊信箱"));
		jpf = new JPasswordField(20);
		jpf.addFocusListener(new jpfHintText(jpf,"請輸入密碼（含英文數字）"));
		jpf.setEchoChar('\0');
		
		jb1 = new JButton("註冊");
		jb1.addActionListener(this);
		jb2 = new JButton("登入");
		jb2.addActionListener(this);
		
		cp1.add(jl1);cp1.add(jtf);
		cp2.add(jl2);cp2.add(jpf);
		cp3.add(jb1);cp3.add(jb2);
		add(cp1);add(cp2);add(cp3);
		
		
		setResizable(false);
		setBounds(550,200,400,230);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		requestFocus();
	}

	
	public class HintText implements FocusListener{
		private JTextField jtf;
		private String hintText;
		
		private HintText(JTextField jtf ,String hintText) {
			this.jtf = jtf;
			this.hintText = hintText;
			jtf.setText(hintText);
			jtf.setForeground(Color.gray);
		}

		@Override
		public void focusGained(FocusEvent e) {
			String temp = jtf.getText();
			if(temp.equals(hintText)) {
				jtf.setText("");
				jtf.setForeground(Color.BLACK);
			}			
		}

		@Override
		public void focusLost(FocusEvent e) {
			String temp = jtf.getText();
			if(temp.equals("")) {
				jtf.setForeground(Color.GRAY);
				jtf.setText(hintText);
			}			
		}
	}

	public class jpfHintText implements FocusListener{
		private JPasswordField jpf;
		private String hintText;
		
		private jpfHintText(JPasswordField jpf ,String hintText) {
			this.jpf = jpf;
			this.hintText = hintText;
			jpf.setText(hintText);
			jpf.setForeground(Color.gray);
		}

		@Override
		public void focusGained(FocusEvent e) {
			String temp = new String(jpf.getPassword());
			if(temp.equals(hintText)) {
				jpf.setText("");
				jpf.setEchoChar('*');
				jpf.setForeground(Color.BLACK);
			}			
		}
		
		@Override
		public void focusLost(FocusEvent e) {
			String temp = new String(jpf.getPassword());
			if(temp.equals("")) {
				jpf.setEchoChar('\0');
				jpf.setForeground(Color.GRAY);
				jpf.setText(hintText);
			}			
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand()=="登入") {
			idCheck();
			
		}else if(e.getActionCommand()=="註冊") {
			dispose();
			new Register();
		} 
	} 	
	
	public void idCheck() {
		String passText = new String(jpf.getPassword());
		try {
			Properties prop =  new Properties();
			   prop.put("user", Common.DB_ACCOUNT);
			   prop.put("password", Common.DB_PASSWORD);
			   Connection conn = DriverManager.getConnection(
					     Common.DB_URL, prop);
			
			PreparedStatement pstmt =conn.prepareStatement(
					"SELECT account,password FROM account WHERE account =?");
			pstmt.setString(1, jtf.getText());			
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				String hashPassword = rs.getString("password");
				if(BCrypt.checkpw(passText, hashPassword)) {
					getDBData();
					new Volleyball(this.id);
					dispose();
				}else {
					JOptionPane.showMessageDialog(null,"密碼錯誤");
					System.out.println("密碼錯誤");
				}
			}else{
				JOptionPane.showMessageDialog(null,"無此帳號");
				System.out.println("無此帳號");
			};			
			
			conn.close();	
	}catch(SQLException e) {System.out.println(e.toString());}

	}

	public void getDBData() {
		Properties prop =  new Properties();
		   prop.put("user", Common.DB_ACCOUNT);
		   prop.put("password", Common.DB_PASSWORD);
		   
		String sql = "SELECT * FROM account WHERE account = ? ";
		try {
			Connection conn = DriverManager.getConnection(
				     Common.DB_URL, prop);
			PreparedStatement pstmt = conn.prepareStatement(
				sql,
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY
				);
		pstmt.setString(1,jtf.getText());
		rs = pstmt.executeQuery();
		rs.next();
		this.id = rs.getString("id");
		}catch(Exception e) {
			System.out.println(e.toString());
		}
	}

}

