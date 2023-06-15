package com.example.aviasales.service.scheduled;

import com.example.aviasales.dto.ReSendMailsResult;
import com.example.aviasales.service.UnsuccessfulMailReSender;
import lombok.RequiredArgsConstructor;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@RequiredArgsConstructor
public class ResendUnsuccessfulMailsJob extends QuartzJobBean {
    private static final String RESULT_KEY = "checkAndResendResult";
    private final UnsuccessfulMailReSender mailReSender;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        var contextMap = context.getJobDetail().getJobDataMap();
        var previousResult = (ReSendMailsResult) contextMap.get(RESULT_KEY);
        System.out.printf("Job started, previous result: %s\n", previousResult);

        var result = mailReSender.checkAndResendIfNecessary();
        contextMap.put(RESULT_KEY, result);
        System.out.printf("Job done with result: %s\n", result);
    }
}
