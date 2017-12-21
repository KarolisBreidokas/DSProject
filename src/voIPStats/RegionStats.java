package voIPStats;

import java.time.Duration;

public class RegionStats {
	public String region;
	public int callCount;
	public Duration allDuration;
	
	public static String[] TableHeader = { "Regionas", "Skambučių kiekis", "suminis skambučių laikas" };
	public RegionStats(String region, int callCount, Duration allDuration) {
		this.region = region;
		this.callCount = callCount;
		this.allDuration = allDuration;
	}
	public String GetSpecificParam(int index) {
		switch(index) {
		case 0:
			return region;
		case 1:
			return String.valueOf(callCount);
		case 2:
			return allDuration.toString();
		}
		return "";
	}
	
}
