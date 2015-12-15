package tutorial.common.guide;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import tutorial.common.guide.Dockabletest.EditorDockable;
import tutorial.common.guide.Dockabletest.EditorFactory;
import tutorial.common.guide.Dockabletest.EditorLayout;
import tutorial.support.JTutorialFrame;
import tutorial.support.Tutorial;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;

@Tutorial(title="摘要",id="摘要")
public class SummaryText extends WindowAdapter implements ActionListener{
	JTutorialFrame frame = new JTutorialFrame( SummaryText.class );
	static String fileContent=null;
	EditorFactory factory = new EditorFactory();
	EditorLayout editorLayout=new EditorLayout("Summary", fileContent, new Color( 255, 200, 200 ));
	EditorDockable editorDockable=new EditorDockable(factory,editorLayout);
	
	JMenuBar menubar = new JMenuBar();
	Font font=new Font("微软雅黑",Font.PLAIN,13);//菜单栏字体
	Font font1=new Font("微软雅黑",Font.PLAIN,15);
	JMenu menuFile1=new JMenu("文件");
	JMenuItem clear=new JMenuItem("Clear");
	JMenuItem submit=new JMenuItem("Submit");
	public SummaryText(){
		CControl control = new CControl( frame );
		frame.destroyOnClose( control );
		frame.add( control.getContentArea() );
		frame.setBounds(200, 300, 500, 300);
		
		/* We need to install our EditorFactory early on, otherwise the framework will not
		 * allow us to register the EditorDockables. */
		control.addMultipleDockableFactory( "file-editor", factory );
		
		/* We now create some dockables and drop them onto the content-area */
		CGrid grid = new CGrid( control );
		grid.add( 0, 0, 1, 1,editorDockable);
		control.getContentArea().deploy( grid );
		menuFile1.setFont(font);
		clear.setFont(font);
		submit.setFont(font);
		menubar.add(menuFile1);
		menuFile1.add(clear);
		menuFile1.add(submit);
		clear.addActionListener(this);
		submit.addActionListener(this);
		editorDockable.text.setFont(font1);
		frame.setJMenuBar(menubar);
		frame.setVisible( true );		
	}
	public static void main(String[] args){
		new SummaryText();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==submit){
			frame.dispose();
		}else if(e.getSource()==clear){
			editorDockable.text.setText("");
		}
	}
}
