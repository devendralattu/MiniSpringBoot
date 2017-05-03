package challenge.db;

import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public interface QueryOperable {

	public int getUser(String username, String password, NamedParameterJdbcTemplate namedParameterJdbcTemplate) throws Exception;
	
	public List<Object> getCurrentUserMessages(int person_id, String searchContent, NamedParameterJdbcTemplate namedParameterJdbcTemplate);
	
	public List<Object> getAllFollowerMessages(int person_id, String searchContent, NamedParameterJdbcTemplate namedParameterJdbcTemplate);

	public List<Object> getUserFollowing(int person_id, NamedParameterJdbcTemplate namedParameterJdbcTemplate);

	public List<Object> getFollowers(int person_id, NamedParameterJdbcTemplate namedParameterJdbcTemplate);

	public int checkFollower(int person_id, int following_person_id, NamedParameterJdbcTemplate namedParameterJdbcTemplate);
	
	public int startFollowingUser(int person_id, int following_person_id,
			NamedParameterJdbcTemplate namedParameterJdbcTemplate);

	public int unfollowUser(int person_id, int unfollow_person_id, NamedParameterJdbcTemplate namedParameterJdbcTemplate);

	public String getPersonByID(int person_id, NamedParameterJdbcTemplate namedParameterJdbcTemplate);

	public int getPersonCount(int person_id, NamedParameterJdbcTemplate namedParameterJdbcTemplate);

	public List<Integer> getPersonIDList(NamedParameterJdbcTemplate namedParameterJdbcTemplate);

	public int getPopularFollowersForPerson(int person_id, NamedParameterJdbcTemplate namedParameterJdbcTemplate);
}
