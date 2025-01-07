#!/usr/bin/groovy

@Library('pipeascode@18.2.0')
@Library('jenkins-shared-utils@0.0.1')

import es.santander.vostok.pipeascode.pipeline.PipelineAbstract;
import es.santander.vostok.pipeascode.pipeline.PipelineFactory;
import es.santander.vostok.pipeascode.stage.AbstractStage;

HashMap mapParameters = new HashMap();
String pipelineType = 'JAVA17-LIBRARY';
mapParameters.put('PIPELINE_TYPE', pipelineType);

HashMap<String, String> stagesPerContainer = new HashMap<>();
stagesPerContainer.put('build','jnlp');
stagesPerContainer.put('static analysis','jnlp');
stagesPerContainer.put('publish artifact','jnlp');


podTemplate(containers: [containerTemplate(
                name: 'jnlp',
                image: 'registry.global.ccc.srvb.bo.paas.cloudcenter.corp/san-vostok-san/base-java-multi:2.0.0',
                args: '/var/jenkins_config/jenkins-agent',
                command: '/bin/sh',
                ttyEnabled: true,
                alwaysPullImage: false,
                workingDir: '/home/jenkins/workspace',
                resourceRequestCpu: '150m',
                resourceLimitCpu: '1500m',
                resourceRequestMemory: '3277Mi',
                resourceLimitMemory: '4096Mi')
        ],
        yaml:'''
---
apiVersion: "v1"
kind: "Pod"
metadata: {}
spec:
  containers:
  - command:
    - "/bin/bash"
    env:
    - name: "GIT_SSL_NO_VERIFY"
      value: "1"
    - name: "JENKINS_AGENT_WORKDIR"
      value: "/home/jenkins/workspace"
    - name: "no_proxy"
      value: ".localhost,.corp,.bsch,.local"
    - name: "http_proxy"
      value: "http://proxyapp.cloudcenter.corp:8080"
    - name: "https_proxy"
      value: "http://proxyapp.cloudcenter.corp:8080"
    image: "registry.global.ccc.srvb.bo.paas.cloudcenter.corp/san-vostok-san/base-java-multi:2.0.0"
    imagePullPolicy: "Always"
    name: "jnlp"
    resources:
      limits:
        memory: "4096Mi"
        cpu: "1500m"
      requests:
        memory: "3277Mi"
        cpu: "150m"
    securityContext:
      privileged: false
      runAsUser: 1000
    tty: true
  hostNetwork: false
  nodeSelector:
    beta.kubernetes.io/os: "linux"
  restartPolicy: "Never"
status: {}

'''
) {

    node (env.POD_LABEL) {

        properties([
                parameters([
                        string(name: 'NEW_VERSION', defaultValue: 'timestamp', description: 'Version of artifact in nexus. Default value for timestamp is yyyy-MM-dd-HH-mm.',required: true ),
                        choice(name: 'RELEASE_CANDIDATE', choices: ['SNAPSHOT', 'RELEASE'], description: 'The version generated for an artifact of type RELEASE must be unique.',required: true )
                ])
        ])

        PipelineAbstract.loadConfiguration(this, mapParameters);
        PipelineAbstract pipe = PipelineFactory.getPipeline(this,mapParameters);

        def listStages = pipe.listStages;
        for (AbstractStage stageInstance : listStages) {
            def currentStage = stageInstance.name;
            stage(stageInstance.name) {
                if (stagesPerContainer.containsKey(currentStage)) {
                    container(name: stagesPerContainer.get(currentStage)) {
                        echo ("running on: " + stagesPerContainer.get(currentStage));
                        stageInstance.execute(mapParameters);
                    }
                } else {
                    echo ("running on: primary container");
                    stageInstance.execute(mapParameters);

                }
            }
        }
    }
}
