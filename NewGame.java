package tw.myproject;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class NewGame extends JFrame implements ActionListener{
	private Volleyball volleyball ;
	private String id ;
	private JLabel jlSite,jlDate,jlYear,jlMon,jlDay,jlBegin,jlEnd,jlH,jlM,jlTitle;
	private JTextField jtfYear,jtfTitle;
	private List<String> siteList ;
	private String[] site = {"中興大學","東海大學","石牌公園"};
	private String[] mon = {"01","02","03","04","05","06","07","08","09","10","11","12"};
	private String[] day = {"01","02","03","04","05","06","07","08","09","10","11","12",
							"13","14","15","16","17","18","19","20","21","22","23","24",
							"25","26","27","28","29","30","31"};
	private String[] hh = {"01","02","03","04","05","06","07","08","09","10","11","12",
							"13","14","15","16","17","18","19","20","21","22","23","00"};
	private String[] mm = {"00","30"};

	private JComboBox<String> jcbSite,jcbMon,jcbDay,jcbBeginH,jcbBeginM,jcbEndH,jcbEndM;
	private JButton jbBack,jbFinish;
	private JPanel jpSite,jpDate,jpBegin,jpEnd;
	
	private String sltSite;
	private String sltDate;
	private String sltBegin;
	private String sltEnd;
	private String sltTitle;
	private String sltSiteCode;
	

	public NewGame(Volleyball volleyball , String id , String[] site) {
		
	super("我要開團");
	this.id = id ;
	this.volleyball = volleyball;
	
	setLayout(null);
	setVisible(true);
	setBounds(650,300,400,250);
	setResizable(false);
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	
	//場地下拉選單
	jpSite = new JPanel(new FlowLayout(0));
	jpSite.setBounds(35, 15, 300, 30);
	jlSite = new JLabel("場地 :");
	jcbSite = new JComboBox<String>(this.site);
	add(jpSite);jpSite.add(jlSite);jpSite.add(jcbSite);
	
	//日期 年月日
	jpDate = new JPanel(new FlowLayout(0));
	jpDate.setBounds(180,43,200,30);
	add(jpDate);
	
	jlDate = new JLabel("日期 ：");
	jlDate.setBounds(40,50,50, 20);
	jtfYear = new JTextField();
	jtfYear.setBounds(85,50,70, 20);
	jlYear = new JLabel("年");
	jlYear.setBounds(160,50,50, 20);
	jcbMon = new JComboBox<String>(mon);
	jlMon = new JLabel("月");
	jcbDay = new JComboBox<String>(day);
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
	jcbBeginM = new JComboBox<String>(mm);
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
	jcbEndM = new JComboBox<String>(mm);
	jlH = new JLabel("點");
	jlM = new JLabel("分");
	jpEnd.add(jlEnd);jpEnd.add(jcbEndH);jpEnd.add(jlH);
	jpEnd.add(jcbEndM);jpEnd.add(jlM);
	
	//標題
	jlTitle = new JLabel("標題 ：");
	jlTitle .setBounds(40,135,70,25);
	jtfTitle  = new JTextField();
	jtfTitle .setBounds(90,135,280,25);
	add(jlTitle );add(jtfTitle );
	
	//返回按鈕
	jbBack = new JButton("返回");
	jbBack.setBounds(110, 170, 60, 35);
	add(jbBack);
	jbBack.addActionListener(this);
	
	//確認按鈕
	jbFinish = new JButton("確認");
	jbFinish.setBounds(250, 170, 60, 35);
	add(jbFinish);
	jbFinish.addActionListener(this);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "返回") {
			dispose();
		}else if(e.getActionCommand() == "確認") {
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
	public void updateToSite(String sltSiteCode,String sltSite,String sltDate ,String sltBegin ,String sltEnd ,String sltTitle) {
		  Connection conn = null;
		  PreparedStatement ps, pstmt = null;
		  ResultSet rs = null;
		  int i = 1;
		  
		  try {
		   Properties prop = new Properties();
		   prop.put("user", Common.DB_ACCOUNT);
		   prop.put("password", Common.DB_PASSWORD);
		   conn = DriverManager.getConnection(Common.DB_URL, prop);
		   String sql = " SELECT MAX(id) AS idd FROM site WHERE id LIKE '" + sltSiteCode + "%' ";
		   
		   ps = conn.prepareStatement(
		     sql,
		     ResultSet.TYPE_SCROLL_INSENSITIVE,
		     ResultSet.CONCUR_READ_ONLY
		     );
		   
		   rs = ps.executeQuery();
		   
		   
		   if(rs.next() && rs.getString("idd") != null) {
		    pstmt = conn.prepareStatement(
		      " INSERT INTO site (id,site,date,time_begin,time_end,title,invite) "
		      + " VALUE ( "
		         + " CONCAT(? , "
		      + " (SELECT SEQ FROM "
		      + " (SELECT CONCAT(YEAR(NOW()), LPAD(MONTH(NOW()),2,'0'), LPAD(DAY(NOW()),2,'0'), LPAD((CAST( SUBSTRING( "
		      + " (SELECT idd FROM(SELECT MAX(id) AS idd FROM site WHERE id LIKE '" + sltSiteCode + "%') AS compareid) ,11,4)AS SIGNED)+1),4,'0') "
		      + " ) AS SEQ ) AS NEW_SEQ )) "
		      + ",?,?,CONCAT(?,'00'),CONCAT(?,'00'),?,(SELECT na FROM(SELECT  username AS na FROM userinfor WHERE id = ? )AS na2)) ");
		   
		   } else {
		    pstmt = conn.prepareStatement(
		      " INSERT INTO site (id,site,date,time_begin,time_end,title,invite) "
		      + " VALUE ( "
		         + " CONCAT(? , "
		      + " (SELECT SEQ FROM "
		      + " (SELECT CONCAT(YEAR(NOW()), LPAD(MONTH(NOW()),2,'0'), LPAD(DAY(NOW()),2,'0'),'0001' ) AS SEQ ) AS NEW_SEQ )) "
		      + " ,?,?,CONCAT(?,'00'),CONCAT(?,'00'),?,(SELECT na FROM(SELECT  username AS na FROM userinfor WHERE id = ? )AS na2))");
		   }
		   
		   pstmt.setString(i++,sltSiteCode);
		   pstmt.setString(i++,sltSite);
		   pstmt.setString(i++,sltDate);
		   pstmt.setString(i++,sltBegin);
		   pstmt.setString(i++,sltEnd);
		   pstmt.setString(i++,sltTitle);
		   pstmt.setString(i,this.id);
		   pstmt.executeUpdate();
		   
		   rs.close();
		   ps.close();
		   pstmt.close();
		   
		   volleyball.tableModel.renew();
		   volleyball.jt1.updateUI();

		   dispose();
		   JOptionPane.showMessageDialog(null,"開團成功");

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
