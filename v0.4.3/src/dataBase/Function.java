package dataBase;

import java.util.*;
import staticContent.*;

public abstract class Function implements java.io.Serializable{
	// _start ��Ա��������������
	public String name; // ��������
	public Vector param; // ��������

	public abstract Operand eval() throws Exception;// �ɲ�����������

	public Function() {
		this.name = "";
		this.param = new Vector();
	}
	
	public  Function(String name) {
		this.name = name;
		this.param = new Vector();
	}

	public void SetName(String name) { //���ú�������
		this.name = name;
	}

	public void SetParam(Vector v) { //���ò�������
		this.param = v;
	}
	
	public void AddParam(Expression exp) { //����Ӧ�Ĳ������뵽������������
		this.param.addElement(exp);
	}
	
	public void PrintFunctionInfo() throws Exception //��ӡ�����ĺ������Ͳ���
	{
		Object ob;
		Expression exp;
		System.out.println("�������ƣ�"+name); 
		System.out.println("����������"+param.size());
		for(int i=0;i<param.size();i++)
		{
			ob=param.get(i);
			System.out.println("��"+i+"���������ͣ�"+ob.getClass().getName());
//			exp=(Expression)ob;
//			System.out.println("��"+i+"������ֵ��"+exp.calExpression());
		}
	}
	// _end
	public static Function get(String fname) {  //������ƥ��
		
	    if (fname.toLowerCase().equals("max")) { return max_f; }
	    else if (fname.toLowerCase().equals("min")) { return min_f; }
	    else if (fname.toLowerCase().equals("length")) { return length_f; }
	    else
	    throw new Error("Unrecognised operator type: " + fname);
	}
	private final static MaxFunction max_f = new MaxFunction();
	private final static MinFunction min_f = new MinFunction();
	private final static LengthFunction length_f = new LengthFunction();
}


//_start ������������
class MaxFunction extends Function { //MAX()����
	public MaxFunction() {
		super("MAX");
	}

	public Operand eval() throws Exception { //�������бȽ�
		Operand od = new Operand();
		Operand od2;
		
			if (param.size() == 0)
				throw new Exception("����Ϊ��");
			od = StaticMethod.calObject(param.get(0));
			for (int i = 1; i < param.size(); i++) {
				od2 =StaticMethod.calObject(param.get(i));
				if (od2.getNumericValue() > od.getNumericValue())
					od = od2;
			}	
		return od;
	}
};

class MinFunction extends Function { //MIN()����
	public MinFunction() {
		super("MIN");
	}

	public Operand eval() throws Exception {  //�������бȽ�
		Operand od = new Operand();
		Operand od2;
		
			if (param.size() == 0)
				throw new Exception("����Ϊ��");
			od = StaticMethod.calObject(param.get(0));
			for (int i = 1; i < param.size(); i++) {
				od2 =StaticMethod.calObject(param.get(i));
				if (od2.getNumericValue() < od.getNumericValue())
					od = od2;
			}
		return od;
	}
};

class LengthFunction extends Function { //LENGTH()����
	public LengthFunction() {
		super("LENGTH");
	}

	public Operand eval() throws Exception {
		Operand od = new Operand();
		Operand ods = new Operand();
		String str;
		
			if (param.size() != 1)
				throw new Exception("������������");
			od = StaticMethod.calObject(param.get(0));
			if(od.getType()!=DataType.STRING)
				throw new Exception("�������ʹ���");
			str=od.GetStringValue();
			ods.SetINT(str.length()-2);
		return ods;
	}
};
// _end
