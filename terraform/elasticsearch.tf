
variable "domain" {
  default = "se-cygni-paintbot"
}

data "aws_region" "current" {}

data "aws_caller_identity" "current" {}

resource "aws_security_group" "elasticsearch" {
  name        = "${aws_vpc.se-cygni-vpc.id}-elasticsearch-${var.domain}"
  description = "Managed by Terraform"
  vpc_id      = aws_vpc.se-cygni-vpc.id

  ingress {
    from_port = 443
    to_port   = 443
    protocol  = "tcp"

    cidr_blocks = [
      aws_vpc.se-cygni-vpc.cidr_block,
    ]
  }
}

resource "aws_iam_service_linked_role" "es" {
  aws_service_name = "es.amazonaws.com"
}

resource "aws_elasticsearch_domain" "es" {
  domain_name           = var.domain
  elasticsearch_version = "6.8"

  cluster_config {
    instance_type = "t2.small.elasticsearch"
  }

  ebs_options {
    ebs_enabled = true
    volume_size = 20
  }

  vpc_options {
    subnet_ids = [
      aws_subnet.se-cygniPrivSN0-0.id
    ]

    security_group_ids = [aws_security_group.elasticsearch.id]
  }

  advanced_options = {
    "override_main_response_version" = "true"
    "rest.action.multi.allow_explicit_index" = "true"
  }

  access_policies = <<CONFIG
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": "es:*",
            "Principal": "*",
            "Effect": "Allow",
            "Resource": "arn:aws:es:${data.aws_region.current.name}:${data.aws_caller_identity.current.account_id}:domain/${var.domain}/*"
        }
    ]
}
CONFIG

  snapshot_options {
    automated_snapshot_start_hour = 23
  }

  tags = {
    Domain = "se-cygni-paintbot-domain"
  }

  depends_on = [
    aws_iam_service_linked_role.es,
  ]
}