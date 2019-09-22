resource "aws_instance" "dojo" {
  ami = data.aws_ami.ubuntu.id
  instance_type = "t3.medium"
  availability_zone = "us-east-1a"

  key_name = "${aws_key_pair.dojo-key.key_name}"

  security_groups = [
    "${aws_security_group.dojo-instance-sg.name}"
  ]

  lifecycle {
    ignore_changes = [
      "private_ip",
      "root_block_device",
      "ebs_block_device",
      "volume_tags"]
  }

  user_data = templatefile("setup.tmpl.sh", {
    device_name = "/dev/nvme1n1",
    mount_dir = "/var/lib/docker"
  })

  tags = {
    Stage = terraform.workspace
    Name = "${terraform.workspace}-dojo"
  }
}

resource "aws_volume_attachment" "dojo_ebs_testnet" {
  device_name = "/dev/sdh"
  instance_id = aws_instance.dojo.id
  volume_id = aws_ebs_volume.dojo_docker.id
}

resource "aws_ebs_volume" "dojo_docker" {
  size = 200
  availability_zone = "us-east-1a"

  tags = {
    Stage = terraform.workspace
    Name = "${terraform.workspace}-dojo-volume"
  }
}
