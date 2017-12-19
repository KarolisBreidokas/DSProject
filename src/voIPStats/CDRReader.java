package voIPStats;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.*;

import voIPStats.CDRLog.LogComparator;
import voIPStats.CDRLog.LogKey;

/**
 * class for reading cdr log files
 * 
 * @author karbre
 *
 */
public class CDRReader {
	/**
	 * patern of cdr Log using regex
	 */
	static String PatternStr = "<NS>;<ND>;<TB>;<TE>;<CS>";
	static Pattern Pttrn;
	static int paramCount = 0;
	static parameterType[] params;

	static void CreateRegexPattern() {
		Pattern tmp = Pattern.compile("<(.*?)>");
		Matcher t = tmp.matcher(PatternStr);
		params = new parameterType[PatternStr.length() / 4];
		StringBuilder ptrn = new StringBuilder();
		int prevIndex = 0;
		while (t.find()) {
			String param = t.group(1);
			params[paramCount] = getParamType(param);
			ptrn.append(PatternStr.substring(prevIndex, t.start()) + "(.*?)");
			prevIndex = t.end();
			paramCount++;
		}
		ptrn.append(PatternStr.substring(prevIndex) + "$");
		Pttrn = Pattern.compile(ptrn.toString());
		// TODO add validation

	}

	static parameterType getParamType(String param) {
		switch (param) {
		case "NS":
			return parameterType.SourceNumber;
		case "ND":
			return parameterType.DestinationNumber;
		case "TB":
			return parameterType.CallStart;
		case "TE":
			return parameterType.CallEnd;
		case "TD":
			return parameterType.CallDuration;
		case "CS":
			return parameterType.CallSuccess;
		case "CF":
			return parameterType.CallFaultCode;
		default:
			return null;
		}
	}

	static CDRLog generateLog(Matcher match) {

		phoneNo calleeId = null;
		phoneNo callerId = null;
		LocalDateTime callStart = null;
		LocalDateTime callEnd = null;
		Duration callDuration = null;
		boolean callSucess = false;

		for (int a = 1; a < paramCount + 1; a++) {
			switch (params[a - 1]) {
			case SourceNumber:
				calleeId = new phoneNo(match.group(a), PhoneDecoder.GetRegion(match.group(a))) ;
				break;
			case DestinationNumber:
				callerId = new phoneNo(match.group(a), PhoneDecoder.GetRegion(match.group(a))) ;
				break;
			case CallStart:
				callStart = LocalDateTime.parse(match.group(a));
				break;
			case CallEnd:
				callEnd = LocalDateTime.parse(match.group(a));
				break;
			case CallDuration:
				callDuration = Duration.parse(match.group(a));
				break;
			case CallSuccess:
				callSucess = Boolean.parseBoolean(match.group(a));

				break;
			default:
				break;
			}
		}
		if (callDuration == null) {
			callDuration = Duration.between(callStart, callEnd);
		}
		return new CDRLog(calleeId, callerId, callStart, callEnd, callDuration, callSucess);
	}

	static CDRLog readLog(String log) {
		Matcher match = Pttrn.matcher(log);
		if (match.find()) {
			return generateLog(match);
		}
		return null;
		// throw new Exception("Pattern does not match log");
	}

	static CustomSkipList<LogKey, CDRLog> getList(String filePath) {
		if (Pttrn == null) {
			CreateRegexPattern();
		}
		CustomSkipList<LogKey, CDRLog> ans = new CustomSkipList<LogKey, CDRLog>(new CDRLog.LogComparator());
		try {
			BufferedReader r = new BufferedReader(new FileReader(filePath));
			String l;
			while ((l = r.readLine()) != null) {
				CDRLog log = readLog(l);
				ans.put(log.generateKey(), log);
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ans;
	}

	public static void main(String[] args) throws Exception {
		CustomSkipList<LogKey, CDRLog> list = getList("log.txt");
		System.out.println(list);
		Client[] clients = { new Client("John", new phoneNo("+37055550101", null)),
				new Client("Smith", new phoneNo("+37055550100", null)) };
		for (Client client : clients) {
			CustomSkipList<LogKey, CDRLog> l1 = list.SelectBySpecificComparator(0,
					new LogKey(null, client.Number, null), new LogComparator() {
						{
							t = new CDRLog.KeyComparare[] { (x, y) -> x.getCalleeId().compareTo(y.getCalleeId()),
									(x, y) -> x.getCallStart().compareTo(y.getCallStart()) };
						}
					});
			System.out.println(client.name);
			System.out.println(l1);
		}

	}

	enum parameterType {
		SourceNumber, DestinationNumber, CallStart, CallEnd, CallDuration, CallSuccess, CallFaultCode
	}
}
