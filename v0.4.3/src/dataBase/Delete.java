package dataBase;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Vector;

import staticContent.DataType;

public class Delete {
	private String sTablename;  //����
	private String colName;		//����
	private String value;      //��ֵ
	private Vector global;     //���Լ��
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
		if(this.colName.equals(""))
			tc.getvData().removeAllElements();
		else {
			//�����Լ�������������Ƿ����
			if(!gtInfo.colNameIsExist(tc,this.colName)) {
				v.addElement(-1);
				v.addElement("�޷�ɾ��������"+this.colName+"�ڱ��в�����");
				return v;
			}
			//Ѱ�ҷ����������е�λ��
			int local=0;
			for(int i=0;i<tc.getvColumn().size();i++) {
				ColumnDef coldef=(ColumnDef)(tc.getvColumn().get(i));
				if(coldef.getColName().equals(this.colName))
					local=i;
			}
			Vector v1;
			//�������ҵ���λ�ã�����ֵ����ɾ������
			for(int i=0;i<tc.getvData().size();i++) {
				v1=(Vector)tc.getvData().get(i);
				if(v1.get(local).equals(this.value)) {
					tc.getvData().removeElementAt(i);
				}
			}
			//�����Ƿ�˱����������ο�
			for(Object i :global){
	             GlobalTableInfo global = (GlobalTableInfo)i;
	             if(this.sTablename.equals((global.getrTablename())) && this.colName.equals(global.getrColumnname())){
	              //���ʱ��ִ��ɾ������ ������������Ϊ���ձ� �������� ֵҲ���� ���Ǹ������ɾ������
	              this.setsTablename(global.pTablename);
	              ReadFromFile();
	              //ִ��ɾ������
	              int local1=0;
	              for(int j=0;j<tables.getvColumn().size();j++){
	                  ColumnDef col=(ColumnDef)(tables.getvColumn().get(j));
	                  if(col.getColName().equals(this.colName)) local1=j;
	              }
	              //��һ���µ��������ո��ĺ������ ������ȵ�ȫ������������ Ȼ���滻
	              Vector row = new Vector<>();
	              for(int j=0;j<tables.getvData().size();j++) {
	                  //��temp����
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
	v.addElement("����ɾ���ɹ�\n");
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
