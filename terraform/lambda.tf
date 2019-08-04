## GET /version

resource "aws_lambda_function" "get_version" {
  function_name = "${terraform.workspace}_get_version"

  # The bucket name as created earlier with "aws s3api create-bucket"
  s3_bucket = "bytabit-serverless"
  s3_key = "v${var.app_version}/bytabit-serverless.zip"

  # "main" is the filename within the zip file (main.js) and "handler"
  # is the name of the property under which the handler function was
  # exported in that file.
  handler = "com.bytabit.serverless.common.GetVersionHandler"
  runtime = "java8"
  timeout = 5

  role = aws_iam_role.lambda_exec.arn

  depends_on = [
    "aws_iam_role_policy.lambda_logging",
    "aws_cloudwatch_log_group.get_version"]
}

# Configure log retention
resource "aws_cloudwatch_log_group" "get_version" {
  name = "/aws/lambda/${terraform.workspace}_get_version"
  retention_in_days = "${var.log_retention_in_days}"

  tags = {
    Stage = terraform.workspace
  }
}

## PUT badges/{profilePubKey}/{id}

resource "aws_lambda_function" "put_badge" {
  function_name = "${terraform.workspace}_put_badge"

  # The bucket name as created earlier with "aws s3api create-bucket"
  s3_bucket = "bytabit-serverless"
  s3_key = "v${var.app_version}/bytabit-serverless.zip"

  # "main" is the filename within the zip file (main.js) and "handler"
  # is the name of the property under which the handler function was
  # exported in that file.
  handler = "com.bytabit.serverless.badge.PutBadgeHandler"
  runtime = "java8"
  memory_size = 256
  timeout = 10

  role = aws_iam_role.lambda_exec.arn

  environment {
    variables = {
      BADGE_TABLE = aws_dynamodb_table.badge.name
    }
  }

  depends_on = [
    "aws_iam_role_policy.lambda_logging",
    "aws_iam_role_policy.lambda_dynamo",
    "aws_cloudwatch_log_group.put_badge",
    "aws_dynamodb_table.badge"]
}

# Configure log retention
resource "aws_cloudwatch_log_group" "put_badge" {
  name = "/aws/lambda/${terraform.workspace}_put_badge"
  retention_in_days = "${var.log_retention_in_days}"

  tags = {
    Stage = terraform.workspace
  }
}

## GET badges/{profilePubKey}

resource "aws_lambda_function" "get_badge" {
  function_name = "${terraform.workspace}_get_badge"

  # The bucket name as created earlier with "aws s3api create-bucket"
  s3_bucket = "bytabit-serverless"
  s3_key = "v${var.app_version}/bytabit-serverless.zip"

  # "main" is the filename within the zip file (main.js) and "handler"
  # is the name of the property under which the handler function was
  # exported in that file.
  handler = "com.bytabit.serverless.badge.GetBadgeHandler"
  runtime = "java8"
  memory_size = 256
  timeout = 10

  role = aws_iam_role.lambda_exec.arn

  environment {
    variables = {
      BADGE_TABLE = aws_dynamodb_table.badge.name
    }
  }

  depends_on = [
    "aws_iam_role_policy.lambda_logging",
    "aws_iam_role_policy.lambda_dynamo",
    "aws_cloudwatch_log_group.get_badge",
    "aws_dynamodb_table.badge"]
}

# Configure log retention
resource "aws_cloudwatch_log_group" "get_badge" {
  name = "/aws/lambda/${terraform.workspace}_get_badge"
  retention_in_days = "${var.log_retention_in_days}"

  tags = {
    Stage = terraform.workspace
  }
}

## PUT /offers/{id}

resource "aws_lambda_function" "put_offer" {
  function_name = "${terraform.workspace}_put_offer"

  # The bucket name as created earlier with "aws s3api create-bucket"
  s3_bucket = "bytabit-serverless"
  s3_key = "v${var.app_version}/bytabit-serverless.zip"

  # "main" is the filename within the zip file (main.js) and "handler"
  # is the name of the property under which the handler function was
  # exported in that file.
  handler = "com.bytabit.serverless.offer.PutOfferHandler"
  runtime = "java8"
  memory_size = 256
  timeout = 10

  role = aws_iam_role.lambda_exec.arn

  environment {
    variables = {
      OFFER_TABLE = aws_dynamodb_table.offer.name
      BADGE_TABLE = aws_dynamodb_table.badge.name
    }
  }

  depends_on = [
    "aws_iam_role_policy.lambda_logging",
    "aws_iam_role_policy.lambda_dynamo",
    "aws_cloudwatch_log_group.put_offer",
    "aws_dynamodb_table.offer",
    "aws_dynamodb_table.badge"]
}

# Configure log retention
resource "aws_cloudwatch_log_group" "put_offer" {
  name = "/aws/lambda/${terraform.workspace}_put_offer"
  retention_in_days = "${var.log_retention_in_days}"

  tags = {
    Stage = terraform.workspace
  }
}

# IAM role which dictates what other AWS services the Lambda function
# may access.
resource "aws_iam_role" "lambda_exec" {
  name = "${terraform.workspace}_lambda_exec"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

# See also the following AWS managed policy: AWSLambdaBasicExecutionRole
resource "aws_iam_role_policy" "lambda_logging" {
  name = "${terraform.workspace}_lambda_logging"
  role = aws_iam_role.lambda_exec.name

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
        {
            "Action": [
                "logs:CreateLogGroup",
                 "logs:CreateLogStream",
                 "logs:PutLogEvents"
            ],
            "Effect": "Allow",
            "Resource": "arn:aws:logs:*:*:*"
        }
    ]
}
EOF
}

# See also the following AWS managed policy: AWSLambdaBasicExecutionRole
resource "aws_iam_role_policy" "lambda_dynamo" {
  name = "${terraform.workspace}_lambda_dynamo"
  role = aws_iam_role.lambda_exec.id

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
        {
            "Action": [
                "dynamodb:*"
            ],
            "Effect": "Allow",
            "Resource": [
              "${aws_dynamodb_table.badge.arn}",
              "${aws_dynamodb_table.offer.arn}"
            ]
        }
    ]
}
EOF
}