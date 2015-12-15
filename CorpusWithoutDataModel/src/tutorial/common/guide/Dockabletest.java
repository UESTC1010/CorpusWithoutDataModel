package tutorial.common.guide;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.google.gson.Gson;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;
import bibliothek.gui.dock.common.DefaultMultipleCDockable;
import bibliothek.gui.dock.common.MultipleCDockableFactory;
import bibliothek.gui.dock.common.MultipleCDockableLayout;
import bibliothek.util.xml.XElement;
import tutorial.support.JTutorialFrame;
import tutorial.support.Tutorial;

//version1.0
//@Tutorial( id="MultipleDockables", title="\"Editors\": MultipleCDockable")
@Tutorial(title = "语料库标注系统", id = "语料库标注系统")
public class Dockabletest extends WindowAdapter implements ActionListener {

	JMenuBar menubar = new JMenuBar();
	Font font = new Font("微软雅黑", Font.PLAIN, 13);// 菜单栏字体
	Font font1 = new Font("微软雅黑", Font.PLAIN, 15);
	// 构建菜单栏项
	JMenu menuFile1 = new JMenu("文件");
	JMenu menuFile2 = new JMenu("自动标注");
	JMenu menuFile3 = new JMenu("手动标注");
	JMenu menuFile4 = new JMenu("自然语言处理");
	JMenu menuFile5 = new JMenu("帮助");
	JMenu menuFile6=new JMenu("撤销");
	// 定义子菜单项
	// 菜单1(menuFile1)的子菜单
	JMenuItem newFile = new JMenuItem("new");
	JMenuItem openFile = new JMenuItem("open");
	JMenuItem saveFile = new JMenuItem("save");
	JMenuItem readFile=new JMenuItem("read");
	JMenuItem deleteFile=new JMenuItem("delete");
	// 菜单2(menuFile2)的子菜单
	JMenuItem tagging = new JMenuItem("tagging");
	JMenuItem keyword = new JMenuItem("keyword");
	JMenuItem keysentence = new JMenuItem("keysentence");
	JMenuItem summary=new JMenuItem("summary");
	JMenuItem all = new JMenuItem("all");
	// 菜单3(menuFile3)的子菜单
	JMenuItem noun=new JMenuItem("/n");
	JMenuItem adjective=new JMenuItem("/a");
	JMenuItem adverb=new JMenuItem("/ad");
	JMenuItem verb=new JMenuItem("/v");
	JMenuItem ncs=new JMenuItem("/ncs");
	// 菜单4(menuFile4)的子菜单
	JMenuItem mKeyword = new JMenuItem("mKeyword");
	JMenuItem mSentence = new JMenuItem("mSentence");
	JMenuItem mSummary = new JMenuItem("mSummary");
	// 菜单5(menuFile5)的子菜单项
	JMenuItem wtCollection = new JMenuItem("词性集");
	JMenuItem clearData = new JMenuItem("清空");
	JMenuItem addCharacteristic = new JMenuItem("添加词性");
	JMenuItem test = new JMenuItem("测试");
	JMenuItem showAll = new JMenuItem("showAll");
	JMenuItem setWordCol=new JMenuItem("设置标注的颜色");
	JMenuItem setCorpusType=new JMenuItem("设置语料类型");
	//菜单6，撤销
	JMenuItem revokeKeyword = new JMenuItem("撤销关键词");
	JMenuItem revokeSentence = new JMenuItem("撤销关键句");
	JMenuItem revokeSummary=new JMenuItem("撤销摘要");
	JMenuItem revokeTagging=new JMenuItem("撤销标注");
	//随机颜色代码
	static int r;
	static int g;
	static int b;
	// 数据区数据
	static String fileContent0 = null;
	static String fileContent1 = null;// 元数据区数据
	static String fileContent2 = null;// 网络数据区数据
	static String fileContent3 = null;// 自然处理数据区数据
	static String fileContent4 = null;// 其他
	static String fileContent5 = null;// summary
	// 提示词
	static String data1 = "元数据:";
	String data2 = "分词结果:";
	String data3 = "关键词提取:";
	String data4 = "关键句提取:";
	String data5 = "摘要:";
	String data6 = "关键词数:";
	String data7 = "关键句数:";
	// 处理后的数据
	static String getSummary=null;
	static String summaryText = null;
	static int keywordNum = 0;
	static int sentenceNum = 0;
	static int keywordFre=0;
	static ArrayList<Integer> keywordFreList=new ArrayList<Integer>();
	static String addKeyword = null;
	static String addKeysentence = null;
	static String metaData=null;
	static List<String> keywordList = new ArrayList<String>();
	static List<String> sentenceList = new ArrayList<String>();
	static List<Term> termList = new ArrayList<Term>();
	static List<String> termListTestCol=new ArrayList<String>();
	static List<String> summaryList = new ArrayList<String>();
    static String TermListString1=null;
	static EditorFactory factory = new EditorFactory();
	static EditorLayout editorLayout1 = new EditorLayout("Metadata",fileContent1, new Color(255, 200, 200));
	static EditorDockable editorDockable1 = new EditorDockable(factory,editorLayout1);
	EditorLayout editorLayout2 = new EditorLayout("InternetData", fileContent2,new Color(255, 255, 200));
	EditorDockable editorDockable2 = new EditorDockable(factory, editorLayout2);
	EditorLayout editorLayout3 = new EditorLayout("HLPData", fileContent3,new Color(200, 255, 200));
	EditorDockable editorDockable3 = new EditorDockable(factory, editorLayout3);
	EditorLayout editorLayout4 = new EditorLayout("Others", fileContent4,new Color(200, 200, 255));
	EditorDockable editorDockable4 = new EditorDockable(factory, editorLayout4);
	EditorLayout editorLayout5 = new EditorLayout("Summary", fileContent5,new Color(200, 200, 255));
	EditorDockable editorDockable5 = new EditorDockable(factory, editorLayout5);
	
	//判断保存到mongo还是读取mongo
	//static boolean mm=true;
	//用于读取mongo中不同collection中的不同id
//	static Readtext readtext2=new Readtext();
//    static long id2=-1;
//    static long id1=-1;
//    static String corpustype1=null;
	String Readcorpustype="熟语料";
	long id=0;
	DBObject ReadDbobject;
	public Dockabletest() {
		JTutorialFrame frame = new JTutorialFrame(Dockabletest.class);
		CControl control = new CControl(frame);
		frame.destroyOnClose(control);
		frame.add(control.getContentArea());
		frame.setBounds(260, 80, 900, 700);

		/*
		 * We need to install our EditorFactory early on, otherwise the
		 * framework will not allow us to register the EditorDockables.
		 */
		control.addMultipleDockableFactory("file-editor", factory);

		/* We now create some dockables and drop them onto the content-area */
		CGrid grid = new CGrid(control);
		grid.add(0, 0, 1, 0.6, editorDockable1);
		grid.add(0, 0.6, 1, 0.4, editorDockable4);
		grid.add(1, 0, 1, 0.2, editorDockable2);
		grid.add(1, 0.2, 1, 0.3, editorDockable5);
		grid.add(1, 0.6, 1, 0.5, editorDockable3);
		control.getContentArea().deploy(grid);

		/*
		 * The CLayoutChoiceMenuPiece creates a dynamic menu which allows us to
		 * save and load the layout. In doing so we will use the EditorFactory.
		 */
		// 给菜单栏添加菜单
		menuFile1.setFont(font);
		menuFile1.setPreferredSize(new Dimension(50, 25));
		menuFile2.setFont(font);
		menuFile2.setPreferredSize(new Dimension(80, 25));
		menuFile3.setFont(font);
		menuFile3.setPreferredSize(new Dimension(80, 25));
		menuFile4.setFont(font);
		menuFile4.setPreferredSize(new Dimension(100, 25));
		menuFile5.setFont(font);
		menuFile5.setPreferredSize(new Dimension(60, 25));
		menuFile6.setFont(font);
		menuFile6.setPreferredSize(new Dimension(50, 25));
		menubar.add(menuFile1);
		menubar.add(menuFile2);
		menubar.add(menuFile3);
		menubar.add(menuFile4);
		menubar.add(menuFile6);
		menubar.add(menuFile5);
		
		// 给菜单1添加子菜单
		newFile.setFont(font);
		menuFile1.add(newFile);
		newFile.addActionListener(this);
		menuFile1.add(openFile);
		openFile.addActionListener(this);
		openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_MASK));
		menuFile1.addSeparator();
		menuFile1.add(saveFile);
		saveFile.addActionListener(this);
		menuFile1.add(readFile);
		readFile.addActionListener(this);
		menuFile1.add(deleteFile);
		deleteFile.addActionListener(this);
		// 给菜单2添加子菜单
		menuFile2.add(tagging);
		tagging.addActionListener(this);
		tagging.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,InputEvent.CTRL_MASK));
		menuFile2.add(keyword);
		keyword.addActionListener(this);
		keyword.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K,InputEvent.CTRL_MASK));
		menuFile2.add(keysentence);
		keysentence.addActionListener(this);
		keysentence.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_MASK));
		menuFile2.add(summary);
		summary.addActionListener(this);
		summary.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK));
		menuFile2.addSeparator();
		menuFile2.add(all);
		all.addActionListener(this);
		// 给菜单3添加子菜单
		menuFile3.add(noun);
		noun.addActionListener(this);
		noun.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_MASK|InputEvent.SHIFT_MASK));
		menuFile3.add(adjective);
		adjective.addActionListener(this);
		adjective.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,InputEvent.CTRL_MASK|InputEvent.SHIFT_MASK));
		menuFile3.add(adverb);
		adverb.addActionListener(this);
		adverb.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,InputEvent.CTRL_MASK|InputEvent.SHIFT_MASK));
		menuFile3.add(verb);
		verb.addActionListener(this);
		verb.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,InputEvent.CTRL_MASK|InputEvent.SHIFT_MASK));
		menuFile3.add(ncs);
		ncs.addActionListener(this);
		// 给菜单4添加子菜单
		menuFile4.add(mKeyword);
		mKeyword.addActionListener(this);
		mKeyword.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,InputEvent.CTRL_MASK|InputEvent.SHIFT_MASK));
		menuFile4.add(mSentence);
		mSentence.addActionListener(this);
		mSentence.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,InputEvent.CTRL_MASK|InputEvent.SHIFT_MASK));
		menuFile4.add(mSummary);
		mSummary.addActionListener(this);
		mSummary.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK|InputEvent.SHIFT_MASK));
		// 给菜单5添加子菜单
		menuFile5.add(wtCollection);
		wtCollection.addActionListener(this);
		menuFile5.add(addCharacteristic);
		addCharacteristic.addActionListener(this);
		menuFile5.add(clearData);
		clearData.addActionListener(this);
		menuFile5.addSeparator();
		menuFile5.add(showAll);
		showAll.addActionListener(this);
		menuFile5.addSeparator();
		menuFile5.add(test);
		test.addActionListener(this);
		menuFile5.add(setWordCol);
		setWordCol.addActionListener(this);
		menuFile5.addSeparator();
		menuFile5.add(setCorpusType);
		setCorpusType.addActionListener(this);
		//给菜单6添加子菜单
		menuFile6.add(revokeKeyword);
		revokeKeyword.addActionListener(this);
		menuFile6.add(revokeSentence);
		revokeSentence.addActionListener(this);
		menuFile6.add(revokeSummary);
		revokeSummary.addActionListener(this);
		menuFile6.add(revokeTagging);
		revokeTagging.addActionListener(this);
		// 设置fileContent文本字体
		editorDockable1.text.setFont(font1);
		editorDockable2.text.setFont(font1);
		editorDockable3.text.setFont(font1);
		editorDockable4.text.setFont(font1);
		editorDockable5.text.setFont(font1);
		frame.setJMenuBar(menubar);
		
		UIManager.put("OptionPane.messageFont", font);
		UIManager.put("OptionPane.buttonFont", font);

		editorDockable2.text.setEditable(false);
		editorDockable3.text.setEditable(false);
		editorDockable4.text.setEditable(false);
		/* and finally we can start the application */
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static class EditorDockable extends DefaultMultipleCDockable {
		// JTextArea text;
		JTextPane text;

		public EditorDockable(EditorFactory factory, EditorLayout layout) {
			/*
			 * it is mandatory to set the factory, the EditorDockable cannot be
			 * created without it.
			 */
			super(factory);

			/* and then we just set up the editor */
			setTitleText(layout.getFileName());

			// text = new JTextArea();
			text = new JTextPane();
			text.setText(layout.getFileContent());
			text.setBackground(layout.getBackground());
			add(new JScrollPane(text));
			// text.setLineWrap(true);
		}

		/*
		 * This convenient method allows us to grab the entire content of this
		 * dockable in one step.
		 */
		public EditorLayout getLayout() {
			return new EditorLayout(getTitleText(), text.getText(),
					text.getBackground());
		}
	}

	public static class EditorFactory implements
			MultipleCDockableFactory<EditorDockable, EditorLayout> {
		/*
		 * An empty layout is required to read a layout from an XML file or from
		 * a byte stream
		 */
		public EditorLayout create() {
			return new EditorLayout();
		}

		/*
		 * An optional method allowing to reuse 'dockable' when loading a new
		 * layout
		 */
		public boolean match(EditorDockable dockable, EditorLayout layout) {
			return dockable.getLayout().equals(layout);
		}

		/* Called when applying a stored layout */
		public EditorDockable read(EditorLayout layout) {
			return new EditorDockable(this, layout);
		}

		/* Called when storing the current layout */
		public EditorLayout write(EditorDockable dockable) {
			return dockable.getLayout();
		}
	}

	public static class EditorLayout implements MultipleCDockableLayout {
		private String fileName;
		private String fileContent;
		private Color background;

		public EditorLayout() {
			// nothing
		}

		public EditorLayout(String fileName, String fileContent,
				Color background) {
			this.fileName = fileName;
			this.fileContent = fileContent;
			this.background = background;
		}

		public String getFileName() {
			return fileName;
		}

		public String getFileContent() {
			return fileContent;
		}

		public Color getBackground() {
			return background;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			EditorLayout other = (EditorLayout) obj;
			return equals(background, other.background)
					&& equals(fileName, other.fileName)
					&& equals(fileContent, other.fileContent);
		}

		private boolean equals(Object a, Object b) {
			if (a == null) {
				return b == null;
			} else {
				return a.equals(b);
			}
		}

		public void readStream(DataInputStream in) throws IOException {
			fileName = in.readUTF();
			fileContent = in.readUTF();
			background = new Color(in.readInt());
		}

		public void readXML(XElement element) {
			fileName = element.getElement("name").getString();
			fileContent = element.getElement("content").getString();
			background = new Color(element.getElement("background").getInt());
		}

		public void writeStream(DataOutputStream out) throws IOException {
			out.writeUTF(fileName);
			out.writeUTF(fileContent);
			out.writeInt(background.getRGB());
		}

		public void writeXML(XElement element) {
			element.addElement("name").setString(fileName);
			element.addElement("content").setString(fileContent);
			element.addElement("background").setInt(background.getRGB());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == openFile) {
			clearData();
			JFileChooser fc = new JFileChooser();
			fc.showOpenDialog(null);
			int value = 0;
			if (value == JFileChooser.APPROVE_OPTION) {
				String filename = null;
				String filedirectory = null;
				String file_and_directoryname = null;
				if (fc.getSelectedFile() == null) {
					return;
				}
				filename = fc.getSelectedFile().getName();
				filedirectory = fc.getCurrentDirectory().toString();
				file_and_directoryname = filedirectory + "/" + filename;
				try {
					FileInputStream filein = new FileInputStream(
							file_and_directoryname);
					byte[] b = new byte[filein.available()];
					filein.read(b);
					fileContent0 = new String(b);
					fileContent1 = fileContent0.replace(" ", "");
					editorDockable1.text.setText(fileContent1);
					filein.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getSource() == all) {
			if (fileContent1 == null || fileContent1 == ""|| fileContent1 == " ") {
				JOptionPane.showMessageDialog(null, "请输入待处理文本");
				fileContent1 = null;
			} else if (fileContent1 != null||fileContent1!="") {
				//NLPTokenizer.SEGMENT.enableNumberQuantifierRecognize(true);
				//NLPTokenizer.SEGMENT.enableOffset(true);
				String simpleContent = HanLP.convertToSimplifiedChinese(fileContent1);
				//termList = NLPTokenizer.segment(simpleContent);
				termList = NLPTokenizer.segment(simpleContent);
				keywordList = HanLP.extractKeyword(simpleContent, 15);
				keywordNum = keywordList.size();
				sentenceList = HanLP.extractSummary(simpleContent, 5);
				sentenceNum = sentenceList.size();
				summaryList=HanLP.extractSummary(simpleContent, 4);
				keywordFreList.clear();
				for (int i = 0; i < keywordList.size(); i++) {
					setColorss(keywordList.get(i), Color.red);
					keywordFreList.add(keywordFre);
					keywordFre=0;
				}			
				fileContent2="关键词频率数组"+keywordFreList;
				fileContent4 = data2 + "\n" + termList.toString();
				fileContent3 = data3 + "\n" + keywordList + "\n" + data6 + "\n"+ keywordNum + "\n" + data4 + "\n" + sentenceList+ "\n" + data7 + "\n" + sentenceNum+"\n"+data5+"\n"+summaryList;
				editorDockable4.text.setText(fileContent4);
				editorDockable3.text.setText(fileContent3);
				editorDockable2.text.setText(fileContent2);
				editorDockable5.text.setText(sentenceList.toString());
				editorDockable3.text.setCaretPosition(editorDockable3.text.getText().indexOf(keywordList.toString()));
				editorDockable4.text.setCaretPosition(editorDockable4.text.getText().indexOf(termList.toString()));
			}
		} else if (e.getSource() == tagging || e.getSource() == keyword
				|| e.getSource() == keysentence|| e.getSource() == summary) {
			if (fileContent1 != null) {
				//NLPTokenizer.SEGMENT.enableNumberQuantifierRecognize(true);
				String simpleContent = HanLP.convertToSimplifiedChinese(fileContent1);
				if (e.getSource() == tagging) {
					//termList = NLPTokenizer.segment(simpleContent);
					//Segment nShortSegment=new NShortSegment().enable
					termList = NLPTokenizer.segment(simpleContent);
					fileContent3 = "";
					fileContent4 = data2 + "\n" + termList.toString();
					editorDockable4.text.setText(fileContent4);
					editorDockable3.text.setText(fileContent3);
					editorDockable3.text.setCaretPosition(editorDockable3.text.getText().indexOf(fileContent3.toString()));
					editorDockable4.text.setCaretPosition(editorDockable4.text.getText().indexOf(termList.toString()));
				} else if (e.getSource() == keyword) {
					keywordList = HanLP.extractKeyword(simpleContent, 15);
					keywordNum = keywordList.size();
					fileContent3 = data3 + "\n" + keywordList + "\n" + data6+ "\n" + keywordNum;
					editorDockable3.text.setText(fileContent3);
					editorDockable4.text.setText("");
					keywordFreList.clear();
					for (int i = 0; i < keywordList.size(); i++) {
						randomColor();
						setColorss(keywordList.get(i), new Color(r,g,b));
						keywordFreList.add(keywordFre);
						keywordFre=0;
					}				
					fileContent2="关键词频率数组"+keywordFreList;
					editorDockable2.text.setText(fileContent2);
					editorDockable3.text.setCaretPosition(editorDockable3.text.getText().indexOf(keywordList.toString()));
				} else if (e.getSource() == keysentence) {
					sentenceList = HanLP.extractSummary(simpleContent, 5);
					sentenceNum = sentenceList.size();
					fileContent3 = data4 + "\n" + sentenceList + "\n" + data7+ "\n" + sentenceNum;
					editorDockable3.text.setText(fileContent3);
					editorDockable3.text.setCaretPosition(editorDockable3.text.getText().indexOf(sentenceList.toString()));
					editorDockable4.text.setText("");
				}else if(e.getSource()==summary){
					summaryList = HanLP.extractSummary(simpleContent, 5);
					//sentenceNum = summaryList.size();
					fileContent5 =Pattern.compile("\\b([\\w\\W])\\b").matcher(summaryList.toString().substring(1, summaryList.toString().length()-1)).replaceAll("$1");
					editorDockable5.text.setText(fileContent5);
					//editorDockable3.text.setCaretPosition(editorDockable3.text.getText().indexOf(sentenceList.toString()));
					editorDockable4.text.setText("");
				}
			} else {
				JOptionPane.showMessageDialog(null, "请输入待处理文本");
				fileContent1 = null;
			}
		} else if (e.getSource() == mKeyword) {
			addKeyword = editorDockable1.text.getSelectedText();
			if(addKeyword==null){
				JOptionPane.showMessageDialog(null, "请选择关键词");
			}else{
				keywordList.add(addKeyword);
				keywordNum = keywordList.size();
				fileContent3 = data3 + "\n" + keywordList + "\n" + data6 + "\n"
						+ keywordNum;
				editorDockable3.text.setText(fileContent3);
				setColorss(keywordList.get(keywordNum - 1), Color.red);
				keywordFreList.add(keywordFre);
				fileContent2 = "关键词频率" + "\n" + keywordFre + "\n" + "关键词频率数组"
						+ "\n" + keywordFreList;// 统计的关键词频率
				editorDockable2.text.setText(fileContent2);
				editorDockable3.text.setCaretPosition(editorDockable3.text
						.getText().indexOf(keywordList.toString()));
				editorDockable4.text.setText("");
				keywordFre = 0;
			}
			
		} else if (e.getSource() == mSentence) {
			addKeysentence = editorDockable1.text.getSelectedText();
			if(addKeysentence==null){
				JOptionPane.showMessageDialog(null, "请选择关键句");
			}else{
				sentenceList.add(addKeysentence);
				sentenceNum = sentenceList.size();
				fileContent3 = data4 + "\n" + sentenceList + "\n" + data7 + "\n"+ sentenceNum;
				editorDockable3.text.setText(fileContent3);
				editorDockable3.text.setCaretPosition(editorDockable3.text.getText().indexOf(data7));
				editorDockable4.text.setText("");
			}			
		} else if (e.getSource() == mSummary) {
			// new SummaryText();
			getSummary=null;
			editorDockable1.text.getCaretPosition();
			getSummary=editorDockable1.text.getSelectedText();
			editorDockable4.text.setText(getSummary);
			if(getSummary==null){
				//summaryText = editorDockable5.text.getText().substring(summaryList.toString().length()-1);
				summaryText=editorDockable5.text.getText();
				summaryList.clear();
				summaryList.add(summaryText);
				fileContent3 = data5 + "\n" + summaryList;
				editorDockable3.text.setText(fileContent3);
				editorDockable4.text.setText(summaryText);
			}else if(getSummary!=null){
				summaryList.add(getSummary);
				getSummary=null;
				String ss=Pattern.compile("\\b([\\w\\W])\\b").matcher(summaryList.toString().substring(1, summaryList.toString().length()-1)).replaceAll("$1");
				editorDockable5.text.setText(ss);
				fileContent3 = data5 + "\n" + summaryList;
				editorDockable3.text.setText(fileContent3);
			}
		} else if (e.getSource() == showAll) {
			fileContent4 = data2 + "\n" + termList.toString();
			fileContent3 = data3 + "\n" + keywordList + "\n" + data6 + "\n"+ keywordNum + "\n" + data4 + "\n" + sentenceList + "\n"+ data7 + "\n" + sentenceNum+"\n"+data5 + "\n" + summaryList;
			editorDockable3.text.setText(fileContent3);
			editorDockable4.text.setText(fileContent4);
			editorDockable3.text.setCaretPosition(editorDockable3.text.getText().indexOf(keywordList.toString()));
			editorDockable4.text.setCaretPosition(editorDockable4.text.getText().indexOf(termList.toString()));
		} else if (e.getSource() == revokeKeyword) {
			if(keywordList.size()==0){
				JOptionPane.showMessageDialog(mSummary, "暂时没有关键词,不需撤销！");
			}else{
				setColorss(keywordList.get(keywordNum-1),Color.DARK_GRAY);
				keywordList.remove(keywordList.size() - 1);
				keywordNum = keywordList.size();
				keywordFreList.remove(keywordFreList.size()-1);
				if(keywordFreList.size()==0){
					fileContent2="关键词频率"+"\n"+"null"+"\n"+"关键词频率数组"+"\n"+keywordFreList;
				}else{
					fileContent2="关键词频率"+"\n"+keywordFreList.get(keywordFreList.size()-1)+"\n"+"关键词频率数组"+"\n"+keywordFreList;
				}				
				fileContent3 = data3 + "\n" + keywordList + "\n" + data6 + "\n"+ keywordNum + "\n" + data4 + "\n" + sentenceList + "\n"+ data7 + "\n" + sentenceNum;
				editorDockable2.text.setText(fileContent2);
				editorDockable3.text.setText(fileContent3);
				editorDockable4.text.setText(fileContent4);
				editorDockable3.text.setCaretPosition(editorDockable3.text.getText().indexOf(keywordList.toString()));
				// editorDockable4.text.setCaretPosition(editorDockable4.text.getText().indexOf(termList.toString()));
			}			
		} else if (e.getSource() == revokeSentence) {
			if(sentenceList.size()==0){
				JOptionPane.showMessageDialog(mSummary, "暂时没有关键句,不需撤销！");
			}else{
				sentenceList.remove(sentenceList.size() - 1);
				sentenceNum = sentenceList.size();
				fileContent3 = data3 + "\n" + keywordList + "\n" + data6 + "\n"+ keywordNum + "\n" + data4 + "\n" + sentenceList + "\n"+ data7 + "\n" + sentenceNum;
				editorDockable3.text.setText(fileContent3);
				editorDockable4.text.setText(fileContent4);
				editorDockable3.text.setCaretPosition(editorDockable3.text.getText().indexOf(data7));
				// editorDockable4.text.setCaretPosition(editorDockable4.text.getText().indexOf(termList.toString()));				
			}			
		} else if(e.getSource()==revokeSummary){
			if(summaryList.size()==0){
				JOptionPane.showMessageDialog(mSummary, "摘要已为空,不需撤销！");
			}else{
				summaryList.remove(summaryList.size()-1);
				fileContent3=data5+"\n"+summaryList;
				editorDockable3.text.setText(fileContent3);
				fileContent5=Pattern.compile("\\b([\\w\\W])\\b").matcher(summaryList.toString().substring(1, summaryList.toString().length()-1)).replaceAll("$1");
				editorDockable5.text.setText(fileContent5);
			}			
		}else if(e.getSource()==revokeTagging){
			if(termList.size()==0){
				JOptionPane.showMessageDialog(mSummary, "没有标注,不可撤销！");
			}else{
				String replace1=termList.get(termList.size()-1).toString();
				String replace2=termList.get(termList.size()-1).word;
				editorDockable1.text.setText(editorDockable1.text.getText().replaceAll(replace1, replace2));
				termList.remove(termList.size()-1);
				editorDockable2.text.setText(termList.toString());				
			}
		}else if (e.getSource() == clearData) {
			clearData();
			editorDockable1.text.setText(fileContent1);
			editorDockable2.text.setText(fileContent2);
			editorDockable3.text.setText(fileContent3);
			editorDockable4.text.setText(fileContent4);
		} else if (e.getSource() == wtCollection) {
			new WordCharacters();
		}else if(e.getSource()==setWordCol){
			termListTestCol.add("中国/n");
			termListTestCol.add("最大/a");
			termListTestCol.add("海警船/n");
			termListTestCol.add("海警2901/n");
			termListTestCol.add("巡航/v");
			termListTestCol.add("宣示/v");
			termListTestCol.add("耐/ad");
			for(int i=0;i<termListTestCol.size();i++){
				setColorss(termListTestCol.get(i),Color.blue);
			}
		}
		//手动标注词性
		else if(e.getSource()==noun){
			editorDockable1.text.getCaretPosition();
			String dd=editorDockable1.text.getSelectedText();
			editorDockable1.text.replaceSelection(dd+"/n");
			setColorss(dd+"/n",Color.blue);
			Term element=new Term(dd, Nature.n);
			termList.add(element);
			editorDockable2.text.setText(termList.toString());
		}else if(e.getSource()==adjective){
			editorDockable1.text.getCaretPosition();
			String dd=editorDockable1.text.getSelectedText();
			editorDockable1.text.replaceSelection(dd+"/a");
			setColorss(dd+"/a",Color.blue);
			Term element=new Term(dd, Nature.a);
			termList.add(element);
			editorDockable2.text.setText(termList.toString());
		}else if(e.getSource()==adverb){
			editorDockable1.text.getCaretPosition();
			String dd=editorDockable1.text.getSelectedText();
			editorDockable1.text.replaceSelection(dd+"/ad");
			Term element=new Term(dd, Nature.ad);
			termList.add(element);
			editorDockable2.text.setText(termList.toString());
			setColorss(dd+"/ad",Color.blue);
		}else if(e.getSource()==verb){
			editorDockable1.text.getCaretPosition();
			String dd=editorDockable1.text.getSelectedText();
			editorDockable1.text.replaceSelection(dd+"/v");
			setColorss(dd+"/v",Color.blue);
			Term element=new Term(dd, Nature.v);
			termList.add(element);
			editorDockable2.text.setText(termList.toString());
		}else if(e.getSource()==test){
			editorDockable1.text.getCaretPosition();
			String dd=editorDockable1.text.getSelectedText();
			//Nature nature1=Nature.;
			editorDockable1.text.replaceSelection(dd+"/a");
			Term element=new Term(dd, Nature.a);
			termList.add(element);
			fileContent2=termList.toString();
			editorDockable2.text.setText(fileContent2);
			//setColorss(dd,Color.blue);
		}else if(e.getSource()==ncs){
			editorDockable1.text.getCaretPosition();
			String dd=editorDockable1.text.getSelectedText();
			editorDockable1.text.replaceSelection(dd+"/ncs");
			setColorss(dd+"/ncs",Color.blue);
			Term element=new Term(dd, Nature.ncs);
			termList.add(element);
			editorDockable2.text.setText(termList.toString());
		}
		else if(e.getSource()==saveFile){
			try {
				saveToMongo();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(e.getSource()==readFile){
			try {
				readMongo();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(e.getSource()==deleteFile){
			try {
				deletecorpus();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}						
		}else if(e.getSource()==setCorpusType){//设置读取时的语料类型(生语料or熟语料)
			String message="请选择存入的语料类型！";
			String corpusType[]={"熟语料","生语料"};
			Object obj=JOptionPane.showInputDialog(null, message, "请选择",JOptionPane.INFORMATION_MESSAGE,null,corpusType,"熟语料");
		    Readcorpustype=((String)obj);
		}
	}
	//清楚数据，在open或new时使用
	public void clearData() {
		fileContent0 = null;
		fileContent1 = null;
		fileContent2 = null;
		fileContent3 = null;
		fileContent4 = null;
		keywordNum = 0;
		sentenceNum = 0;
		keywordFre=0;
		id=0;
		keywordFreList.clear();
		addKeyword = null;
		addKeysentence =null;
		keywordList.clear();
		sentenceList.clear();
		termList.clear();
		editorDockable2.text.setText(fileContent2);
		editorDockable3.text.setText(fileContent3);
		editorDockable4.text.setText(fileContent4);
		editorDockable5.text.setText(fileContent5);
	}

	// 设置字体颜色
	public static void setColor(String str, AttributeSet attrSet)throws BadLocationException {
		StyledDocument doc = editorDockable1.text.getStyledDocument();
		int offset1;
		for(offset1=-1;offset1<=editorDockable1.text.getText().lastIndexOf(str);++offset1){
			 offset1 = editorDockable1.text.getText().indexOf(str,offset1);
			doc.setCharacterAttributes(offset1, str.length(), attrSet, true);
			keywordFre++;
		}	
	}
	public static void setColors(String str, Color col)throws BadLocationException {
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setForeground(attrSet, col);
		setColor(str, attrSet);
	}
	public static void setColorss(String str,Color col){
		try {
			setColors(str, col);
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	//随机生成颜色
	public static void randomColor(){
		Random rand=new Random();
		r=rand.nextInt(255);
		g=rand.nextInt(255);
		b=rand.nextInt(255);
	}
	
	//将数据存入mongodb
	public void saveToMongo() throws UnknownHostException{
		//选择生语料库还是熟语料库
		String message="请选择存入的语料类型！";
		String corpusType[]={"熟语料","生语料"};
		Object obj=JOptionPane.showInputDialog(null, message, "请选择",JOptionPane.INFORMATION_MESSAGE,null,corpusType,"熟语料");
		String corpustype=((String)obj);
		
		MongoClient mongoClient=new MongoClient();
		DB corpus=mongoClient.getDB("corpus");//在mongo中的数据库名字				
		Gson gson=new Gson();
		DBCursor cursor;
		DBCollection collection = null;
		//long id;
		long count;
		if(id==0){
			if(corpustype=="生语料"||corpustype=="熟语料"){
				if(corpustype=="生语料"){
					collection=corpus.getCollection("raw");		    
				}else if(corpustype=="熟语料"){
					collection=corpus.getCollection("ripe");
				}
				cursor=collection.find();			
				count=collection.getCount();	
				if(count>0){
					long idd=0;
					//获取id中的最大值
					while(cursor.hasNext()){
						DBObject st=cursor.next();
						Savetext u=gson.fromJson(st.toString(), Savetext.class);
						if(idd<u.id){
							idd=u.id;
						}
					}
					id=idd+1;
				}else{
					id=1;
				}
				Savetext saveText1=new Savetext();
				saveText1.setMetaData1(fileContent1);
				saveText1.setKeyword1(keywordList);
				saveText1.setKeySentence1(sentenceList);
				saveText1.setSummary1(summaryList);
				saveText1.setTermList1(termList);
				saveText1.TermListString1=editorDockable1.text.getText();
				saveText1.setTermListString1(saveText1.TermListString1);
				saveText1.setTextID(id);		
				//转换成json字符串，再转换成DBObject对象
				DBObject dbObject=(DBObject) JSON.parse(gson.toJson(saveText1));
				//插入数据库
				collection.insert(dbObject);
				JOptionPane.showMessageDialog(null, "保存成功");
			}else if(corpustype==null){
				//JOptionPane.showMessageDialog(null, "请选择存入语料的类型！！");
				return;
			}else if(corpustype.equals("")){
				JOptionPane.showMessageDialog(null, "请选择存入语料的类型！！");
				return;
			}	
			id=0;
			System.out.println(id);
		}else if(id!=0){
			if(Readcorpustype.equals(corpustype)){
				//此处为在本类型语料中修改
				if(corpustype=="生语料"){
					collection=corpus.getCollection("raw");		    
				}else if(corpustype=="熟语料"){
					collection=corpus.getCollection("ripe");
				}				
				Savetext saveText1=new Savetext();
				saveText1.setMetaData1(fileContent1);
				saveText1.setKeyword1(keywordList);
				saveText1.setKeySentence1(sentenceList);
				saveText1.setSummary1(summaryList);
				saveText1.setTermList1(termList);
				saveText1.TermListString1=editorDockable1.text.getText();
				saveText1.setTermListString1(saveText1.TermListString1);
				saveText1.setTextID(id);		
				//转换成json字符串，再转换成DBObject对象
				DBObject dbObject=(DBObject) JSON.parse(gson.toJson(saveText1));
				collection.update(ReadDbobject, dbObject);
				JOptionPane.showMessageDialog(null, "修改成功");
			}else{
				//此处为向corpustype中添加一个一个新语料，并删除Readcorpustype中的此语料。
				if(corpustype=="生语料"){
					collection=corpus.getCollection("raw");		    
				}else if(corpustype=="熟语料"){
					collection=corpus.getCollection("ripe");
				}
				DBCollection collection1 = null;
				if(Readcorpustype=="生语料"){
					collection1=corpus.getCollection("raw");		    
				}else if(Readcorpustype=="熟语料"){
					collection1=corpus.getCollection("ripe");
				}
				cursor=collection.find();			
				count=collection.getCount();	
				if(count>0){
					long idd=0;
					//获取id中的最大值
					while(cursor.hasNext()){
						DBObject st=cursor.next();
						Savetext u=gson.fromJson(st.toString(), Savetext.class);
						if(idd<u.id){
							idd=u.id;
						}
					}
					id=idd+1;
				}else{
					id=1;
				}
				Savetext saveText1=new Savetext();
				saveText1.setMetaData1(fileContent1);
				saveText1.setKeyword1(keywordList);
				saveText1.setKeySentence1(sentenceList);
				saveText1.setSummary1(summaryList);
				saveText1.setTermList1(termList);
				saveText1.TermListString1=editorDockable1.text.getText();
				saveText1.setTermListString1(saveText1.TermListString1);
				saveText1.setTextID(id);		
				//转换成json字符串，再转换成DBObject对象
				DBObject dbObject=(DBObject) JSON.parse(gson.toJson(saveText1));
				collection.insert(dbObject);							
				collection1.remove(ReadDbobject);
				JOptionPane.showMessageDialog(null, "保存成功");
			}
		}		
	}
	//根据id读取语料
	public void readMongo()throws UnknownHostException{
		//new XialaLiebiaoTest();
		String message="请输入要读取的文本编号！";
		long idm=1;
		Object obj=JOptionPane.showInputDialog(null, message,idm);
		String obj1=(String)obj;
		
		if(obj1==null){
			return;
		}
		id= Long.parseLong(obj1);
		
		MongoClient mc=null;
		DB db=null;
		mc=new MongoClient("127.0.0.1",27017);
		db=mc.getDB("corpus");
		DBCollection textsave=null;
		if(Readcorpustype=="熟语料"){
			textsave=db.getCollection("ripe");
		}else if(Readcorpustype=="生语料"){
			textsave=db.getCollection("raw");
		}else if(Readcorpustype==null){
			JOptionPane.showMessageDialog(null, "请在帮助菜单中设置语料类型！");
			return;
		}
		DBCursor cursor=textsave.find();
		//SimpleAttributeSet att=new SimpleAttributeSet();
		Gson gson=new Gson();
		long countoftextsave=textsave.getCount();
		long counti=0;
		long uid=-1;
		while(cursor.hasNext()){
			DBObject st=cursor.next();
			Savetext u=gson.fromJson(st.toString(), Savetext.class);
			counti++;
			uid=u.id;
			if(uid==id){
				editorDockable1.text.setText(u.TermListString1);
				termList=u.TermList1;
				fileContent1=u.MetaData1;
				keywordList=u.Keyword1;
				sentenceList=u.KeySentence1;
				summaryList=u.Summary1;
				TermListString1=u.TermListString1;
				for(int i=0;i<termList.size();i++){
					randomColor();
					setColorss(termList.get(i).toString(),new Color(r,g,b));
				}
				ReadDbobject=cursor.curr();
				break;
			}else if(uid!=id){
				continue;
			}
		}
		if((counti==countoftextsave)&&(uid!=id)){
			JOptionPane.showMessageDialog(null, "wrong id!");
			return;
		}
		System.out.println(countoftextsave);
		System.out.println(counti);
		System.out.println(ReadDbobject);
	}
	//删除指定id的语料
	public void deletecorpus() throws UnknownHostException{
		String message="请输入要读取的文本编号！";
		long idm=1;
		Object obj=JOptionPane.showInputDialog(null, message,idm);
		String obj1=(String)obj;
		
		if(obj1==null){
			return;
		}
		long id1= Long.parseLong(obj1);
		
		MongoClient mc=null;
		DB db=null;
		mc=new MongoClient("127.0.0.1",27017);
		db=mc.getDB("corpus");
		DBCollection textsave=null;
		if(Readcorpustype=="熟语料"){
			textsave=db.getCollection("ripe");
		}else if(Readcorpustype=="生语料"){
			textsave=db.getCollection("raw");
		}else if(Readcorpustype==null){
			JOptionPane.showMessageDialog(null, "请在帮助菜单中设置语料类型！");
			return;
		}
		DBCursor cursor=textsave.find();
		Gson gson=new Gson();
		long countoftextsave=textsave.getCount();
		long counti=0;
		long uid=-1;
		while(cursor.hasNext()){
			DBObject st=cursor.next();
			Savetext u=gson.fromJson(st.toString(), Savetext.class);
			counti++;
			uid=u.id;
			if(uid==id1){
				ReadDbobject=cursor.curr();
				textsave.remove(ReadDbobject);
				System.out.println("删除成功");
				break;
			}else if(uid!=id1){
				continue;
			}
		}
		if((counti==countoftextsave)&&(uid!=id1)){
			JOptionPane.showMessageDialog(null, "wrong id!");
			return;
		}
	}

	public static void main(String[] args){
		new Dockabletest();		
	}
}
