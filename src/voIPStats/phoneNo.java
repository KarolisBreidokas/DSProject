package voIPStats;

public class phoneNo {
	public String PhoneNo;
	public String Region;
	
	public phoneNo(String phoneNo,String region) {
		PhoneNo=phoneNo;
		Region=region;
	}
	@Override
	public String toString() {
		return PhoneNo;
	}
}
