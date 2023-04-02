package narasimhaa.com.mitraservice.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import narasimhaa.com.mitraservice.Utility.MyUtilities;

/**
 * 
 * @author srinivas
 * 
 */
public class DBUtil {

	private static DBUtil instance = null;
	private static final String DATABASE_NAME = "mrmason.db";
	private static final int DATABASE_VERSION = 1;

	private SQLiteDatabase db;

	public static DBUtil getDBUtil(Context context) {
		if (instance == null)
			instance = new DBUtil(context);

		return instance;
	}

	private DBUtil(Context context) {
		try {
			OpenHelper openHelper = new OpenHelper(context);
			this.db = openHelper.getWritableDatabase();
			//openHelper.onCreate(db);
		} catch (SQLiteException e) {
			Log.e("Exception", e.getMessage());
		}
	}


	private static class OpenHelper extends SQLiteOpenHelper {

		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			setTableData(db);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			onDatabaseUpgrade(db);
		}
	}

	public static void setTableData(SQLiteDatabase db) {
		ArrayList<TableDataCore> TableDetailslist = new ArrayList<TableDataCore>();

		// Users
		TableDataCore user = new TableDataCore();
		user.setTableName(DBSupport.TABLE.T_USER);
		user.setTableFields(DBSupport.getTableFields(new UserT()));
		TableDetailslist.add(user);


		// Users



		//TableDataCore updateamount = new TableDataCore();
		//updateamount.setTableName(DBSupport.TABLE.D_updateexpetedAmount);
		//updateamount.setTableFields(DBSupport.getTableFields(new ExptedAmountD()));
		//TableDetailslist.add(updateamount);



		readingTableData(db, TableDetailslist);
	}

	private static void readingTableData(SQLiteDatabase db,
										 List<TableDataCore> list) {
		// TODO Auto-generated method stub
		if (list != null) {
			Iterator<TableDataCore> iterator = list.iterator();
			DBUpdateCore tableData = null;
			while (iterator.hasNext()) {
				tableData = new DBUpdateCore();
				TableDataCore table = (TableDataCore) iterator.next();
				tableData.setTableName(table.getTableName());
				String[][] data = table.getTableFields();
				String fields[] = new String[data[0].length];
				String dataTypes[] = new String[data[1].length];
				for (int i = 0; i < data.length; i++) {
					for (int j = 0; j < data[i].length; j++) {
						if (i == 0)
							fields[j] = data[0][j];
						if (i == 1)
							dataTypes[j] = data[1][j];
					}
				}
				tableData.setFields(fields);
				tableData.setDataTypes(dataTypes);
				callAlterForTable(db, tableData);
			}

		} else {
			Log.e("Reading Table Data", "Error No Data");
		}
	}

	private static void callAlterForTable(SQLiteDatabase db,
										  DBUpdateCore tableData) {
		// If Table exists
		if (isTableExists(db, tableData.getTableName())) {
			String[] fields = tableData.getFields();
			String[] fieldTypes = tableData.getDataTypes();
			// Fie
			if (fields.length == fieldTypes.length) {
				for (int i = 0; i < fields.length; i++) {
					try {
						db.execSQL("ALTER TABLE " + tableData.getTableName()
								+ " ADD COLUMN " + fields[i] + " "
								+ fieldTypes[i] + ";");
					} catch (Exception e) {

					}
				}
			}
		} else {
			// If Table Not exists
			StringBuilder createtable = new StringBuilder();
			createtable.append("CREATE TABLE IF NOT EXISTS "
					+ tableData.getTableName()
					+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT");
			String[] fields = tableData.getFields();
			String[] fieldTypes = tableData.getDataTypes();

			if (fields.length == fieldTypes.length) {
				for (int i = 0; i < fields.length; i++) {
					createtable.append("," + fields[i] + " " + fieldTypes[i]);
				}
				createtable.append(")");
			}
			try {
				db.execSQL(createtable.toString());
			} catch (Exception e) {
				Log.w("Exception", e.getMessage());
			}

		}
	}


	//BY Subhani

	public ArrayList<Object> getAllValuesDistinctByOrderFromTable(
            String TableName, String where, Object object, String order,
            String distictColum) {

		ArrayList<Object> allValues = new ArrayList<Object>();

		Cursor cursor = this.db.query(true, TableName, null, where, null,
				distictColum, null, order, null);

		if (cursor.moveToFirst()) {
			do {

				Object obj = new Object();

				try {
					obj = ObjectUtilities.clone(object);
				} catch (CloneNotSupportedException e1) {

					e1.printStackTrace();
				}

				Class<?> clazz = obj.getClass();

				for (Field field : clazz.getDeclaredFields()) {

					try {
						field.setAccessible(true);

						if (field.getType().isPrimitive()) {
							if (field.getType().getName().equals("long")) {

								field.set(obj, cursor.getLong((cursor
										.getColumnIndex(field.getName()))));

							} else if (field.getType().getName().equals("int")) {
								field.set(obj, cursor.getInt(cursor
										.getColumnIndex(field.getName())));
							} else if (field.getType().getName()
									.equals("boolean")) {
								if (cursor.getInt(cursor.getColumnIndex(field
										.getName())) == 1) {
									field.set(obj, true);
								} else {
									field.set(obj, false);
								}
							} else if (field.getType().getName()
									.equals("double")) {
								field.set(obj, Double.parseDouble(cursor
										.getString(cursor.getColumnIndex(field
												.getName()))));
							} else if (field.getType().getName()
									.equals("short")) {
								field.set(obj, cursor.getInt(cursor
										.getColumnIndex(field.getName())));
							} else if (field.getType().getName().equals("byte")) {
								field.set(obj, cursor.getInt(cursor
										.getColumnIndex(field.getName())));
							}
						}
						if (field.getType() == Long.class) {

							field.set(obj, cursor.getLong(cursor
									.getColumnIndex(field.getName())));

						} else if (field.getType() == Integer.class) {
							field.set(obj, cursor.getInt(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Boolean.class) {
							if (cursor.getInt(cursor.getColumnIndex(field
									.getName())) == 1) {
								field.set(obj, true);
							} else {
								field.set(obj, false);
							}
						} else if (field.getType() == String.class) {
							// String str = cursor.getString(cursor
							// .getColumnIndex(field.getName()));
							// str = str.replaceAll("$", "'");
							field.set(obj, cursor.getString(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Double.class) {
							field.set(obj, Double.parseDouble(cursor
									.getString(cursor.getColumnIndex(field
											.getName()))));
						} else if (field.getType() == Short.class) {
							field.set(obj, cursor.getInt(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Byte.class) {
							field.set(obj, cursor.getInt(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Byte[].class) {
							field.set(obj, cursor.getBlob(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType().getName().equals("[B")) {
							field.set(obj, cursor.getBlob(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Float.class) {
							field.set(obj, Float.parseFloat(cursor
									.getString(cursor.getColumnIndex(field
											.getName()))));
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				allValues.add(obj);

			} while (cursor.moveToNext());

		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return allValues;
	}


	/**
	 * @param TableName
	 * @param where
	 * @param object
	 * @param columnName
	 * @param limitValue
	 * @return
	 */

	public ArrayList<Object> getAllValuesFromTableOrderByColumn(String TableName, String where, Object object, String columnName, String limitValue) {

		ArrayList<Object> allValues = new ArrayList<Object>();

		Cursor cursor = this.db.query(TableName, null, where, null, null, null, columnName, limitValue);

		if (cursor.moveToFirst()) {
			do {

				Object obj = new Object();

				try {
					obj = ObjectUtilities.clone(object);
				} catch (CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				Class<?> clazz = obj.getClass();

				for (Field field : clazz.getDeclaredFields()) {

					try {
						field.setAccessible(true);

						if (field.getType().isPrimitive()) {
							if (field.getType().getName().equals("long")) {

								field.set(obj, cursor.getLong((cursor
										.getColumnIndex(field.getName()))));

							} else if (field.getType().getName().equals("int")) {
								field.set(obj, cursor.getInt(cursor
										.getColumnIndex(field.getName())));
							} else if (field.getType().getName()
									.equals("boolean")) {
								if (cursor.getInt(cursor.getColumnIndex(field
										.getName())) == 1) {
									field.set(obj, true);
								} else {
									field.set(obj, false);
								}
							} else if (field.getType().getName()
									.equals("double")) {
								field.set(obj, Double.parseDouble(cursor
										.getString(cursor.getColumnIndex(field
												.getName()))));
							} else if (field.getType().getName()
									.equals("short")) {
								field.set(obj, cursor.getInt(cursor
										.getColumnIndex(field.getName())));
							} else if (field.getType().getName().equals("byte")) {
								field.set(obj, cursor.getInt(cursor
										.getColumnIndex(field.getName())));
							}
						}
						if (field.getType() == Long.class) {

							field.set(obj, cursor.getLong(cursor
									.getColumnIndex(field.getName())));

						} else if (field.getType() == Integer.class) {
							field.set(obj, cursor.getInt(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Boolean.class) {
							if (cursor.getInt(cursor.getColumnIndex(field
									.getName())) == 1) {
								field.set(obj, true);
							} else {
								field.set(obj, false);
							}
						} else if (field.getType() == String.class) {
							// String str = cursor.getString(cursor
							// .getColumnIndex(field.getName()));
							// str = str.replaceAll("$", "'");
							field.set(obj, cursor.getString(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Double.class) {
							field.set(obj, Double.parseDouble(cursor
									.getString(cursor.getColumnIndex(field
											.getName()))));
						} else if (field.getType() == Short.class) {
							field.set(obj, cursor.getInt(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Byte.class) {
							field.set(obj, cursor.getInt(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Byte[].class) {
							field.set(obj, cursor.getBlob(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType().getName().equals("[B")) {
							field.set(obj, cursor.getBlob(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Float.class) {
							field.set(obj, Float.parseFloat(cursor
									.getString(cursor.getColumnIndex(field
											.getName()))));
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				allValues.add(obj);

			} while (cursor.moveToNext());

		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return allValues;
	}

	/**
	 * @param tableName
	 * @param list      list of keys
	 * @param where     null to retrive all
	 * @param orderby   null or column name
	 * @return
	 */

	public ArrayList<Object> readReportValuesFromTableView(String tableName, List<Object> list, String where, String orderby) {


		Cursor cursor = this.db.query(tableName, null, where, null, null, null, orderby);

		ArrayList<Object> row = new ArrayList<Object>();
		int listSize = list.size();
		if (cursor.moveToFirst()) {


			do {

				ArrayList<String> listz = new ArrayList<String>();

				for (int i = 0; i < listSize; i++) {
					String key = ((String) list.get(i)).replace(" ", "");
					listz.add(cursor.getString(cursor.getColumnIndex(key)));

				}

				row.add(listz);

				listz = null;
			} while (cursor.moveToNext());

		}
		return row;

	}


	/**
	 * @param tableName
	 * @param hack
	 * @param values
	 * @return -1 if not inserted else rowId
	 */

	public long insertTable(String tableName, String hack, ContentValues values) {

		return this.db.insert(tableName, null, values);
	}


	/**
	 * @param tableName
	 * @param colName   -delete duplicates based on col
	 */
	public void deleteDuplicates(String tableName, String colName) {


		try {
			if (isTableExists(db, tableName))
				this.db.execSQL("delete from '" + tableName + "' where _id not in (SELECT MIN(_id) FROM '" + tableName + "' GROUP BY " + colName + " having count(_id)>1)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	/**
	 * @param tableName
	 */

	public void createTable(String tableName) {

		this.db.execSQL(tableName);
	}


	/**
	 * @param tableName
	 * @param hack
	 * @param values
	 * @return number of rows affected
	 */

	public int updateTable(String tableName, String where, ContentValues values) {

		return this.db.update(tableName, values, where, null);
	}


	/**
	 * @param tableName
	 * @param list      list of keys
	 * @param where     null to retrive all
	 * @param orderby   null or column name
	 * @return
	 */

	public ArrayList<Object> readReportValuesFromTable(String tableName, List<Object> list, String where, String orderby) {


		Cursor cursor = this.db.query(tableName, null, where, null, null, null, orderby);

		ArrayList<Object> row = new ArrayList<Object>();
		int listSize = list.size();
		if (cursor.moveToFirst()) {


			do {

				LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

				for (int i = 0; i < listSize; i++) {
					String key = ((String) list.get(i)).replace(" ", "");
					map.put(key, cursor.getString(cursor.getColumnIndex(key)));

				}

				row.add(map);

				map = null;
			} while (cursor.moveToNext());

		}
		return row;

	}


	/**
	 * @param tableName
	 * @param list      list of keys
	 * @param where     null to retrive all
	 * @param orderby   null or column name
	 * @return
	 */

	public ArrayList<Object> readRawReportValuesFromTable(String tableName, List<Object> list, String where, String orderby) {
		Cursor cursor = this.db.rawQuery("SELECT * FROM '" + tableName + "' where " + where + " ORDER BY " + orderby + "", null);

		//	Cursor	cursor = this.db.query(tableName,null, where, null, null, null, orderby);

		ArrayList<Object> row = new ArrayList<Object>();
		int listSize = list.size();
		if (cursor.moveToFirst()) {


			do {

				LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

				for (int i = 0; i < listSize; i++) {
					String key = ((String) list.get(i)).replace(" ", "");
					map.put(key, cursor.getString(cursor.getColumnIndex(key)));

				}

				row.add(map);

				map = null;
			} while (cursor.moveToNext());

		}
		return row;

	}

	private static boolean isTableExists(SQLiteDatabase db, String tableName) {
		Cursor cursor = db.rawQuery(
				"select DISTINCT tbl_name from sqlite_master where tbl_name = '"
						+ tableName + "'", null);
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		}
		return false;
	}

	public static void onDatabaseUpgrade(SQLiteDatabase db) {
		setTableData(db);
	}

	/**
	 * ]
	 *
	 * @param TableName
	 * @param object
	 * @param insertflag
	 * @param where
	 * @return
	 */
	public int insertOrUpdateTable(String TableName, Object object,
                                   boolean insertflag, String where) {

		Class<?> clazz = object.getClass();

		ContentValues cv = new ContentValues();
		for (Field field : clazz.getDeclaredFields()) {

			try {
				field.setAccessible(true);

				if (field.getName().equals("_id"))
					continue;

				if (field.getType().isPrimitive()) {

					if (field.getType().getName().equals("long")) {
						cv.put(field.getName(), (Long) field.get(object));
					} else if (field.getType().getName().equals("int")) {
						cv.put(field.getName(), (Integer) field.get(object));
					} else if (field.getType().getName().equals("boolean")) {
						cv.put(field.getName(), (Boolean) field.get(object));
					} else if (field.getType().getName().equals("double")) {
						cv.put(field.getName(), (Double) field.get(object));
					} else if (field.getType().getName().equals("short")) {
						cv.put(field.getName(), (Short) field.get(object));
					} else if (field.getType().getName().equals("byte")) {
						cv.put(field.getName(), (Byte) field.get(object));
					}
				}

				if (field.get(object) == null)
					continue;

				if (field.getType() == Long.class) {
					cv.put(field.getName(), (Long) field.get(object));
				} else if (field.getType() == Integer.class) {

					cv.put(field.getName(), (Integer) field.get(object));

				} else if (field.getType() == Boolean.class) {
					cv.put(field.getName(), (Boolean) field.get(object));
				} else if (field.getType() == String.class) {
					if (MyUtilities.isNull((String) field.get(object))) {
						String str = (String) field.get(object);
						str = str.replaceAll("'", "");
						cv.put(field.getName(), str);
					}
				} else if (field.getType() == Double.class) {
					cv.put(field.getName(), (Double) field.get(object));
				} else if (field.getType() == Short.class) {
					cv.put(field.getName(), (Short) field.get(object));
				} else if (field.getType() == Byte.class) {
					cv.put(field.getName(), (Byte) field.get(object));
				} else if (field.getType().getName().equals("[B")) {
					cv.put(field.getName(), (byte[]) field.get(object));
				} else if (field.getType() == Float.class) {
					cv.put(field.getName(), (Float) field.get(object));
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (insertflag)
			return (int) db.insert(TableName, null, cv);
		else
			return db.update(TableName, cv, where, null);

	}

	/**
	 * @param table
	 * @param cv
	 * @param where
	 * @return
	 */


	public int updateAllForms(String table, ContentValues cv, String where) {
		return db.update(table, cv, where, null);
	}


	/**
	 * @param TableName
	 * @param where
	 * @param object
	 * @return
	 */

	public ArrayList<Object> getAllValuesFromTableLimit(String TableName, String where, Object object, String size) {

		ArrayList<Object> allValues = new ArrayList<Object>();


		Cursor cursor = null;
		if (!MyUtilities.isNull(where)) {
			cursor = this.db.rawQuery("SELECT * FROM '" + TableName + "' ORDER BY _id ASC LIMIT '" + size + "'", null);//OFFSET (SELECT COUNT(*) FROM '" + TableName + "')-10

		} else {
			cursor = this.db.rawQuery("SELECT * FROM '" + TableName + "' where " + where + " ORDER BY _id ASC LIMIT '" + size + "'", null);//OFFSET (SELECT COUNT(*) FROM '" + TableName + "')-10

		}

		if (cursor.moveToFirst()) {
			do {

				Object obj = new Object();

				try {
					obj = ObjectUtilities.clone(object);
				} catch (CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				Class<?> clazz = obj.getClass();

				for (Field field : clazz.getDeclaredFields()) {

					try {
						field.setAccessible(true);

						if (field.getType().isPrimitive()) {
							if (field.getType().getName().equals("long")) {

								field.set(obj, cursor.getLong((cursor
										.getColumnIndex(field.getName()))));

							} else if (field.getType().getName().equals("int")) {
								field.set(obj, cursor.getInt(cursor
										.getColumnIndex(field.getName())));
							} else if (field.getType().getName()
									.equals("boolean")) {
								if (cursor.getInt(cursor.getColumnIndex(field
										.getName())) == 1) {
									field.set(obj, true);
								} else {
									field.set(obj, false);
								}
							} else if (field.getType().getName()
									.equals("double")) {
								field.set(obj, Double.parseDouble(cursor
										.getString(cursor.getColumnIndex(field
												.getName()))));
							} else if (field.getType().getName()
									.equals("short")) {
								field.set(obj, cursor.getInt(cursor
										.getColumnIndex(field.getName())));
							} else if (field.getType().getName().equals("byte")) {
								field.set(obj, cursor.getInt(cursor
										.getColumnIndex(field.getName())));
							}
						}
						if (field.getType() == Long.class) {

							field.set(obj, cursor.getLong(cursor
									.getColumnIndex(field.getName())));

						} else if (field.getType() == Integer.class) {
							field.set(obj, cursor.getInt(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Boolean.class) {
							if (cursor.getInt(cursor.getColumnIndex(field
									.getName())) == 1) {
								field.set(obj, true);
							} else {
								field.set(obj, false);
							}
						} else if (field.getType() == String.class) {
							// String str = cursor.getString(cursor
							// .getColumnIndex(field.getName()));
							// str = str.replaceAll("$", "'");
							field.set(obj, cursor.getString(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Double.class) {
							field.set(obj, Double.parseDouble(cursor
									.getString(cursor.getColumnIndex(field
											.getName()))));
						} else if (field.getType() == Short.class) {
							field.set(obj, cursor.getInt(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Byte.class) {
							field.set(obj, cursor.getInt(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Byte[].class) {
							field.set(obj, cursor.getBlob(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType().getName().equals("[B")) {
							field.set(obj, cursor.getBlob(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Float.class) {
							field.set(obj, Float.parseFloat(cursor
									.getString(cursor.getColumnIndex(field
											.getName()))));
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				allValues.add(obj);

			} while (cursor.moveToNext());

		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return allValues;
	}


	/**
	 * @param tableName
	 * @return
	 */

	public int getCount(String tableName) {


		try {

			//isTableExists(tableName)
			Cursor mCount = this.db.rawQuery("SELECT COUNT(*) FROM '" + tableName + "'", null);
			mCount.moveToFirst();
			int count = mCount.getInt(0);
			mCount.close();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}


	/**
	 * @param tableName
	 * @param where     string
	 * @return
	 */

	public int getCount(String tableName, String where) {


		try {

			//isTableExists(tableName)
			Cursor mCount = this.db.rawQuery("SELECT COUNT(*) FROM '" + tableName + "' where " + where + "", null);
			mCount.moveToFirst();
			int count = mCount.getInt(0);
			mCount.close();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	/**
	 * @param TableName
	 * @param where
	 * @param object
	 * @return
	 */

	public ArrayList<Object> getAllValuesFromTable(String TableName,
                                                   String where, Object object) {

		ArrayList<Object> allValues = new ArrayList<Object>();

		Cursor cursor = this.db.query(TableName, null, where, null, null, null, "_id");

		if (cursor.moveToFirst()) {
			do {

				Object obj = new Object();

				try {

					obj = ObjectUtilities.clone(object);
				} catch (CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				Class<?> clazz = obj.getClass();

				for (Field field : clazz.getDeclaredFields()) {

					try {
						field.setAccessible(true);

						if (field.getType().isPrimitive()) {
							if (field.getType().getName().equals("long")) {

								field.set(obj, cursor.getLong((cursor
										.getColumnIndex(field.getName()))));

							} else if (field.getType().getName().equals("int")) {
								field.set(obj, cursor.getInt(cursor
										.getColumnIndex(field.getName())));
							} else if (field.getType().getName()
									.equals("boolean")) {
								if (cursor.getInt(cursor.getColumnIndex(field
										.getName())) == 1) {
									field.set(obj, true);
								} else {
									field.set(obj, false);
								}
							} else if (field.getType().getName()
									.equals("double")) {
								field.set(obj, Double.parseDouble(cursor
										.getString(cursor.getColumnIndex(field
												.getName()))));
							} else if (field.getType().getName()
									.equals("short")) {
								field.set(obj, cursor.getInt(cursor
										.getColumnIndex(field.getName())));
							} else if (field.getType().getName().equals("byte")) {
								field.set(obj, cursor.getInt(cursor
										.getColumnIndex(field.getName())));
							}
						}
						if (field.getType() == Long.class) {

							field.set(obj, cursor.getLong(cursor
									.getColumnIndex(field.getName())));

						} else if (field.getType() == Integer.class) {
							field.set(obj, cursor.getInt(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Boolean.class) {
							if (cursor.getInt(cursor.getColumnIndex(field
									.getName())) == 1) {
								field.set(obj, true);
							} else {
								field.set(obj, false);
							}
						} else if (field.getType() == String.class) {
							// String str = cursor.getString(cursor
							// .getColumnIndex(field.getName()));
							// str = str.replaceAll("$", "'");
							field.set(obj, cursor.getString(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Double.class) {
							field.set(obj, Double.parseDouble(cursor
									.getString(cursor.getColumnIndex(field
											.getName()))));
						} else if (field.getType() == Short.class) {
							field.set(obj, cursor.getInt(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Byte.class) {
							field.set(obj, cursor.getInt(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Byte[].class) {
							field.set(obj, cursor.getBlob(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType().getName().equals("[B")) {
							field.set(obj, cursor.getBlob(cursor
									.getColumnIndex(field.getName())));
						} else if (field.getType() == Float.class) {
							field.set(obj, Float.parseFloat(cursor
									.getString(cursor.getColumnIndex(field
											.getName()))));
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				allValues.add(obj);

			} while (cursor.moveToNext());

		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return allValues;
	}


	public boolean isTableExists(String tableName) {
		Cursor cursor = db.rawQuery(
				"select DISTINCT tbl_name from sqlite_master where tbl_name = '"
						+ tableName + "'", null);
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		}
		return false;
	}


	public int deleteRecord(String tableName, String where) {
		return this.db.delete(tableName, where, null);
	}

	public void DropLoginTable(String tableName) {
		db.execSQL("DROP TABLE IF EXISTS " + tableName);
		onDatabaseUpgrade(db);
	}

}