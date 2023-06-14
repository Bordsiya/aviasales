package com.example.aviasales.config;

import com.example.aviasales.service.scheduled.ResendUnsuccessfulMailsJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReSendMailJobConfig {
    @Value("${scheduler.re_send_mail.cron}")
    private String cron;
    @Value("${scheduler.re_send_mail.group_name}")
    private String groupName;

    @Bean
    JobDetail showReSendMailJobDetail() {
        return JobBuilder
                .newJob(ResendUnsuccessfulMailsJob.class)
                .withIdentity("ReSendMailJob", groupName)
                .storeDurably()
                .requestRecovery(true)
                .build();
    }

    @Bean
    Trigger showReSendMailTrigger(JobDetail showReSendMailJobDetail) {
        return TriggerBuilder
                .newTrigger()
                .forJob(showReSendMailJobDetail)
                .withIdentity("ReSendMailJobTrigger", groupName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();
    }
}
