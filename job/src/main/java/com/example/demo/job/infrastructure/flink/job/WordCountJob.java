package com.example.demo.job.infrastructure.flink.job;

import com.example.demo.job.infrastructure.JobProperties;
import com.example.demo.job.infrastructure.flink.Job;
import com.example.demo.job.infrastructure.flink.source.FileSource;
import com.example.demo.job.infrastructure.flink.task.TokenizeWordTask;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.core.fs.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WordCountJob implements Job {

    public static final String JOB_NAME = "wordCount";
    private static final Logger LOGGER = LoggerFactory.getLogger(WordCountJob.class);
    private final FileSource fileSource;
    private final TokenizeWordTask tokeniseWordTask;
    private final ExecutionEnvironment executionEnvironment;
    private final JobProperties jobProperties;
    private final WordTokenReduceGroup wordTokenReduceGroup;
    private final WordTokenTextFormatter wordTokenTextFormatter;

    public WordCountJob(FileSource fileSource,
                        TokenizeWordTask tokeniseWordTask,
                        ExecutionEnvironment executionEnvironment,
                        JobProperties jobProperties,
                        WordTokenReduceGroup wordTokenReduceGroup,
                        WordTokenTextFormatter wordTokenTextFormatter) {
        this.fileSource = fileSource;
        this.tokeniseWordTask = tokeniseWordTask;
        this.executionEnvironment = executionEnvironment;
        this.jobProperties = jobProperties;
        this.wordTokenReduceGroup = wordTokenReduceGroup;
        this.wordTokenTextFormatter = wordTokenTextFormatter;
    }

    @Override
    public JobExecutionResult start() throws Exception {
        fileSource.getLinesFromFile(jobProperties.getInputFilePath())
            .flatMap(tokeniseWordTask)
            .groupBy("word")
            .reduceGroup(wordTokenReduceGroup)
            .writeAsFormattedText(jobProperties.getOutputFilePath(), FileSystem.WriteMode.OVERWRITE, wordTokenTextFormatter).setParallelism(1);// TODO sink in jdbc

        return executionEnvironment.execute();
    }

    @Override
    public String getName() {
        return JOB_NAME;
    }

}
