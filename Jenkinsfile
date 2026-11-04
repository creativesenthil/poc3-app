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
                    echo "===== JAVA VERSION ====="
                    java -version

                    echo "===== JAVAC VERSION ====="
                    javac -version

                    echo "===== MAVEN VERSION ====="
                    mvn -version

                    echo "===== DOCKER VERSION ====="
                    docker --version
                '''
            }
        }

        stage('Maven Build') {
            steps {
                sh '''
                    echo "===== MAVEN BUILD ====="
                    java -version
                    javac -version

                    mvn -B clean package -DskipTests
                '''
            }
        }

        stage('Unit Tests') {
            steps {
                sh '''
                    echo "===== UNIT TESTS ====="
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
                    echo "===== DOCKER BUILD ====="
                    docker build -t poc3-app:${BUILD_NUMBER} .
                '''
            }
        }

        stage('Docker Validation') {
            steps {
                sh '''
                    echo "===== DOCKER VALIDATION ====="
                    docker image inspect poc3-app:${BUILD_NUMBER}
                '''
            }
        }

        stage('Argo CD Deployment') {
            steps {
                sh '''
                    echo "===== ARGO CD DEPLOYMENT ====="

                    if command -v argocd >/dev/null 2>&1; then
                        echo "Argo CD CLI found"

                        argocd app get poc3-app || true

                        echo "Argo CD stage is ready"
                    else
                        echo "Argo CD CLI is not installed yet."
                        echo "Argo CD deployment configuration will be completed after Argo CD installation."
                    fi
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully'
        }

        failure {
            echo 'Pipeline failed'
        }

        always {
            echo 'Pipeline execution completed'
        }
    }
}
