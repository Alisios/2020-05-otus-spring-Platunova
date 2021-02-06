package ru.otus.spring.configs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.storage.dto.StockDtoCsv;
import ru.otus.spring.storage.dto.StockMapper;
import ru.otus.spring.storage.StockEntity;

@Configuration
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("all")
public class JobConfig {

    public static final String OUTPUT_FILE_NAME = "outputFileName";
    public static final String INPUT_FILE_NAME = "inputFileName";
    public static final String IMPORT_USER_JOB_NAME = "importUserJob";
    private final AppProps appProps;

    public static final int CHUNK_SIZE = 10;


    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final StockMapper mapper;

    @StepScope
    @Bean
    public FlatFileItemReader<StockDtoCsv> reader(@Value("#{jobParameters['" + INPUT_FILE_NAME + "']}") String inputFileName) {
        return new FlatFileItemReaderBuilder<StockDtoCsv>()
                .name("stickItemReader")
                .resource(new FileSystemResource(inputFileName))
                .delimited()
                .delimiter(";")
                .names("ticker", "per", "date", "time", "open", "high", "low", "close", "volume")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<StockDtoCsv>() {{
                    setTargetType(StockDtoCsv.class);
                }})
                .build();
    }


    @StepScope
    @Bean
    public ItemProcessor processor(StockMapper mapper) {
        return (ItemProcessor<StockDtoCsv, StockEntity>) (item) -> mapper.mapFromCsv(item, appProps.getInputFile());
    }

    @StepScope
    @Bean
    public MongoItemWriter<StockEntity> mongoItemWriter(MongoTemplate mongoTemplate) {//(@Value("#{jobParameters['" + OUTPUT_FILE_NAME + "']}") String outputFileName) {
        return new MongoItemWriterBuilder<StockEntity>()
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public Job importUserJob(Step step1) {
        return jobBuilderFactory.get(IMPORT_USER_JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        log.info("Начало выполнения миграции из csv в бд");
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        log.info("Окончание выполнения миграции из csv в бд");
                        ;
                    }
                })
                .build();
    }

    @Bean
    public Step step1
            (MongoItemWriter<StockEntity> writer, ItemReader<StockDtoCsv> reader, ItemProcessor itemProcessor) {
        return stepBuilderFactory.get("step1")
                .chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .build();
    }
}
