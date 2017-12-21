package voIPStats;

public class Client{
	public String name;
	public phoneNo Number;
	public Client(String name, phoneNo number) {
		this.name = name;
		Number = number;
	}
	@Override
	public String toString() {
		return name+":"+Number.PhoneNo;
	}
}
