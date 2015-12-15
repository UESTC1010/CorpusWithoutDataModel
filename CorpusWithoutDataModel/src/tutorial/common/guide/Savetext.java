package tutorial.common.guide;

import java.util.List;

import com.hankcs.hanlp.seg.common.Term;

public class Savetext {
	String MetaData1;
	List<String> Keyword1;
	List<String> KeySentence1;
	List<String> Summary1;
	List<Term> TermList1;
	String TermListString1;
	Oid _id;
	long id;
	
	public class Oid{
		String $oid;
		public String get$oid(){
			return $oid;
		}
		public void set$oid(String $oid){
			this.$oid=$oid;
		}
	}
	
	public String getMetaData1(){
		return MetaData1;
	}
	public void setMetaData1(String MetaData1){
		this.MetaData1=MetaData1;
	}
	public List<String> getKeyword1(){
		return Keyword1;
	}
	public void setKeyword1(List<String> Keyword1){
		this.Keyword1=Keyword1;
	}
	public List<String> getKeySentence1(){
		return KeySentence1;
	}
	public void setKeySentence1(List<String> KeySentence1){
		this.KeySentence1=KeySentence1;
	}
	public List<String> getSummary1(){
		return Summary1;
	}
	public void setSummary1(List<String> Summary1){
		this.Summary1=Summary1;
	}
	public List<Term> getTermList1(){
		return TermList1;
	}
	public void setTermList1(List<Term> TermList1){
		this.TermList1=TermList1;
	}
	public String getTermListString1(){
		return TermListString1;
	}
	public void setTermListString1(String TermListString1){
		this.TermListString1=TermListString1;
	}
	public long getTextID(){
		return id;
	}
	public void setTextID(long id){
		this.id=id;
	}
}
