package io.dropwizard.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.coursera.metrics.datadog.*;
import org.coursera.metrics.datadog.transport.AbstractTransportFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static org.coursera.metrics.datadog.DatadogReporter.Expansion;

@JsonTypeName("datadog")
public class DatadogReporterFactory extends BaseReporterFactory {

    @JsonProperty
    private String host = null;

    @JsonProperty
    private List<String> tags = null;

    @Valid
    @JsonProperty
    private DynamicTagsCallbackFactory dynamicTagsCallback = null;

    @Valid
    @JsonProperty
    private MetricNameTagsCallbackFactory metricNameTagsCallback = null;

    @JsonProperty
    private String prefix = null;

    @Valid
    @NotNull
    @JsonProperty
    private EnumSet<Expansion> expansions = EnumSet.allOf(Expansion.class);

    @Valid
    @NotNull
    @JsonProperty
    private MetricNameFormatterFactory metricNameFormatter = new DefaultMetricNameFormatterFactory();

    @Valid
    @NotNull
    @JsonProperty
    private AbstractTransportFactory transport = null;

    public ScheduledReporter build(MetricRegistry registry) {
        try {
            final DatadogReporter.Builder ddReporterBuilder = DatadogReporter.forRegistry(registry);
            final DatadogReporter.Builder builder = ddReporterBuilder
                    .withTransport(transport.build())
                    .withTags(tags)
                    .withPrefix(prefix)
                    .withExpansions(expansions)
                    .withMetricNameFormatter(metricNameFormatter.build())
                    .withDynamicTagCallback(Optional.ofNullable(dynamicTagsCallback).map(DynamicTagsCallbackFactory::build).orElse(null))
                    .withMetricNameTagsCallback(Optional.ofNullable(metricNameTagsCallback).map(MetricNameTagsCallbackFactory::build).orElse(null))
                    .filter(getFilter())
                    .convertDurationsTo(getDurationUnit())
                    .convertRatesTo(getRateUnit());

            if (host != null) {
                builder.withHost(host);
            } else {
                builder.withEC2InstanceId();
            }

            return builder.build();
        } catch (IOException e) {
            throw new RuntimeException("Error while trying to build DD ScheduledReporter", e);
        }
    }
}