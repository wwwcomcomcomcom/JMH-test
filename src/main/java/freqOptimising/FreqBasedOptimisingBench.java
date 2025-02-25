package freqOptimising;

import finalBench.FinalBench;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class FreqBasedOptimisingBench {
    @Benchmark
    public void mostlyTrue(){
        for(int i = 0; i<9; i++){
            doCall(true);
        }
        doCall(false);
    }
    @Benchmark
    public void mostlyFalse(){
        for(int i = 0; i<9; i++){
            doCall(false);
        }
        doCall(true);
    }
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int doCall(boolean condition){
        if(condition){
            return 1;
        }else{
            return 0;
        }
    }
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(FreqBasedOptimisingBench.class.getSimpleName())
                .build();
        new org.openjdk.jmh.runner.Runner(opt).run();
    }
}
