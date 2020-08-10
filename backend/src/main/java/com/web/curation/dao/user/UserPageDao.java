package com.web.curation.dao.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.web.curation.model.user.UserPage;

public interface UserPageDao extends JpaRepository<UserPage, String> {

	// 유저아이디로 검색해서 유저가 팔로잉하는 유저아이디 가져옴
	@Query(value = "SELECT b.user_following " + "FROM user a " + "INNER JOIN following b ON a.user_id = b.user_id "
			+ "WHERE a.user_id = :userId", nativeQuery = true)
	ArrayList<String> getUserFollowingByUserId(String userId);

	// 유저이메일로 검색해서 유저를 팔로워하는 유저아이디 가져옴
	@Query(value = "SELECT b.user_follower " + "FROM user a " + "INNER JOIN follower b ON a.user_id = b.user_id "
			+ "WHERE a.user_Id = :userId", nativeQuery = true)
	ArrayList<String> getUserFollowerByUserId(String userId);

	@Query(value = "SELECT count(*) FROM following WHERE user_id  = :userId", nativeQuery = true)
	int getUserFollowingCount(String userId);

	@Query(value = "SELECT count(*) FROM follower WHERE user_id  = :userId", nativeQuery = true)
	int getUserFollowerCount(String userId);

	@Query(value = "SELECT count(*) FROM following_request WHERE user_id  = :userId", nativeQuery = true)
	int getUserFolloingRequestCount(String userId);

	@Query(value = "SELECT user_comment FROM user WHERE user_id  = :userId", nativeQuery = true)
	String getUserCommentByUserId(String userId);

	@Query(value = "SELECT count(*) FROM following WHERE user_id = :userId and user_following = :followeruserId", nativeQuery = true)
	int getFollowingCountByUserIdByUserFollowing(String userId, String followeruserId);

	@Query(value = "SELECT count(*) FROM following_request WHERE user_id = :followeruserId and user_following = :userId", nativeQuery = true)
	int getFollowingRequestCountByUserIdByUserFollowing(String userId, String followeruserId);

	// 유저이메일로 검색해서 유저를 팔로잉 요청한 아이디 가져옴
	@Query(value = "SELECT b.user_following " + "FROM user a "
			+ "INNER JOIN following_request b ON a.user_id = b.user_id "
			+ "WHERE a.user_Id = :userId", nativeQuery = true)
	ArrayList<String> getFollowingrequestUserByUserId(String userId);

	@Query(value = "DELETE FROM following_request WHERE user_id = :userId and user_following = :followingRequestId", nativeQuery = true)
	String deleteFollowingrequestUser(String userId, String followingRequestId);

	@Query(value = "INSERT INTO follower" + "(user_id, user_follower) "
			+ "value (:userId, :followingRequestId)", nativeQuery = true)
	String insertFollowerUser(String userId, String followingRequestId);

	@Query(value = "INSERT INTO following_request" + "(user_id, user_following) "
			+ "value (:followerId, :userId)", nativeQuery = true)
	String setUserIdByUserIdAndfollowerId(String userId, String followerId);

	@Query(value = "DELETE FROM following WHERE user_id = :userId and user_following = :anotherId", nativeQuery = true)
	String deleteFollowing(String userId, String anotherId);

	@Query(value = "SELECT a.* " + "FROM meetup a " + "INNER JOIN meetup_member b ON a.meetup_id = b.meetup_id "
			+ "WHERE b.meetup_member = :userId", nativeQuery = true)
	ArrayList<String> getMeetupByUserId(String userId);

	@Query(value = "SELECT meetup_member " + "FROM meetup_member " + "WHERE meetup_id = :meetupId", nativeQuery = true)
	ArrayList<String> getUsersByMeetupId(String meetupId);

	@Query(value = "INSERT INTO following" + "(user_id, user_following) "
			+ "value (:followingRequestId, :userId)", nativeQuery = true)
	String insertFollowingUser(String userId, String followingRequestId);

	@Query(value = "DELETE FROM follower WHERE user_id = :anotherId and user_follower = :userId", nativeQuery = true)
	String deleteFollower(String userId, String anotherId);

	@Query(value = "DELETE FROM following_request WHERE user_id = :anotherId and user_following = :userId", nativeQuery = true)
	String deleteFollowingRequest(String userId, String anotherId);

	// userid로 좋아요한 가게 id 모두 가져옴
	@Query(value = "SELECT rest_id from restaurant_like where user_id= :userid", nativeQuery = true)
	List<Integer> selectRestLikeIdByUserId(String userid);

	// userid로 스크랩한 가게 id 모두 가져옴
	@Query(value = "SELECT rest_id from scrap where user_id= :userid", nativeQuery = true)
	List<Integer> selectRestScrapIdbyUserId(String userid);

	// 좋아요 입력
	@Query(value = "insert into restaurant_like value(:userid,:restid)", nativeQuery = true)
	void insertRestLike(String userid, String restid);

	// 좋아요 취소
	@Query(value = "delete from restaurant_like where user_id= :userid AND rest_id = :restid", nativeQuery = true)
	void deleteRestLike(String userid, String restid);

	// 좋아요 증가
	@Query(value = "update restaurant set rest_like = rest_like+1 where rest_id = :restid", nativeQuery = true)
	void updateRestLike(String restid);

	// 좋아요 감소
	@Query(value = "update restaurant set rest_like = rest_like-1 where rest_id = :restid", nativeQuery = true)
	void updateRestLikeM(String restid);

	// 스크랩 입력
	@Query(value = "insert into scrap value(:userid,:restid)", nativeQuery = true)
	void insertRestScrap(String userid, String restid);

	// 스크랩 취소
	@Query(value = "delete from scrap where user_id= :userid AND rest_id = :restid", nativeQuery = true)
	void deleteRestScrap(String userid, String restid);

	// 스크랩 증가
	@Query(value = "update restaurant set rest_scrap = rest_scrap+1 where rest_id = :restid", nativeQuery = true)
	void updateRestScrap(String restid);

	// 스크랩 감소
	@Query(value = "update restaurant set rest_scrap = rest_scrap-1 where rest_id = :restid", nativeQuery = true)
	void updateRestScrapM(String restid);

}
