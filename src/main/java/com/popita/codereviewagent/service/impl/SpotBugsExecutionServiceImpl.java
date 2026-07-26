package com.popita.codereviewagent.service.impl;


import com.popita.codereviewagent.service.SpotBugsExecutionService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;


@Service
public class SpotBugsExecutionServiceImpl
        implements SpotBugsExecutionService {


    @Override
    public void runSpotBugs(Path repositoryPath) {


        try {


            ProcessBuilder processBuilder =
                    new ProcessBuilder(
                            "E:\\Spring_boot\\apache-maven-3.9.10-bin\\apache-maven-3.9.10\\bin\\mvn.cmd",
                            "com.github.spotbugs:spotbugs-maven-plugin:4.9.3.0:spotbugs"
                    );


            processBuilder.directory(
                    repositoryPath.toFile()
            );


            processBuilder.redirectErrorStream(true);



            Process process =
                    processBuilder.start();



            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    process.getInputStream()
                            )
                    );


            String line;


            while((line = reader.readLine()) != null){

                System.out.println(line);

            }



            int exitCode =
                    process.waitFor();



            System.out.println(
                    "SpotBugs Exit Code : "
                            + exitCode
            );



        } catch(Exception e){

            throw new RuntimeException(
                    "SpotBugs execution failed",
                    e
            );

        }

    }
}