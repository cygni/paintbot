resource "aws_internet_gateway" "se-cygni-ig" {
  vpc_id = "${aws_vpc.se-cygni-vpc.id}"

  tags = {
    Name = "se-cygni-ig"
  }
}
