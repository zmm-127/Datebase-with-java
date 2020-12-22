package dataBase;

import java.util.Vector;

public class UpdateTable {
	private String sTablename;  //����
	private Vector colName;		//����
	private Vector value;      //��ֵ
	
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
		Vector v=new Vector();//������������һ��Ԫ��Ϊ���ش��룬-1��ʾ�����ڶ���Ԫ��Ϊ������ʾ�ַ���
		//�����Լ��һ���������Ƿ����
		GlobalTableInfo gtInfo=new GlobalTableInfo();
		if(!gtInfo.fileIsExist(this.sTablename)) {
			v.addElement(-1);
			v.addElement("���ݿ��в����ڴ˱�");
			return v;
		}
		TableContent tc=new TableContent();
		try {
			tc=tc.ReadFromFile(this.getsTablename());
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		//�����Լ�������������Ƿ����
		for(int i=0;i<this.colName.size();i++) {
			if(!gtInfo.colNameIsExist(tc,this.colName.get(i).toString())) {
				v.addElement(-1);
				v.addElement("�޷����£�����"+this.colName.get(i).toString()+"�ڱ��в�����");
				return v;
			}
		}
		//Ѱ�ҷ����������е�λ��
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
		//�������ҵ���λ�ã�����ֵ���и��²���
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
		v.addElement("���ݸ��³ɹ�\n");
		return v;
	}
}
