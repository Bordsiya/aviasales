package com.example.aviasales.config;

import java.util.List;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzSchedulerConfig {
    @Value("${scheduler.re_send_mail.group_name}")
    private String groupName;

    @Bean
    Scheduler scheduler(List<Trigger> triggers, List<JobDetail> jobDetails, SchedulerFactoryBean factory) throws SchedulerException {
        factory.setWaitForJobsToCompleteOnShutdown(true);
        var scheduler = factory.getScheduler();

        revalidateJobs(jobDetails, scheduler);
        rescheduleTriggers(triggers, scheduler);

        scheduler.start();
        return scheduler;
    }

    void rescheduleTriggers(List<Trigger> triggers, Scheduler scheduler) throws SchedulerException {
        for (Trigger it : triggers) {
            if (!scheduler.checkExists(it.getKey())) {
                scheduler.scheduleJob(it);
            } else {
                scheduler.rescheduleJob(it.getKey(), it);
            }
        }
    }

    void revalidateJobs(List<JobDetail> jobDetails, Scheduler scheduler) throws SchedulerException {
        var jobKeys = jobDetails
                .stream()
                .map(JobDetail::getKey)
                .toList();

        scheduler
                .getJobKeys(GroupMatcher.jobGroupEquals(groupName))
                .forEach(it -> {
                            if (!jobKeys.contains(it)) {
                                try {
                                    scheduler.deleteJob(it);
                                } catch (SchedulerException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                );
    }
}
