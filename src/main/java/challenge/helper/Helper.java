package challenge.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Helper {

	public static List<Object> iterateListMap(List<Map<String, Object>> listMap) {
		List<Object> list = new ArrayList<>();
		for (Map m : listMap) {
			List<Object> l = new ArrayList<Object>(m.values());
			list.addAll(l);
		}
		return list;
	}

	public static List<Integer> convertToIntegerList(List<Object> strList) {
		List<Integer> listInt = new ArrayList<Integer>();

		for (Object o : strList) {
			try {
				String s = String.valueOf(o);
				listInt.add(Integer.parseInt(s));
			} catch (Exception e) {
				throw new NumberFormatException();
			}
		}
		return listInt;
	}

	public static boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
