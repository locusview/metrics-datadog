package org.coursera.metrics.datadog.transport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.coursera.metrics.datadog.AwsHelper;

import javax.validation.constraints.NotNull;
import java.io.IOException;

@JsonTypeName("udp")
public class UdpTransportFactory implements AbstractTransportFactory {

    @NotNull
    @JsonProperty
    private String statsdHost = "localhost";

    @JsonProperty
    private int port = 8125;

    @JsonProperty
    private boolean retryingLookup = false;

    @JsonProperty
    private String prefix = null;

    @JsonProperty
    private boolean useEc2Host = false;

    public UdpTransport build() {
        try {
            final UdpTransport.Builder udpTransportBuilder = new UdpTransport.Builder()
                    .withPrefix(prefix)
                    .withPort(port)
                    .withRetryingLookup(retryingLookup);

            if (useEc2Host) {
                udpTransportBuilder.withStatsdHost(AwsHelper.getEc2PrivateIp());
            } else {
                udpTransportBuilder.withStatsdHost(statsdHost);
            }

            return udpTransportBuilder.build();

        } catch (IOException e) {
            throw new RuntimeException("Error while trying to build Udp transport", e);
        }

    }

}
