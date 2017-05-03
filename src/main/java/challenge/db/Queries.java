package challenge.db;

import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import challenge.helper.Helper;

public class Queries implements QueryOperable {

	@Override
	public int getUser(String username, String password, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		String sql = "select id from person where name = :name and password = :password";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource("name", username).addValue("password",
				password);

		int id = namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
		return id;
	}

	@Override
	public List<Object> getCurrentUserMessages(int person_id, String searchContent,
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		String search = "";
		if (null != searchContent && searchContent.length() > 0) {
			search = " AND content LIKE '%" + searchContent + "%'";
		}

		String sql = "SELECT content FROM tweet WHERE person_id = :person_id ";
		sql += search;

		SqlParameterSource namedParameters = new MapSqlParameterSource("person_id", person_id);
		List<Map<String, Object>> listMap = namedParameterJdbcTemplate.queryForList(sql, namedParameters);
		List<Object> list = Helper.iterateListMap(listMap);
		return list;
	}

	@Override
	public List<Object> getAllFollowerMessages(int person_id, String searchContent,
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		String search = "";
		if (null != searchContent && searchContent.length() > 0) {
			search = " AND content LIKE '%" + searchContent + "%'";
		}

		String sql = "SELECT content FROM tweet WHERE person_id IN "
				+ "(SELECT person_id FROM followers WHERE follower_person_id = :person_id)";
		sql += search;

		SqlParameterSource namedParameters = new MapSqlParameterSource("person_id", person_id);
		List<Map<String, Object>> listMap = namedParameterJdbcTemplate.queryForList(sql, namedParameters);
		List<Object> list = Helper.iterateListMap(listMap);
		return list;
	}

	@Override
	public List<Object> getUserFollowing(int person_id, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		String sql = "SELECT name FROM person WHERE id IN "
				+ "(SELECT person_id FROM followers WHERE follower_person_id = :person_id)";

		SqlParameterSource namedParameters = new MapSqlParameterSource("person_id", person_id);
		List<Map<String, Object>> listMap = namedParameterJdbcTemplate.queryForList(sql, namedParameters);
		List<Object> list = Helper.iterateListMap(listMap);
		return list;
	}

	@Override
	public List<Object> getFollowers(int person_id, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		String sql = "SELECT name FROM person WHERE id IN "
				+ "(SELECT follower_person_id FROM followers WHERE person_id = :person_id)";

		SqlParameterSource namedParameters = new MapSqlParameterSource("person_id", person_id);
		List<Map<String, Object>> listMap = namedParameterJdbcTemplate.queryForList(sql, namedParameters);
		List<Object> list = Helper.iterateListMap(listMap);
		return list;
	}

	@Override
	public int checkFollower(int person_id, int following_person_id,
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		String sql = "select count(*) from followers where person_id = :following_person_id AND follower_person_id = :person_id";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource("following_person_id", following_person_id)
				.addValue("person_id", person_id);

		int id = namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
		return id;
	}

	@Override
	public int startFollowingUser(int person_id, int following_person_id,
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		String sql = "INSERT INTO followers (person_id, follower_person_id) VALUES (:following_person_id, :person_id)";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource("following_person_id", following_person_id)
				.addValue("person_id", person_id);

		int rows = namedParameterJdbcTemplate.update(sql, namedParameters);
		return rows;
	}

	@Override
	public int unfollowUser(int person_id, int unfollow_person_id,
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {

		String sql = "DELETE FROM followers WHERE person_id = :unfollow_person_id AND follower_person_id = :person_id";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource("unfollow_person_id", unfollow_person_id)
				.addValue("person_id", person_id);

		int rows = namedParameterJdbcTemplate.update(sql, namedParameters);
		return rows;
	}

	@Override
	public String getPersonByID(int person_id, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		String sql = "SELECT name from person where id = :person_id";

		MapSqlParameterSource namedParameters = new MapSqlParameterSource("person_id", person_id);
		try {
			String personName = namedParameterJdbcTemplate.queryForObject(sql, namedParameters, String.class);
			return personName;
		} catch (EmptyResultDataAccessException e1) {
			return null;
		} catch (IncorrectResultSizeDataAccessException e2) {
			return null;
		}
		// queryFor[Object] expects that the executed query will return only one
		// result; otherwise throws an exception
	}

	@Override
	public int getPersonCount(int person_id, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		String sql = "SELECT count(name) from person where id = :person_id";

		MapSqlParameterSource namedParameters = new MapSqlParameterSource("person_id", person_id);
		try {
			int count = namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
			return count;
		} catch (EmptyResultDataAccessException e1) {
			return 0;
		} catch (IncorrectResultSizeDataAccessException e2) {
			return 0;
		}
		// queryFor[Object] expects that the executed query will return only one
		// result; otherwise throws an exception
	}

	@Override
	public List<Integer> getPersonIDList(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		String sql = "SELECT id FROM person";
		SqlParameterSource namedParameters = new MapSqlParameterSource();
		List<Map<String, Object>> listMap = namedParameterJdbcTemplate.queryForList(sql, namedParameters);
		
		List<Object> list = Helper.iterateListMap(listMap);
		
		List<Integer> listInteger = Helper.convertToIntegerList(list);
		
		return listInteger;
	}

	@Override
	public int getPopularFollowersForPerson(int person_id, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		String sql = "SELECT person_id  FROM followers WHERE person_id IN "
				+ "(SELECT follower_person_id FROM followers where person_id = :person_id ) "
				+ "GROUP BY person_id ORDER BY count(follower_person_id)  desc LIMIT 1";
		SqlParameterSource namedParameters = new MapSqlParameterSource("person_id", person_id);

		int id = namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
		return id;
	}

}
