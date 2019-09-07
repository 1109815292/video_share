package com.juheshi.video.service;

import com.juheshi.video.dao.VideoCommentDao;
import com.juheshi.video.dao.VideoStoreDao;
import com.juheshi.video.entity.CommentLike;
import com.juheshi.video.entity.VideoComment;
import com.juheshi.video.entity.VideoStore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class VideoCommentService {

    @Resource
    private VideoCommentDao videoCommentDao;
    @Resource
    private VideoStoreDao videoStoreDao;

    public List<VideoComment> findCommentByVideoId(Integer videoId){
        return videoCommentDao.findCommentByVideoId(videoId);
    }

    public void save(VideoComment videoComment){
        VideoStore videoStore = videoStoreDao.findVideoStoreById(videoComment.getVideoId());
        Integer commentCount = videoStore.getCommentCount()!=null?videoStore.getCommentCount():0;
        videoStore.setCommentCount(commentCount + 1);
        videoStoreDao.updateVideoStore(videoStore);
        videoCommentDao.insertVideoComment(videoComment);
    }

    public String checkCommentLike(Integer commentId, Integer userId){
        List<CommentLike> commentLikeList = videoCommentDao.findCommentLike(commentId, userId);
        if (commentLikeList.size() > 0){
            return "Y";
        }else{
            return "N";
        }
    }

    public void saveCommentLike(CommentLike commentLike){
        VideoComment videoComment = videoCommentDao.findCommentById(commentLike.getCommentId());
        if (videoComment != null){
            Integer likeCount = videoComment.getLikeCount()!=null?videoComment.getLikeCount():0;
            videoComment.setLikeCount(likeCount + 1);
            videoCommentDao.updateVideoComment(videoComment);
        }
        videoCommentDao.insertCommentLike(commentLike);
    }

    public void removeLike(Integer commentId, Integer userId){
        VideoComment videoComment = videoCommentDao.findCommentById(commentId);
        if (videoComment != null){
            Integer likeCount = videoComment.getLikeCount()!=null?videoComment.getLikeCount():0;
            videoComment.setLikeCount(likeCount - 1);
            videoCommentDao.updateVideoComment(videoComment);
        }
        videoCommentDao.deleteCommentLike(commentId, userId);
    }
}
