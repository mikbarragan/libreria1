#!/usr/bin/env groovy

// Documentation: https://confluence.alm.europe.cloudcenter.corp/display/C3AL/Pipelines+Introduction

@Library(['jenkins-shared-utils',
          'jenkinsfile-shared-libraries']) _

def config = readYaml text: """
---
  PROJECT_NAME: 'san-portaladn360'
  APP_NAME: 'adn360-core'
  APP_TYPE: 'Maven'
  DEPLOYMENT_TYPE: 'LIB'
  BLUEGREEN: 'OFF'
  CORPORATION: 'DARWIN'
  CB_MASTER: 'sanes-adn360'
  SONAR_PRJ_KEY: 'SANES:DARWIN:san-portaladn360:adn360-core'
  DEBUG_MODE: '0'
  LOG_LEVEL: 'INFO'
  """

config.keySet().each{
    env."${it}" = config[it]
}

"${pipelineForTechBranch(env)}"(config)