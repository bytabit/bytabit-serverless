resource "aws_dynamodb_table" "badge" {
  name = "${terraform.workspace}_badge"
  billing_mode = "PROVISIONED"
  read_capacity = 1
  write_capacity = 1
  hash_key = "profilePubKey"
  range_key = "id"

  attribute {
    name = "profilePubKey"
    type = "S"
  }

  attribute {
    name = "id"
    type = "S"
  }

  tags = {
    Name = "badge"
    Stage = "${terraform.workspace}"
  }
}

resource "aws_dynamodb_table" "offer" {
  name = "${terraform.workspace}_offer"
  billing_mode = "PROVISIONED"
  read_capacity = 1
  write_capacity = 1
  hash_key = "id"

  attribute {
    name = "id"
    type = "S"
  }

  tags = {
    Name = "offer"
    Stage = "${terraform.workspace}"
  }
}
