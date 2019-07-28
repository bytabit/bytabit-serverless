output "base_url" {
  value = aws_api_gateway_deployment.serverless.invoke_url
}

resource "aws_api_gateway_rest_api" "bytabit_serverless" {
  name = "${var.app_stage}_bytabit_serverless"
  description = "Bytabit Serverless APIs"
}

## GET /version

resource "aws_api_gateway_resource" "version" {
  rest_api_id = aws_api_gateway_rest_api.bytabit_serverless.id
  parent_id = aws_api_gateway_rest_api.bytabit_serverless.root_resource_id
  path_part = "version"
}

resource "aws_api_gateway_method" "get_version" {
  rest_api_id = aws_api_gateway_rest_api.bytabit_serverless.id
  resource_id = aws_api_gateway_resource.version.id
  http_method = "GET"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "get_version" {
  rest_api_id = aws_api_gateway_rest_api.bytabit_serverless.id
  resource_id = aws_api_gateway_method.get_version.resource_id
  http_method = aws_api_gateway_method.get_version.http_method

  integration_http_method = "POST"
  type = "AWS_PROXY"
  uri = aws_lambda_function.get_version.invoke_arn
}

## PUT /offers/{id}



resource "aws_api_gateway_base_path_mapping" "api" {
  api_id = "${aws_api_gateway_rest_api.bytabit_serverless.id}"
  stage_name = "${aws_api_gateway_deployment.serverless.stage_name}"
  domain_name = "${aws_api_gateway_domain_name.api.domain_name}"
}

resource "aws_api_gateway_deployment" "serverless" {
  depends_on = [
    "aws_api_gateway_integration.get_version"
  ]

  rest_api_id = aws_api_gateway_rest_api.bytabit_serverless.id
  stage_name = var.app_stage
}

resource "aws_lambda_permission" "apigw" {
  statement_id = "AllowAPIGatewayInvoke"
  action = "lambda:InvokeFunction"
  function_name = "${aws_lambda_function.get_version.arn}"
  principal = "apigateway.amazonaws.com"

  # The /*/* portion grants access from any method on any resource
  # within the API Gateway "REST API".
  source_arn = "${aws_api_gateway_deployment.serverless.execution_arn}/*/*"
}
