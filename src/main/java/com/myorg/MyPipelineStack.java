package com.myorg;

import java.util.Arrays;

import software.amazon.awscdk.pipelines.*;
import software.constructs.Construct;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.StageProps;

public class MyPipelineStack extends Stack {
    public MyPipelineStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public MyPipelineStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        final CodePipeline pipeline = CodePipeline.Builder.create(this, "pipeline")
                .pipelineName("MyPipeline")
                .synth(ShellStep.Builder.create("Synth")
                        .input(CodePipelineSource.connection("zlhades/myPipeline", "master",ConnectionSourceOptions.builder()
                                .connectionArn("arn:aws:codestar-connections:us-east-1:759640244333:connection/fb1d14e9-8166-4f25-8554-b734ed01a374")
                                .build()))
                        .commands(Arrays.asList("npm install -g aws-cdk", "cdk synth"))
                        .build())
                .build();

//        pipeline.addStage(new MyPipelineAppStage(this, "test", StageProps.builder()
//                .env(Environment.builder()
//                        .account("759640244333")
//                        .region("eu-west-1")
//                        .build())
//                .build()));


        StageDeployment testingStage =
                pipeline.addStage(new MyPipelineAppStage(this, "test", StageProps.builder()
                        .env(Environment.builder()
                                .account("759640244333")
                                .region("us-east-1")
                                .build())
                        .build()));

        testingStage.addPost(new ManualApprovalStep("approval"));

        testingStage.addPost(ShellStep.Builder.create("validate")
                .commands(Arrays.asList("curl -Ssf https://my.webservice.com/"))
                .build());

    }
}