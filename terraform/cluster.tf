resource "aws_ecs_cluster" "se-cygni-ecs-cluster" {
  name = "${var.ecs_cluster}"
}
