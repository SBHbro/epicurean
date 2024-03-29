package com.web.curation.controller.restaurant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.web.curation.dao.meetup.MeetUpDao;
import com.web.curation.dao.restaurant.RestaurantDao;
import com.web.curation.dao.restaurant.ReviewCommentDao;
import com.web.curation.dao.restaurant.ReviewDao;
import com.web.curation.dao.user.UserDao;
import com.web.curation.model.BasicResponse;
import com.web.curation.model.meetup.Meetup;
import com.web.curation.model.review.Restaurant;
import com.web.curation.model.review.Review;
import com.web.curation.model.review.ReviewComment;
import com.web.curation.model.user.User;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = { "*" })
@RestController
public class ReviewController {

	@Autowired
	RestaurantDao restDao;
	@Autowired
	UserDao userDao;
	@Autowired
	ReviewDao reviewDao;
	@Autowired
	ReviewCommentDao reviewCommentDao;
	@Autowired
	MeetUpDao meetUpDao;

	// 리뷰 추가
	@PutMapping("/review")
	@ApiOperation(value = "리뷰 작성")
	public Object addReview(@RequestBody Review review) {
		final BasicResponse result = new BasicResponse();
		System.out.println(review);
		reviewDao.save(review); 
		restDao.updateRestReview(review.getRestId());

		result.status = true;
		result.data = "success";
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// 리뷰 검색
	@GetMapping("/review/search")
	@ApiOperation(value = "리뷰 검색")
	public Map<String, Object> searchReview(@RequestParam(required = true) final int id) {
		System.out.println(id);
		// 1. 음식점 id를 가져온다
		// 2. 음식점 id로 리뷰 검색
		// 3. 리뷰의 밋업 id로 팀원 닉네임 검색

		System.out.println(id);
		List<Review> list = reviewDao.selectReviewById(id);

		List<String>[] nickname = new ArrayList[list.size()];
		int index = 0;
		for (Review a : list) {
			nickname[index] = new ArrayList<String>();
			List<User> temp = userDao.selectUserNickNameByMeetUpId(Integer.parseInt(a.getMeetupId()));
			for (User b : temp) {
				nickname[index].add(b.getUserNickname());
			}
			index++;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println("닉네임 " + nickname);
		map.put("review", list);
		map.put("member", nickname);

		return map;
	}

	// 리뷰 추가
	@PutMapping("/review/comment")
	@ApiOperation(value = "리뷰 작성")
	public Object addReviewComment(@RequestBody ReviewComment comment) {
		final BasicResponse result = new BasicResponse();
		System.out.println(comment);
		reviewCommentDao.save(comment); 

		result.status = true;
		result.data = "success";
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 리뷰에 달린 댓글 검색
	@GetMapping("/review/comment/{reviewId}")
	@ApiOperation(value = "review id에 해당하는 리뷰에 달린 댓글 리스트 조회")
	public Object searchReviewComment(@PathVariable String reviewId) {
		final BasicResponse result = new BasicResponse();
		Optional<List<ReviewComment>> comments = reviewCommentDao.findAllByReviewId(reviewId);
		Map<String, Object> map = new HashMap<String, Object>();
		List<User> user  = new ArrayList<User>();
		
		if(comments.isPresent()) {
			map.put("status", true);
			map.put("data", "success");
			map.put("object", comments.get());
			for (ReviewComment rc : comments.get()) {
				User u = userDao.getUserByUserId(rc.getWriter());
				user.add(u);
			}
			map.put("user",user);
		}else {
			map.put("status", true);
			map.put("data", "fail");
		}
		return map;
	}
	

}
