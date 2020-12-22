package dataBase;

import java.util.Vector;

public class UpdateTable {
	private String sTablename;  //表名
	private Vector colName;		//列名
	private Vector value;      //列值
	
	public UpdateTable() {
		sTablename="";
		colName=new Vector();
		value=new Vector();
	}
	
	public void setsTablename(String sTablename) {
		this.sTablename=sTablename;
	}
	
	public String getsTablename() {
		return this.sTablename;
	}
	
	public void addColname(String colName) {
		this.colName.addElement(colName);
	}
	
	public void addValue(String value) {
		this.value.addElement(value);
	}
	
	public Vector exeSQL() throws Exception {
		Vector v=new Vector();//返回向量，第一个元素为返回代码，-1表示出错，第二个元素为返回显示字符串
		//合理性检查一：检测表名是否存在
		GlobalTableInfo gtInfo=new GlobalTableInfo();
		if(!gtInfo.fileIsExist(this.sTablename)) {
			v.addElement(-1);
			v.addElement("数据库中不存在此表");
			return v;
		}
		TableContent tc=new TableContent();
		try {
			tc=tc.ReadFromFile(this.getsTablename());
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		//合理性检查二：检测列名是否存在
		for(int i=0;i<this.colName.size();i++) {
			if(!gtInfo.colNameIsExist(tc,this.colName.get(i).toString())) {
				v.addElement(-1);
				v.addElement("无法更新，列名"+this.colName.get(i).toString()+"在表中不存在");
				return v;
			}
		}
		//寻找符合条件的列的位置
		int local1=-1,local2=-1;
		for(int i=0;i<tc.getvColumn().size();i++) {
			ColumnDef coldef=(ColumnDef)(tc.getvColumn().get(i));
			if(coldef.getColName().equals(this.colName.get(0).toString()))
				local1=i;
			if(this.colName.size()>1) {
				if(coldef.getColName().equals(this.colName.get(1).toString()))
					local2=i;
			}
		}
		Vector v1;
		//根据所找到的位置，对列值进行更新操作
		if(local2==-1) {
			for(int i=0;i<tc.getvData().size();i++) {
				((Vector)tc.getvData().get(i)).set(local1, this.value.get(0));
			}
		}else {
			for(int i=0;i<tc.getvData().size();i++) {
				v1=(Vector)tc.getvData().get(i);
				if(v1.get(local2).equals(this.value.get(1))){
					((Vector)tc.getvData().get(i)).set(local1, this.value.get(0));
				}
			}
		}
		System.out.println(tc.getDescribe());
		try {
			tc.WriteToFile();
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		v.addElement(1);
		v.addElement("数据更新成功\n");
		return v;
	}
}
