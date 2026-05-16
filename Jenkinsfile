pipeline {
    agent any

    environment {
        IMAGE_NAME      = "shakilahamed/minikube-app"
        IMAGE_TAG       = "latest"
        DEPLOYMENT_NAME = "minikube-app"
        KUBE_NAMESPACE  = "default"
        KUBE_MANIFEST   = "k8/minikube.yaml"
        KUBECONFIG_PATH = "/home/jenkins/.kube/config" // Jenkins user must have kubeconfig
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/shakilmunavary/minikube-app.git'
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
                    kubectl config use-context minikube

                    # Apply manifests
                    kubectl apply -f ${KUBE_MANIFEST} --namespace=${KUBE_NAMESPACE}

                    # Verify deployment rollout
                    kubectl rollout status deployment/${DEPLOYMENT_NAME} -n ${KUBE_NAMESPACE}

                    # Show service info
                    kubectl get svc -n ${KUBE_NAMESPACE}
                    """
                }
            }
        }
    }
}
