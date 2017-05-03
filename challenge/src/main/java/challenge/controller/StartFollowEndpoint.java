package challenge.controller;

import java.util.Map;

import javax.sql.DataSource;

import org.omg.PortableServer.REQUEST_PROCESSING_POLICY_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import challenge.db.Queries;
import challenge.db.QueryOperable;
import challenge.helper.Helper;
import challenge.helper.RESPONSE;

@Repository
@RestController
public class StartFollowEndpoint {
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private QueryOperable Q = new Queries();

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@RequestMapping(value = "/startFollowing", method = RequestMethod.POST)
	public String readMessage(@RequestParam Map<String, String> map) throws Exception {

		// Verify Request
		if (!map.containsKey("password") || !map.containsKey("username") || !map.containsKey("following_person_id")) {
			return RESPONSE.INVALID.msg;
		}

		String username = map.get("username");
		String password = map.get("password");
		String following = map.get("following_person_id");
		int following_person_id = 0;
		if(Helper.isInteger(following)) {
			following_person_id = Integer.parseInt(following);
		} else {
			return RESPONSE.INVALID.msg;
		}

		// verify User
		int id = Q.getUser(username, password, this.namedParameterJdbcTemplate);
		if (id == 0) {
			return RESPONSE.AUTH_FAIL.msg;
		}

		// check if the new person has any entry in db
		String user = Q.getPersonByID(following_person_id, this.namedParameterJdbcTemplate);
		if (null == user || user.length() == 0) {
			return RESPONSE.PERSON_NOT_EXISTS.msg;
		}

		// check if the current user is already following the new person
		int follow = Q.checkFollower(id, following_person_id, this.namedParameterJdbcTemplate);
		if (follow == 1) {
			return RESPONSE.NOCHANGE.msg;
		}

		// start following another user
		int result = Q.startFollowingUser(id, following_person_id, this.namedParameterJdbcTemplate);

		return result + RESPONSE.ROWS_AFFECTED.msg;
	}
}
