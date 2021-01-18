package org.coursera.metrics.datadog;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface MetricNameTagsCallbackFactory extends Discoverable {
  public MetricNameTagsCallback build();
}
