package challenge.controller;

import java.util.Map;

import javax.sql.DataSource;

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

public class UnfollowEndpoint {
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private QueryOperable Q = new Queries();

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@RequestMapping(value = "/unfollow", method = RequestMethod.POST)
	public String readMessage(@RequestParam Map<String, String> map) throws Exception {

		// Verify Request
		if (!map.containsKey("password") || !map.containsKey("username") || !map.containsKey("unfollow_person_id")) {
			return RESPONSE.INVALID.msg;
		}

		String username = map.get("username");
		String password = map.get("password");
		String unfollow = map.get("unfollow_person_id");
		int unfollow_person_id = 0;
		if (Helper.isInteger(unfollow)) {
			unfollow_person_id = Integer.parseInt(unfollow);
		} else {
			return RESPONSE.INVALID.msg;
		}

		// verify User
		int id = Q.getUser(username, password, this.namedParameterJdbcTemplate);
		if (id == 0) {
			return RESPONSE.AUTH_FAIL.msg;
		}

		// check if the new person has any entry in db
		String user = Q.getPersonByID(unfollow_person_id, this.namedParameterJdbcTemplate);
		if (null == user || user.length() == 0) {
			return RESPONSE.PERSON_NOT_EXISTS.msg;
		}

		// check if already unfollowed or not following at all
		int follow = Q.checkFollower(id, unfollow_person_id, this.namedParameterJdbcTemplate);
		if (follow == 0) {
			return RESPONSE.NOCHANGE.msg;
		}
		
		// unfollow that new user
		int result = Q.unfollowUser(id, unfollow_person_id, this.namedParameterJdbcTemplate);
		if (result == 1) {
			return RESPONSE.SUCCESS.msg;
		}

		return RESPONSE.NOCHANGE.msg;
	}
}
