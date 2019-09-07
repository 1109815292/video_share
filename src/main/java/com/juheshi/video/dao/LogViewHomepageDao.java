package com.juheshi.video.dao;

import com.juheshi.video.entity.LogViewHomepage;
import org.apache.ibatis.annotations.Param;

public interface LogViewHomepageDao {

    int save(LogViewHomepage logViewHomepage);

    int checkUserView(@Param("userPageId") Integer userPageId,@Param("userId") Integer userId);
}
