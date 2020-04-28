package com.mst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*@MapperScan(basePackages = {"com.mst.service","com.mst.dao"})*/
@SpringBootApplication
public class RunBoot{

	   public static void main(String[] args) throws Exception {

	      SpringApplication.run(RunBoot.class, args);

	   }


	    

}
