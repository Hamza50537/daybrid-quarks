pipeline {
  environment {
    nexus_registry_password= credentials('nexus-registry-pass')  
    }
    agent any
    tools{
      maven '3.8.5'
      jdk "jdk17"
    }
  stages { 
      stage ('Test'){
      steps{
        sh './mvnw package -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-mandrel:21.3-java17'
        sh 'mvn -version'
        sh 'java -version'
        //sh './mvnw verify'
        //sh './mvnw package -Pnative -Dquarkus.native.container-build=true -Dquarkus.container-image.build=true'
               //sh 'mvn clean package -Dnative -Dquarkus.native.container-build=true \
    //-Dquarkus.native.native-image-xmx=10G'
        //sh 'mvn package -Dnative -Dquarkus.native.native-image-xmx=10G'
        //sh'./mvnw package -Pnative'
        //sh './mvnw package'
        //sh 'mvn clean install -DskipTests --debug' 

      }}
      stage('Build') {
    steps {
        
        sh 'docker build -f src/main/docker/Dockerfile.native -t  registry.hiqs.de/daybird-quarkus-api:${GIT_COMMIT} .'
        
        
       // sh 'docker build -t registry.hiqs.de/flightbedfront-jekins:${GIT_COMMIT} ./src/main/ui'
      }

    }

    stage('Push to Hiqs registry') {

      steps {
        sh 'docker login registry.hiqs.de --username=furqan.iqbal --password=${nexus_registry_password}'
        sh 'docker push registry.hiqs.de/daybird-quarkus-api:${GIT_COMMIT}'

      }

    }   
	  
  }
}