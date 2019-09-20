resource "aws_key_pair" "dojo-key" {
  key_name = "${terraform.workspace}_dojo-key"
  public_key = "${file("~/.ssh/id_rsa.pub")}"
}