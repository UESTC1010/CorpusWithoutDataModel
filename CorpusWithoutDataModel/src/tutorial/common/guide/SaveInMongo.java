package tutorial.common.guide;

import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class SaveInMongo{
	DBCollection TextSave;
	public void saveTooMongo() throws UnknownHostException{
		MongoClient mongoClient=new MongoClient();
		
		DB corpus=mongoClient.getDB("corpus");//在mongo中的数据库名字
		TextSave=corpus.getCollection("TextSave");
		
//		Savetext saveText1=new Savetext();
//		saveText1.setMetaData1(fileContent1);
//		saveText1.setKeyword1(keywordList);
//		saveText1.setKeySentence1(sentenceList);
//		saveText1.setSummary1(summaryList);
//		saveText1.setTermList1(termList);
//		Gson gson=new Gson();
//		//转换成json字符串，再转换成DBObject对象
//		DBObject dbObject=(DBObject) JSON.parse(gson.toJson(saveText1));
//		//插入数据库
//		TextSave.insert(dbObject);
//		//
//		JOptionPane.showMessageDialog(null, "保存成功");
	}
}
