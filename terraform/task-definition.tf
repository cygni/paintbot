data "aws_ecs_task_definition" "paintbot" {
  task_definition = "${aws_ecs_task_definition.paintbot.family}"
  depends_on      = ["aws_ecs_task_definition.paintbot"]
}

resource "aws_ecs_task_definition" "paintbot" {
  family = "paintbot"

  container_definitions = <<DEFINITION
[
  {
    "name": "paintbot",
    "image": "paintbot/paintbot-server:latest",
    "essential": true,
    "portMappings": [
      {
        "containerPort": 8080,
        "hostPort": 0
      }
    ],
    "memory": 400,
    "cpu": 256
  }
]
DEFINITION
}
