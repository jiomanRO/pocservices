# Microservices application stack project on Google Cloud
Application stack adheres to the ["twelve-factor app"](https://12factor.net/) paradigm and the microservices architecture:
- frontend - stateless frontservice application
  - implements Spring Session functionality using Redis as session cache
  - implements restricted access (number of requests to backend quoteservice per minute) for unauthenticated users on /quote endpoint
  - unlimited access for authenticated (admin, user) users to /unlimitedQuote and /unlimitedQuoteB endpoints
    - different implementations using synchron (blocking) RestTemplate or reactive approach
- Redis
  - used as a HTTP session cache for Spring Session objects
  - used as a database for tracking information for user accesses to quoteservice
- backend - stateless quoteservice application
  - connects to database and fetches "quotes"
- PostgreSQL database
  - persistent data repository
  
## Prerequisities for deploying on Google Cloud
- Google Cloud user with access to a project and necessary rights
- GKE cluster created
- Redis (Memorystore -> Redis) instance created in same zone as GKE cluster
- PostgreSQL (Cloud SQL) database instance created
- service account (IAM & Admin -> Service Accounts) created with Role that grants access to Cloud SQL
- [recommended] Google Cloud SDK installed locally (https://cloud.google.com/sdk/docs/install)
  - additionally install kubectl and configure for GKE cluster
  ```
  gcloud components install kubectl
  ```
  - configure kubectl to connect to GKE cluster (https://cloud.google.com/kubernetes-engine/docs/how-to/cluster-access-for-kubectl)
  ```
  gcloud container clusters get-credentials <cluster_name> --zone=<zone_name>
  ```
  - additionally install docker-credential-gcr
  ```
  gcloud components install docker-credential-gcr
  ```
  - additionally install skaffold
  ```
  gcloud components install skaffold
  ```
- maven
  
## Deploy the project
Clone project
```
git clone
```
For each service do:
```
cd <service dir>
skaffold dev - to trigger the watch loop build & deploy workflow with cleanup on exit
skaffold run - to build & deploy once
```

## Check functionality
Check deployments in Google Cloud (Kubernetes Engine -> Workloads)
Wait for External load balancer to be created and an external IP to be assigned.
Check endpoints:
- no authentication http://External_IP:8080/quote
- with Basic Authentication http://External_IP:8080/unlimitedQuote and http://External_IP:8080/unlimitedQuoteB
