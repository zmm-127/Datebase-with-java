package staticContent;

public class DataType {
	static final public int INT=1;		//����
	static final public int FLOAT=2;	//С��
	static final public int STRING=3;	//�ַ���
	static final public int BOOL=4;	//����ֵ
//	static final int DATE=5;	//����
	static final public int SET=6;		//����
	static final public int BLOB=7;	//������
	static final public int NULL=8;	//��ֵ
	
	public static String getDescribe(int datatype)
	{
		switch(datatype)
		{
			case DataType.INT:
				return "����";
			case DataType.FLOAT:
				return "С��";
			case DataType.STRING:
				return "�ַ�����";
			case DataType.BOOL:
				return "������";
			case DataType.SET:
				return "������";
			case DataType.BLOB:
				return "������ֵ��";
			case DataType.NULL:
				return "��ֵ��";
		}
		return "��֧�ֵ���������";
	}
	
}
