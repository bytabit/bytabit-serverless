resource "aws_security_group" "dojo-instance-sg" {
  name = "${terraform.workspace}_dojo-in-out-access"

  # SSH Access
  ingress {
    from_port = 22
    to_port = 22
    protocol = "tcp"
    cidr_blocks = [
      "0.0.0.0/0"]
  }

  # Server outbound communication
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = [
      "0.0.0.0/0"]
  }

  tags = {
    Name = "${terraform.workspace}-dojo-instance"
    Stage = terraform.workspace
  }
}