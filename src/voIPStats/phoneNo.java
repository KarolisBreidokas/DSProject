package voIPStats;

public class phoneNo implements Comparable<phoneNo>{
	public String PhoneNo;
	public String Region;
	
	public phoneNo(String phoneNo,String region) {
		PhoneNo=phoneNo;
		Region=region;
	}
	@Override
	public String toString() {
		return PhoneNo+" ("+Region+")";
	}
	@Override
	public int compareTo(phoneNo o) {
		return PhoneNo.compareTo(o.PhoneNo);
	}
	
}
