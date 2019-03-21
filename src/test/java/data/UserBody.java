package data;

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

	public UserBody withAddItemMoreExpanded(String addItemMoreExpanded) {
		this.put("AddItemMoreExpanded", addItemMoreExpanded);
		return this;
	}

	public String getAddItemMoreExpanded() {
		return get("AddItemMoreExpanded");
	}

	public UserBody withDefaultProjectId(String defaultProjectId) {
		this.put("DefaultProjectId", defaultProjectId);
		return this;
	}

	public String getDefaultProjectId() {
		return get("DefaultProjectId");
	}

	public String getEmail() {
		return get("Email");
	}

	public String getFullName() {
		return get("FullName");
	}

	public String getPassword() {
		return get("Password");
	}

}
