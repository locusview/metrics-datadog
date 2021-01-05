package org.coursera.metrics.datadog;

import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AwsHelper {

  private static final Logger logger = LoggerFactory.getLogger(AwsHelper.class);
  private static final String EC2_INFO_PREFIX = "http://169.254.169.254/latest/meta-data/";
  private static final String EC2_IP_ENDPOINT = "local-ipv4";
  private static final String EC2_INSTANCE_ID_ENDPOINT = "instance-id";

  public static String getEc2PrivateIp() throws IOException {
    try {
      final String ec2Ip = Request.Get(EC2_INFO_PREFIX + EC2_IP_ENDPOINT).execute().returnContent().asString();
      logger.debug("Host Ec2 IP:{}...", ec2Ip);
      return ec2Ip.trim();
    } catch (Throwable t) {
      throw new IOException(t);
    }
  }

  public static String getEc2InstanceId() throws IOException {
    try {
      final String ec2InstanceId = Request.Get(EC2_INFO_PREFIX + EC2_INSTANCE_ID_ENDPOINT).execute().returnContent().asString();
      logger.debug("Host Ec2 Instance ID:{}...", ec2InstanceId);
      return ec2InstanceId.trim();
    } catch (Throwable t) {
      throw new IOException(t);
    }
  }
}