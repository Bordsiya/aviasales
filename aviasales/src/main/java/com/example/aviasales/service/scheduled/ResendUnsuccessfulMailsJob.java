package com.example.aviasales.service.scheduled;

import com.example.aviasales.dto.ReSendMailsResult;
import com.example.aviasales.service.UnsuccessfulMailReSender;
import lombok.RequiredArgsConstructor;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@RequiredArgsConstructor
public class ResendUnsuccessfulMailsJob extends QuartzJobBean {
    private static final Logger log = LoggerFactory.getLogger(ResendUnsuccessfulMailsJob.class);
    private static final String RESULT_KEY = "checkAndResendResult";
    private final UnsuccessfulMailReSender mailReSender;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        var contextMap = context.getJobDetail().getJobDataMap();
        var previousResult = (ReSendMailsResult) contextMap.get(RESULT_KEY);
        log.info("Starting a job: previous result: {}", previousResult);

        var result = mailReSender.checkAndResendIfNecessary();
        contextMap.put(RESULT_KEY, result);
        log.info("Job done with result: {}", result);
    }
}
