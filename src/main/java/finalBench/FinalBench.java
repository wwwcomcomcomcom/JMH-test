package finalBench;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class FinalBench {

    static final long x_static_final = Long.getLong("divisor", 1000);
    static       long x_static       = Long.getLong("divisor", 1000);
    final long x_inst_final   = Long.getLong("divisor", 1000);
    long x_inst         = Long.getLong("divisor", 1000);

    @Benchmark public long _static_final() { return 1000 / x_static_final; }
    @Benchmark public long _static()       { return 1000 / x_static;       }
    @Benchmark public long _inst_final()   { return 1000 / x_inst_final;   }
    @Benchmark public long _inst()         { return 1000 / x_inst;         }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(FinalBench.class.getSimpleName())
                .build();
        new org.openjdk.jmh.runner.Runner(opt).run();
    }
}