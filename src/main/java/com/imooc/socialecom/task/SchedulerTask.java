package com.imooc.socialecom.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.socialecom.pojo.Trade;
import com.imooc.socialecom.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
public class SchedulerTask {

    @Autowired
    private TradeService tradeService;

//    @Scheduled(cron = "0/5 * * * * ?")
    private void taskOrder() {
        QueryWrapper<Trade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        //当前时间剪掉15分钟
        queryWrapper.lt("create_time", LocalDateTime.now().minusMinutes(15));
        List<Trade> trades = tradeService.list(queryWrapper);
        if (trades != null) {
            for (Trade trade : trades) {
                trade.setStatus(3);
                tradeService.cancel(trade);
            }
        }
    }
}
