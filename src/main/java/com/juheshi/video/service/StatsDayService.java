package com.juheshi.video.service;

import com.juheshi.video.dao.StatsDayDao;
import com.juheshi.video.entity.StatsDay;
import com.juheshi.video.util.UtilDate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
public class StatsDayService {

    @Resource
    private StatsDayDao statsDayDao;

    public int createNewStats(StatsDay statsDay) {
        statsDay.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
        return statsDayDao.save(statsDay);
    }

    public int modifyNewsStats(StatsDay statsDay) {
        int exists = statsDayDao.checkExists(statsDay.getStatsYear(), statsDay.getStatsMonth(), statsDay.getStatsDay());
        if (exists > 0) {
            return statsDayDao.update(statsDay);
        } else {
            statsDay.setCreatedTime(Timestamp.valueOf(UtilDate.getDateFormatter()));
            return statsDayDao.save(statsDay);
        }
    }

    public int checkExists(int year, int month, int day) {
        return statsDayDao.checkExists(year, month, day);
    }

    public StatsDay findToday() {
        Calendar cal = Calendar.getInstance();
        return statsDayDao.selectByYearMonthDay(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
    }

    public StatsDay findMonth() {
        Calendar cal = Calendar.getInstance();
        Map<String, Object> param = new HashMap<>();
        param.put("statsYear", cal.get(Calendar.YEAR));
        param.put("statsMonth", cal.get(Calendar.MONTH) + 1);
        return statsDayDao.selectSumByParam(param);
    }
}
