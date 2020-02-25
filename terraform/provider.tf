provider "aws" {
  region = "eu-north-1"
}

terraform {
  backend "s3" {
    bucket                 = "se.cygni.paintbot-server-terraform"
    key                    = "terraform-state/state.tfstate"
    region                 = "eu-north-1"
    skip_region_validation = "true"
  }
}
