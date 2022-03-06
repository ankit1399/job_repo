package com.example.job.demo;

import java.io.Serializable;
import java.util.List;
import org.springframework.batch.core.Job;
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
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;


@SpringBootApplication
@EnableBatchProcessing
public class DemoApplication {
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	
	@Bean
	public JobExecutionDecider getJobExecutionDecider() {
		
		return new myJobExecutionDecider();
		
	}
	
	public static String[] tokens= new String[] {"id",	"desc",	"name","id2",	"id3","id4","id5","abc","company","id7"};
			
	@Bean
	public ItemReader<Employee> getMyItemReader() {
		
		FlatFileItemReader<Employee> itemReader= new FlatFileItemReader<Employee>();
		itemReader.setLinesToSkip(1);
		itemReader.setResource(new FileSystemResource("C:\\Users\\ANKIT\\Downloads\\Sample-Spreadsheet-10-rows.csv"));
		DefaultLineMapper<Employee> lineMapper= new DefaultLineMapper<Employee>();
		DelimitedLineTokenizer tokenizer= new DelimitedLineTokenizer();
		tokenizer.setNames(tokens);
		lineMapper.setLineTokenizer(tokenizer);
		lineMapper.setFieldSetMapper(new getSetFieldSetMapper());
		itemReader.setLineMapper(lineMapper);
		
		
		return itemReader;
		
	}
	
	
	@Bean
	public Step getChunkBasedStep() {
		
		return this.stepBuilderFactory.get("ChunkBasedStep").<Employee,Employee>chunk(3).reader(getMyItemReader()).writer(new ItemWriter<Employee>() {

			@Override
			public void write(List<? extends Employee> items) throws Exception {
				// TODO Auto-generated method stub
				items.forEach(k -> System.out.println(k.getAbc()+" "+k.getId()+" "+k.getName()+" "+k.getId5()+" "+k.getId4()+" "+k.getId3()));;
				
			}
			
			
		}).build();
	}
	
	@Bean
	public Job getJob() {
		
		return this.jobBuilderFactory.get("job1").start(getChunkBasedStep())
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
