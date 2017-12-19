package voIPStats;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;

public class CDRLog {

	@Override
	public String toString() {
		return "CDRLog [calleeId=" + calleeId + ", callerId=" + callerId + ", callStart=" + callStart + ", callEnd="
				+ callEnd + ", callDuration=" + callDuration + ", callSucess=" + callSucess + "]";
	}

	private phoneNo calleeId = null;
	private phoneNo callerId = null;
	private LocalDateTime callStart = null;
	private LocalDateTime callEnd = null;
	private Duration callDuration = null;
	private boolean callSucess = false;

	public phoneNo getCalleeId() {
		return calleeId;
	}

	public phoneNo getCallerId() {
		return callerId;
	}

	public LocalDateTime getCallStart() {
		return callStart;
	}

	public LocalDateTime getCallEnd() {
		return callEnd;
	}

	public Duration getCallDuration() {
		return callDuration;
	}

	public boolean isCallSucess() {
		return callSucess;
	}

	public CDRLog() {

	}

	public CDRLog(phoneNo calleeId, phoneNo callerId, LocalDateTime callStart, LocalDateTime callEnd,
			Duration callDuration, boolean callSucess) {
		this.calleeId = calleeId;
		this.callerId = callerId;
		this.callStart = callStart;
		this.callEnd = callEnd;
		this.callDuration = callDuration;
		this.callSucess = callSucess;
	}

	public LogKey generateKey() {
		return new LogKey(calleeId, callerId, callStart);
	}

	public static class LogKey {
		@Override
		public String toString() {
			return "LogKey [calleeId=" + calleeId + ", callerId=" + callerId + ", callStart=" + callStart + "]";
		}

		public phoneNo getCalleeId() {
			return calleeId;
		}

		public phoneNo getCallerId() {
			return callerId;
		}

		public LocalDateTime getCallStart() {
			return callStart;
		}

		private phoneNo calleeId = null;
		private phoneNo callerId = null;
		private LocalDateTime callStart = null;

		public LogKey(phoneNo calleeId, phoneNo callerId, LocalDateTime callStart) {
			this.calleeId = calleeId;
			this.callerId = callerId;
			this.callStart = callStart;
		}
	}

	public static class LogComparator extends customComparer {
		public LogComparator() {
			t = new KeyComparare[] { (x, y) -> x.callerId.compareTo(y.callerId),
					(x, y) -> x.calleeId.compareTo(y.calleeId), (x, y) -> x.callStart.compareTo(y.callStart) };
		}
	}

	abstract static class customComparer implements MultiLevelComparator<LogKey> {
		public KeyComparare[] t;

		@Override
		public int compareBykey(int index, LogKey obj1, LogKey obj2) {
			if (index >= t.length) {
				throw new IndexOutOfBoundsException();
			}
			return t[index].compare(obj1, obj2);
		}

		@Override
		public int comparasonCount() {
			return t.length;
		}
	}

	public interface KeyComparare extends Comparator<LogKey> {
	}
}
