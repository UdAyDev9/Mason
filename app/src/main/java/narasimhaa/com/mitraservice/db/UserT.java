package narasimhaa.com.mitraservice.db;

public class UserT implements Cloneable {



	private Integer _id,regType;
	private String uid;
    private String name;
	private String email;
	private String password;
	private String state;
	private String distract;
	private String city;
	private String mobile;
	private String pincode;

	public Integer get_id() {
		return _id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void set_id(Integer _id) {
		this._id = _id;
	}

	public Integer getRegType() {
		return regType;
	}

	public void setRegType(Integer regType) {
		this.regType = regType;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDistract() {
		return distract;
	}

	public void setDistract(String distract) {
		this.distract = distract;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

}
