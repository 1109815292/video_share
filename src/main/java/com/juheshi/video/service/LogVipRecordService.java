package com.juheshi.video.service;

import com.juheshi.video.dao.LogVipRecordDao;
import com.juheshi.video.entity.LogVipRecord;
import com.juheshi.video.util.UtilDate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;

@Service
public class LogVipRecordService {

    @Resource
    private LogVipRecordDao logVipRecordDao;

    public int createLog(LogVipRecord record) {
        record.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
        return logVipRecordDao.save(record);
    }
}
