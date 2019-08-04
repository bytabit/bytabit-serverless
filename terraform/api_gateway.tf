## output info about apis

output "base_url" {
  value = aws_api_gateway_deployment.bytabit.invoke_url
}

## toplevel rest api

resource "aws_api_gateway_rest_api" "bytabit" {
  name = "${terraform.workspace}_bytabit_serverless"
  description = "Bytabit Serverless APIs"
}

## GET /version

resource "aws_api_gateway_resource" "version" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  parent_id = aws_api_gateway_rest_api.bytabit.root_resource_id
  path_part = "version"
}

resource "aws_api_gateway_method" "get_version" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  resource_id = aws_api_gateway_resource.version.id
  http_method = "GET"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "get_version" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  resource_id = aws_api_gateway_method.get_version.resource_id
  http_method = aws_api_gateway_method.get_version.http_method

  integration_http_method = "POST"
  type = "AWS_PROXY"
  uri = aws_lambda_function.get_version.invoke_arn
}

resource "aws_lambda_permission" "get_version" {
  statement_id = "AllowAPIGatewayInvoke"
  action = "lambda:InvokeFunction"
  function_name = aws_lambda_function.get_version.arn
  principal = "apigateway.amazonaws.com"

  # The /*/* portion grants access from any method on any resource
  # within the API Gateway "REST API".
  source_arn = "${aws_api_gateway_deployment.bytabit.execution_arn}/*/*"
}

## PUT badges/{profilePubKey}/{id}

resource "aws_api_gateway_resource" "badges" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  parent_id = aws_api_gateway_rest_api.bytabit.root_resource_id
  path_part = "badges"
}

resource "aws_api_gateway_resource" "badge_profilepubkey" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  parent_id = aws_api_gateway_resource.badges.id
  path_part = "{profilePubKey}"
}

resource "aws_api_gateway_resource" "badge_id" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  parent_id = aws_api_gateway_resource.badge_profilepubkey.id
  path_part = "{id}"
}

resource "aws_api_gateway_method" "put_badge" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  resource_id = aws_api_gateway_resource.badge_id.id
  http_method = "PUT"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "put_badge" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  resource_id = aws_api_gateway_method.put_badge.resource_id
  http_method = aws_api_gateway_method.put_badge.http_method

  integration_http_method = "POST"
  type = "AWS_PROXY"
  uri = aws_lambda_function.put_badge.invoke_arn
}

resource "aws_lambda_permission" "put_badge" {
  statement_id = "AllowAPIGatewayInvoke"
  action = "lambda:InvokeFunction"
  function_name = aws_lambda_function.put_badge.arn
  principal = "apigateway.amazonaws.com"

  # The /*/* portion grants access from any method on any resource
  # within the API Gateway "REST API".
  source_arn = "${aws_api_gateway_deployment.bytabit.execution_arn}/*/*"
}

## GET /offers/{profilePubKey}

resource "aws_api_gateway_method" "get_badge" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  resource_id = aws_api_gateway_resource.badge_profilepubkey.id
  http_method = "GET"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "get_badge" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  resource_id = aws_api_gateway_method.get_badge.resource_id
  http_method = aws_api_gateway_method.get_badge.http_method

  integration_http_method = "POST"
  type = "AWS_PROXY"
  uri = aws_lambda_function.get_badge.invoke_arn
}

resource "aws_lambda_permission" "get_badge" {
  statement_id = "AllowAPIGatewayInvoke"
  action = "lambda:InvokeFunction"
  function_name = aws_lambda_function.get_badge.arn
  principal = "apigateway.amazonaws.com"

  # The /*/* portion grants access from any method on any resource
  # within the API Gateway "REST API".
  source_arn = "${aws_api_gateway_deployment.bytabit.execution_arn}/*/*"
}

## PUT /offers/{id}

resource "aws_api_gateway_resource" "offers" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  parent_id = aws_api_gateway_rest_api.bytabit.root_resource_id
  path_part = "offers"
}

resource "aws_api_gateway_resource" "offer_id" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  parent_id = aws_api_gateway_resource.offers.id
  path_part = "{id}"
}

resource "aws_api_gateway_method" "put_offer" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  resource_id = aws_api_gateway_resource.offer_id.id
  http_method = "PUT"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "put_offer" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  resource_id = aws_api_gateway_method.put_offer.resource_id
  http_method = aws_api_gateway_method.put_offer.http_method

  integration_http_method = "POST"
  type = "AWS_PROXY"
  uri = aws_lambda_function.put_offer.invoke_arn
}

resource "aws_lambda_permission" "put_offer" {
  statement_id = "AllowAPIGatewayInvoke"
  action = "lambda:InvokeFunction"
  function_name = aws_lambda_function.put_offer.arn
  principal = "apigateway.amazonaws.com"

  # The /*/* portion grants access from any method on any resource
  # within the API Gateway "REST API".
  source_arn = "${aws_api_gateway_deployment.bytabit.execution_arn}/*/*"
}

## GET /offers

resource "aws_api_gateway_method" "getAll_offer" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  resource_id = aws_api_gateway_resource.offers.id
  http_method = "GET"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "getAll_offer" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  resource_id = aws_api_gateway_method.getAll_offer.resource_id
  http_method = aws_api_gateway_method.getAll_offer.http_method

  integration_http_method = "POST"
  type = "AWS_PROXY"
  uri = aws_lambda_function.getAll_offer.invoke_arn
}

resource "aws_lambda_permission" "getAll_offer" {
  statement_id = "AllowAPIGatewayInvoke"
  action = "lambda:InvokeFunction"
  function_name = aws_lambda_function.getAll_offer.arn
  principal = "apigateway.amazonaws.com"

  # The /*/* portion grants access from any method on any resource
  # within the API Gateway "REST API".
  source_arn = "${aws_api_gateway_deployment.bytabit.execution_arn}/*/*"
}

## GET /offers/{id}

resource "aws_api_gateway_method" "getById_offer" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  resource_id = aws_api_gateway_resource.offer_id.id
  http_method = "GET"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "getById_offer" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  resource_id = aws_api_gateway_method.getById_offer.resource_id
  http_method = aws_api_gateway_method.getById_offer.http_method

  integration_http_method = "POST"
  type = "AWS_PROXY"
  uri = aws_lambda_function.getById_offer.invoke_arn
}

resource "aws_lambda_permission" "getById_offer" {
  statement_id = "AllowAPIGatewayInvoke"
  action = "lambda:InvokeFunction"
  function_name = aws_lambda_function.getById_offer.arn
  principal = "apigateway.amazonaws.com"

  # The /*/* portion grants access from any method on any resource
  # within the API Gateway "REST API".
  source_arn = "${aws_api_gateway_deployment.bytabit.execution_arn}/*/*"
}

## DELETE /offers/{id}

resource "aws_api_gateway_method" "delete_offer" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  resource_id = aws_api_gateway_resource.offer_id.id
  http_method = "DELETE"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "delete_offer" {
  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  resource_id = aws_api_gateway_method.delete_offer.resource_id
  http_method = aws_api_gateway_method.delete_offer.http_method

  integration_http_method = "POST"
  type = "AWS_PROXY"
  uri = aws_lambda_function.delete_offer.invoke_arn
}

resource "aws_lambda_permission" "delete_offer" {
  statement_id = "AllowAPIGatewayInvoke"
  action = "lambda:InvokeFunction"
  function_name = aws_lambda_function.delete_offer.arn
  principal = "apigateway.amazonaws.com"

  # The /*/* portion grants access from any method on any resource
  # within the API Gateway "REST API".
  source_arn = "${aws_api_gateway_deployment.bytabit.execution_arn}/*/*"
}

##

resource "aws_api_gateway_base_path_mapping" "api" {
  api_id = "${aws_api_gateway_rest_api.bytabit.id}"
  stage_name = "${aws_api_gateway_deployment.bytabit.stage_name}"
  domain_name = "${aws_api_gateway_domain_name.api.domain_name}"
}

resource "aws_api_gateway_deployment" "bytabit" {
  depends_on = [
    "aws_api_gateway_integration.get_version",
    "aws_api_gateway_integration.put_badge",
    "aws_api_gateway_integration.put_offer"
  ]

  rest_api_id = aws_api_gateway_rest_api.bytabit.id
  stage_name = terraform.workspace
}
