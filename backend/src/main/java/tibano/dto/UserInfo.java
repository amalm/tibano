package tibano.dto;

import java.util.List;

public class UserInfo {
	private final String name;
	private final List<String> licensePlates;
	
	public UserInfo(String name, List<String> licensePlates) {
		this.name = name;
		this.licensePlates = licensePlates;
	}

	public String getName() {
		return name;
	}

	public List<String> getLicensePlates() {
		return licensePlates;
	}

}
