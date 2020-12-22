package dataBase;

import java.io.File;
import java.util.Vector;

public class DropTableInfo {
	private String sTablename;
	
	public DropTableInfo() {
		sTablename="";
	}
	
	public void setsTablename(String sTablename) {
		this.sTablename=sTablename;
	}
	
	public String getsTablename() {
		return sTablename;
	}
	
	public Vector exeSQL() {
		Vector v=new Vector();//返回向量，第一个元素为返回代码，-1表示出错，第二个元素为返回显示字符串
		//合理检查一：检测表名是否存在
		GlobalTableInfo gtInfo=new GlobalTableInfo();
		if(!gtInfo.fileIsExist(this.sTablename)) {
			v.addElement(-1);
			v.addElement("数据库中不存在此表");
			return v;
		}
		//合理性检查二：没有其他表参照引用此表
		//合理性检查三：表中无数据
		//检查完毕，开始删除文件
		try {
			File file=new File(this.sTablename+".db");
			if(file.delete())
				System.out.println("数据库表"+this.sTablename+".db删除成功！");
			else
				System.out.println("数据库表"+this.sTablename+".db删除失败！");
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		v.addElement(1);
		v.addElement("执行成功");
		return v;
	}
}
