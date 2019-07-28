variable "root_domain_name" {
  default = "bytabit.net"
}

provider "aws" {
  region = "us-east-1"
}

variable "app_version" {
  default = "0.0.0"
}

variable "app_stage" {
  default = "test"
}

variable "log_retention_in_days" {
  default = 30
}