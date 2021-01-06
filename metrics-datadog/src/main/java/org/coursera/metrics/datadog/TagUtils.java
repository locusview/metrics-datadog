package org.coursera.metrics.datadog;

import io.dropwizard.metrics5.MetricName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

class TagUtils {
    private static final Logger LOG = LoggerFactory.getLogger(TagUtils.class);

    private static final String COLON = ":";

    /**
     * @param tags       list of tags, each tag should be in the format of "key:value"
     * @param metricName metricName including tags as key-value map
     * @return merged tags list. If there is duplicated key, tags from metricName will overwrite tags
     * in tags1, and tags in the back of the list will overwrite tags in the front of the list.
     */
    public static List<String> mergeTags(List<String> tags, MetricName metricName) {
        List<String> metricTagsAsList = new ArrayList<>();
        Map<String, String> metricTags = metricName.getTags();
        if (metricTags != null) {
            metricTagsAsList = toTagsList(metricTags);
        }

        return mergeTags(tags, metricTagsAsList);
    }

    /**
     * @param tags1 list of tags, each tag should be in the format of "key:value"
     * @param tags2 list of tags, each tag should be in the format of "key:value"
     * @return merged tags list. If there is duplicated key, tags in tags2 will overwrite tags
     * in tags1, and tags in the back of the list will overwrite tags in the front of the list.
     */
    public static List<String> mergeTags(List<String> tags1, List<String> tags2) {
        if (tags2 == null || tags2.isEmpty()) {
            return tags1;
        }
        if (tags1 == null || tags1.isEmpty()) {
            return tags2;
        }

        List<String> newTags = new ArrayList<>();
        newTags.addAll(tags1);
        newTags.addAll(tags2);

        Map<String, String> map = new HashMap<>();
        for (String tag : newTags) {
            String[] strs = tag.split(COLON);
            if (strs.length != 2) {
                LOG.warn("Invalid tag: " + tag);
            } else {
                map.put(strs[0], strs[1]);
            }
        }

        return toTagsList(map);
    }

    private static List<String> toTagsList(Map<String, String> map) {
        return map.entrySet().stream()
                .map(entry -> String.join(COLON, entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
