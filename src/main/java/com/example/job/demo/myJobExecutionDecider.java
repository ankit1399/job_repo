package com.example.job.demo;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class myJobExecutionDecider implements JobExecutionDecider {

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		// TODO Auto-generated method stub
		boolean a =true;
		String string = a==true ? "GO AHEAD" : "DONT GO AHEAD";
		System.out.println("order is to "+ string );
		return  new FlowExecutionStatus(string);
	}

}
