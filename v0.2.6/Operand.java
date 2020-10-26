package dataBase;

import staticContent.*;
/*
 * 操作数类，每个操作数
 */
/*
 * 序列化：把原本在内存中的对象状态 变成可存储或传输的过程称之为序列化。序列化之后，就可以把序列化后的内容写入磁盘，或者通过网络传输到别的机器上。
 * 例以java类为例。这个VO类中的两个字段name和age在程序运行后都在堆内存中，程序执行完毕后内存得到释放，name和age的值也不复存在。
 * 如果现在计算机要把这个类的实例发送到另一台机器、或是想保存这个VO类的实例到数据库（持久化对象），以便以后再取出来用。
 * 这时就需要对这个类进行序列化，便于传送或保存。用的时候再反序列化重新生成这个对象的实例。
 */

public class Operand implements java.io.Serializable{		//序列化接口

	private byte[] content; // 内容

	// _start 构造、设置及取值函数
	public Operand() {
		this.SetNULL();
	}
	public void SetNULL() {	//将content数组设为空

		if (content == null)
			content = new byte[1];
		if (1 != content.length) {
			content = null;
			content = new byte[1];
		}
		content[0] = (byte) DataType.NULL;	//content数组的首元素存储数据类型
		

	}
	/**
	 * 构造函数-整数
	 * 
	 * @param i 需要存储的整数
	 * @return 操作数类
	 */
	public Operand(int i) {
		this.SetINT(i);
	}

	/**
	 * 设置值函数-整数
	 * 
	 * @param i 需要存储的整数
	 * @return 无
	 */
	/*
	 * 左移 << 丢弃左边指定位数，右边补0
	 * 右移>>丢弃右边指定位数，左边补上符号位
	 */
	public void SetINT(int i) {

		if (content == null)	//如若content数组长度为0，将其大小设置为5个字节
			content = new byte[5];
		if (5 != content.length) {	//如若content数组长度小于5，先将其设为空，再将其大小设置为5个字节
			content = null;
			content = new byte[5];
		}
		content[0] = (byte) DataType.INT;	//数组第一个位置存储数据类型即整型
		content[1] = (byte) ((i & 0xff000000) >> 24);  //取该整数的第一个字节,并右移24位，将该数第一个字节所代表的数存储于数组第二个位置
		content[2] = (byte) ((i & 0x00ff0000) >> 16);  //取该整数的第二个字节,并右移16位，将该数第二个字节所代表的数存储于数组第三个位置
		content[3] = (byte) ((i & 0x0000ff00) >> 8);   //取该整数的第三个字节,并右移8位，将该数第三个字节所代表的数存储于数组第四个位置
		content[4] = (byte) (i & 0x000000ff);  //取该整数的第四个字节，将该数第四个字节所代表的数存储于数组第五个位置
	}

	/**
	 * 取值函数-整数
	 * 
	 * @return 对应的整数值，如果不是整数类型则抛出异常
	 */
	public int GetIntValue() throws Exception {
		if (5 != content.length)  //如若数组长度不为5，代表不是整型，抛出异常
			throw new Exception("非法数据类型转换：");
		if (content.length > 0) {  //如若数组的第一个位置上存储的数据类型不是整型，抛出异常
			if (DataType.INT != content[0])
				throw new Exception("非法数据类型转换：");
		}
		//将数组2-5位置存储的数分别左移相应的位数，还原为原数
		return (0xff000000 & (content[1] << 24)) | (0x00ff0000 & (content[2] << 16)) 
				| (0x0000ff00 & (content[3] << 8))| (0x000000ff & content[4]);
	}

	/**
	 * 构造函数-浮点数
	 * 
	 * @param f 需要存储的浮点数
	 * @return 操作数类
	 */
	public Operand(float f) {
		this.SetFLOAT(f);
	}

	/**
	 * 设置值函数-浮点数
	 * 
	 * @param f 需要存储的浮点数
	 * @return 无
	 */
	/*
	 * 按照IEEE 754标准，32位浮点数在计算机中二进制存储形式共三部分：S(1位，符号) E(8位，阶码) M(23位，尾数)
	 * 例：Float.floatToIntBits(20.5f)按照如下方式计算：20.59D=10100.1B=1.01001*2^4B 指数e=4
	 * S=0-->正数  E=4+127=131D=10000011B-->真实指数e变成阶码E时需加127  M=01001B
	 * 则32位2进制存储形式为：0 10000011 01001000000000000000000
	 * 转换成10进制即1101266944
	 */
	public void SetFLOAT(float f) {		//同上
		if (content == null)
			content = new byte[5];
		if (5 != content.length) {
			content = null;
			content = new byte[5];
		}
		int i = Float.floatToIntBits(f);  //将浮点数的值以二进制的形式存储再将其转换为十进制于内存中
		this.SetINT(i);
		content[0] = DataType.FLOAT;	//数组第一个位置存储数据类型即浮点型
	}

	/**
	 * 取值函数-浮数
	 * 
	 * @return 对应的浮点数值，如果不是浮点数类型则抛出异常
	 */
	public float GetFloatValue() throws Exception {	//同上
		if (5 != content.length)
			throw new Exception("非法数据类型转换：");
		if (content.length > 0) {
			if (DataType.FLOAT != content[0])
				throw new Exception("非法数据类型转换：");
		}

		return Float.intBitsToFloat((0xff000000 & (content[1] << 24)) | (0x00ff0000 & (content[2] << 16))
				| (0x0000ff00 & (content[3] << 8)) | (0x000000ff & content[4]));
	}

	/**
	 * 构造函数-布尔值
	 * 
	 * @param b 需要存储的布尔值
	 * @return 操作数类
	 */
	public Operand(boolean b) {
		this.SetBOOL(b);
	}

	/**
	 * 设置值函数-布尔值
	 * 
	 * @param b 需要存储的布尔值，如果是true，则保存为1，如果为false，则保存为0
	 * @return 无
	 */
	public void SetBOOL(boolean b) {	//同上

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
	 * 取值函数-布尔值
	 * 
	 * @return 对应的布尔值，如果不是布尔值类型则抛出异常
	 */
	public boolean GetBoolValue() throws Exception {	//同上
		if (2 != content.length)
			throw new Exception("非法数据类型转换：");
		if (content.length > 0) {
			if (DataType.BOOL != content[0])
				throw new Exception("非法数据类型转换：");
		}

		return (content[1] == 1);  //如若content[1]是1则返回true反之返回false
	}

	/**
	 * 构造函数-字符串
	 * 
	 * @param str 需要存储的字符串
	 * @return 操作数类
	 */
	public Operand(String str) {
		this.SetSTRING(str);
	}

	/**
	 * 设置值函数-字符串
	 * 
	 * @param str 需要存储的字符串
	 * @return 无
	 */
	public void SetSTRING(String str) {	//同上
		if (content == null)
			content = new byte[str.length() + 2];
		if (str.length() + 2 != content.length) {
			content = null;
			content = new byte[str.length() + 2];
		}
		content[0] = (byte) DataType.STRING;
		content[1] = (byte) (str.length()); //存储字符串长度，字符串长度不能超过128
		for (int i = 2; i < str.length() + 2; i++)
			content[i] = (byte) str.charAt(i - 2);
	}

	/**
	 * 取值函数-字符串
	 * 
	 * @return 对应的字符串值，如果不是字符串类型则抛出异常
	 */
	public String GetStringValue() throws Exception {
		if (content.length < 2)
			throw new Exception("非法数据类型转换：");
		if (DataType.STRING != content[0])
			throw new Exception("非法数据类型转换：");

		String str = "";
		for (int i = 2; i < content[1] + 2; i++)
			str += (char) content[i];
		return str;

	}

	// _end*/

	// _start 辅助函数
	/**
	 * 显示操作数的原始内容
	 */
	public void showOriginContent() {
		for (int i = 0; i < content.length; i++)
			System.out.print("0x" + byteToHex(content[i]) + " "); //以十六进制形式输出
		System.out.println();
	}

	/**
	 * 显示操作数的字符串形式
	 */
	public String getString() {
		if (content == null)
			return "";
		try {
			switch (content[0]) {
			case DataType.INT:
				return Integer.toString(GetIntValue());
			case DataType.FLOAT:
				return Float.toString(GetFloatValue());
			case DataType.BOOL:
				return Boolean.toString(GetBoolValue());
			case DataType.STRING:
				return GetStringValue();
			default:
				return "";
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return "";
	}

	/**
	 * 字节转十六进制
	 * 
	 * @param b 需要进行转换的byte字节
	 * @return 转换后的Hex字符串
	 */
	public static String byteToHex(byte b) {
		String hex = Integer.toHexString(b & 0xFF);  //转换为十六进制的字符串
		if (hex.length() < 2) {  //若十六进制的表示不够两位时，在前面补0，如2则补成02
			hex = "0" + hex;
		}
		return hex;
	}
	
	public float getNumericValue() throws Exception  //获取操作数的值
	{
		if (!(DataType.INT == content[0] || DataType.FLOAT == content[0] ))
			throw new Exception("操作数无法获得数值");
		if (DataType.INT == this.content[0])
			return (float)this.GetIntValue();
		else 
			return this.GetFloatValue();
	}
	public int getType()	//获取相对应的content数据类型
	{
		return (int)content[0];
	}
	// _end

	// _start 操作数运算函数 加 减 乘 除 取余
	/*
	 * 加法运算
	 */
	public Operand OperatorAdd(Operand od) throws Exception {
		if (!(DataType.INT == content[0] || DataType.FLOAT == content[0] || DataType.STRING == content[0]))
			throw new Exception("操作数无法做加法运算");
		if (!(DataType.INT == od.content[0] || DataType.FLOAT == od.content[0] || DataType.STRING == od.content[0]))
			throw new Exception("操作数无法做加法运算");
		Operand rod=new Operand();
		// 整数+整数 结果为整数
		if (DataType.INT == this.content[0] && DataType.INT == od.content[0]) {
			rod.SetINT(this.GetIntValue() + od.GetIntValue());
		}
		// 整数+浮点数 结果为浮点数
		else if (DataType.INT == this.content[0] && DataType.FLOAT == od.content[0]) {
			rod.SetFLOAT(this.GetIntValue() + od.GetFloatValue());
		}
		// 浮点数+整数 结果为浮点数
		else if (DataType.FLOAT == this.content[0] && DataType.INT == od.content[0]) {
			rod.SetFLOAT(this.GetFloatValue() + od.GetIntValue());
		}
		// 浮点数+浮点数 结果为浮点数
		else if (DataType.FLOAT == this.content[0] && DataType.FLOAT == od.content[0]) {
			rod.SetFLOAT(this.GetFloatValue() + od.GetFloatValue());
		} else if (DataType.STRING == this.content[0] && DataType.STRING == od.content[0]) { //字符串相加
			rod.SetSTRING(this.getString() + od.getString());
		} else
			throw new Exception("操作数无法做加法运算");
		return rod;
	}

	/*
	 * 减法运算
	 */
	public Operand OperatorSub(Operand od) throws Exception {
		if (!(DataType.INT == this.content[0] || DataType.FLOAT == this.content[0]))
			throw new Exception("操作数无法做减法运算");
		if (!(DataType.INT == od.content[0] || DataType.FLOAT == od.content[0]))
			throw new Exception("操作数无法做减法运算");
		Operand rod=new Operand();
		// 整数-整数 结果为整数
		if (DataType.INT == this.content[0] && DataType.INT == od.content[0]) {
			rod.SetINT(this.GetIntValue() - od.GetIntValue());
		}
		// 整数-浮点数 结果为浮点数
		else if (DataType.INT == this.content[0] && DataType.FLOAT == od.content[0]) {
			rod.SetFLOAT(this.GetIntValue() - od.GetFloatValue());
		}
		// 浮点数-整数 结果为浮点数
		else if (DataType.FLOAT == this.content[0] && DataType.INT == od.content[0]) {
			rod.SetFLOAT(this.GetFloatValue() - od.GetIntValue());
		}
		// 浮点数-浮点数 结果为浮点数
		else if (DataType.FLOAT == this.content[0] && DataType.FLOAT == od.content[0]) {
			rod.SetFLOAT(this.GetFloatValue() - od.GetFloatValue());
		}
		return rod;
	}

	/*
	 * 乘法运算
	 */
	public Operand OperatorMul(Operand od) throws Exception {
		if (!(DataType.INT == this.content[0] || DataType.FLOAT == this.content[0]))
			throw new Exception("操作数无法做乘法运算");
		if (!(DataType.INT == od.content[0] || DataType.FLOAT == od.content[0]))
			throw new Exception("操作数无法做乘法运算");
		Operand rod=new Operand();
		// 整数*整数 结果为整数
		if (DataType.INT == this.content[0] && DataType.INT == od.content[0]) {
			rod.SetINT(this.GetIntValue() * od.GetIntValue());
		}
		// 整数*浮点数 结果为浮点数
		else if (DataType.INT == this.content[0] && DataType.FLOAT == od.content[0]) {
			rod.SetFLOAT(this.GetIntValue() * od.GetFloatValue());
		}
		// 浮点数*整数 结果为浮点数
		else if (DataType.FLOAT == this.content[0] && DataType.INT == od.content[0]) {
			rod.SetFLOAT(this.GetFloatValue() * od.GetIntValue());
		}
		// 浮点数*浮点数 结果为浮点数
		else if (DataType.FLOAT == this.content[0] && DataType.FLOAT == od.content[0]) {
			rod.SetFLOAT(this.GetFloatValue() * od.GetFloatValue());
		}
		return rod;
	}

	/*
	 * 除法运算(非整除)
	 */
	public Operand OperatorFDiv(Operand od) throws Exception {
		if (!(DataType.INT == this.content[0] || DataType.FLOAT == this.content[0]))
			throw new Exception("操作数无法做除法运算");
		if (!(DataType.INT == od.content[0] || DataType.FLOAT == od.content[0]))
			throw new Exception("操作数无法做除法运算");
		float f1, f2;
		Operand rod=new Operand();
		// 整数/整数 结果为浮点数
		if (DataType.INT == this.content[0] && DataType.INT == od.content[0]) {
			f1 = (float) this.GetIntValue();
			f2 = (float) od.GetIntValue();
			rod.SetFLOAT(f1 / f2);
		}
		// 整数/浮点数 结果为浮点数
		else if (DataType.INT == this.content[0] && DataType.FLOAT == od.content[0]) {
			f1 = (float) this.GetIntValue();
			rod.SetFLOAT(f1 / od.GetFloatValue());
		}
		// 浮点数/整数 结果为浮点数
		else if (DataType.FLOAT == this.content[0] && DataType.INT == od.content[0]) {
			f2 = (float) od.GetIntValue();
			rod.SetFLOAT(this.GetFloatValue() / f2);
		}
		// 浮点数/浮点数 结果为浮点数
		else if (DataType.FLOAT == this.content[0] && DataType.FLOAT == od.content[0]) {
			rod.SetFLOAT(this.GetFloatValue() / od.GetFloatValue());
		}
		return rod;
	}

	/*
	 * 取余运算，必须是两个整数才能取余
	 */
	public Operand OperatorMod(Operand od) throws Exception {
		if (!(DataType.INT == this.content[0]))
			throw new Exception("操作数无法做取余运算");
		if (!(DataType.INT == od.content[0]))
			throw new Exception("操作数无法做取余运算");
		Operand rod=new Operand();
		// 整数/整数 结果为整数
		rod.SetINT(this.GetIntValue() % od.GetIntValue());
		return rod;
	}

	/*
	 * 大于运算
	 */
	public Operand OperatorGreat(Operand od) throws Exception {
		if (!(DataType.INT == this.content[0] || DataType.FLOAT == this.content[0]))
			throw new Exception("操作数无法做比较运算");
		if (!(DataType.INT == od.content[0] || DataType.FLOAT == od.content[0]))
			throw new Exception("操作数无法做比较运算");
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
	 * 小于运算
	 */
	public Operand OperatorLess(Operand od) throws Exception {
		if (!(DataType.INT == this.content[0] || DataType.FLOAT == this.content[0]))
			throw new Exception("操作数无法做比较运算");
		if (!(DataType.INT == od.content[0] || DataType.FLOAT == od.content[0]))
			throw new Exception("操作数无法做比较运算");
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
	 * 等于运算
	 */
	public Operand OperatorEqual(Operand od) throws Exception {
		if (!(DataType.INT == this.content[0] || DataType.FLOAT == this.content[0]))
			throw new Exception("操作数无法做比较运算");
		if (!(DataType.INT == od.content[0] || DataType.FLOAT == od.content[0]))
			throw new Exception("操作数无法做比较运算");
		Operand rod=new Operand();
		if (this.content[0] == od.content[0]) {
			rod.SetBOOL((content[1] == od.content[1]) && (content[2] == od.content[2]) && (content[3] == od.content[3])
					&& (content[4] == od.content[4])); //将数组中存储的每一个字节的数进行比较
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
	 * 布尔值的与运算
	 */
	public Operand OperatorAnd(Operand od) throws Exception {
		if (!(DataType.BOOL == this.content[0] && DataType.BOOL == od.content[0]))
			throw new Exception("操作数无法做与运算");
		Operand rod=new Operand();
		rod.SetBOOL(this.GetBoolValue() && od.GetBoolValue());
		return rod;
	}

	/*
	 * 布尔值的或运算
	 */
	public Operand OperatorOr(Operand od) throws Exception {
		if (!(DataType.BOOL == this.content[0] && DataType.BOOL == od.content[0]))
			throw new Exception("操作数无法做或运算");
		Operand rod=new Operand();
		rod.SetBOOL(this.GetBoolValue() || od.GetBoolValue());
		return rod;
	}
	
	/*
	 * 布尔值的非运算
	 */
	public Operand OperatorNot(Operand od) throws Exception {
		if (!( DataType.BOOL == od.content[0]))
			throw new Exception("操作数无法做非运算");
		Operand rod=new Operand();
		rod.SetBOOL(!od.GetBoolValue());
		return rod;
	}
	// _end

	public static void main(String args[]) {
		System.out.println("-----操作数类测试----");
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
