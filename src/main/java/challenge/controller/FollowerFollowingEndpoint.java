package challenge.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import challenge.db.Queries;
import challenge.db.QueryOperable;
import challenge.helper.RESPONSE;

@Repository
@RestController
public class FollowerFollowingEndpoint {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private QueryOperable Q = new Queries();

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@RequestMapping(value = "/getFollowerFollowing", method = RequestMethod.POST)
	public String readMessage(@RequestParam Map<String, String> map) throws Exception {

		// Verify Request
		if (!map.containsKey("password") || !map.containsKey("username")) {
			return RESPONSE.INVALID.msg;
		}

		String username = map.get("username");
		String password = map.get("password");

		// verify User
		int id = Q.getUser(username, password, this.namedParameterJdbcTemplate);
		if (id == 0) {
			return RESPONSE.AUTH_FAIL.msg;
		}

		// Get list of people the user is following
		List<Object> listUserFollowing = Q.getUserFollowing(id, this.namedParameterJdbcTemplate);

		// Get users followers
		List<Object> listFollowers = Q.getFollowers(id, this.namedParameterJdbcTemplate);

		// convert to JSON response format
		Map<String, List<Object>> gMap = new HashMap<>();
		gMap.put("userFollowing", listUserFollowing);
		gMap.put("followers", listFollowers);

		String json = new Gson().toJson(gMap);

		return json;
	}

}
