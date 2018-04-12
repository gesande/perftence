package org.fluentjava.perftence.reporting.summary;

enum Fields implements FieldDefinition {
    Max {
        @Override
        public String fullName() {
            return "max:";
        }
    },
    Threads {
        @Override
        public String fullName() {
            return "threads:";
        }
    },
    ExecutionTime {

        @Override
        public String fullName() {
            return "execution time (ms):";
        }
    },
    Throughput {

        @Override
        public String fullName() {
            return "throughput:";
        }
    },
    Percentile95 {

        @Override
        public String fullName() {
            return "95 percentile:";
        }
    },
    Median {

        @Override
        public String fullName() {
            return "median:";
        }
    },
    Average {

        @Override
        public String fullName() {
            return "average:";
        }
    },
    Samples {

        @Override
        public String fullName() {
            return "samples:";
        }
    },
    EstimatedTimeLeft {

        @Override
        public String fullName() {
            return "estimated time left:";
        }
    };
}