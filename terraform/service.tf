resource "aws_ecs_service" "paintbot-ecs-service" {
  name            = "paintbot-ecs-service"
  iam_role        = "${aws_iam_role.ecs-service-role.name}"
  cluster         = "${aws_ecs_cluster.se-cygni-ecs-cluster.id}"
  task_definition = "${aws_ecs_task_definition.paintbot.family}:${max("${aws_ecs_task_definition.paintbot.revision}", "${data.aws_ecs_task_definition.paintbot.revision}")}"
  desired_count   = 1

  load_balancer {
    target_group_arn = "${aws_alb_target_group.ecs-target-group.arn}"
    container_port   = 8080
    container_name   = "paintbot"
  }
}
