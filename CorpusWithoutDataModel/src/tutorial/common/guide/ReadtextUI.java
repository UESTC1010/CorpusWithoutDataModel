package tutorial.common.guide;

import java.awt.Button;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;

import bibliothek.gui.dock.common.CControl;
import tutorial.support.JTutorialFrame;
import tutorial.support.Tutorial;

@Tutorial(title="根据id读取不同collection中的语料",id="摘要")
public class ReadtextUI extends WindowAdapter implements ActionListener{

	JTutorialFrame frame = new JTutorialFrame( ReadtextUI.class );
	Panel panel1=new Panel();
	Label L1=new Label("语料类型");
	Label L2=new Label("id");
	TextField tf1=new TextField("corpustype");
	TextField tf2=new TextField("id");
	Button btn1=new Button("确定");
	public ReadtextUI(){
		panel1.add(L1);
		panel1.add(tf1);
		panel1.add(L2);
		panel1.add(tf2);
		panel1.add(btn1);
		
		frame.add(panel1);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 200);
		frame.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args){
		new ReadtextUI();
	}
}
