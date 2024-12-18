
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.cloudwatch.options;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import com.google.common.annotations.Beta;
import org.jclouds.cloudwatch.domain.Dimension;
import org.jclouds.cloudwatch.domain.Statistics;
import org.jclouds.cloudwatch.domain.Unit;
import org.jclouds.http.options.BaseHttpRequestOptions;

/**
 * Options used to describe alarms for metric.
 *
 * @see <a href="http://docs.aws.amazon.com/AmazonCloudWatch/latest/APIReference/API_DescribeAlarmsForMetric.html" />
 */
@Beta
public class ListAlarmsForMetric extends BaseHttpRequestOptions {

    private static final int MAX_DIMENSIONS = 10;
    private static final int MAX_LENGTH = 255;
    private int dimensionIndex = 1;

    /**
     * The list of dimensions associated with the metric.
     *
     * @param dimensions the list of dimensions associated with the metric
     * @return this {@code ListAlarmsForMetric} object
     */
    public ListAlarmsForMetric dimensions(Set<Dimension> dimensions) {
        checkNotNull(dimensions, "dimensions");
        dimensions.forEach(this::dimension);
        return this;
    }

    /**
     * The dimension associated with the metric.
     *
     * @param dimension the dimension associated with the metric
     * @return this {@code ListAlarmsForMetric} object
     */
    public ListAlarmsForMetric dimension(Dimension dimension) {
        validateDimension(dimension);
        formParameters.put("Dimensions.member." + dimensionIndex + ".Name", dimension.getName());
        formParameters.put("Dimensions.member." + dimensionIndex + ".Value", dimension.getValue());
        dimensionIndex++;
        return this;
    }

    private void validateDimension(Dimension dimension) {
        checkNotNull(dimension, "dimension");
        checkArgument(dimensionIndex <= MAX_DIMENSIONS, "maximum number of dimensions is " + MAX_DIMENSIONS);
    }

    /**
     * The name of the metric.
     *
     * @param metricName the name of the metric
     * @return this {@code ListAlarmsForMetric} object
     */
    public ListAlarmsForMetric metricName(String metricName) {
        validateString(metricName, "metricName");
        formParameters.put("MetricName", metricName);
        return this;
    }

    /**
     * The namespace of the metric.
     *
     * @param namespace namespace of the metric
     * @return this {@code ListAlarmsForMetric} object
     */
    public ListAlarmsForMetric namespace(String namespace) {
        validateString(namespace, "namespace");
        formParameters.put("Namespace", namespace);
        return this;
    }

    private void validateString(String value, String fieldName) {
        checkNotNull(value, fieldName);
        checkArgument(value.length() <= MAX_LENGTH, fieldName + " must be between 1 and " + MAX_LENGTH + " characters in length");
    }

    /**
     * The period in seconds over which the statistic is applied.
     *
     * @param period period in seconds over which the statistic is applied
     * @return this {@code ListAlarmsForMetric} object
     */
    public ListAlarmsForMetric period(int period) {
        formParameters.put("Period", String.valueOf(period));
        return this;
    }

    /**
     * The statistic for the metric.
     *
     * @param statistic statistic for the metric
     * @return this {@code ListAlarmsForMetric} object
     */
    public ListAlarmsForMetric statistic(Statistics statistic) {
        validateEnum(statistic, Statistics.UNRECOGNIZED, "statistic");
        formParameters.put("Statistic", statistic.toString());
        return this;
    }

    /**
     * The unit for the metric.
     *
     * @param unit unit for the metric
     * @return this {@code ListAlarmsForMetric} object
     */
    public ListAlarmsForMetric unit(Unit unit) {
        validateEnum(unit, Unit.UNRECOGNIZED, "unit");
        formParameters.put("Unit", unit.toString());
        return this;
    }

    private <T> void validateEnum(T value, T unrecognizedValue, String fieldName) {
        checkNotNull(value, fieldName);
        checkArgument(!value.equals(unrecognizedValue), fieldName + " unrecognized");
    }
}
