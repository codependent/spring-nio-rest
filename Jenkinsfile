pipeline {
  agent any
  tools { 
    maven 'M3' 
    jdk 'JDK8' 
  }
  stages {
    stage ('Build Application') {
      steps {
        echo 'Building application'
        sh '''
           echo "PATH = ${PATH}"
           echo "M2_HOME = ${M2_HOME}"
           ''' 
      }
    }
    stage ('Finish') {
      steps {
        echo 'Fninishing application'
      }
    }
  }
}
