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
		Vector v=new Vector();//������������һ��Ԫ��Ϊ���ش��룬-1��ʾ�����ڶ���Ԫ��Ϊ������ʾ�ַ���
		//������һ���������Ƿ����
		GlobalTableInfo gtInfo=new GlobalTableInfo();
		if(!gtInfo.fileIsExist(this.sTablename)) {
			v.addElement(-1);
			v.addElement("���ݿ��в����ڴ˱�");
			return v;
		}
		//�����Լ�����û��������������ô˱�
		//�����Լ����������������
		//�����ϣ���ʼɾ���ļ�
		try {
			File file=new File(this.sTablename+".db");
			if(file.delete())
				System.out.println("���ݿ��"+this.sTablename+".dbɾ���ɹ���");
			else
				System.out.println("���ݿ��"+this.sTablename+".dbɾ��ʧ�ܣ�");
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		v.addElement(1);
		v.addElement("ִ�гɹ�");
		return v;
	}
}
