pipeline {
    agent any

    environment {
        MAVEN_OPTS = '-Xmx384m'
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
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
                    echo "=== Environment Validation ==="
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
                    echo "=== Maven Build ==="
                    java -version
                    javac -version
                    mvn -B -DskipTests package
                '''
            }
        }

        stage('Unit Tests') {
            steps {
                sh '''
                    echo "=== Unit Tests ==="
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
                sh '''
                    docker build -t poc3-app:${BUILD_NUMBER} .
                '''
            }
        }

        stage('Docker Validation') {
            steps {
                sh '''
                    docker image inspect poc3-app:${BUILD_NUMBER}
                '''
            }
        }

        stage('Argo CD Sync') {
            steps {
                sh '''
                    echo "=== Argo CD Sync ==="
                    argocd app sync poc3-app
                '''
            }
        }

        stage('Argo CD Wait') {
            steps {
                sh '''
                    echo "=== Waiting for Argo CD deployment ==="
                    argocd app wait poc3-app --health --sync --timeout 300
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully and application deployment was triggered through Argo CD'
        }

        failure {
            echo 'Pipeline failed. Check the failed stage above.'
        }

        always {
            echo 'Pipeline execution completed'
        }
    }
}
