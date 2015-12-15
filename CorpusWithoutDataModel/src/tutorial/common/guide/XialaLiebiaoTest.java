package tutorial.common.guide;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
//在单元格中添加基本组件 下拉框
@SuppressWarnings("serial")
public class XialaLiebiaoTest extends JFrame implements ActionListener{
	
	JPanel jp1,jp2,jp3;
	JComboBox<Object> jcb1;
	JTextField jtf;
	JLabel Jlb1,Jlb2;
	JButton btn1;
	JButton btn2;
	long readId;
	String readType;
	//Long tag = -1L;
	//Readtext readtext1=new Readtext();
	public XialaLiebiaoTest(){
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		Jlb1=new JLabel("所查询语料的类型:");
		Jlb2=new JLabel("所查询语料的id:");
		String t1[]={"熟语料","生语料"};
		Font font=new Font("微软雅黑",Font.PLAIN,14);
		Jlb1.setFont(font);
		Jlb2.setFont(font);
		jcb1=new JComboBox<Object>(t1);
		jcb1.setFont(font);
		jcb1.setPreferredSize(new Dimension(105,30));
		jtf=new JTextField("1",11);
		jp1.add(Jlb1);
		jp1.add(jcb1);
		jp2.add(Jlb2);
		jp2.add(jtf);
		btn1=new JButton("确定");
		btn2=new JButton("取消");
		btn1.setFont(font);
		btn2.setFont(font);
		jp3.add(btn1);
		jp3.add(btn2);
		this.setLayout(new GridLayout(3,1));//将组建设置为3行1列的网格布局
	
		this.add(jp1);
		this.add(jp2);
		this.add(jp3);
		this.setSize(300,200);
		this.setLocation(800, 400);
		//this.setBackground(Color.DARK_GRAY);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		btn1.addActionListener(this);
		btn2.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==btn1){
			String s=(String)jcb1.getSelectedItem();
			if(s==null){
				JOptionPane.showMessageDialog(null, "请选择所查询语料的类型");
				return;
			}
			setReadtextType(s);
			String s2=null;
			s2=jtf.getText();
			if(s2==null){
				JOptionPane.showMessageDialog(null, "请输入所查询语料的id");
				return;
			}else if(s2!=null){
				setReadtextId(Long.parseLong(s2));
			}			
			System.out.println(getReadtextType());
			System.out.println(Long.parseLong(s2));
			//tag = Long.parseLong(s2);
			this.setVisible(false);
//			Dockabletest doctest=new Dockabletest();
//			try {
//				doctest.readMongo1();
//			} catch (UnknownHostException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
		}else if(e.getSource()==btn2){
			String s=(String)jcb1.getSelectedItem();
			if(s==null){
				return;
			}
			String s2=jtf.getText();
			if(s2==null){
				return;
			}
		}
	}
	
	public void setReadtextId(long id){
		this.readId=id;
	}
	public long getReadtextId(){
		return readId;
	}
	public String getReadtextType(){
		return readType;
	}
	public void setReadtextType(String readtype){
		this.readType=readtype;
	}
	
	public static void main(String[] args)
	{
		//new XialaLiebiaoTest();
	}
}
