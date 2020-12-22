package dataBase;

import java.util.Vector;

public class InsertIntoInfo {
	private String sTablename;  //表名
	private Vector vColumnname;  //列名集合
	private Vector vValue;  //列值集合
	
	public InsertIntoInfo() {
		sTablename="";
		vColumnname=new Vector<String>();
		vValue=new Vector<String>();
	}
	
	public void setsTablename(String sTablename) {
		this.sTablename=sTablename;
	}
	
	public String getsTablename() {
		return sTablename;
	}
	
	public void setvColumnname(Vector vColumnname) {
		this.vColumnname=vColumnname;
	}
	
	public Vector getvColumnname() {
		return this.vColumnname;
	}
	
	public void setvValue(Vector vValue) {
		this.vValue=vValue;
	}
	
	public  Vector getvValue() {
		return this.vValue;
	}
	
	public void addColumnname(String colname) {
		this.vColumnname.addElement(colname);
	}
	
	public void addValue(String value) {
		this.vValue.addElement(value);
	}
	
	public Vector exeSQL() {
		Vector v=new Vector();//返回向量，第一个元素为返回代码，-1表示出错，第二个元素为返回显示字符串
		//合理性检查一：检测表名是否存在
		GlobalTableInfo gtInfo=new GlobalTableInfo();
		if(!gtInfo.fileIsExist(this.sTablename)) {
			v.addElement(-1);
			v.addElement("数据库中不存在此表");
			return v;
		}
		//合理性检查二：检测列名是否存在
		TableContent tc=new TableContent();
		try {
			tc=tc.ReadFromFile(this.getsTablename());
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		for(int i=0;i<this.getvColumnname().size();i++) {
			if(!gtInfo.colNameIsExist(tc,(String)this.getvColumnname().get(i))) {
				v.addElement(-1);
				v.addElement("无法插入，列名"+(String)this.getvColumnname().get(i)+"在表中不存在");
				return v;
			}
		}
		Vector vRowData=new Vector();//插入值
		vRowData=this.getvValue();
		tc.addRowData(vRowData);
		System.out.println(tc.getDescribe());
		try {
			tc.WriteToFile();
		}catch(Exception e) {
			System.out.println(e.toString());
		}
	v.addElement(1);
	v.addElement("数据插入成功\n");
	return v;
	}
}
