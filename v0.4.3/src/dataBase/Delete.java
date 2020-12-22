package dataBase;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Vector;

import staticContent.DataType;

public class Delete {
	private String sTablename;  //表名
	private String colName;		//列名
	private String value;      //列值
	private Vector global;     //外键约束
	private TableContent tables;
	
	public Delete() {
		sTablename="";
		colName="";
		value="";
		global=new Vector();
	}

	public void setsTablename(String sTablename) {
		this.sTablename=sTablename;
	}
	
	public String getsTablename() {
		return this.sTablename;
	}
	
	public void setsColName(String colName) {
		this.colName=colName;
	}
	
	public String getColName() {
		return this.colName;
	}
	
	public void setValue(String value) {
		this.value=value;
	}
	
	public String getValue() {
		return this.value;
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
		if(this.colName.equals(""))
			tc.getvData().removeAllElements();
		else {
			//合理性检查二：检测列名是否存在
			if(!gtInfo.colNameIsExist(tc,this.colName)) {
				v.addElement(-1);
				v.addElement("无法删除，列名"+this.colName+"在表中不存在");
				return v;
			}
			//寻找符合条件的列的位置
			int local=0;
			for(int i=0;i<tc.getvColumn().size();i++) {
				ColumnDef coldef=(ColumnDef)(tc.getvColumn().get(i));
				if(coldef.getColName().equals(this.colName))
					local=i;
			}
			Vector v1;
			//根据所找到的位置，对列值进行删除操作
			for(int i=0;i<tc.getvData().size();i++) {
				v1=(Vector)tc.getvData().get(i);
				if(v1.get(local).equals(this.value)) {
					tc.getvData().removeElementAt(i);
				}
			}
			//查找是否此表被其他表所参考
			for(Object i :global){
	             GlobalTableInfo global = (GlobalTableInfo)i;
	             if(this.sTablename.equals((global.getrTablename())) && this.colName.equals(global.getrColumnname())){
	              //这个时候执行删除操作 即将表名定义为参照表 列名不变 值也不变 在那个表进行删除操作
	              this.setsTablename(global.pTablename);
	              ReadFromFile();
	              //执行删除命令
	              int local1=0;
	              for(int j=0;j<tables.getvColumn().size();j++){
	                  ColumnDef col=(ColumnDef)(tables.getvColumn().get(j));
	                  if(col.getColName().equals(this.colName)) local1=j;
	              }
	              //用一个新的向量接收更改后的向量 即不相等的全部存入新向量 然后替换
	              Vector row = new Vector<>();
	              for(int j=0;j<tables.getvData().size();j++) {
	                  //用temp保存
	                  Vector temp = (Vector) tables.getvData().get(j);
	                  if (!temp.get(local1).equals(this.value)){
	                      row.addElement(tables.getvData().get(j));
	                  }
	              }
	              tables.setvData(row);
	              tables.getDescribe();
	              tables.WriteToFile();
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
	v.addElement("数据删除成功\n");
	return v;
	}
	
	public void ReadFromFile() throws Exception{
        String filename=this.getsTablename()+".db";
        String path = "D:\\eclipse\\program\\ZMDB";
        ObjectInputStream oos=new ObjectInputStream(new FileInputStream(path+filename));
        tables= (TableContent)oos.readObject();
        oos.close();
    }
    public void ReadGlobalFromFile() throws Exception{
        String filename="globaltable.txt";
        String path = "D:\\eclipse\\program\\ZMDB";
        ObjectInputStream oos=new ObjectInputStream(new FileInputStream(path+filename));
        this.global= (Vector)oos.readObject();
        oos.close();
    }
}
