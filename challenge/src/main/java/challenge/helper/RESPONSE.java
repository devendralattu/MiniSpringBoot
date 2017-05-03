package challenge.helper;

public enum RESPONSE {
	INVALID("Invalid Parameters. Please pass all the required paramaters."), 
	AUTH_FAIL("Authentication Failed"),
	ROWS_AFFECTED("Rows affected"),
	SUCCESS("Operation Successful"),
	ERROR("Couldn't Perform the following operation"),
	NOCHANGE("No changes were made"),
	PERSON_NOT_EXISTS("No such person to follow");
	public String msg;

	RESPONSE(String msg) {
		this.msg = msg;
	}
}
