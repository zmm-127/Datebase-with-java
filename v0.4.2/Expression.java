package dataBase;

import java.util.*;
import staticContent.*;

public class Expression implements java.io.Serializable  {

	private Vector  elements = new Vector();
	private	String	expstr="";
	
	public void setExpStr(String str) //设置表达式的字符串
	{
		expstr=str;
	}
	
	public String getString() //返回表达式的字符串
	{
		return expstr;
	}
	
	public void addOperator(Operator op) { //将操作符加入数组
		elements.add(op);
	}

	public void addElement(Object ob) {  //判断输入的表达式ob是否属于操作数，操作符，函数，变量。如若是则加入element数组中
		if (ob instanceof Operand || ob instanceof Operator || ob instanceof Function || ob instanceof Variable) {
			elements.add(ob);
		} else {
			throw new Error("Unknown element type added to expression: " + ob.getClass());
		}
	}

	public Object elementAt(int n) { //返回指定位置的数组元素
		return elements.get(n);
	}

	public void setexpression(Vector e) {
		elements=e;
	}
	
	public void setElementAt(int n, Object ob) { //设置指定位置的数组中的元素
		elements.set(n, ob);
	}
	
	public Vector getElements() //返回表达式数组
	{
		return elements;
	}
	
	public void printElements() { //打印表达式数组中的元素
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
	
	public String getDescribe() { //打印表达式数组中的元素
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

	public void mergerExpression(Expression exp2) { //将表达式加入表达式数组中
		int n = exp2.elements.size();
		for (int i = 0; i < n; i++)
			this.elements.add(exp2.elementAt(i));
	}
	
}
