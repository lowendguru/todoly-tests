package utils;

import java.util.HashMap;


@SuppressWarnings("serial")
public class UserBody extends HashMap<String, String> {

	public UserBody withEmail(String email) {
		this.put("Email", email);
		return this;
	}

	public UserBody withFullName(String fullName) {
		this.put("FullName", fullName);
		return this;
	}

	public UserBody withPassword(String password) {
		this.put("Password", password);
		return this;
	}

}
