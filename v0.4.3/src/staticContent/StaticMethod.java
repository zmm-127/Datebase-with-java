package staticContent;

import java.util.Stack;
import java.util.Vector;
import dataBase.*;

public class StaticMethod {
	public static void main(String[] args) {
	}
	public static int calOd(int od1,int od2,String opstacktopstr) {//��������������ļ���
		int num=0;
		if (0 == opstacktopstr.compareTo("+"))//"+"
			num=od1+od2;
		if (0 == opstacktopstr.compareTo("-"))//"-"
			num=od1-od2;
		if (0 == opstacktopstr.compareTo("*"))//"*"
			num=od1*od2;
		if (0 == opstacktopstr.compareTo("/"))//"/"
			num=od1/od2;
		return num;//���ؼ���ֵ
	}
	
	public static int calSimpleExp(Vector v) throws Exception {//����ջ���洢���ʽ
		if (v.size() == 0)//��v��СΪ0ʱ˵���ޱ��ʽ���룬����޷����м���
			throw new Exception("�޷�����");
		Stack opstack = new Stack();// ������ջ
		Stack odstack = new Stack();// ������ջ
		Object o;//����Object����
		String opstr, opstacktopstr;//��������������ջ������������
		int od1, od2;//��������������������������
		for (int i = 0; i < v.size(); i++) {//�����ʽ������ջ
			o = v.get(i);//��v����Ķ���ֵ��o
			if (o instanceof Integer)//�ж�o�����������Ƿ�Ϊ����
				odstack.push(o);// ������ֱ��ѹջ
			else {
				if (0 == opstack.size()) {//����������ջΪ�գ���������ֱ����ջ
					opstack.add(o);
					continue;
				}
				opstacktopstr = opstack.peek().toString();//�鿴ջ��Ԫ��
				opstr = o.toString();//��o�Ķ����¸�ֵ�Ĳ���������ת��ΪString���͸�ֵ��opstr����
				if (getOpPrecedence(opstr) > getOpPrecedence(opstacktopstr)) {//�Ƚ�ջ��Ԫ�غͽ�Ҫ��ջ��Ԫ�ص����ȼ�
					opstack.push(opstr);//opstr�����ȼ�����opstacktopstr����opstrֱ����ջ
					continue;
				}
				while (true) {
					if (0 == opstack.size()) {//��������ջ�Ĵ�СΪ0ʱ�������������������ʱ�����µĲ�����ֱ����ջ
						opstack.push(opstr);
						break;
					}
					opstacktopstr = opstack.peek().toString();//��������ջ��Ԫ�ظ�ֵ��opstacktopstr����
					if (getOpPrecedence(opstr) > getOpPrecedence(opstacktopstr)) {
						opstack.push(opstr);
						break;
					}
					od2 = (int) odstack.pop();
					od1 = (int) odstack.pop();
					opstacktopstr = opstack.pop().toString();
					odstack.push(calOd(od1, od2, opstacktopstr));//���в������������������
				}
			}
		}
		while(0<opstack.size())//��������ջ�Ĵ�С��Ϊ0ʱ�����в������������������
		{
			od2 = (int) odstack.pop();
			od1 = (int) odstack.pop();
			opstacktopstr = opstack.pop().toString();
			odstack.push(calOd(od1, od2, opstacktopstr));
		}
		return (int)odstack.peek();//���ز�������ջ��Ԫ��
	}

	public static int getOpPrecedence(String opstr) {	//����+-*/���ȼ�
		if (0 == opstr.compareTo("+"))
			return 1;
		if (0 == opstr.compareTo("-"))
			return 1;
		if (0 == opstr.compareTo("*"))
			return 2;
		if (0 == opstr.compareTo("/"))
			return 2;
		return 0;
	}
	
	public static Operand calExpression(Vector v) {
		Stack odStack=new Stack();   //������ջ
		Stack opStack=new Stack();   //������ջ
		Operator op1,op2;
		Operand od1,od2;
		for(int i=0;i<v.size();i++) {
			//������ֱ��ѹջ
			if(v.get(i).getClass().getName()=="dataBase.Operand") 
				odStack.push(v.get(i));
			else if(v.get(i).getClass().getSuperclass().getName()=="dataBase.Operator") {
				op1=(Operator)v.get(i);
				//���������ջΪ�գ�ֱ����ջ
				if(opStack.isEmpty())
					opStack.push(v.get(i));
				else {
					op2=(Operator)opStack.peek();
					//�����ǰ���������ȼ���ջ��Ԫ�����ȼ���,��ֱ��ѹջ
					if(op1.precedence()>op2.precedence())
						opStack.push(v.get(i));
					else {
						//��ǰ���������ȼ���ջ��Ԫ�����ȼ��ͻ����
						while(true) {
							//���������ջΪ����ֱ��ѹ��
						}
					}
				}
				if(op1.getClass().getName()=="dataBase.LParaOperator")
					opStack.push(v.get(i));
				else if(op1.getClass().getName()=="dataBase.RParaOperator") {
					//������������ջ����˵�������˴���
					if(opStack.isEmpty())
						throw new Error("���ʽ�������");
					while(true) {
						
					}
				}
			}
		}
		return null;
	}
	
	public static Operand calObject(Object ob) throws Exception
	{
		Expression exp;
		Function f;
		int i=0;
		if(ob instanceof Operand)
			return (Operand)ob;
		else if(ob instanceof Expression)
		{
			exp=(Expression) ob;
			return calExpression(exp.getElements());
		}
		else if(ob instanceof Function)
		{
			f=((Function) ob).get(((Function) ob).name);
			return f.eval();
		}
		else 
			throw new Exception("�޷������Ԫ��");	
	}
}
