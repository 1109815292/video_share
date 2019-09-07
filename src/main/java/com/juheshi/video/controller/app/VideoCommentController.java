package com.juheshi.video.controller.app;

import com.juheshi.video.common.Constants;
import com.juheshi.video.entity.CommentLike;
import com.juheshi.video.entity.VideoComment;
import com.juheshi.video.entity.VideoStore;
import com.juheshi.video.service.VideoCommentService;
import com.juheshi.video.service.VideoStoreService;
import com.juheshi.video.util.UtilDate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/app/comment")
@SessionAttributes(names = {Constants.SESSION_APP_STATION_COPY_NO, Constants.SESSION_APP_USER_ID})
public class VideoCommentController {

    @Resource
    private VideoCommentService videoCommentService;
    @Resource
    private VideoStoreService videoStoreService;


    @RequestMapping(value = "/saveComment", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Object saveComment(VideoComment videoComment, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId){
        videoComment.setUserId(userId);
        videoComment.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
        videoComment.setLikeCount(0);
        videoCommentService.save(videoComment);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("data", videoComment);
        resultMap.put("message", "success");

        return resultMap;
    }

    @RequestMapping(value = "/getVideoComment", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getVideoComment(Integer videoId, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId){

        List<VideoComment> videoCommentList = videoCommentService.findCommentByVideoId(videoId);
        for (VideoComment p:videoCommentList){
            p.setLikeFlag(videoCommentService.checkCommentLike(p.getId(), userId));
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("data", videoCommentList);

        return resultMap;
    }

    @RequestMapping(value = "/commentLike")
    @ResponseBody
    public Object commentLike(Integer commentId, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId){
        CommentLike commentLike = new CommentLike();
        commentLike.setCommentId(commentId);
        commentLike.setUserId(userId);
        commentLike.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));

        videoCommentService.saveCommentLike(commentLike);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("message", "success");
        return resultMap;
    }

    @RequestMapping(value = "/cancelLike")
    @ResponseBody
    public Object cancelLike(Integer commentId, @ModelAttribute(Constants.SESSION_APP_USER_ID) Integer userId){

        videoCommentService.removeLike(commentId, userId);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code", 200);
        resultMap.put("message", "success");
        return resultMap;
    }
}
