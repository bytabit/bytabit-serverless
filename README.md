
## Setup aws

Install aws cli tool and configure with bytabit access key id, secret key, region 'us-east-1', default output 'json'.

```bash
$ brew install awscli
$ aws configure
```

## Setup Terraform

Install terraform if not already installed.

```bash
$ brew install terraform
```

If this is the first time you've used terraform for this project, init project, 
select and refresh workspaces.

```bash
$ cd terraform
$ terraform init
$ terraform workspace select regtest
$ terraform refresh
$ terraform workspace select test
$ terraform refresh
```

## Build Application

Build and zip application jar and it's dependencies and store on aws S3. For now manually set version (eg. v0.4.0).

```bash
$ ./gradlew buildZip # to build the application jar 
$ aws s3 cp build/distributions/bytabit-serverless.zip s3://bytabit-serverless/v0.4.0/bytabit-serverless.zip
```

## Deploy Application with Terraform

In order to deploy the application APIs select the workspace (regtest or test) and apply the terraform config files.

REGTEST
```bash
$ cd terraform
$ terraform workspace select regtest
$ terraform apply -var="app_version=0.4.0" # manually set version, see above
```

TEST
```bash
$ cd terraform
$ terraform workspace select test
$ terraform apply -var="app_version=0.4.0" # manually set version, see above
```

## Verify Application is Deployed

```bash
$ curl get https://regtest.bytabit.net/version
$ curl get https://test.bytabit.net/version
```