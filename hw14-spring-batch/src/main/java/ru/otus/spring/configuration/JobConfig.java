package ru.otus.spring.configuration;

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
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.subscriptionmanager.database.dto.SubMapper;
import ru.otus.spring.subscriptionmanager.database.dto.SubscriptionMongoDto;
import ru.otus.spring.subscriptionmanager.database.dto.UserMongoDto;
import ru.otus.spring.subscriptionmanager.database.entities.Subscription;
import ru.otus.spring.subscriptionmanager.database.entities.User;

import javax.persistence.EntityManagerFactory;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class JobConfig {

    public static final String IMPORT_SUB_JOB_NAME = "importSubJob";
    public static final String IMPORT_USER_JOB_NAME = "importUserJob";

    public static final int CHUNK_SIZE = 10;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public ItemReader<Subscription> reader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Subscription>()
                .name("SqlSubReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT s FROM Subscription s JOIN FETCH s.users")
                .build();
    }

    @Bean
    public ItemReader<User> userReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<User>()
                .name("SqlUserReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT u FROM User u JOIN FETCH u.subscriptions")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Subscription, SubscriptionMongoDto> processor(SubMapper mapper) {
        return mapper::mapFromSqlToMongoDto;
    }

    @StepScope
    @Bean
    public ItemProcessor<User, UserMongoDto> userProcessor(SubMapper mapper) {
        return mapper::mapFromSqlToMongoDto;
    }

    @StepScope
    @Bean
    public MongoItemWriter<SubscriptionMongoDto> mongoItemWriter(MongoTemplate mongoTemplate) {//(@Value("#{jobParameters['" + OUTPUT_FILE_NAME + "']}") String outputFileName) {
        return new MongoItemWriterBuilder<SubscriptionMongoDto>()
                .template(mongoTemplate)
                .build();
    }

    @StepScope
    @Bean
    MongoItemWriter<UserMongoDto> userWriter(MongoTemplate mongoTemplate) {//(@Value("#{jobParameters['" + OUTPUT_FILE_NAME + "']}") String outputFileName) {
        return new MongoItemWriterBuilder<UserMongoDto>()
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public Job importSubJob(Step transferToMongo) {
        return jobBuilderFactory.get(IMPORT_SUB_JOB_NAME)
                .incrementer(new RunIdIncrementer())
//                .flow(transferUserToMongo)
                .flow(transferToMongo)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        log.info("Начало выполнения миграции из postgres в mongo");
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        log.info("Окончание выполнения миграции из postrgres в mongo");
                        ;
                    }
                })
                .build();
    }

    @Bean
    public Job importUserJob(Step transferUserToMongo) {
        return jobBuilderFactory.get(IMPORT_USER_JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .flow(transferUserToMongo)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        log.info("Начало выполнения миграции users из postgres в mongo");
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        log.info("Окончание выполнения миграции users из postrgres в mongo");
                        ;
                    }
                })
                .build();
    }

    @Bean
    public Step transferToMongo(MongoItemWriter<SubscriptionMongoDto> writer, ItemReader<Subscription> reader, ItemProcessor<Subscription, SubscriptionMongoDto> itemProcessor) {
        return stepBuilderFactory.get("transferToMongo")
                .<Subscription, SubscriptionMongoDto>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step transferUserToMongo(MongoItemWriter<UserMongoDto> userWriter, ItemReader<User> userReader, ItemProcessor<User, UserMongoDto> userItemProcessor) {
        return stepBuilderFactory.get("transferUserToMongo")
                .<User, UserMongoDto>chunk(CHUNK_SIZE)
                .reader(userReader)
                .processor(userItemProcessor)
                .writer(userWriter)
                .build();
    }
}
