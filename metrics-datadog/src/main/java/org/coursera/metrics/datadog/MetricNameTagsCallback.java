package org.coursera.metrics.datadog;

import java.util.List;

/**
 * An implementation of this interface can be used to pass a callback to the builder of
 * {@link DatadogReporter DatadogReporter}, so that DatadogReporter
 * can use tags derived from metric name
 */
public interface MetricNameTagsCallback {
  /**
   *
   * @return dynamic tags that will merge into the static tags. Dynamic tags will overwrite
   * static tags with the same key
   */
  public List<String> getTagsFor(String metricName);
}
