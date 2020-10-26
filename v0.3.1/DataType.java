package staticContent;

public class DataType {
	static final public int INT=1;		//整数
	static final public int FLOAT=2;	//小数
	static final public int STRING=3;	//字符串
	static final public int BOOL=4;	//布尔值
//	static final int DATE=5;	//日期
	static final public int SET=6;		//集合
	static final public int BLOB=7;	//二进制
	static final public int NULL=8;	//空值
	
	public static String getDescribe(int datatype)
	{
		switch(datatype)
		{
			case DataType.INT:
				return "整数";
			case DataType.FLOAT:
				return "小数";
			case DataType.STRING:
				return "字符串型";
			case DataType.BOOL:
				return "布尔型";
			case DataType.SET:
				return "集合型";
			case DataType.BLOB:
				return "二进制值型";
			case DataType.NULL:
				return "空值型";
		}
		return "不支持的数据类型";
	}
	
}
