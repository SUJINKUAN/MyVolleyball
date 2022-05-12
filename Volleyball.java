package tw.myproject;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


public  class Volleyball extends JFrame  implements ActionListener ,ItemListener{
	

	
	private JButton rename,newGame,joinGame,updateAndDelete;
	private JLabel username;
	private JLabel gender;
	private JPanel jp1,tab1,tab2,siteInfor,msg;
	JTable jt1,jt2;
	private String[] site = {"全部","中興大學","東海大學","石牌公園"};
	private JComboBox<String> jcb = new JComboBox<String>(site);
	private final String id ;
	private String SQLuserName ;
	private String SQLgender;
	private ResultSet rs;
	private String[] header = {"編號","地點","日期","起始時間","結束時間","標題","主揪人"};
	private String[] msg_header = {"姓名","性別"};
	TableModel tableModel;
	MsgTableModel msgTableModel;
	private String sltRow; 
	
	
	
	public Volleyball(String id) {
		super("揪愛打排球");
		this.id = id ;
		System.out.println("登入後得id"+id);
		getUserInfor();
		setLayout(null);
		setVisible(true);
		setBounds(400,200,720,480);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		jp1 = new JPanel();
		jp1.setLayout(null);
		jp1.setBounds(0,0,700,100);
		add(jp1);
		
		//建立物件
		username = new JLabel("姓名 : " + SQLuserName);
		gender = new JLabel("性別 : " + SQLgender);
		rename = new JButton("修改資訊");
		rename.addActionListener(this);
		newGame = new JButton("我要開團");
		newGame.addActionListener(this);
		joinGame = new JButton("我要加加");
		joinGame.addActionListener(this);
		updateAndDelete = new JButton("修改");
		updateAndDelete.addActionListener(this);
		jcb.addItemListener(this);
		
		
		tab1 = new JPanel();
		tab2 = new JPanel();
		tab2.setLayout(null);
		siteInfor = new JPanel(new BorderLayout());
		msg = new JPanel(new BorderLayout());
		JTabbedPane tp=new JTabbedPane();
		
		tab2.add(siteInfor);
		tab2.add(msg);
		tab2.add(jcb);
		tab2.add(newGame);
		tab2.add(joinGame);
		tab2.add(updateAndDelete);
		
		
		tableModel = new TableModel();
		msgTableModel = new MsgTableModel();
		jt1 = new JTable(tableModel);
		jt2 = new JTable(msgTableModel);
		JScrollPane tableContainer = new JScrollPane(jt1);
		JScrollPane tableContainer2 = new JScrollPane(jt2);
		siteInfor.add(tableContainer, BorderLayout.CENTER);
		msg.add(tableContainer2,BorderLayout.CENTER);
		
		jt1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (jt1.getSelectedRow() > -1) {
		            // print first column value from selected row
					sltRow = jt1.getValueAt(jt1.getSelectedRow(), 0).toString();
					msgTableModel.renew();
					jt2.updateUI();
		        }
			}
		});
		
		tp.add("公告",tab1);
		tp.add("場地",tab2);	   

		jp1.add(username);jp1.add(gender);jp1.add(rename);
		add(tp);
	    
		//設定尺寸位置
		username.setBounds(120, 15,200,20);
		gender.setBounds(120, 35,200,20);
		rename.setBounds(110, 60,100,30);
		tp.setBounds(10,100,700,350);  
		jcb.setBounds(10,10,150,30);
		siteInfor.setBounds(10,50,400,250);		
		msg.setBounds(450,50,200,250);
		newGame.setBounds(335,15,80,35);
		joinGame.setBounds(580,15,80,35);
		updateAndDelete.setBounds(180,15,80,35);

	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "修改資訊") {
			try {
			new UserInfor(this, id);
			}catch(Exception s) {
				System.out.println(e.toString());
			}
		}else if(e.getActionCommand() == "我要開團") {
			new NewGame(this, id , site );
		}else if(e.getActionCommand() == "我要加加") {
			joinGame();
		}else if(e.getActionCommand() == "修改") {
			new UpdateAndDelete(this, id,sltRow );
		}
	}

	public void joinGame() {
		 Properties prop =  new Properties();
		   prop.put("user", Common.DB_ACCOUNT);
		   prop.put("password", Common.DB_PASSWORD);
		   
		String sql = " INSERT INTO join_infor(site_id,user_id,checkdouble) "
				+ " VALUES ( ? , ? , CONCAT( ? ,? )) ";
		try {
		Connection conn = DriverManager.getConnection(Common.DB_URL, prop);
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, sltRow);
		pstmt.setString(2, id);
		pstmt.setString(3, sltRow);
		pstmt.setString(4, id);
		pstmt.executeUpdate();
		
		msgTableModel.renew();
		jt2.updateUI();
		rs.close();
		conn.close() ;
		}catch(SQLIntegrityConstraintViolationException sqlicve) {
			JOptionPane.showMessageDialog(null,"你已經加團囉","提醒",JOptionPane.ERROR_MESSAGE);
		}catch(SQLException sqle) {
			System.out.println(sqle.toString());
		}
		
	}
	
 	public void getUserInfor() {
 		 Properties prop =  new Properties();
		   prop.put("user", Common.DB_ACCOUNT);
		   prop.put("password", Common.DB_PASSWORD);
		String sql = "SELECT * FROM userinfor WHERE id = ? ";
		try {
			Connection conn = DriverManager.getConnection(Common.DB_URL, prop);
			PreparedStatement pstmt = conn.prepareStatement(
				sql,
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY
				);
		
		pstmt.setString(1,this.id);
		rs = pstmt.executeQuery();
		rs.next();
		this.SQLuserName = rs.getString("username");
		this.SQLgender = rs.getString("gender");
		}catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	public void setUserInfor(String userName , String gender) {
		this.username.setText("姓名 : " + userName);
		this.gender.setText("性別 : " + gender);
	} 
	
	public class TableModel extends DefaultTableModel{
		private ResultSet rs;
		private int rowCount;
		public TableModel() {
			getDBData();
		}
		
		public void renew() {
			getDBData();
		}
		public void renew(String site) {
			if(site.equals("全部")) {
				getDBData();
			}else {
				getDBData(site);
			}
			jt1.updateUI();
		}
		
		private void getDBData() {
			
			Properties prop =  new Properties();
			prop.put("user", Common.DB_ACCOUNT);
			prop.put("password", Common.DB_PASSWORD);
			
			String sql = "SELECT id,site,DATE_FORMAT(date,'%m/%d'),DATE_FORMAT(time_begin,'%k:%i'),DATE_FORMAT(time_end,'%H:%i'),title,invite FROM site";
			try {
				Connection conn = DriverManager.getConnection(Common.DB_URL, prop);
				PreparedStatement pstmt = conn.prepareStatement(
					sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY
					);
			rs = pstmt.executeQuery();
			rs.last();
			
			rowCount = rs.getRow(); //計算全部幾筆資料
			
			}catch(Exception e) {
				System.out.println(e.toString());
			}
		}
		
		private void getDBData(String site) {
			
			Properties prop =  new Properties();
			prop.put("user", Common.DB_ACCOUNT);
			prop.put("password", Common.DB_PASSWORD);
			
			String sql = "SELECT id,site,"
					+ "DATE_FORMAT(date,'%m/%d')"
					+ ",DATE_FORMAT(time_begin,'%k:%i'),"
					+ "DATE_FORMAT(time_end,'%H:%i'),"
					+ "title,invite "
					+ "FROM site WHERE site LIKE '"+site+"'";
			try {
				Connection conn = DriverManager.getConnection(Common.DB_URL, prop);
				PreparedStatement pstmt = conn.prepareStatement(
					sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY
					);
			rs = pstmt.executeQuery();
			rs.last();
			rowCount = rs.getRow(); //計算全部幾筆資料
			}catch(Exception e) {
				System.out.println(e.toString());
			}
		}

		@Override
		public int getColumnCount() {
			return 7;
		}
		
		@Override
		public String getColumnName(int column) {
			return 	header[column];
		}
		
		@Override
		public int getRowCount() {
			return rowCount;
		}
		@Override
		public Object getValueAt(int row, int column) {
			String ret ;
			try {
			rs.absolute(row+1);
			ret = rs.getString(column+1);
			}catch(Exception e ) {
				ret = "xxx";
			}
			return ret;
		}
		
		@Override  //是否可以修改內容
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}
	
	public class MsgTableModel extends DefaultTableModel{
		private ResultSet rs;
		private int rowCount;
		public MsgTableModel() {
			getMsgData();
		}
		
		public void renew() {
			getMsgData();
		}

		private void getMsgData() {
			
			Properties prop =  new Properties();
			prop.put("user", Common.DB_ACCOUNT);
			prop.put("password", Common.DB_PASSWORD);
			
			String sql = " SELECT ui.username,ui.gender "
				     + " FROM join_infor ji "
				     + " JOIN userinfor ui ON ji.user_id=ui.id "
				     + " WHERE site_id = ? ";
			try {
				Connection conn = DriverManager.getConnection(Common.DB_URL, prop);

				PreparedStatement pstmt = conn.prepareStatement(
					sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY
					);
			pstmt.setString(1, sltRow);
			rs = pstmt.executeQuery();
			rs.last();
			rowCount = rs.getRow(); //計算全部幾筆資料
			}catch(Exception e) {
				System.out.println(e.toString());
			}
		}

		@Override
		public int getColumnCount() {
			return 2;
		}
		
		@Override
		public String getColumnName(int column) {
			return msg_header[column];
		}
		
		@Override
		public int getRowCount() {
			return rowCount;
		}
		@Override
		public Object getValueAt(int row, int column) {
			String ret ;
			try {
			rs.absolute(row+1);
			ret = rs.getString(column+1);
			}catch(Exception e ) {
				ret = "xxx";
			}
			return ret;
		}

		@Override  //是否可以修改內容
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}
	public void updateSiteInfor() {
		tableModel.renew();
	}


	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.SELECTED)	{
			tableModel.renew(e.getItem().toString());
		}
	}
	
}

