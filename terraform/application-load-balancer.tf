resource "aws_alb" "ecs-load-balancer" {
  name            = "ecs-load-balancer"
  security_groups = [aws_security_group.se-cygni_public_sg.id]
  subnets         = [aws_subnet.se-cygniPubSN0-0.id, aws_subnet.se-cygniPubSN0-1.id]

  tags = {
    Name = "ecs-load-balancer"
  }
}

resource "aws_alb_target_group" "ecs-target-group" {
  name     = "ecs-target-group"
  port     = "80"
  protocol = "HTTP"
  vpc_id   = aws_vpc.se-cygni-vpc.id

  health_check {
    enabled             = true
    healthy_threshold   = "5"
    unhealthy_threshold = "2"
    interval            = "30"
    matcher             = "200"
    path                = "/health"
    port                = "traffic-port"
    protocol            = "HTTP"
    timeout             = "5"
  }

  tags = {
    Name = "ecs-target-group"
  }
}

# TODO: reintroduce into tf state
#resource "aws_alb_listener" "alb-listener" {
#  load_balancer_arn = aws_alb.ecs-load-balancer.arn
#  port              = "80"
#  protocol          = "HTTP"
#
#  default_action {
#    target_group_arn = aws_alb_target_group.ecs-target-group.arn
#    type             = "forward"
#  }
#}

