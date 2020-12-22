package dataBase;

import staticContent.*;

import java.io.*;
import java.util.Vector;

public class TableContent implements java.io.Serializable {
	private static final long serialVersionUID=1L;
	private String tName; //表名
	private Vector vColumn; //列
	private Vector vConstraint; //约束
	private Vector vData; //表中的数据
	
	public TableContent() {
		tName="";
		vColumn=new Vector();
		vConstraint=new Vector();
		vData=new Vector();
	}
	
	public void settName(String tName) {
		this.tName=tName;
	}
	
	public String gettName() {
		return tName;
	}
	
	public void setvColumn(Vector vColumn) {
		 this.vColumn= vColumn;
	}
	
	public Vector getvColumn() {
		return vColumn;
	}
	
	public void setvConstraint(Vector vConstraint) {
		this.vConstraint= vConstraint;
	}
	
	public Vector getvConstraint() {
		return vConstraint;
	}
	
	public void setvData(Vector vData) {
		this.vData=vData;
	}
	
	public Vector getvData() {
		return vData;
	}
	
	public Vector executeSQL() throws Exception{
		Vector v=new Vector();  //返回向量，第一个元素为返回代码，-1表示出错，第二个值为返回显示的字符串
		//合理性检查一：表名不能与已有的表重复
		GlobalTableInfo gtInfo=new GlobalTableInfo();
		if(gtInfo.fileIsExist(this.gettName())) {
			v.addElement(-1);
			v.addElement("无法建表，数据库中已有同名表");
			return v;
		}
		//合理性检查二：主键只能有一个
		int pknum=0;
		for(int i=0;i<this.vColumn.size();i++) {
			if(((ColumnDef)this.vColumn.get(i)).isPrimaryKey()) {
				pknum++;
			}
			if(pknum>1) {
				v.addElement(-1);
				v.addElement("无法建表，在多个列中有主键定义");
				return v;
			}
		}
		for(int i=0;i<this.vConstraint.size();i++) {
			if(((TableConstraint)this.vConstraint.get(i)).getTcType()==1)
				pknum++;
			if(pknum>1) {
				v.addElement(-1);
				v.addElement("无法建表，多处有主键定义");
				return v;
			}
		}
		//检查外键
		TableConstraint tc;
		for(int i=0;i<vConstraint.size();i++) {
			tc=(TableConstraint)vConstraint.get(i);
			if(2!=tc.getTcType())
				continue;               //外键约束

			TableContent table = ReadFromFile((String)tc.getTcV().get(1));//参考表
            if(table.getFieldIndex((String)tc.getTcV().get(2),table.getvColumn())==-1){//本表，参考表，列名是否相等
                System.out.println((String)tc.getTcV().get(1)+"表中不存在"+(String)tc.getTcV().get(2)+"列，无法创建外键约束");
                System.exit(0);
            }

            String c1 = (String)(tc.getTcV().get(0));
            String c2= (String)(tc.getTcV().get(2));
            if(!c1.equals(c2)){
                System.out.println("本表和参考表两列名不相等，无法创建外键！");
                System.exit(0);
            }
			
			gtInfo.addReference(this.tName,tc.getTcV().get(0).toString(),
									tc.getTcV().get(1).toString(),tc.getTcV().get(2).toString());
			
			gtInfo.WriteToFile();
			
		}
		//写入文件
		try {
			this.WriteToFile();
		}catch(Exception e){
			System.out.println(e.toString());
		}
		v.addElement(1);
		v.addElement("执行成功");
		return v;
	}
	
	public void addRowData(Vector vRowData) {  //将列数据存入数组中
		this.vData.addElement(vRowData);
	}
	
	//得到主键列，每个元素为一个ColumnDef对象
	public Vector getPrimaryColumn() {
		Vector v=new Vector();
		for(int i=0;i<this.vColumn.size();i++) {
			if(((ColumnDef)this.vColumn.get(i)).isPrimaryKey()) {
				v.addElement(this.vColumn.get(i));
				return v;
			}
		}
		for(int i=0;i<this.vConstraint.size();i++) {
			if(((TableConstraint)this.vConstraint.get(i)).getTcType()==1)
				v.addElement(this.vConstraint.get(i));
				return v;
		}
		return v;
	}
	
	//得到主键列下标，每个元素为一个整数，即字段的序号
	public Vector getPrimaryColumnIndex() {
		Vector v=new Vector();
		for(int i=0;i<this.vColumn.size();i++) {
			if(((ColumnDef)this.vColumn.get(i)).isPrimaryKey()) {
				v.addElement(i);
				return v;
			}
		}
		for(int i=0;i<this.vConstraint.size();i++) {
			if(((TableConstraint)this.vConstraint.get(i)).getTcType()==1) {
				v.addElement(i);
				return v;
			}
		}
		return v;
	}
	
	public int getFieldIndex(String colname,Vector vColumn) {
		ColumnDef coldef=null;
		for(int i=0;i<vColumn.size();i++) {
			coldef=(ColumnDef)vColumn.get(i);
			if(0==colname.compareToIgnoreCase(coldef.getColName()))
				return i;
		}
		return -1;
	}
	
	public String getDescribe() {
		String result="";
		Vector v;
		result+="表名："+this.gettName()+"\n";
		result+="共有"+this.getvColumn().size()+"个字段\n";
		v=this.getvColumn();
		for(int i=0;i<v.size();i++) 
			result+=((ColumnDef) v.get(i)).getDescribe();
		result+="共有"+this.getvConstraint().size()+"个约束\n";
		v=this.getvConstraint();
		for(int i=0;i<v.size();i++)
			result+=((TableConstraint)v.get(i)).getDescribe();
		result+="共有"+this.getvData().size()+"条数据\n";
		v=this.getvColumn();
		for(int i=0;i<v.size();i++)
			result+=((ColumnDef)v.get(i)).getColName()+"\t";
		result+="\n";
		Vector vRowData;
		for(int i=0;i<this.vData.size();i++) {
			vRowData=(Vector)this.vData.get(i);
			for(int j=0;j<vRowData.size();j++) {
				result+=vRowData.get(j)+"\t";
			}
			result+="\n";
		}
		return result;
	}

	/*
	 * 对象操作流(ObjectOutputStream,ObjectInputStream)可以将一个对象写出，或者读取一个对象到程序中
	 * 也就是执行了序列化和反序列化操作。
	 */
	public void WriteToFile() throws Exception{
		String filename=this.gettName()+".db";
		ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(filename));
		oos.writeObject(this);
		oos.close();
	}
	
	public TableContent ReadFromFile(String tablename) throws IOException,ClassNotFoundException {
		ObjectInputStream ois =new ObjectInputStream(new FileInputStream(tablename+".db"));
		TableContent tc=(TableContent)ois.readObject();
		ois.close();
		return tc;
	}
	
}
