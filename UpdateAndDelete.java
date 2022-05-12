package tw.myproject;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UpdateAndDelete extends JFrame implements ActionListener{
	private Volleyball volleyball ;
	private String id ;
	private JButton back,delete,update;
	private JPanel jpSite,jpDate,jpBegin,jpEnd,jpButton;
	private String sltRow;
	private JTextField jtfYear,jtfTitle;
	private JLabel jlId,jlSite,jlDate,jlYear,jlMon,jlDay,jlBegin,jlEnd,jlH,jlM,jlTitle;
	private String[] site = {"中興大學","東海大學","石牌公園"};
	private String[] mon = {"01","02","03","04","05","06","07","08","09","10","11","12"};
	private String[] day = {"01","02","03","04","05","06","07","08","09","10","11","12",
							"13","14","15","16","17","18","19","20","21","22","23","24",
							"25","26","27","28","29","30","31"};
	private String[] hh = {"01","02","03","04","05","06","07","08","09","10","11","12",
							"13","14","15","16","17","18","19","20","21","22","23","00"};
	private String[] mm = {"00","30"};
	private JComboBox<String> jcbSite,jcbMon,jcbDay,jcbBeginH,jcbBeginM,jcbEndH,jcbEndM;
	
	private String sltSite;
	private String sltDate;
	private String sltBegin;
	private String sltEnd;
	private String sltTitle;
	private String sltSiteCode; 
	
	private String sltRowId;
	private String sltRowSite;
	private String sltRowDateY;
	private String sltRowDateM;
	private String sltRowDateD;
	private String sltRowBeginH;
	private String sltRowBeginM;
	private String sltRowEndH;
	private String sltRowEndM;
	private String sltRowTitle;
	
	private ResultSet rs;

	
	public UpdateAndDelete(Volleyball volleyball , String id ,String sltRow) {	
	super("修改與刪除");
	this.id = id ;
	this.volleyball = volleyball;
	this.sltRow = sltRow;
	getRowData(sltRow);
	initUI();
	
	}
	
	
	
	void initUI(){
		setLayout(null);
		setVisible(true);
		setBounds(650,300,400,250);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		jpButton = new JPanel();
		jpButton.setLayout(null);
		jpButton.setBounds(0, 170, 290, 100);
		add(jpButton);
		
		//場地下拉選單
		jpSite = new JPanel(new FlowLayout(0));
		jpSite.setBounds(35, 15, 300, 30);
		jlSite = new JLabel("場地 :");
		jcbSite = new JComboBox<String>(this.site);
		jcbSite.setSelectedItem(sltRowSite);
		add(jpSite);jpSite.add(jlSite);jpSite.add(jcbSite);
		
		//日期 年月日
		jpDate = new JPanel(new FlowLayout(0));
		jpDate.setBounds(180,43,200,30);
		add(jpDate);
		
		jlDate = new JLabel("日期 ：");
		jlDate.setBounds(40,50,50, 20);
		jtfYear = new JTextField(sltRowDateY);
		jtfYear.setBounds(85,50,70, 20);
		jlYear = new JLabel("年");
		jlYear.setBounds(160,50,50, 20);
		jcbMon = new JComboBox<String>(mon);
		jcbMon.setSelectedItem(sltRowDateM);
		jlMon = new JLabel("月");
		jcbDay = new JComboBox<String>(day);
		jcbDay.setSelectedItem(sltRowDateD);
		jlDay = new JLabel("日");
		add(jlDate);
		add(jtfYear);add(jlYear);
		jpDate.add(jcbMon);jpDate.add(jlMon);
		jpDate.add(jcbDay);jpDate.add(jlDay);
		
		//開始時間
		jpBegin = new JPanel(new FlowLayout());
		jpBegin.setBounds(20, 70, 300, 30);
		add(jpBegin);
		jlBegin = new JLabel("開始時間 ：");
		jcbBeginH = new JComboBox<String>(hh);
		jcbBeginH.setSelectedItem(sltRowBeginH);
		jcbBeginM = new JComboBox<String>(mm);
		jcbBeginM.setSelectedItem(sltRowBeginM);
		jlH = new JLabel("點");
		jlM = new JLabel("分");
		jpBegin.add(jlBegin);jpBegin.add(jcbBeginH);jpBegin.add(jlH);
		jpBegin.add(jcbBeginM);jpBegin.add(jlM);
		//結束時間
		jpEnd = new JPanel(new FlowLayout());
		jpEnd.setBounds(20, 100, 300, 30);
		add(jpEnd);
		jlEnd = new JLabel("結束時間 ：");
		jcbEndH = new JComboBox<String>(hh);
		jcbEndH.setSelectedItem(sltRowEndH);
		jcbEndM = new JComboBox<String>(mm);
		jcbEndM.setSelectedItem(sltRowEndM);
		jlH = new JLabel("點");
		jlM = new JLabel("分");
		jpEnd.add(jlEnd);jpEnd.add(jcbEndH);jpEnd.add(jlH);
		jpEnd.add(jcbEndM);jpEnd.add(jlM);
		
		//標題
		jlTitle = new JLabel("標題 ：");
		jlTitle .setBounds(40,135,70,25);
		jtfTitle  = new JTextField(sltRowTitle);
		jtfTitle .setBounds(90,135,280,25);
		add(jlTitle );add(jtfTitle );
		
		//按鈕
		back = new JButton("返回");
		back.setBounds(20,0,50,35);
		back.addActionListener(this);
		
		delete = new JButton("刪除");
		delete.setBounds(120,0,50,35);
		delete.addActionListener(this);

		update = new JButton("更改");
		update.setBounds(240, 0, 50, 35);
		update.addActionListener(this);

		jpButton.add(back);jpButton.add(delete);jpButton.add(update);
		
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "返回") {
			dispose();
		}else if(e.getActionCommand() == "刪除") {
			deleteGame(sltRow);
		}else if(e.getActionCommand() == "更改") {
			sltSite = jcbSite.getSelectedItem().toString();
			 if(sltSite.equals("東海大學")) {
				 sltSiteCode = "TH";
			 }else if(sltSite.equals("中興大學")) {
				 sltSiteCode = "CH";
			 }else if(sltSite.equals("石牌公園")) {
				 sltSiteCode = "SP";
			 }
			 
			 sltDate = jtfYear.getText()
					 +jcbMon.getSelectedItem().toString()
					 +jcbDay.getSelectedItem().toString();
			 sltBegin = jcbBeginH.getSelectedItem().toString()
					 	+jcbBeginM.getSelectedItem().toString();
			 sltEnd = jcbEndH.getSelectedItem().toString()
					 +jcbEndM.getSelectedItem().toString();
			 sltTitle = jtfTitle.getText();
			updateToSite(sltSiteCode,sltSite,sltDate,sltBegin,sltEnd,sltTitle);
		}
	}
	private void deleteGame(String sltRow) {
		System.out.println(sltRow);

		 Connection conn = null;
		 PreparedStatement pstmt = null;
		
		 try {
			   Properties prop =  new Properties();
			   prop.put("user", Common.DB_ACCOUNT);
			   prop.put("password", Common.DB_PASSWORD);
			   conn = DriverManager.getConnection(
					     Common.DB_URL, prop);
			   pstmt = conn.prepareStatement( "DELETE FROM site WHERE id = ? ");
			   
			   pstmt.setString(1, sltRow);
			   pstmt.executeUpdate();
			   pstmt.close();
			   
			   volleyball.tableModel.renew();
			   volleyball.jt1.updateUI();
			   volleyball.jt1.clearSelection();
			   
			   dispose();
			   JOptionPane.showMessageDialog(null,"刪除成功");

		 }catch(Exception e) {
			 System.out.println(e.toString());
		 }
	}
	
	private void getRowData(String sltRow) {
		
		Properties prop =  new Properties();
		   prop.put("user", Common.DB_ACCOUNT);
		   prop.put("password", Common.DB_PASSWORD);
		  
		
		try {
			 Connection conn = DriverManager.getConnection(Common.DB_URL, prop);
		PreparedStatement pstmt = conn.prepareStatement(
				"SELECT id,site,"
				+ "YEAR(date),LPAD(MONTH(date),2,'0'),LPAD(DAY(date),2,'0'),"
				+ "DATE_FORMAT(time_begin,'%H'),DATE_FORMAT(time_begin,'%i'),"
				+ "DATE_FORMAT(time_end,'%H'),DATE_FORMAT(time_end,'%i'),"
				+ "title "
				+ "FROM site WHERE id = ?" );	
		pstmt.setString(1, sltRow);
		rs = pstmt.executeQuery();
		rs.next();
		int a =1;
		sltRowId = rs.getString(a++);
		sltRowSite = rs.getString(a++);
		sltRowDateY = rs.getString(a++);
		sltRowDateM = rs.getString(a++);
		sltRowDateD = rs.getString(a++);
		sltRowBeginH = rs.getString(a++);
		sltRowBeginM = rs.getString(a++);
		sltRowEndH = rs.getString(a++);
		sltRowEndM = rs.getString(a++);
		sltRowTitle = rs.getString(a);
		
		rs.close();
		pstmt.close();
		
		}catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public void updateToSite(String sltSiteCode,String sltSite,String sltDate ,String sltBegin ,String sltEnd ,String sltTitle) {
		  Connection conn = null;
		  PreparedStatement pstmt = null;
		  int i = 1;
		  
		  try {
			  Properties prop =  new Properties();
			   prop.put("user", Common.DB_ACCOUNT);
			   prop.put("password", Common.DB_PASSWORD);
			   conn = DriverManager.getConnection(
					     Common.DB_URL, prop);
		  
		    pstmt = conn.prepareStatement(
		      " UPDATE  site  "
		      + " SET site = ? ,"
		      + " date = ? ,"
		      + " time_begin = CONCAT(?,'00'),"
		      + " time_end = CONCAT(?,'00'),"
		      + " title = ?"
		      + " WHERE id = ?");
		   
		   
		   pstmt.setString(i++,sltSite);
		   pstmt.setString(i++,sltDate);
		   pstmt.setString(i++,sltBegin);
		   pstmt.setString(i++,sltEnd);
		   pstmt.setString(i++,sltTitle);
		   pstmt.setString(i,sltRowId);
		   pstmt.executeUpdate();
		   
		   pstmt.close();
		   
		   volleyball.tableModel.renew();
		   volleyball.jt1.updateUI();

		   dispose();
		   JOptionPane.showMessageDialog(null,"更改成功");

		  } catch(SQLException e) {
		   System.out.println(e.toString());
		  } finally {
		   try {
		    if(conn != null) {
		     conn.close();
		    }
		   } catch(Exception e) {
		    e.printStackTrace();
		   }
		  }
	}

}
