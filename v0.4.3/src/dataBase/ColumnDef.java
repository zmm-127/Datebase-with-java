package dataBase;

import staticContent.DataType;

public class ColumnDef implements java.io.Serializable {
	private String colName;			//列名
	private int colType;			//数据类型
	private boolean isUnique;		//是否唯一
	private boolean isNotNull;		//是否为空
	private boolean isPrimaryKey;	//是否为列级主键
	private Expression defaultExp;	//缺省值
	//构造函数
	public ColumnDef()
	{
		colName="";
		colType=DataType.NULL;
		isUnique=false;
		isNotNull=false;
		isPrimaryKey=false;
		defaultExp=new Expression();
	}
	
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public int getColType() {
		return colType;
	}
	public void setColType(int colType) {
		this.colType = colType;
	}
	public boolean isUnique() {
		return isUnique;
	}
	public void setUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}
	public boolean isNotNull() {
		return isNotNull;
	}
	public void setNotNull(boolean isNotNull) {
		this.isNotNull = isNotNull;
	}
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public Expression getDefaultExp() {
		return defaultExp;
	}
	public void setDefaultExp(Expression defaultExp) {
		this.defaultExp = defaultExp;
	}
	
	public String getDescribe() //对定义的列进行描述
	{
		String result="";
		result+="列名:"+this.getColName()+"\n";
		result+="类型："+DataType.getDescribe(this.getColType())+"\n";
		result+="是否唯一：";
		if(this.isUnique)
			result+="是";
		else
			result+="否";
		result+="\n";
		result+="不能为空：";
		if(this.isNotNull)
			result+="是";
		else
			result+="否";
		result+="\n";
		result+="是否主键：";
		if(this.isPrimaryKey)
			result+="是";
		else
			result+="否";
		result+="\n";
		if(defaultExp.getElements().size()>0)
			result+="缺省值表达式为："+defaultExp.getDescribe();
		else
			result+="无缺省值表达式\n";
		return result;
	}
}
