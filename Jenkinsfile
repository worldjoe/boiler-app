pipeline {
   agent any
   env.JAVA_HOME="${tool 'jdk-15.0.2+7'}"
   env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"

   stages {
      stage('Build') {
         steps {
            // Get some code from a GitHub repository
            git 'https://github.com/worldjoe/boiler-app.git'

            // Run Maven on a Unix agent.
            sh "./mvnw -Dmaven.test.failure.ignore=true clean package"

            // To run Maven on a Windows agent, use
            // bat "mvn -Dmaven.test.failure.ignore=true clean package"
         }

         post {
            // If Maven was able to run the tests, even if some of the test
            // failed, record the test results and archive the jar file.
            success {
               junit '**/target/surefire-reports/TEST-*.xml'
               archiveArtifacts 'target/*.jar'
            }
         }
      }
   }
}
