
data "aws_route53_zone" "root_domain" {
  name = "${var.root_domain_name}"
  private_zone = false
}

# Find a certificate that is issued
data "aws_acm_certificate" "api" {
  domain = "${terraform.workspace}.${var.root_domain_name}"
  statuses = [
    "ISSUED"]
}

# The domain name to use with api-gateway
resource "aws_api_gateway_domain_name" "api" {
  domain_name = "${terraform.workspace}.${var.root_domain_name}"
  certificate_arn = "${data.aws_acm_certificate.api.arn}"
}

resource "aws_route53_record" "sub_domain" {
  name = "${terraform.workspace}.${var.root_domain_name}"
  type = "A"
  zone_id = "${data.aws_route53_zone.root_domain.zone_id}"

  alias {
    name = "${aws_api_gateway_domain_name.api.cloudfront_domain_name}"
    zone_id = "${aws_api_gateway_domain_name.api.cloudfront_zone_id}"
    evaluate_target_health = false
  }
}