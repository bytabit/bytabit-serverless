## GET /version

resource "aws_lambda_function" "get_version" {
  function_name = "${var.app_stage}_get_version"

  # The bucket name as created earlier with "aws s3api create-bucket"
  s3_bucket = "bytabit-serverless"
  s3_key = "v${var.app_version}/bytabit-serverless.zip"

  # "main" is the filename within the zip file (main.js) and "handler"
  # is the name of the property under which the handler function was
  # exported in that file.
  handler = "com.bytabit.serverless.common.GetVersionHandler"
  runtime = "java8"

  role = aws_iam_role.lambda_exec.arn

  depends_on = [
    "aws_iam_role_policy_attachment.lambda_logs",
    "aws_cloudwatch_log_group.get_version"]

}

# Configure log retention
resource "aws_cloudwatch_log_group" "get_version" {
  name = "/aws/lambda/${var.app_stage}_get_version"
  retention_in_days = "${var.log_retention_in_days}"

  tags = {
    Environment = "${var.app_stage}"
    Application = "${var.root_domain_name}"
  }
}

## PUT /offers/{id}


# IAM role which dictates what other AWS services the Lambda function
# may access.
resource "aws_iam_role" "lambda_exec" {
  name = "lambda_exec"

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
resource "aws_iam_policy" "lambda_logging" {
  name = "lambda_logging"
  path = "/"
  description = "IAM policy for logging from a lambda"

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

resource "aws_iam_role_policy_attachment" "lambda_logs" {
  role = "${aws_iam_role.lambda_exec.name}"
  policy_arn = "${aws_iam_policy.lambda_logging.arn}"
}