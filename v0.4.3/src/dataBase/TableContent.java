package dataBase;

import staticContent.*;

import java.io.*;
import java.util.Vector;

public class TableContent implements java.io.Serializable {
	private static final long serialVersionUID=1L;
	private String tName; //����
	private Vector vColumn; //��
	private Vector vConstraint; //Լ��
	private Vector vData; //���е�����
	
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
		Vector v=new Vector();  //������������һ��Ԫ��Ϊ���ش��룬-1��ʾ�����ڶ���ֵΪ������ʾ���ַ���
		//�����Լ��һ���������������еı��ظ�
		GlobalTableInfo gtInfo=new GlobalTableInfo();
		if(gtInfo.fileIsExist(this.gettName())) {
			v.addElement(-1);
			v.addElement("�޷��������ݿ�������ͬ����");
			return v;
		}
		//�����Լ���������ֻ����һ��
		int pknum=0;
		for(int i=0;i<this.vColumn.size();i++) {
			if(((ColumnDef)this.vColumn.get(i)).isPrimaryKey()) {
				pknum++;
			}
			if(pknum>1) {
				v.addElement(-1);
				v.addElement("�޷������ڶ����������������");
				return v;
			}
		}
		for(int i=0;i<this.vConstraint.size();i++) {
			if(((TableConstraint)this.vConstraint.get(i)).getTcType()==1)
				pknum++;
			if(pknum>1) {
				v.addElement(-1);
				v.addElement("�޷������ദ����������");
				return v;
			}
		}
		//������
		TableConstraint tc;
		for(int i=0;i<vConstraint.size();i++) {
			tc=(TableConstraint)vConstraint.get(i);
			if(2!=tc.getTcType())
				continue;               //���Լ��

			TableContent table = ReadFromFile((String)tc.getTcV().get(1));//�ο���
            if(table.getFieldIndex((String)tc.getTcV().get(2),table.getvColumn())==-1){//�����ο��������Ƿ����
                System.out.println((String)tc.getTcV().get(1)+"���в�����"+(String)tc.getTcV().get(2)+"�У��޷��������Լ��");
                System.exit(0);
            }

            String c1 = (String)(tc.getTcV().get(0));
            String c2= (String)(tc.getTcV().get(2));
            if(!c1.equals(c2)){
                System.out.println("����Ͳο�������������ȣ��޷����������");
                System.exit(0);
            }
			
			gtInfo.addReference(this.tName,tc.getTcV().get(0).toString(),
									tc.getTcV().get(1).toString(),tc.getTcV().get(2).toString());
			
			gtInfo.WriteToFile();
			
		}
		//д���ļ�
		try {
			this.WriteToFile();
		}catch(Exception e){
			System.out.println(e.toString());
		}
		v.addElement(1);
		v.addElement("ִ�гɹ�");
		return v;
	}
	
	public void addRowData(Vector vRowData) {  //�������ݴ���������
		this.vData.addElement(vRowData);
	}
	
	//�õ������У�ÿ��Ԫ��Ϊһ��ColumnDef����
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
	
	//�õ��������±꣬ÿ��Ԫ��Ϊһ�����������ֶε����
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
		result+="������"+this.gettName()+"\n";
		result+="����"+this.getvColumn().size()+"���ֶ�\n";
		v=this.getvColumn();
		for(int i=0;i<v.size();i++) 
			result+=((ColumnDef) v.get(i)).getDescribe();
		result+="����"+this.getvConstraint().size()+"��Լ��\n";
		v=this.getvConstraint();
		for(int i=0;i<v.size();i++)
			result+=((TableConstraint)v.get(i)).getDescribe();
		result+="����"+this.getvData().size()+"������\n";
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
	 * ���������(ObjectOutputStream,ObjectInputStream)���Խ�һ������д�������߶�ȡһ�����󵽳�����
	 * Ҳ����ִ�������л��ͷ����л�������
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
