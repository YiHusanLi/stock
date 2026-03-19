package tool.stock.v8sniper.dto;

import tool.stock.v8sniper.entity.StrategyPickDetailEntity;
import tool.stock.v8sniper.entity.StrategyPickResultEntity;

import java.util.List;

public class StrategyResultBundle {
    private final boolean picked;
    private final StrategyPickResultEntity result;
    private final List<StrategyPickDetailEntity> details;
    public StrategyResultBundle(boolean picked, StrategyPickResultEntity result, List<StrategyPickDetailEntity> details) {
        this.picked = picked; this.result = result; this.details = details;
    }
    public boolean isPicked() { return picked; }
    public StrategyPickResultEntity getResult() { return result; }
    public List<StrategyPickDetailEntity> getDetails() { return details; }
}
