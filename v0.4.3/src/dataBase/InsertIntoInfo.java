package dataBase;

import java.util.Vector;

public class InsertIntoInfo {
	private String sTablename;  //����
	private Vector vColumnname;  //��������
	private Vector vValue;  //��ֵ����
	
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
		Vector v=new Vector();//������������һ��Ԫ��Ϊ���ش��룬-1��ʾ�����ڶ���Ԫ��Ϊ������ʾ�ַ���
		//�����Լ��һ���������Ƿ����
		GlobalTableInfo gtInfo=new GlobalTableInfo();
		if(!gtInfo.fileIsExist(this.sTablename)) {
			v.addElement(-1);
			v.addElement("���ݿ��в����ڴ˱�");
			return v;
		}
		//�����Լ�������������Ƿ����
		TableContent tc=new TableContent();
		try {
			tc=tc.ReadFromFile(this.getsTablename());
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		for(int i=0;i<this.getvColumnname().size();i++) {
			if(!gtInfo.colNameIsExist(tc,(String)this.getvColumnname().get(i))) {
				v.addElement(-1);
				v.addElement("�޷����룬����"+(String)this.getvColumnname().get(i)+"�ڱ��в�����");
				return v;
			}
		}
		Vector vRowData=new Vector();//����ֵ
		vRowData=this.getvValue();
		tc.addRowData(vRowData);
		System.out.println(tc.getDescribe());
		try {
			tc.WriteToFile();
		}catch(Exception e) {
			System.out.println(e.toString());
		}
	v.addElement(1);
	v.addElement("���ݲ���ɹ�\n");
	return v;
	}
}
