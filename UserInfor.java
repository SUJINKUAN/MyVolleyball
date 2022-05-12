package tw.myproject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


public class UserInfor  extends JFrame implements ActionListener {
	private JButton jb1,jb2;
	private JLabel jl1,jl2,jl3;
	private JTextField jtf1;
	private JRadioButton jrbMale = new JRadioButton("男性");
	private JRadioButton jrbFemale = new JRadioButton("女性");
	private ResultSet rs;
	private final String id;
	private Volleyball volleyball;

	
	public UserInfor(Volleyball volleyball , String id) {
		super("修改用戶者資訊");
		this.id = id ;
		this.volleyball = volleyball;
		setLayout(null);
		setVisible(true);
		setBounds(650,300,300,200);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		

		jl1 = new JLabel("使用者編號 : " +id);add(jl1);
		jl2 = new JLabel("姓名 ：");add(jl2);
		jl3 = new JLabel("性別 ：");add(jl3);
		jtf1 = new JTextField();add(jtf1);
		jb1 = new JButton("返回");add(jb1);
		jb1.addActionListener(this);
		jb2 = new JButton("確認");add(jb2);
		jb2.addActionListener(this);

		jl1.setBounds(50, 10, 200, 30);
		jl2.setBounds(50, 40, 80, 30);
		jtf1.setBounds(100, 40, 130, 30);
		jl3.setBounds(50,70,80,30);
		jb1.setBounds(70, 120, 70, 30);
		jb2.setBounds(150, 120, 70, 30);
		
		jrbMale.setBounds(100, 60, 70,50);
		jrbFemale.setBounds(170, 60, 70,50);
		add(jrbMale);add(jrbFemale);
		jrbMale.addActionListener(this);
		jrbFemale.addActionListener(this);
		ButtonGroup group = new ButtonGroup();
	    group.add(jrbMale);group.add(jrbFemale);
		
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "返回") {
			dispose();
		}else if(e.getActionCommand() == "確認") {
			if(jrbMale.isSelected()) {
				updateUserInfor(jtf1.getText(), "男性");
			}else if(jrbFemale.isSelected()) {
				updateUserInfor(jtf1.getText(), "女性");
			}
		}
	}
public void updateUserInfor(String username,String gender) {
		
		try {
			 Properties prop =  new Properties();
			 prop.put("user", Common.DB_ACCOUNT);
			 prop.put("password", Common.DB_PASSWORD);
			 Connection conn = DriverManager.getConnection(Common.DB_URL, prop);
	
			PreparedStatement pstmt =conn.prepareStatement(
					"UPDATE userinfor SET username = ? ,gender = ? WHERE id = ? ");
			
			pstmt.setString(1, username);
			pstmt.setString(2, gender);
			pstmt.setString(3, this.id);
			
			pstmt.executeUpdate();
			
			conn.close();
			
			dispose();
			volleyball.setUserInfor(username,gender);
			
			JOptionPane.showMessageDialog(null,"更改完成");
						
		}catch(SQLIntegrityConstraintViolationException e1) {
			JOptionPane.showMessageDialog(null,"失敗");
		}
		catch(SQLException e2) {
			System.out.println(e2.toString());
		}
	}

}
