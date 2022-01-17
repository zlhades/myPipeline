package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class MyPipelineApp {
    public static void main(final String[] args) {
        App app = new App();

        new MyPipelineStack(app, "PipelineStack", StackProps.builder()
                .env(Environment.builder()
                        .account("759640244333")
                        .region("us-east-1")
                        .build())
                .build());

        app.synth();
    }
}