package lock;

import gc.ShuffledReferenceBench;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput) // 초당 실행 횟수 기준 측정
@OutputTimeUnit(TimeUnit.MILLISECONDS) // 시간 단위 설정
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS) // 워밍업 설정
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS) // 측정 설정
@Fork(
        value = 1,
        jvmArgsAppend = {
                "-XX:+UnlockDiagnosticVMOptions",
                "-XX:+LogCompilation",
                "-XX:+PrintInlining",
                "-XX:LogFile=jit-log.xml"
        }
) // JIT 로그 기록 옵션
@State(Scope.Thread) // 각 스레드에 대해 별도의 상태 유지
public class CommonCoarseningBenchmark {

    int x=0;

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public void test() {
        for (int c = 0; c < 1000; c++) {
            synchronized (this) {
                x += 0x42;
            }
        }
    }
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CommonCoarseningBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
