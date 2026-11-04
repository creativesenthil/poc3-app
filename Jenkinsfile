pipeline {
    agent any

    environment {
        MAVEN_OPTS = '-Xmx384m'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Environment Validation') {
            steps {
                sh '''
                    java -version
                    javac -version
                    mvn -version
                    docker --version
                '''
            }
        }

        stage('Maven Build') {
            steps {
                sh '''
                    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
                    export PATH=$JAVA_HOME/bin:$PATH

                    java -version
                    javac -version
                    mvn -B -DskipTests package
                '''
            }
        }

        stage('Unit Tests') {
            steps {
                sh '''
                    export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
                    export PATH=$JAVA_HOME/bin:$PATH

                    java -version
                    javac -version
                    mvn -B test
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'SonarQube integration will be configured next'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t poc3-app:${BUILD_NUMBER} .'
            }
        }

        stage('Docker Validation') {
            steps {
                sh 'docker image inspect poc3-app:${BUILD_NUMBER}'
            }
        }
    }

    post {
        always {
            echo 'Pipeline execution completed'
        }
    }
}
