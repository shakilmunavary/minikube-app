pipeline {
    agent any

    environment {
        IMAGE_NAME      = "shakilahamed/minikube-app"
        IMAGE_TAG       = "latest"
        DEPLOYMENT_NAME = "minikube-app"
        SERVICE_NAME    = "minikube-app-service"
        KUBE_NAMESPACE  = "default"
        KUBE_MANIFEST   = "k8/minikube.yaml"
        KUBECONFIG_PATH = "/home/rba/minikube/config"
        KUBE_CONTEXT    = "minikube"

        // ✅ Sonar Configuration (NO Jenkins global config required)
        SONAR_HOST = "https://sop-testing-alb-2059918749.us-west-2.elb.amazonaws.com/sonarqube"
        SONAR_PROJECT_KEY = "rba-test-project"
        SONAR_TOKEN = credentials('sonarqube_token')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/shakilmunavary/minikube-app.git'
            }
        }

        stage('Cleanup Docker') {
            steps {
                script {
                    echo "🧹 Cleaning up Docker environment..."
                    sh '''
                        docker rm -f $(docker ps -aq) || true
                        docker rmi -f $(docker images -q) || true
                        docker system prune -af || true
                    '''
                }
            }
        }

        stage('Build App') {
            steps {
                script {
                    echo "⚙️ Building Java app with Maven..."
                    sh "mvn clean package -DskipTests"
                }
            }
        }

                stage('Sonar Analysis') {
                            steps {
                                script {
                                    echo "🔍 Running SonarQube Analysis..."
                
                                    // Using double quotes (""") lets Jenkins evaluate variables like ${SONAR_TOKEN}
                                    sh """
                                            mvn sonar:sonar \
                                          -Dmaven.wagon.http.ssl.allowall=true \
                                          -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                                          -Dsonar.host.url=${SONAR_HOST} \
                                          -Dsonar.token=${SONAR_TOKEN} \
                                          -Dsonar.scanner.skipCertificateValidation=true
                                    """
                                }
                            }
                        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "🔨 Building Docker image..."
                    sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    echo "📦 Pushing Docker image to DockerHub..."
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-creds',
                                                     usernameVariable: 'DOCKER_USER',
                                                     passwordVariable: 'DOCKER_PASS')]) {
                        sh """
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push ${IMAGE_NAME}:${IMAGE_TAG}
                        """
                    }
                }
            }
        }

        stage('Deploy to Minikube') {
            steps {
                script {
                    echo "🚀 Deploying to Minikube..."
                    sh """
                    export KUBECONFIG=${KUBECONFIG_PATH}
                    kubectl config use-context ${KUBE_CONTEXT}

                    echo "Deleting old deployment and service..."
                    kubectl delete deployment ${DEPLOYMENT_NAME} --ignore-not-found -n ${KUBE_NAMESPACE}
                    kubectl delete svc ${SERVICE_NAME} --ignore-not-found -n ${KUBE_NAMESPACE}

                    echo "Recreating deployment and service..."
                    kubectl apply -f ${KUBE_MANIFEST} -n ${KUBE_NAMESPACE}

                    echo "Waiting for rollout..."
                    kubectl rollout status deployment/${DEPLOYMENT_NAME} -n ${KUBE_NAMESPACE}

                    echo "Service info:"
                    kubectl get svc ${SERVICE_NAME} -n ${KUBE_NAMESPACE}
                    """
                }
            }
        }
    }

    post {
        success {
            echo "✅ Pipeline completed successfully"
        }

        failure {
            echo "❌ Pipeline failed"
        }
    }
}
