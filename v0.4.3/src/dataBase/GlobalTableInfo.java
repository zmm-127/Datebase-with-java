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
	
	public Vector<GlobalTableInfo> globalFkey;//�����Ϣ
	public String pTablename;//������
	public String pColumnname;//��������
	public String rTablename;//�ο�����
	public String rColumnname;//�ο��� 
	
	public GlobalTableInfo(){
		pTablename="";
		pColumnname="";
		rTablename="";
		rColumnname="";
		globalFkey=new Vector<GlobalTableInfo>();
	}
	
	public void setpTablename(String pTablename){this.pTablename=pTablename;}
    public String getpTablename(){return this.pTablename;}
    public void setpColumnname(String pColumnname){this.pColumnname=pColumnname;}
    public String getpColumnname(){return this.pColumnname;}
    public void setrTablename(String rTablename){this.rTablename=rTablename;}
    public String getrTablename(){return this.rTablename;}
    public void setrColumnname(String rColumnname){this.rColumnname=rColumnname;}
    public String getrColumnname(){return this.rColumnname;}
	
	public boolean fileIsExist(String tname){
		String path="";
		path="D:\\eclipse\\program\\ZMDB\\"+tname+".db";
		File f=new File(path);
		if(!f.exists())
			return false;
		else
			return true;
	}
	
	public boolean colNameIsExist(TableContent tc,String cname) {
		for(int i=0;i<tc.getvColumn().size();i++) {
			if(cname.equalsIgnoreCase(((ColumnDef)tc.getvColumn().get(i)).getColName()))
				return true;
		}
		return false;
	}
	
	//������ձ�tNameΪ��������c1Ϊ���������õļ���tnΪ�ο�������c2Ϊ�ο������ο��ļ�
	public void addReference(String tName, String c1, String tn, String c2) {
		this.pTablename=tName;
		this.pColumnname=c1;
		this.rTablename=tn;
		this.rColumnname=c2;
	}
	
	public void WriteToFile() throws Exception{
		String filename="globaltable.txt";
		File f = new File(filename);
        if(!f.exists()){
            globalFkey.addElement(this);
            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(filename));
            oos.writeObject(globalFkey);
            oos.close();
        }else {
            ObjectInputStream ois =new ObjectInputStream(new FileInputStream(filename));
            globalFkey = (Vector)ois.readObject();
            globalFkey.addElement(this);
            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(filename));
            oos.writeObject(globalFkey);
            oos.close();
        }
	}
	

}

