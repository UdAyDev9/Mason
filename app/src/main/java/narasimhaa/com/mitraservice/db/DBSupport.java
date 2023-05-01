package narasimhaa.com.mitraservice.db;

import java.lang.reflect.Field;

public class DBSupport {
	public final class TABLE {

		public static final String T_USER = "USER";

	}

	public static String[][] getTableFields(Object object) {

		Class<?> clazz = object.getClass();
		boolean _idFlage = false;
		String[][] fields;
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);

			if (field.getName().equals("_id")) {

				_idFlage = true;

			}
		}
		if (_idFlage) {
			fields = new String[2][clazz.getDeclaredFields().length - 1];
		} else {
			fields = new String[2][clazz.getDeclaredFields().length];
		}

		int i = 0;

		for (Field field : clazz.getDeclaredFields()) {

			try {
				field.setAccessible(true);
				// value.getClass().isPrimitive() ||

				if (field.getName().equals("_id"))
					continue;

				fields[0][i] = field.getName();

				if (field.getType().isPrimitive()) {
					if (field.getType().getName().equals("long")) {
						fields[1][i] = "INTEGER";
					} else if (field.getType().getName().equals("int")) {
						fields[1][i] = "INTEGER";
					} else if (field.getType().getName().equals("boolean")) {
						fields[1][i] = "INTEGER";
					} else if (field.getType().getName().equals("double")) {
						fields[1][i] = "TEXT";
					} else if (field.getType().getName().equals("short")) {
						fields[1][i] = "INTEGER";
					} else if (field.getType().getName().equals("byte")) {
						fields[1][i] = "INTEGER";
					}

				} else if (field.getType() == Long.class) {
					fields[1][i] = "INTEGER";
				} else if (field.getType() == Integer.class) {
					fields[1][i] = "INTEGER";
				} else if (field.getType() == Boolean.class) {
					fields[1][i] = "INTEGER";
				} else if (field.getType() == String.class) {
					fields[1][i] = "TEXT";
				} else if (field.getType() == Double.class) {
					fields[1][i] = "TEXT";
				} else if (field.getType() == Short.class) {
					fields[1][i] = "INTEGER";
				} else if (field.getType() == Byte.class) {
					fields[1][i] = "INTEGER";
				} else if (field.getType() == Byte[].class) {
					fields[1][i] = "BLOB";
				} else if (field.getType().getName().equals("[B")) {
					fields[1][i] = "BLOB";
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			i++;
		}

		return fields;
	}
}
