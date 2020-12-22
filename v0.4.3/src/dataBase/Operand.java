package dataBase;

import staticContent.*;
/*
 * �������࣬ÿ��������
 */
/*
 * ���л�����ԭ�����ڴ��еĶ���״̬ ��ɿɴ洢����Ĺ��̳�֮Ϊ���л������л�֮�󣬾Ϳ��԰����л��������д����̣�����ͨ�����紫�䵽��Ļ����ϡ�
 * ����java��Ϊ�������VO���е������ֶ�name��age�ڳ������к��ڶ��ڴ��У�����ִ����Ϻ��ڴ�õ��ͷţ�name��age��ֵҲ�������ڡ�
 * ������ڼ����Ҫ��������ʵ�����͵���һ̨�����������뱣�����VO���ʵ�������ݿ⣨�־û����󣩣��Ա��Ժ���ȡ�����á�
 * ��ʱ����Ҫ�������������л������ڴ��ͻ򱣴档�õ�ʱ���ٷ����л�����������������ʵ����
 */

public class Operand implements java.io.Serializable{		//���л��ӿ�

	private byte[] content; // ����
	

	// _start ���졢���ü�ȡֵ����
	public Operand() {
		this.SetNULL();
	}
	public void SetNULL() {	//��content������Ϊ��

		if (content == null)
			content = new byte[1];
		if (1 != content.length) {
			content = null;
			content = new byte[1];
		}
		content[0] = (byte) DataType.NULL;	//content�������Ԫ�ش洢��������
		

	}
	/**
	 * ���캯��-����
	 * 
	 * @param i ��Ҫ�洢������
	 * @return ��������
	 */
	public Operand(int i) {
		this.SetINT(i);
	}

	/**
	 * ����ֵ����-����
	 * 
	 * @param i ��Ҫ�洢������
	 * @return ��
	 */
	/*
	 * ���� << �������ָ��λ�����ұ߲�0
	 * ����>>�����ұ�ָ��λ������߲��Ϸ���λ
	 */
	public void SetINT(int i) {

		if (content == null)	//����content���鳤��Ϊ0�������С����Ϊ5���ֽ�
			content = new byte[5];
		if (5 != content.length) {	//����content���鳤��С��5���Ƚ�����Ϊ�գ��ٽ����С����Ϊ5���ֽ�
			content = null;
			content = new byte[5];
		}
		content[0] = (byte) DataType.INT;	//�����һ��λ�ô洢�������ͼ�����
		content[1] = (byte) ((i & 0xff000000) >> 24);  //ȡ�������ĵ�һ���ֽ�,������24λ����������һ���ֽ�����������洢������ڶ���λ��
		content[2] = (byte) ((i & 0x00ff0000) >> 16);  //ȡ�������ĵڶ����ֽ�,������16λ���������ڶ����ֽ�����������洢�����������λ��
		content[3] = (byte) ((i & 0x0000ff00) >> 8);   //ȡ�������ĵ������ֽ�,������8λ���������������ֽ�����������洢��������ĸ�λ��
		content[4] = (byte) (i & 0x000000ff);  //ȡ�������ĵ��ĸ��ֽڣ����������ĸ��ֽ�����������洢����������λ��
	}

	/**
	 * ȡֵ����-����
	 * 
	 * @return ��Ӧ������ֵ��������������������׳��쳣
	 */
	public int GetIntValue() throws Exception {
		if (5 != content.length)  //�������鳤�Ȳ�Ϊ5�����������ͣ��׳��쳣
			throw new Exception("�Ƿ���������ת����");
		if (content.length > 0) {  //��������ĵ�һ��λ���ϴ洢���������Ͳ������ͣ��׳��쳣
			if (DataType.INT != content[0])
				throw new Exception("�Ƿ���������ת����");
		}
		//������2-5λ�ô洢�����ֱ�������Ӧ��λ������ԭΪԭ��
		return (0xff000000 & (content[1] << 24)) | (0x00ff0000 & (content[2] << 16)) 
				| (0x0000ff00 & (content[3] << 8))| (0x000000ff & content[4]);
	}

	/**
	 * ���캯��-������
	 * 
	 * @param f ��Ҫ�洢�ĸ�����
	 * @return ��������
	 */
	public Operand(float f) {
		this.SetFLOAT(f);
	}

	/**
	 * ����ֵ����-������
	 * 
	 * @param f ��Ҫ�洢�ĸ�����
	 * @return ��
	 */
	/*
	 * ����IEEE 754��׼��32λ�������ڼ�����ж����ƴ洢��ʽ�������֣�S(1λ������) E(8λ������) M(23λ��β��)
	 * ����Float.floatToIntBits(20.5f)�������·�ʽ���㣺20.59D=10100.1B=1.01001*2^4B ָ��e=4
	 * S=0-->����  E=4+127=131D=10000011B-->��ʵָ��e��ɽ���Eʱ���127  M=01001B
	 * ��32λ2���ƴ洢��ʽΪ��0 10000011 01001000000000000000000
	 * ת����10���Ƽ�1101266944
	 */
	public void SetFLOAT(float f) {		//ͬ��
		if (content == null)
			content = new byte[5];
		if (5 != content.length) {
			content = null;
			content = new byte[5];
		}
		int i = Float.floatToIntBits(f);  //����������ֵ�Զ����Ƶ���ʽ�洢�ٽ���ת��Ϊʮ�������ڴ���
		this.SetINT(i);
		content[0] = DataType.FLOAT;	//�����һ��λ�ô洢�������ͼ�������
	}

	/**
	 * ȡֵ����-����
	 * 
	 * @return ��Ӧ�ĸ�����ֵ��������Ǹ������������׳��쳣
	 */
	public float GetFloatValue() throws Exception {	//ͬ��
		if (5 != content.length)
			throw new Exception("�Ƿ���������ת����");
		if (content.length > 0) {
			if (DataType.FLOAT != content[0])
				throw new Exception("�Ƿ���������ת����");
		}

		return Float.intBitsToFloat((0xff000000 & (content[1] << 24)) | (0x00ff0000 & (content[2] << 16))
				| (0x0000ff00 & (content[3] << 8)) | (0x000000ff & content[4]));
	}

	/**
	 * ���캯��-����ֵ
	 * 
	 * @param b ��Ҫ�洢�Ĳ���ֵ
	 * @return ��������
	 */
	public Operand(boolean b) {
		this.SetBOOL(b);
	}

	/**
	 * ����ֵ����-����ֵ
	 * 
	 * @param b ��Ҫ�洢�Ĳ���ֵ�������true���򱣴�Ϊ1�����Ϊfalse���򱣴�Ϊ0
	 * @return ��
	 */
	public void SetBOOL(boolean b) {	//ͬ��

		if (content == null)
			content = new byte[2];
		if (2 != content.length) {
			content = null;
			content = new byte[2];
		}
		content[0] = (byte) DataType.BOOL;
		if (b)
			content[1] = (byte) 1;
		else
			content[1] = (byte) 0;
	}

	/**
	 * ȡֵ����-����ֵ
	 * 
	 * @return ��Ӧ�Ĳ���ֵ��������ǲ���ֵ�������׳��쳣
	 */
	public boolean GetBoolValue() throws Exception {	//ͬ��
		if (2 != content.length)
			throw new Exception("�Ƿ���������ת����");
		if (content.length > 0) {
			if (DataType.BOOL != content[0])
				throw new Exception("�Ƿ���������ת����");
		}

		return (content[1] == 1);  //����content[1]��1�򷵻�true��֮����false
	}

	/**
	 * ���캯��-�ַ���
	 * 
	 * @param str ��Ҫ�洢���ַ���
	 * @return ��������
	 */
	public Operand(String str) {
		this.SetSTRING(str);
	}

	/**
	 * ����ֵ����-�ַ���
	 * 
	 * @param str ��Ҫ�洢���ַ���
	 * @return ��
	 */
	public void SetSTRING(String str) {	//ͬ��
		if (content == null)
			content = new byte[str.length() + 2];
		if (str.length() + 2 != content.length) {
			content = null;
			content = new byte[str.length() + 2];
		}
		content[0] = (byte) DataType.STRING;
		content[1] = (byte) (str.length()); //�洢�ַ������ȣ��ַ������Ȳ��ܳ���128
		for (int i = 2; i < str.length() + 2; i++)
			content[i] = (byte) str.charAt(i - 2);
	}

	/**
	 * ȡֵ����-�ַ���
	 * 
	 * @return ��Ӧ���ַ���ֵ����������ַ����������׳��쳣
	 */
	public String GetStringValue() throws Exception {
		if (content.length < 2)
			throw new Exception("�Ƿ���������ת����");
		if (DataType.STRING != content[0])
			throw new Exception("�Ƿ���������ת����");

		String str = "";
		for (int i = 2; i < content[1] + 2; i++)
			str += (char) content[i];
		return str;

	}

	// _end*/

	// _start ��������
	/**
	 * ��ʾ��������ԭʼ����
	 */
	public void showOriginContent() {
		for (int i = 0; i < content.length; i++)
			System.out.print("0x" + byteToHex(content[i]) + " "); //��ʮ��������ʽ���
		System.out.println();
	}

	/**
	 * ��ʾ���������ַ�����ʽ
	 */
	public String getString() {
		if (this.content == null)
			return "";
		try {
			switch (this.content[0]) {
			case DataType.INT:
				return Integer.toString(this.GetIntValue());
			case DataType.FLOAT:
				return Float.toString(this.GetFloatValue());
			case DataType.BOOL:
				return Boolean.toString(this.GetBoolValue());
			case DataType.STRING:
				return this.GetStringValue();
			default:
				return "";
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return "";
	}

	/**
	 * �ֽ�תʮ������
	 * 
	 * @param b ��Ҫ����ת����byte�ֽ�
	 * @return ת�����Hex�ַ���
	 */
	public static String byteToHex(byte b) {
		String hex = Integer.toHexString(b & 0xFF);  //ת��Ϊʮ�����Ƶ��ַ���
		if (hex.length() < 2) {  //��ʮ�����Ƶı�ʾ������λʱ����ǰ�油0����2�򲹳�02
			hex = "0" + hex;
		}
		return hex;
	}
	
	public float getNumericValue() throws Exception  //��ȡ��������ֵ
	{
		if (!(DataType.INT == content[0] || DataType.FLOAT == content[0] ))
			throw new Exception("�������޷������ֵ");
		if (DataType.INT == this.content[0])
			return (float)this.GetIntValue();
		else 
			return this.GetFloatValue();
	}
	public int getType()	//��ȡ���Ӧ��content��������
	{
		return (int)content[0];
	}
	// _end

	// _start ���������㺯�� �� �� �� �� ȡ��
	/*
	 * �ӷ�����
	 */
	public Operand OperatorAdd(Operand od) throws Exception {
		if (!(DataType.INT == content[0] || DataType.FLOAT == content[0] || DataType.STRING == content[0]))
			throw new Exception("�������޷����ӷ�����");
		if (!(DataType.INT == od.content[0] || DataType.FLOAT == od.content[0] || DataType.STRING == od.content[0]))
			throw new Exception("�������޷����ӷ�����");
		Operand rod=new Operand();
		// ����+���� ���Ϊ����
		if (DataType.INT == this.content[0] && DataType.INT == od.content[0]) {
			rod.SetINT(this.GetIntValue() + od.GetIntValue());
		}
		// ����+������ ���Ϊ������
		else if (DataType.INT == this.content[0] && DataType.FLOAT == od.content[0]) {
			rod.SetFLOAT(this.GetIntValue() + od.GetFloatValue());
		}
		// ������+���� ���Ϊ������
		else if (DataType.FLOAT == this.content[0] && DataType.INT == od.content[0]) {
			rod.SetFLOAT(this.GetFloatValue() + od.GetIntValue());
		}
		// ������+������ ���Ϊ������
		else if (DataType.FLOAT == this.content[0] && DataType.FLOAT == od.content[0]) {
			rod.SetFLOAT(this.GetFloatValue() + od.GetFloatValue());
		} else if (DataType.STRING == this.content[0] && DataType.STRING == od.content[0]) { //�ַ������
			rod.SetSTRING(this.getString() + od.getString());
		} else
			throw new Exception("�������޷����ӷ�����");
		return rod;
	}

	/*
	 * ��������
	 */
	public Operand OperatorSub(Operand od) throws Exception {
		if (!(DataType.INT == this.content[0] || DataType.FLOAT == this.content[0]))
			throw new Exception("�������޷�����������");
		if (!(DataType.INT == od.content[0] || DataType.FLOAT == od.content[0]))
			throw new Exception("�������޷�����������");
		Operand rod=new Operand();
		// ����-���� ���Ϊ����
		if (DataType.INT == this.content[0] && DataType.INT == od.content[0]) {
			rod.SetINT(this.GetIntValue() - od.GetIntValue());
		}
		// ����-������ ���Ϊ������
		else if (DataType.INT == this.content[0] && DataType.FLOAT == od.content[0]) {
			rod.SetFLOAT(this.GetIntValue() - od.GetFloatValue());
		}
		// ������-���� ���Ϊ������
		else if (DataType.FLOAT == this.content[0] && DataType.INT == od.content[0]) {
			rod.SetFLOAT(this.GetFloatValue() - od.GetIntValue());
		}
		// ������-������ ���Ϊ������
		else if (DataType.FLOAT == this.content[0] && DataType.FLOAT == od.content[0]) {
			rod.SetFLOAT(this.GetFloatValue() - od.GetFloatValue());
		}
		return rod;
	}

	/*
	 * �˷�����
	 */
	public Operand OperatorMul(Operand od) throws Exception {
		if (!(DataType.INT == this.content[0] || DataType.FLOAT == this.content[0]))
			throw new Exception("�������޷����˷�����");
		if (!(DataType.INT == od.content[0] || DataType.FLOAT == od.content[0]))
			throw new Exception("�������޷����˷�����");
		Operand rod=new Operand();
		// ����*���� ���Ϊ����
		if (DataType.INT == this.content[0] && DataType.INT == od.content[0]) {
			rod.SetINT(this.GetIntValue() * od.GetIntValue());
		}
		// ����*������ ���Ϊ������
		else if (DataType.INT == this.content[0] && DataType.FLOAT == od.content[0]) {
			rod.SetFLOAT(this.GetIntValue() * od.GetFloatValue());
		}
		// ������*���� ���Ϊ������
		else if (DataType.FLOAT == this.content[0] && DataType.INT == od.content[0]) {
			rod.SetFLOAT(this.GetFloatValue() * od.GetIntValue());
		}
		// ������*������ ���Ϊ������
		else if (DataType.FLOAT == this.content[0] && DataType.FLOAT == od.content[0]) {
			rod.SetFLOAT(this.GetFloatValue() * od.GetFloatValue());
		}
		return rod;
	}

	/*
	 * ��������(������)
	 */
	public Operand OperatorFDiv(Operand od) throws Exception {
		if (!(DataType.INT == this.content[0] || DataType.FLOAT == this.content[0]))
			throw new Exception("�������޷�����������");
		if (!(DataType.INT == od.content[0] || DataType.FLOAT == od.content[0]))
			throw new Exception("�������޷�����������");
		float f1, f2;
		Operand rod=new Operand();
		// ����/���� ���Ϊ������
		if (DataType.INT == this.content[0] && DataType.INT == od.content[0]) {
			f1 = (float) this.GetIntValue();
			f2 = (float) od.GetIntValue();
			rod.SetFLOAT(f1 / f2);
		}
		// ����/������ ���Ϊ������
		else if (DataType.INT == this.content[0] && DataType.FLOAT == od.content[0]) {
			f1 = (float) this.GetIntValue();
			rod.SetFLOAT(f1 / od.GetFloatValue());
		}
		// ������/���� ���Ϊ������
		else if (DataType.FLOAT == this.content[0] && DataType.INT == od.content[0]) {
			f2 = (float) od.GetIntValue();
			rod.SetFLOAT(this.GetFloatValue() / f2);
		}
		// ������/������ ���Ϊ������
		else if (DataType.FLOAT == this.content[0] && DataType.FLOAT == od.content[0]) {
			rod.SetFLOAT(this.GetFloatValue() / od.GetFloatValue());
		}
		return rod;
	}

	/*
	 * ȡ�����㣬������������������ȡ��
	 */
	public Operand OperatorMod(Operand od) throws Exception {
		if (!(DataType.INT == this.content[0]))
			throw new Exception("�������޷���ȡ������");
		if (!(DataType.INT == od.content[0]))
			throw new Exception("�������޷���ȡ������");
		Operand rod=new Operand();
		// ����/���� ���Ϊ����
		rod.SetINT(this.GetIntValue() % od.GetIntValue());
		return rod;
	}

	/*
	 * ��������
	 */
	public Operand OperatorGreat(Operand od) throws Exception {
		if (!(DataType.INT == this.content[0] || DataType.FLOAT == this.content[0]))
			throw new Exception("�������޷����Ƚ�����");
		if (!(DataType.INT == od.content[0] || DataType.FLOAT == od.content[0]))
			throw new Exception("�������޷����Ƚ�����");
		float f1, f2;
		Operand rod=new Operand();
		if (DataType.INT == this.content[0])
			f1 = (float) this.GetIntValue();
		else
			f1 = this.GetFloatValue();
		if (DataType.INT == od.content[0])
			f2 = (float) od.GetIntValue();
		else
			f2 = od.GetFloatValue();
		rod.SetBOOL(f1 > f2);
		return rod;
	}

	/*
	 * С������
	 */
	public Operand OperatorLess(Operand od) throws Exception {
		if (!(DataType.INT == this.content[0] || DataType.FLOAT == this.content[0]))
			throw new Exception("�������޷����Ƚ�����");
		if (!(DataType.INT == od.content[0] || DataType.FLOAT == od.content[0]))
			throw new Exception("�������޷����Ƚ�����");
		float f1, f2;
		Operand rod=new Operand();
		if (DataType.INT == this.content[0])
			f1 = (float) this.GetIntValue();
		else
			f1 = this.GetFloatValue();
		if (DataType.INT == od.content[0])
			f2 = (float) od.GetIntValue();
		else
			f2 = od.GetFloatValue();
		rod.SetBOOL(f1 < f2);
		return rod;
	}

	/*
	 * ��������
	 */
	public Operand OperatorEqual(Operand od) throws Exception {
		if (!(DataType.INT == this.content[0] || DataType.FLOAT == this.content[0]))
			throw new Exception("�������޷����Ƚ�����");
		if (!(DataType.INT == od.content[0] || DataType.FLOAT == od.content[0]))
			throw new Exception("�������޷����Ƚ�����");
		Operand rod=new Operand();
		if (this.content[0] == od.content[0]) {
			rod.SetBOOL((content[1] == od.content[1]) && (content[2] == od.content[2]) && (content[3] == od.content[3])
					&& (content[4] == od.content[4])); //�������д洢��ÿһ���ֽڵ������бȽ�
		} else {
			float f1, f2;
			if (DataType.INT == this.content[0])
				f1 = (float) this.GetIntValue();
			else
				f1 = this.GetFloatValue();
			if (DataType.INT == od.content[0])
				f2 = (float) od.GetIntValue();
			else
				f2 = od.GetFloatValue();
			rod.SetBOOL(Math.abs(f1 - f2) < 0.0000001);
		}
		return rod;
	}

	/*
	 * ����ֵ��������
	 */
	public Operand OperatorAnd(Operand od) throws Exception {
		if (!(DataType.BOOL == this.content[0] && DataType.BOOL == od.content[0]))
			throw new Exception("�������޷���������");
		Operand rod=new Operand();
		rod.SetBOOL(this.GetBoolValue() && od.GetBoolValue());
		return rod;
	}

	/*
	 * ����ֵ�Ļ�����
	 */
	public Operand OperatorOr(Operand od) throws Exception {
		if (!(DataType.BOOL == this.content[0] && DataType.BOOL == od.content[0]))
			throw new Exception("�������޷���������");
		Operand rod=new Operand();
		rod.SetBOOL(this.GetBoolValue() || od.GetBoolValue());
		return rod;
	}
	
	
	
	/*
	 * ����ֵ�ķ�����
	 */
	public Operand OperatorNot(Operand od) throws Exception {
		if (!( DataType.BOOL == od.content[0]))
			throw new Exception("�������޷���������");
		Operand rod=new Operand();
		rod.SetBOOL(!od.GetBoolValue());
		return rod;
	}
	// _end

	public static void main(String args[]) {
		System.out.println("-----�����������----");
		Operand o1 = new Operand(-1);
//		/Operand o2 = new Operand(7);

		try {
			//System.out.println(o1.OperatorAdd(o2));
			System.out.println(o1.GetIntValue());
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}
