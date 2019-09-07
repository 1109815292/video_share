package com.juheshi.video.dao;

import com.juheshi.video.entity.CommentLike;
import com.juheshi.video.entity.VideoComment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideoCommentDao {

    VideoComment findCommentById(Integer id);

    List<VideoComment> findCommentByVideoId(Integer videoId);

    List<CommentLike> findCommentLike(@Param("commentId") Integer commentId, @Param("userId") Integer userId);

    void insertVideoComment(VideoComment videoComment);

    void updateVideoComment(VideoComment videoComment);

    void insertCommentLike(CommentLike commentLike);

    void deleteCommentLike(@Param("commentId") Integer commentId, @Param("userId") Integer userId);
}
