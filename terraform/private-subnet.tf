resource "aws_subnet" "se-cygniPrivSN0-0" {
  vpc_id            = aws_vpc.se-cygni-vpc.id
  cidr_block        = "200.0.20.0/24"
  availability_zone = "eu-west-1a"

  tags = {
    Name = "se-cygniPrivSN0-0-0"
  }
}