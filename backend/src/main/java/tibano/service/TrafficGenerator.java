package tibano.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TrafficGenerator {
    private static final Logger log = LoggerFactory.getLogger(TrafficGenerator.class);

    private static final List<Integer> DELTAS = Arrays.asList(-1, -1, -1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, -1);
    private static List<GenerationState> indicesIntoDeltas;
    private final MonitorService monitorService;
    private final CounterService counterService;

    @Autowired
    public TrafficGenerator(MonitorService monitorService, CounterService counterService) {
        this.monitorService = monitorService;
        this.counterService = counterService;
        this.indicesIntoDeltas = Arrays.asList(new GenerationState(2, 4), new GenerationState(3, 8));
    }

    @Scheduled(fixedRate = 5000)
    public void generateTraffic() {
        for (GenerationState g : indicesIntoDeltas) {
            int index = g.getAndIncrementIndex();
            if (DELTAS.get(index) < 0) {
                counterService.exit(g.getAreaId());
            }
            else {
                counterService.entry(g.getAreaId());
            }
        }
    }

    private static class GenerationState {
        private AtomicInteger deltaIndex;
        private long areaId;

        public GenerationState(long areaId, int deltaIndex) {
            this.areaId = areaId;
            this.deltaIndex = new AtomicInteger(deltaIndex);
        }

        public long getAreaId() {
            return areaId;
        }

        public int getAndIncrementIndex() {
            if (deltaIndex.get() == DELTAS.size()-1) {
                deltaIndex.set(-1);

            }
            return deltaIndex.incrementAndGet();
        }
    }
}