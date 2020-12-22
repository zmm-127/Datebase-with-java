package dataBase;

import staticContent.*;
import java.util.Vector;

public class TableConstraint implements java.io.Serializable {
	private String tcName;  //Լ����
	private int tcType;   //Լ�����ͣ�1-������2-�����3-checkԼ��
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
	
	 //��Ϊ���ʱ��3����ɾ����4�������,2������Ϊ�գ�3������
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
