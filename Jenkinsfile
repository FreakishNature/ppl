node{
    def mvnHome = tool 'maven_home'
    stages{
        stage('Test'){
            sh '${mvnHome}/bin/mvn test'
        }
    }
}