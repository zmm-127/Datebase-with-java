package dataBase;

import java.util.Vector;

public class SelectTable {
	private String sTablename;  //表名
	private Vector colName;		//列名
	private Vector value;      //列值
	
	public SelectTable() {
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
			if(this.colName.get(i).equals("*")) {
				break;
			}
			if(!gtInfo.colNameIsExist(tc,this.colName.get(i).toString())) {
				v.addElement(-1);
				v.addElement("无法查询，列名"+this.colName.get(i).toString()+"在表中不存在");
				return v;
			}
		}
		if(tc.getvData().size()==0)
			System.out.println(this.sTablename+"表内无数据");
		//打印出所有列值
		if(this.colName.get(0).equals("*")) {
			for(int i=0;i<tc.getvColumn().size();i++) {
				System.out.print(((ColumnDef)tc.getvColumn().get(i)).getColName()+"\t");
			}
			System.out.println();
			for(int j=0;j<tc.getvData().size();j++) {
				System.out.println(tc.getvData().elementAt(j)+"\t");
			}
		}else {//寻找符合条件的列的位置
			Vector<Integer>  local = new Vector<Integer>();
			for(int i=0;i<this.colName.size();i++) {
				for(int j=0;j<tc.getvColumn().size();j++) {
					ColumnDef coldef=(ColumnDef)(tc.getvColumn().get(j));
					if(coldef.getColName().equals(this.colName.get(i)))
						local.addElement(j);
				}
			}
			Vector v1;
			//没有where条件的情况
			if(this.value.size()==0) {
				for(int i=0;i<this.colName.size();i++) {//打印相应列名
					System.out.print(this.colName.get(i).toString()+"\t");
				}
				System.out.println();
				for(int i=0;i<tc.getvData().size();i++) {
					v1=(Vector)tc.getvData().get(i);
					for(int j:local) {
						System.out.print(v1.get(j)+"\t");
					}
					System.out.println();
				}
			}else {//有where条件的情况
				for(int i=0;i<this.colName.size()-1;i++) {//打印相应列名
					System.out.print(this.colName.get(i).toString()+"\t");
				}
				System.out.println();
				for(int i=0;i<tc.getvData().size();i++) {
					Vector vec = (Vector)(tc.getvData().get(i));
					//找出where后面那列的位置
					int Num = local.lastElement();
					//找出where后面那列的名字
					String localColName = (String)(this.colName.lastElement());
					//取该行where后面那列的位置上的值
					String localValue = (String)(vec.get(Num));
					//如果这个值等于values
					if(localValue.equals(this.value.get(0))) {
						//打印出from后面那几列的值 那几列的位置存在local中
						for(int j=0;j<local.size()-1;j++) {
							System.out.print(vec.get(local.elementAt(j))+"\t");
						}
						System.out.println();
					}
				}
			}
		}
		try {
			tc.WriteToFile();
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		v.addElement(1);
		v.addElement("数据查询成功\n");
		return v;
	}
}