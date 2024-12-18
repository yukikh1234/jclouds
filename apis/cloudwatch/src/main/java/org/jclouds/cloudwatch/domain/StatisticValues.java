
package org.jclouds.cloudwatch.domain;

public class StatisticValues {

    private final double maximum;
    private final double minimum;
    private final double sampleCount;
    private final double sum;

    private StatisticValues(Builder builder) {
        this.maximum = builder.maximum;
        this.minimum = builder.minimum;
        this.sampleCount = builder.sampleCount;
        this.sum = builder.sum;
    }

    public double getMaximum() {
        return maximum;
    }

    public double getMinimum() {
        return minimum;
    }

    public double getSampleCount() {
        return sampleCount;
    }

    public double getSum() {
        return sum;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private double maximum;
        private double minimum;
        private double sampleCount;
        private double sum;

        public Builder maximum(double maximum) {
            this.maximum = maximum;
            return this;
        }

        public Builder minimum(double minimum) {
            this.minimum = minimum;
            return this;
        }

        public Builder sampleCount(double sampleCount) {
            this.sampleCount = sampleCount;
            return this;
        }

        public Builder sum(double sum) {
            this.sum = sum;
            return this;
        }

        public StatisticValues build() {
            return new StatisticValues(this);
        }
    }
}
