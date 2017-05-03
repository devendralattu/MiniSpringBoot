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
public class ReadMessageEndpoint {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private QueryOperable Q = new Queries();

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@RequestMapping(value = "/readMessage", method = RequestMethod.POST)
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

		String searchContent = "";
		if (map.containsKey("search")) {
			searchContent = map.get("search");
		}

		// Read message sent by current User
		List<Object> listUserMsg = Q.getCurrentUserMessages(id, searchContent, this.namedParameterJdbcTemplate);

		// Read message of all the users which our current user follows
		List<Object> listUserFollowerMsg = Q.getAllFollowerMessages(id, searchContent, this.namedParameterJdbcTemplate);

		// 1. Merge and shuffle (to achieve randomness). Messages will be sorted
		// by their timestamp in real scenarios
		// OR
		// 2. just append to JSON response format
		// convert to JSON response format
		Map<String, List<Object>> gMap = new HashMap<>();
		gMap.put("MsgSentByUser", listUserMsg);
		gMap.put("MsgByPersonTheyFollow", listUserFollowerMsg);

		String json = new Gson().toJson(gMap);

		return json;
	}
}