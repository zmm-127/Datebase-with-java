package dataBase;

import staticContent.*;
import java.util.Vector;

public class TableConstraint implements java.io.Serializable {
	private String tcName;  //约束名
	private int tcType;   //约束类型，1-主键，2-外键，3-check约束
	Vector tcv;
	
	public TableConstraint() {
		tcName="";
		tcType=DataType.NULL;
		tcv=new Vector();
	}
	
	public void setTcName(String tcname) {
		tcName=tcname;
	}
	
	public String getTcName() {
		return tcName;
	}
	
	public void setTcType(int tctype) {
		tcType=tctype;
	}
	
	public int getTcType() {
		return tcType;
	}
	
	 //当为外键时，3代表删除，4代表更新,2代表设为空，3代表级联
    public void setElement(int i, int j) {
		tcv.addElement(i);
		tcv.addElement(j);
	}
    
	public void addElement(Object o1) {
		tcv.addElement(o1);
	}
	
	public Vector getTcV() {
		Vector v=new Vector();
		v.addElement(tcv.get(0));
		v.addElement(tcv.get(1));
		v.addElement(tcv.get(2));
		return v;
	}
	
	public String getDescribe() {
		String result="";
		if(tcType==1){
			int i;
			result+="primary key(";
			for(i=0;i<tcv.size()-1;i++) {
				result+=tcv.get(i).toString();
				result+=",";
			}
			result+=tcv.get(i).toString();
			result+=")";
		}else if(tcType==2) {
			result+="foreign key (";
			result+=tcv.get(0).toString();
			result+=") references ";
			result+=tcv.get(1).toString()+" (";
			result+=tcv.get(2).toString()+")";
			if(tcv.size()>5) {
				if(tcv.get(5).toString().equals("3"))
					result+=" on delete ";
				else if(tcv.get(5).toString().equals("4"))
					result+=" on update ";
				if(tcv.get(6).toString().equals("2"))
					result+="set null ";
				else if(tcv.get(6).toString().equals("3"))
					result+="cascade ";
			}
		}else if(tcType==3) {
			result+="check ";
			result+=((Expression)tcv.get(0)).getDescribe();
		}
		result+="\n";
		return result;
	}
}
