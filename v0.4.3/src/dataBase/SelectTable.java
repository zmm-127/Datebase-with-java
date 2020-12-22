package dataBase;

import java.util.Vector;

public class SelectTable {
	private String sTablename;  //����
	private Vector colName;		//����
	private Vector value;      //��ֵ
	
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
			if(this.colName.get(i).equals("*")) {
				break;
			}
			if(!gtInfo.colNameIsExist(tc,this.colName.get(i).toString())) {
				v.addElement(-1);
				v.addElement("�޷���ѯ������"+this.colName.get(i).toString()+"�ڱ��в�����");
				return v;
			}
		}
		if(tc.getvData().size()==0)
			System.out.println(this.sTablename+"����������");
		//��ӡ��������ֵ
		if(this.colName.get(0).equals("*")) {
			for(int i=0;i<tc.getvColumn().size();i++) {
				System.out.print(((ColumnDef)tc.getvColumn().get(i)).getColName()+"\t");
			}
			System.out.println();
			for(int j=0;j<tc.getvData().size();j++) {
				System.out.println(tc.getvData().elementAt(j)+"\t");
			}
		}else {//Ѱ�ҷ����������е�λ��
			Vector<Integer>  local = new Vector<Integer>();
			for(int i=0;i<this.colName.size();i++) {
				for(int j=0;j<tc.getvColumn().size();j++) {
					ColumnDef coldef=(ColumnDef)(tc.getvColumn().get(j));
					if(coldef.getColName().equals(this.colName.get(i)))
						local.addElement(j);
				}
			}
			Vector v1;
			//û��where���������
			if(this.value.size()==0) {
				for(int i=0;i<this.colName.size();i++) {//��ӡ��Ӧ����
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
			}else {//��where���������
				for(int i=0;i<this.colName.size()-1;i++) {//��ӡ��Ӧ����
					System.out.print(this.colName.get(i).toString()+"\t");
				}
				System.out.println();
				for(int i=0;i<tc.getvData().size();i++) {
					Vector vec = (Vector)(tc.getvData().get(i));
					//�ҳ�where�������е�λ��
					int Num = local.lastElement();
					//�ҳ�where�������е�����
					String localColName = (String)(this.colName.lastElement());
					//ȡ����where�������е�λ���ϵ�ֵ
					String localValue = (String)(vec.get(Num));
					//������ֵ����values
					if(localValue.equals(this.value.get(0))) {
						//��ӡ��from�����Ǽ��е�ֵ �Ǽ��е�λ�ô���local��
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
		v.addElement("���ݲ�ѯ�ɹ�\n");
		return v;
	}
}