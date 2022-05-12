package tw.myproject;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;



class Register extends JFrame implements ActionListener{
	private JPanel cp1 ,cp2,cp3,cp4,cp5,cp6,cp7,cp8;
	private JLabel jl1,jl2,jl3;
	private JLabel ms1,ms2,ms3,ms4;
	private JButton jb1,jb2;
	private JTextField jtf1;
	private JPasswordField jpf1,jpf2;
	private String accountHint = "使用信箱帳號註冊";
	private String pswdHint = "含大小寫英文.數字 (不含特殊字元符號）";
	private String pswd2Hint = "再次輸入密碼";
	
	
	public Register () {
		super("註冊");
		setLayout(null);
		cp1 =new JPanel(new FlowLayout());
		cp2 =new JPanel(new FlowLayout());
		cp3 =new JPanel(new FlowLayout());
		cp4 =new JPanel(new FlowLayout()); 
		cp5 = new JPanel(new FlowLayout());
		cp6 = new JPanel(new FlowLayout());
		cp7 = new JPanel(new FlowLayout());
		cp8 = new JPanel(new FlowLayout());
		
		cp1.setBounds(5,20,395,30);
		cp2.setBounds(5,60,395,30);
		cp3.setBounds(5,100,395,30);
		cp4.setBounds(100,150,200,30);
		cp5.setBounds(270,5,100,28);
		cp6.setBounds(15,44,300,28);
		cp7.setBounds(15,84,300,28);
		cp8.setBounds(16,124,313,28);
		
		jl1 = new JLabel("註冊帳號 ：");
		jl2 = new JLabel("設定密碼 ：");
		jl3 = new JLabel("確認密碼 ：");
		ms1 = new JLabel("");
		ms2 = new JLabel("");
		ms3 = new JLabel("");
		ms4 = new JLabel("");

		jtf1 = new JTextField(20);
		jtf1.addFocusListener(new jtfHintText(jtf1,accountHint));
		jpf1 = new JPasswordField(20);
		jpf1.addFocusListener(new jpfHintText(jpf1,pswdHint));
		jpf2 = new JPasswordField(20);
		jpf2.addFocusListener(new jpfHintText(jpf2,pswd2Hint));
		jpf1.setEchoChar('\0');
		jpf2.setEchoChar('\0');

		jb1 = new JButton("返回");
		jb1.addActionListener(this);
		jb2 = new JButton("確認");
		jb2.addActionListener(this);
		
		cp1.add(jl1);cp1.add(jtf1);
		cp2.add(jl2);cp2.add(jpf1);
		cp3.add(jl3);cp3.add(jpf2);
		cp4.add(jb1);cp4.add(jb2);
		cp5.add(ms1) ;cp6.add(ms2);
		cp7.add(ms3) ;cp8.add(ms4);
		add(cp1);add(cp2);add(cp3);add(cp4);
		add(cp5);add(cp6);add(cp7);add(cp8);
		
		
		setResizable(false);
		setBounds(550,200,400,230);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		requestFocus();
	}
	public class jtfHintText implements FocusListener{
		private JTextField jtf;
		private String hintText;
		
		private jtfHintText(JTextField jtf ,String hintText) {
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
			if (e.getActionCommand() == "返回") {
				dispose();
				new LogIn();
			}else if(e.getActionCommand() == "確認") {
				String passText1 = new String(jpf1.getPassword());
				String passText2 = new String(jpf2.getPassword());
				boolean temp ;
				//確認是否有空白欄位
				if(jtf1.getText().equals(accountHint)||
					(passText1.equals(pswdHint))||
					(passText2.equals(pswd2Hint))){
				
						
					ms1.setForeground(Color.red);
					ms1.setText("尚有空白欄位");
					temp = false;
				}else {
					ms1.setText("");
					temp = true;
				}
				if(temp == true) {
				//帳號格式確認
				if(jtf1.getText().matches("[A-Za-z0-9+_.-]+@[A-Za-z0-9+_.-]+.com")) {
					ms2.setText("");
				}else {
					ms2.setForeground(Color.red);
					ms2.setText("帳號格式錯誤");
				}
				//密碼格式確認
				if(passText1.matches("[A-Za-z0-9]+")) {
					ms3.setText("");
				}else {
					ms3.setForeground(Color.red);
					ms3.setText("密碼格式錯誤");
				}
				//再次確認密碼
				if(passText2.equals(passText1)) {
					ms4.setText("");
				}else {
					ms4.setForeground(Color.red);
					ms4.setText("與設定密碼不同");
				}
				}
				if(ms1.getText().equals("")&&
						ms2.getText().equals("")&&
						ms3.getText().equals("")&&
						ms4.getText().equals("")) {
					
						regisTOSQL(jtf1.getText(), passText1);
				}
			
				
			}
		}
	public void regisTOSQL(String account,String password) {
		
		try {
			Properties prop =  new Properties();
			   prop.put("user", Common.DB_ACCOUNT);
			   prop.put("password", Common.DB_PASSWORD);
			   Connection conn = DriverManager.getConnection(Common.DB_URL, prop);
	
			PreparedStatement pstmt =conn.prepareStatement(
				     " INSERT INTO account (id,account,password) "
				    	     + " VALUES ( "
				    	     + " (SELECT SEQ FROM "
				    	     + " (SELECT CONCAT(YEAR(NOW()), LPAD(MONTH(NOW()),2,'0'), LPAD(DAY(NOW()),2,'0'), LPAD((CAST( SUBSTRING(MAX(id),9,4)AS SIGNED)+1),4,'0')"
				    	     + " ) AS SEQ FROM account ORDER BY id) AS NEW_SEQ )"
				    	     + " ,?,?) ");

			
			pstmt.setString(1, account);
			pstmt.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
			
			pstmt.executeUpdate();
			
			conn.close();
			insertTOUserInfor(account);
			dispose();
			JOptionPane.showMessageDialog(null,"註冊成功");
			new LogIn();
		}catch(SQLIntegrityConstraintViolationException e1) {
			JOptionPane.showMessageDialog(null,"該信箱已被註冊");
		}
		catch(SQLException e2) {
			System.out.println(e2.toString());
		}
		
	}
public void insertTOUserInfor(String account) {
		
		try {
			 Properties prop =  new Properties();
			 prop.put("user", Common.DB_ACCOUNT);
			 prop.put("password", Common.DB_PASSWORD);
			 Connection conn = DriverManager.getConnection(
					     Common.DB_URL, prop);
			   
			PreparedStatement pstmt =conn.prepareStatement(
				      " INSERT INTO userinfor (id) "
					+ " VALUES((SELECT id FROM account WHERE account = ?))");

			
			pstmt.setString(1, account);
			
			pstmt.executeUpdate();
			
			conn.close();
		}
		catch(SQLException e2) {
			System.out.println(e2.toString());
		}
		
	}
}
