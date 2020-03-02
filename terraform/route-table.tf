resource "aws_route_table" "se-cygniPubSN0-0RT" {
  vpc_id = aws_vpc.se-cygni-vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.se-cygni-ig.id
  }

  tags = {
    Name = "se-cygniPubSN0-0RT"
  }
}

resource "aws_route_table_association" "se-cygniSN0-0RTAssn" {
  subnet_id      = aws_subnet.se-cygniPubSN0-0.id
  route_table_id = aws_route_table.se-cygniPubSN0-0RT.id
}

resource "aws_route_table" "se-cygniPubSN0-1RT" {
  vpc_id = aws_vpc.se-cygni-vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.se-cygni-ig.id
  }

  tags = {
    Name = "se-cygniPubSN0-1RT"
  }
}

resource "aws_route_table_association" "se-cygniSN0-1RTAssn" {
  subnet_id      = aws_subnet.se-cygniPubSN0-1.id
  route_table_id = aws_route_table.se-cygniPubSN0-1RT.id
}

