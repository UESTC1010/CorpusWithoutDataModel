package tutorial.common.guide;

import java.awt.Color;
import java.awt.Font;

import tutorial.common.guide.Dockabletest.EditorDockable;
import tutorial.common.guide.Dockabletest.EditorFactory;
import tutorial.common.guide.Dockabletest.EditorLayout;
import tutorial.support.JTutorialFrame;
import tutorial.support.Tutorial;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;

@Tutorial(title="词性集合",id="词性集合")
public class WordCharacters {

	JTutorialFrame frame = new JTutorialFrame( WordCharacters.class );
	static String fileContent=null;
	EditorFactory factory = new EditorFactory();
	EditorLayout editorLayout=new EditorLayout("词性       编码", fileContent, new Color( 255, 200, 200 ));
	EditorDockable editorDockable=new EditorDockable(factory,editorLayout);

    //菜单栏字体
	Font font1=new Font("微软雅黑",Font.PLAIN,15);
	public WordCharacters(){
		CControl control = new CControl( frame );
		frame.destroyOnClose( control );
		frame.add( control.getContentArea() );
		frame.setBounds(60, 60, 500, 600);
		
		/* We need to install our EditorFactory early on, otherwise the framework will not
		 * allow us to register the EditorDockables. */
		control.addMultipleDockableFactory( "file-editor", factory );
		
		/* We now create some dockables and drop them onto the content-area */
		CGrid grid = new CGrid( control );
		grid.add( 0, 0, 1, 1,editorDockable);
		control.getContentArea().deploy( grid );
		String text1="词性               标记\n"+"形容词                /a\n"+"副形词                /ad\n"+"形容词性语素                /ag\n"+"形容词性惯用语                /al\n"+"名形词                /an\n"
				+"区别词                /b\n"+"仅用于终##终                /begin\n"+"区别语素                /bg\n"+"区别词性惯用语素                /bl\n"+"连词                /c\n"+"并列连词                /cc\n"
				+"副词                /d\n"+"副语素词                /dg\n"+"连语                /dl\n"+"动词                /v\n"+"叹词                /e\n"+"仅用于始##始                /end\n"
				+"方位词                /f\n"+"学术词汇                /g\n"+"生物相关词                /gb\n"+"动词                /v\n"+"生物类别                /gbc\n"+"化学相关词                /gc\n"
				+"地理地址相关词                /gg\n"+"动词                /v\n"+"计算机相关词                /gi\n"+"数学相关词                /gm\n"+"物理相关词                /gp\n"+"前缀                /h\n"
				+"成语                /i\n"+"简称略语                /j\n"+"后缀                /k\n"+"习用语                /l\n"+"数词                /m\n"+"数语素                /mg\n"
				+"甲乙丙丁之类的数词                /Mg\n"+"数量词                /mq\n"+"名词                /n\n"+"生物名                /nb\n"+"动物名                /nba\n"+"动物纲目                /nbc\n"
				+"植物名                /nbp\n"+"食品                /nf\n"+"名词性语素                /ng\n"+"医药疾病等健康相关名词                /nh\n"+"疾病                /nhb\n"+"药品                /nhm\n"
				+"机构相关                /ni\n"+"下属机构                /nic\n"+"机构后缀                /nis\n"+"教育相关机构                /nit\n"+"名词性惯用语                /nl\n"+"物品名                /nm\n"
				+"工作相关名词                /nn\n"+"职业                /nnd\n"+"职务职称                /nnt\n"+"人名                /nr\n"+"复姓                /nr1\n"+"蒙古姓名                /nr2\n"
				+"音译人名                /nrf\n"+"日语人名                /nrj\n"+"化学品名                /nmc\n"+"地名                /ns\n"+"音译地名                /nsf\n"+"机构团体名                /nt\n"
				+"公司名                /ntc\n";
		editorDockable.text.setFont(font1);
		editorDockable.text.setText(text1);
		editorDockable.text.setCaretPosition(editorDockable.text.getText().indexOf(text1));
		frame.setVisible( true );		
	}
	public static void main(String[] args){
		new WordCharacters();
	}
}
