data "aws_ecs_task_definition" "paintbot" {
  task_definition = aws_ecs_task_definition.paintbot.family
  depends_on      = [aws_ecs_task_definition.paintbot]
}

resource "aws_ecs_task_definition" "paintbot" {
  family = "paintbot"
  requires_compatibilities = ["EC2"]
  memory = 900


  container_definitions = <<DEFINITION
[
  {
    "name": "paintbot",
    "image": "paintbot/paintbot-server:latest",
    "essential": true,
    "portMappings": [
      {
        "containerPort": 8080,
        "protocol" : "tcp",
        "hostPort": 0
      }
    ],
    "environment" : [],
    "mountPoints" : [],
    "volumesFrom" : []
  }
]
DEFINITION

}

