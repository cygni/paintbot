resource "aws_vpc" "se-cygni-vpc" {
  cidr_block = "200.0.0.0/16"

  tags = {
    Name = "se-cygni-vpc"
  }
}
