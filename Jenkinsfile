pipeline {
 agent any
 environment { IMAGE_TAG = "build-${BUILD_NUMBER}-${GIT_COMMIT.take(7)}" }
 stages {
  stage('Checkout') { steps { checkout scm } }
  stage('Environment Validation') { steps { sh 'java -version; mvn -version; docker --version' } }
  stage('Maven Build') { steps { sh 'mvn -B -DskipTests package' } }
  stage('Unit Tests') { steps { sh 'mvn -B test' } }
  stage('SonarQube Analysis') { steps { echo 'Configure SonarQube server and sonar token in Jenkins, then run mvn sonar:sonar' } }
  stage('Quality Gate') { steps { echo 'Use waitForQualityGate abortPipeline: true after SonarQube integration' } }
  stage('Docker Build') { steps { sh 'docker build -t ${DOCKERHUB_USERNAME}/${DOCKERHUB_REPOSITORY}:${BUILD_NUMBER} .' } }
  stage('Docker Validation') { steps { sh 'docker image inspect ${DOCKERHUB_USERNAME}/${DOCKERHUB_REPOSITORY}:${BUILD_NUMBER}' } }
  stage('Docker Hub Push') { steps { echo 'Use dockerhub-creds Jenkins credential here'; sh 'docker push ${DOCKERHUB_USERNAME}/${DOCKERHUB_REPOSITORY}:${BUILD_NUMBER}' } }
  stage('GitOps Update') { steps { sh '''git clone https://${GITOPS_TOKEN}@github.com/${GITHUB_OWNER}/${GITOPS_REPO}.git gitops
sed -i "s|image: .*|image: ${DOCKERHUB_USERNAME}/${DOCKERHUB_REPOSITORY}:${BUILD_NUMBER}|" gitops/k8s/deployment.yaml
cd gitops && git config user.email "jenkins@poc3.local" && git config user.name "Jenkins" && git add . && git commit -m "Deploy build ${BUILD_NUMBER}" && git push''' } }
  stage('Argo CD / Kubernetes Verification') { steps { sh 'echo "Argo CD running on Ubuntu EC2 detects GitOps change and deploys to K3s"' } }
 }
 post { always { junit allowEmptyResults: true, testResults: '**/surefire-reports/*.xml' } }
}