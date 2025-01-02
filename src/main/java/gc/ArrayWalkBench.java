package gc;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1, jvmArgsAppend = {"-Xms8g", "-Xmx8g", "-Xmn7g" })
public class ArrayWalkBench {

    @Param({"16", "256", "4096", "65536", "1048576", "16777216"})
    int size;

    @Param({"false", "true"})
    boolean shuffle;

    @Param({"false", "true"})
    boolean gc;

    String[] arr;

    @Setup
    public void setup() throws IOException, InterruptedException {
        arr = new String[size];
        for (int c = 0; c < size; c++) {
            arr[c] = "Value" + c;
        }
        if (shuffle) {
            Collections.shuffle(Arrays.asList(arr), new Random(0xAABBCC));
        }
        if (gc) {
            for (int c = 0; c < 5; c++) {
                System.gc();
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }

    @Benchmark
    public void walk(Blackhole bh) {
        for (String val : arr) {
            bh.consume(val.hashCode());
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ArrayWalkBench.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}