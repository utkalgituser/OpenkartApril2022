pipeline{
    
    agent any
    stages{
        
        stage("Build"){
            steps{
            echo("build the project")
            }
        }
        
        stage("Deploy on DEV env"){
            steps{
            echo("Deploy on dev env")
            }
        }
        
        stage("Deploy on QA env"){
            steps{
            echo("Deploy on qa env")
            }
        }

        stage("Run regression TC on QA env"){
            steps{
            echo("running regression TC on QA env")
            }
        }    

        stage("Deploy on stage env"){
            steps{
            echo("Deploy on stage env")
            }
        }

        stage("Run sanity TC on satge env"){
            steps{
            echo("running sanity TC on satge env")
            }
        }   
        
        stage("Deploy on PROD env"){
            steps{
            echo("Deploy on PROD env")
            }
        }        
    }
}