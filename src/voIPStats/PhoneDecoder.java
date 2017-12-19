package voIPStats;

import java.io.BufferedReader;
import java.io.FileReader;

public class PhoneDecoder {
	static CustomSkipList<String, String> DialCodes=readDialCodeList("International_dial_codes.csv");
	private static CustomSkipList<String, String> readDialCodeList(String filePath){
		CustomSkipList<String, String> ret=new CustomSkipList<>(new StringComparer(7));try {
		BufferedReader r = new BufferedReader(new FileReader(filePath));
			String l;
			while ((l = r.readLine()) != null) {
				String[] no =l.split(";");
				ret.put(no[0],no[1]);
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	public static String GetRegion(String No) {
		No=No.replaceAll("[+ ]","");
		return DialCodes.get(No);
	}
}
