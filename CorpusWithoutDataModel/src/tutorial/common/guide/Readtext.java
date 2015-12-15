package tutorial.common.guide;

public class Readtext {
	long readId;
	String readType;
	int btnListener1=0;
	int btnListener2=0;
	
	public long getReadtextId(){
		return readId;
	}
	public void setReadtextId(long readid){
		this.readId=readid;
	}
	
	public String getReadtextType(){
		return readType;
	}
	public void setReadtextType(String readtype){
		this.readType=readtype;
	}
	public long getBtnListener1(){
		return btnListener1;
	}
	public void setBtnListener1(int btnListener1){
		this.btnListener1=btnListener1;
	}
	public long getBtnListener2(){
		return btnListener2;
	}
	public void setBtnListener2(int btnListener2){
		this.btnListener2=btnListener2;
	}
}
