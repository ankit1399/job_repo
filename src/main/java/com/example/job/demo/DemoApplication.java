package com.example.job.demo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
public class DemoApplication {
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	public DataSource datasource;
	
	@Autowired
	public JobLauncher jobLauncher;
	
	@Scheduled(cron ="*/10 * * * * * ")
	public void runJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, Exception {
		JobParametersBuilder jobparam= new JobParametersBuilder();
		jobparam.addDate("runtime", new Date());
		this.jobLauncher.run(getJob(), jobparam.toJobParameters());
	} 

	
	
	
	@Bean
	public JobExecutionDecider getJobExecutionDecider() {
		
		return new myJobExecutionDecider();
		
	}
	
	public static String[] tokens= new String[] {"id",	"desc",	"name","id2",	"id3","id4","id5","abc","company","id7"};
			
//	@Bean
//	public ItemReader<Employee> getMyItemReader() {
//		
//		FlatFileItemReader<Employee> itemReader= new FlatFileItemReader<Employee>();
//		itemReader.setLinesToSkip(1);
//		itemReader.setResource(new FileSystemResource("C:\\Users\\ANKIT\\Downloads\\Sample-Spreadsheet-10-rows.csv"));
//		DefaultLineMapper<Employee> lineMapper= new DefaultLineMapper<Employee>();
//		DelimitedLineTokenizer tokenizer= new DelimitedLineTokenizer();
//		tokenizer.setNames(tokens);
//		lineMapper.setLineTokenizer(tokenizer);
//		lineMapper.setFieldSetMapper(new getSetFieldSetMapper());
//		itemReader.setLineMapper(lineMapper);
//		
//		
//		return itemReader;
//		
//	}
	
	private String sql= "select id ,descr,name,id2,id3,id4,id5,abc,company,id7 from employee order by id";
	
//	private String insert_sql= "insert into employee_out( id ,descr,name,id2,id3,id4,id5,abc,company,id7) values ( :id ,:descr,:name,:id2,:id3,:id4,:id5,:abc,:company,:id7) ";
	private String insert_sql= "insert into employee_out( id ,descr,name,id2,id3,id4,id5,abc,company,id7) values ( ?,?,?,?,?,?,?,?,?,?) ";
	
	
	@Bean
	public ItemReader<Employee> getMyItemReader() throws Exception {
		
		return new JdbcPagingItemReaderBuilder<Employee>()
				.dataSource(datasource)
				.name("myJdbcItemReader")
				.queryProvider(queryProvider())
				.rowMapper(new myRowmapper())
				.pageSize(3)
				.saveState(false)
				.build();
		
	}
	
	@Bean
	public PagingQueryProvider queryProvider() throws Exception {
		SqlPagingQueryProviderFactoryBean factory= new SqlPagingQueryProviderFactoryBean();
		factory.setSelectClause("select id ,descr,name,id2,id3,id4,id5,abc,company,id7");
		factory.setDataSource(datasource);
		factory.setFromClause("from employee ");
		factory.setSortKey("id");
		return factory.getObject() ;
		
		
	}

//	
//	@Bean
//	public ItemReader<Employee> getMyItemReader() {
//		
//		return new JdbcCursorItemReaderBuilder<Employee>()
//				.dataSource(datasource)
//				.name("myJdbcItemReader")
//				.sql(sql)
//				.rowMapper(new myRowmapper())
//				.build();
//		
//	}
	
	
//	@Bean
//	public Step getChunkBasedStep() throws Exception {
//		
//		return this.stepBuilderFactory.get("ChunkBasedStep").<Employee,EmployeeFinal>chunk(3)
//				.reader(getMyItemReader())
//				.processor(itemProcessor())
//				.faultTolerant()
//				.skip(RuntimeException.class)
//				.skipLimit(5)
//				.listener(new skipListener())
//				.writer(itemWriter())
//				.build();
//	}


	@Bean
	public Step getChunkBasedStep() throws Exception {
		
		return this.stepBuilderFactory.get("ChunkBasedStep").<Employee,EmployeeFinal>chunk(3)
				.reader(getMyItemReader())
				.processor(itemProcessor())
				.faultTolerant()
				.retry(RuntimeException.class)
				.retryLimit(5)
				.listener( new retryCustomListener())
				.writer(itemWriter())
				.taskExecutor(stepTaskExecutor())
				.build();
	}
	@Bean
	public TaskExecutor stepTaskExecutor() {
		ThreadPoolTaskExecutor taskExecutor= new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(2);
		taskExecutor.setMaxPoolSize(10);
	return taskExecutor;
}


	@Bean
	public ItemProcessor<Employee, EmployeeFinal> itemProcessor() {
		// TODO Auto-generated method stub
		return new CompositeItemProcessorBuilder<Employee, EmployeeFinal>()
				.delegates(filterItemProcessor(), employeeFinalItemProcessor()).build();
	}

	@Bean
	public ItemProcessor<Employee, Employee> filterItemProcessor() {
		BeanValidatingItemProcessor<Employee> itemProcessor = new BeanValidatingItemProcessor<Employee>();
		itemProcessor.setFilter(true);
		return itemProcessor;
	}

	@Bean
	public ItemProcessor<Employee, EmployeeFinal> employeeFinalItemProcessor() {
		return new EmployeeFinalItemProcessor();
	}

//	private ItemWriter<Employee> itemWriter() {
//	// TODO Auto-generated method stub
//	FlatFileItemWriter<Employee> itemwriter 	=new FlatFileItemWriter<Employee>();
//	itemwriter.setResource(new FileSystemResource("C:\\Users\\ANKIT\\Downloads\\out.csv"));
//	DelimitedLineAggregator<Employee> lineAggregator = new DelimitedLineAggregator<Employee>();
//	lineAggregator.setDelimiter(",");
//	BeanWrapperFieldExtractor<Employee> feildExtractor = new BeanWrapperFieldExtractor<Employee>();
//	feildExtractor.setNames(tokens);
//	lineAggregator.setFieldExtractor(feildExtractor);
//	itemwriter.setLineAggregator(lineAggregator);
//	
//	
//	return itemwriter;
//}
	
//	@Bean
//	public ItemWriter<Employee> itemWriter() {
//	
//		return new JdbcBatchItemWriterBuilder<Employee>()
//				.dataSource(datasource)
//				.sql(insert_sql)
//				.beanMapped()
//				.build();
//	}
	
	
	@Bean
	public ItemWriter<Employee> itemWriter() {
	
		return new JsonFileItemWriterBuilder<Employee>()
				.resource( new FileSystemResource("C:\\\\Users\\\\ANKIT\\\\Downloads\\\\out.json"))
				.jsonObjectMarshaller( new JacksonJsonObjectMarshaller<Employee>())
				.name("JsonItemWriter")
				.build();
	}
	
//	
//	@Bean
//	public ItemWriter<Employee> itemWriter() {
//	
//		return new JdbcBatchItemWriterBuilder<Employee>()
//				.dataSource(datasource)
//				.sql(insert_sql)
//				.itemPreparedStatementSetter(new EmployeeitemPreparedStatementSetter())
//				.build();
//	}
//			

	@Bean
	public Job getJob() throws Exception {
		
		return this.jobBuilderFactory.get("job1").start(getChunkBasedStep()).next(getRunTimeStep())
				.build();
	}
	@Bean	
	public Step getRunTimeStep() {
		return this.stepBuilderFactory.get("getRunTimeStep")
				.tasklet(new Tasklet(){

					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
							throws Exception {
						// TODO Auto-generated method stub
						System.out.println("running on "+ LocalDateTime.now());	
						return RepeatStatus.FINISHED;
					}
					
				})
				.build();
}




//	
//	@Bean
//	public Job getStepJob() {
//		
//		return this.jobBuilderFactory.get("stepJob").start(myOnlyStep())
//				.build();
//	}
	
//	@Bean
//	public Flow myFlow() {
//		
//		return new FlowBuilder<SimpleFlow>("myFlow").start(startFirstStep())
//				.next(startSecondStep()).on("FAILED").stop()
//				.from(startSecondStep()).on("*").to(startThirdStep()).on("*").to(getJobExecutionDecider())
//				.on("GO AHEAD")
//				.to(startFourthStep())
//				.from(getJobExecutionDecider()).on("DONT GO AHEAD").to(dontGoAheadStep())
//				.build();
//				
//	}
	
//	@Bean
//	public Step startFirstStep() {
//		
//		return this.stepBuilderFactory.get("FirstStep").job(getStepJob()).build();
//				
//	}
	
	@Bean
	public Step dontGoAheadStep() {
		
		return this.stepBuilderFactory.get("dontGoAheadStep").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				// TODO Auto-generated method stub
//				String item =chunkContext.getStepContext().getJobParameters().get("item").toString();
//				System.out.println(String.format("%s job finished.", item));	
				System.out.println("Dont go ahead");
				return RepeatStatus.FINISHED;
			}
		}).build();
				
	}
	
	@Bean
	public Step myOnlyStep() {
		
		return this.stepBuilderFactory.get("myOnlyStep").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				// TODO Auto-generated method stub
//				String item =chunkContext.getStepContext().getJobParameters().get("item").toString();
//				System.out.println(String.format("%s job finished.", item));	
				System.out.println("This is a first step from a job Step");
				return RepeatStatus.FINISHED;
			}
		}).build();
				
	}
	
	@Bean
	public Step startSecondStep() {
		
		return this.stepBuilderFactory.get("SecondStep").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				
				
				boolean fail=false;
				if (fail) {
					throw new RuntimeException("got some bug");
				}
				// TODO Auto-generated method stub
//				String item =chunkContext.getStepContext().getJobParameters().get("item").toString();
//				System.out.println(String.format("%s job finished.", item));	
				System.out.println("This is the second step");
				return RepeatStatus.FINISHED;
			}
		}).build();
				
	}
	@Bean
	public Step startThirdStep() {

		return this.stepBuilderFactory.get("ThirdStep").tasklet(new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				// TODO Auto-generated method stub
//				String item =chunkContext.getStepContext().getJobParameters().get("item").toString();
//				System.out.println(String.format("%s job finished.", item));	
				System.out.println("This is the third step");
				return RepeatStatus.FINISHED;
			}
		}).build();

	}
	@Bean
	public Step startFourthStep() {

		return this.stepBuilderFactory.get("FourthStep").tasklet(new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				// TODO Auto-generated method stub
//			String item =chunkContext.getStepContext().getJobParameters().get("item").toString();
//			System.out.println(String.format("%s job finished.", item));	
				System.out.println("This is the fourth step");
				return RepeatStatus.FINISHED;
			}
		}).listener(getMyListner()).build();

	}
	
	private StepExecutionListener getMyListner() {
		// TODO Auto-generated method stub
		return new getMyListner();
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
