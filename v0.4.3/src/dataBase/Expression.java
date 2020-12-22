package dataBase;

import java.util.*;
import staticContent.*;

public class Expression implements java.io.Serializable  {

	private Vector  elements = new Vector();
	private	String	expstr="";
	
	public void setExpStr(String str) //���ñ��ʽ���ַ���
	{
		expstr=str;
	}
	
	public String getString() //���ر��ʽ���ַ���
	{
		return expstr;
	}
	
	public void addOperator(Operator op) { //����������������
		elements.add(op);
	}

	public void addElement(Object ob) {  //�ж�����ı��ʽob�Ƿ����ڲ��������������������������������������element������
		if (ob instanceof Operand || ob instanceof Operator || ob instanceof Function || ob instanceof Variable) {
			elements.add(ob);
		} else {
			throw new Error("Unknown element type added to expression: " + ob.getClass());
		}
	}

	public Object elementAt(int n) { //����ָ��λ�õ�����Ԫ��
		return elements.get(n);
	}

	public void setexpression(Vector e) {
		elements=e;
	}
	
	public void setElementAt(int n, Object ob) { //����ָ��λ�õ������е�Ԫ��
		elements.set(n, ob);
	}
	
	public Vector getElements() //���ر��ʽ����
	{
		return elements;
	}
	
	public void printElements() { //��ӡ���ʽ�����е�Ԫ��
		Object ob;
		for (int i = 0; i < elements.size(); i++) {
			ob = elements.get(i);
			System.out.print(ob.getClass().getName() + " ");
			if (ob instanceof Operand)
				System.out.print(((Operand) ob).getString() + " ");
			else if (ob instanceof Operator)
				System.out.print(((Operator) ob).stringRepresentation());
			System.out.println();
		}
	}
	
	public String getDescribe() { //��ӡ���ʽ�����е�Ԫ��
		Object ob;
		String str1="";
		for (int i = 0; i < elements.size(); i++) {
			ob = elements.get(i);
			if (ob instanceof Operand)
				str1+=((Operand) ob).getString() + " ";
			else if(ob instanceof Variable) 
				str1+=((Variable)ob).GetName() + " ";
			else if (ob instanceof Operator)
				str1+=((Operator) ob).stringRepresentation() + " ";
		}
		str1+="\n";
		return str1;
	}

	public void mergerExpression(Expression exp2) { //�����ʽ������ʽ������
		int n = exp2.elements.size();
		for (int i = 0; i < n; i++)
			this.elements.add(exp2.elementAt(i));
	}
	
}
