package narasimhaa.com.mitraservice.db;

public class DBUpdateCore {

	private String tableName;
	private String[] fields;
	private String[] dataTypes;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public String[] getDataTypes() {
		return dataTypes;
	}

	public void setDataTypes(String[] dataTypes) {
		this.dataTypes = dataTypes;
	}

}
