package staticContent;

import java.util.Stack;
import java.util.Vector;
import dataBase.*;

public class StaticMethod {
	public static void main(String[] args) {
	}
	public static int calOd(int od1,int od2,String opstacktopstr) {//进行数与运算符的计算
		int num=0;
		if (0 == opstacktopstr.compareTo("+"))//"+"
			num=od1+od2;
		if (0 == opstacktopstr.compareTo("-"))//"-"
			num=od1-od2;
		if (0 == opstacktopstr.compareTo("*"))//"*"
			num=od1*od2;
		if (0 == opstacktopstr.compareTo("/"))//"/"
			num=od1/od2;
		return num;//返回计算值
	}
	
	public static int calSimpleExp(Vector v) throws Exception {//调用栈来存储表达式
		if (v.size() == 0)//当v大小为0时说明无表达式输入，因此无法进行计算
			throw new Exception("无法计算");
		Stack opstack = new Stack();// 操作符栈
		Stack odstack = new Stack();// 操作数栈
		Object o;//定义Object对象
		String opstr, opstacktopstr;//定义操作符对象和栈顶操作符对象
		int od1, od2;//定义需进行运算的两个整数对象
		for (int i = 0; i < v.size(); i++) {//将表达式进行入栈
			o = v.get(i);//将v数组的对象赋值给o
			if (o instanceof Integer)//判断o的数据类型是否为整型
				odstack.push(o);// 操作数直接压栈
			else {
				if (0 == opstack.size()) {//如若操作符栈为空，将操作符直接入栈
					opstack.add(o);
					continue;
				}
				opstacktopstr = opstack.peek().toString();//查看栈顶元素
				opstr = o.toString();//将o的对象即新赋值的操作符对象转化为String类型赋值给opstr对象
				if (getOpPrecedence(opstr) > getOpPrecedence(opstacktopstr)) {//比较栈顶元素和将要入栈的元素的优先级
					opstack.push(opstr);//opstr的优先级大于opstacktopstr，则将opstr直接入栈
					continue;
				}
				while (true) {
					if (0 == opstack.size()) {//当操作符栈的大小为0时，即操作符已运算完毕时，将新的操作符直接入栈
						opstack.push(opstr);
						break;
					}
					opstacktopstr = opstack.peek().toString();//将操作符栈顶元素赋值给opstacktopstr对象
					if (getOpPrecedence(opstr) > getOpPrecedence(opstacktopstr)) {
						opstack.push(opstr);
						break;
					}
					od2 = (int) odstack.pop();
					od1 = (int) odstack.pop();
					opstacktopstr = opstack.pop().toString();
					odstack.push(calOd(od1, od2, opstacktopstr));//进行操作数与操作符的运算
				}
			}
		}
		while(0<opstack.size())//当操作符栈的大小不为0时，进行操作数与操作符的运算
		{
			od2 = (int) odstack.pop();
			od1 = (int) odstack.pop();
			opstacktopstr = opstack.pop().toString();
			odstack.push(calOd(od1, od2, opstacktopstr));
		}
		return (int)odstack.peek();//返回操作数的栈顶元素
	}

	public static int getOpPrecedence(String opstr) {	//定义+-*/优先级
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
		Stack odStack=new Stack();   //操作数栈
		Stack opStack=new Stack();   //操作符栈
		Operator op1,op2;
		Operand od1,od2;
		for(int i=0;i<v.size();i++) {
			//操作数直接压栈
			if(v.get(i).getClass().getName()=="dataBase.Operand") 
				odStack.push(v.get(i));
			else if(v.get(i).getClass().getSuperclass().getName()=="dataBase.Operator") {
				op1=(Operator)v.get(i);
				//如果操作符栈为空，直接入栈
				if(opStack.isEmpty())
					opStack.push(v.get(i));
				else {
					op2=(Operator)opStack.peek();
					//如果当前操作符优先级比栈顶元素优先级高,则直接压栈
					if(op1.precedence()>op2.precedence())
						opStack.push(v.get(i));
					else {
						//当前操作符优先级比栈顶元素优先级低或相等
						while(true) {
							//如果操作符栈为空则直接压入
						}
					}
				}
				if(op1.getClass().getName()=="dataBase.LParaOperator")
					opStack.push(v.get(i));
				else if(op1.getClass().getName()=="dataBase.RParaOperator") {
					//右括号碰到空栈，则说明产生了错误
					if(opStack.isEmpty())
						throw new Error("表达式计算错误。");
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
			throw new Exception("无法计算的元素");	
	}
}
