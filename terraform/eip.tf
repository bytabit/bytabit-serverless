resource "aws_eip" "dojo-eip" {
  instance = aws_instance.dojo.id
}