provider "aws" {
  region = "eu-west-1"
}

terraform {
  backend "s3" {
    bucket                 = "se.cygni.paintbot-server-terraform-2"
    key                    = "terraform-state/state.tfstate"
    region                 = "eu-west-1"
    skip_region_validation = "true"
  }
}

