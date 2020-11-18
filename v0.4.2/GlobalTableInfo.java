package dataBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

public class GlobalTableInfo implements java.io.Serializable {
	private static final long serialVersionUID=1L;
	class TableInfo implements java.io.Serializable{
		
	}

	class ReferRelation implements java.io.Serializable{
		private static final long serialVersionUID=1L;
		public String ptablename; //主表名
		public String pcolumnname;  //主表字段名
		public String rtablename;  //引用表名
		public String rcolumnname;  //引用表字段名
	}
	
	public Vector vAllTables; //所有表信息
	public Vector vAllReferences;  //所有引用表信息
	
	public GlobalTableInfo(){
		vAllTables=new Vector();
		vAllReferences=new Vector();
	}
	
	public void addTable(String tablename) {
		vAllTables.addElement(tablename);
	}

	public boolean fileIsExist(String tname){
		String path="";
		path="D:\\eclipse\\program\\ZMDB\\"+tname+".db";
		File f=new File(path);
		if(!f.exists())
			return false;
		else
			return true;
	}
	
	public void WriteToFile() throws Exception{
		String filename="globaltable.txt";
		ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(filename));
		String str="";
		str+="\n"+vAllTables.get(0).toString()+"\n";
		oos.writeObject(str);
		oos.close();
	}
	
	public GlobalTableInfo ReadFromFile(String tablename) throws IOException,ClassNotFoundException {
		ObjectInputStream ois =new ObjectInputStream(new FileInputStream(tablename));
		GlobalTableInfo gti=(GlobalTableInfo)ois.readObject();
		ois.close();
		return gti;
	}

	public void delTable(String getsTablename) {
		
	}

	//外键参照表，tName为本表名，c1为本表所设置的键，tn为参考表名，c2为参考表所参考的键
	public void addReference(String tName, String c1, String tn, String c2) {
		
	}
}

