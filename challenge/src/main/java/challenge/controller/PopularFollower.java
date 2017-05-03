package challenge.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import challenge.db.Queries;
import challenge.db.QueryOperable;
import challenge.helper.RESPONSE;

@Repository
@RestController
public class PopularFollower {
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private QueryOperable Q = new Queries();

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@RequestMapping(value = "/getPopularFollowers")
	public String readMessage() throws Exception {
		
		List<Integer> listPerson = new ArrayList<>();
		listPerson = Q.getPersonIDList(this.namedParameterJdbcTemplate);
		
		if(null == listPerson || listPerson.size() == 0) {
			return RESPONSE.ERROR.msg;
		}
		
		Map<Integer, Integer> map = new HashMap<>();
		for(int id : listPerson) {
			int result = Q.getPopularFollowersForPerson(id, this.namedParameterJdbcTemplate);
			map.put(id, result);
		}
		String json = new Gson().toJson(map);
		return json;
	}
}
