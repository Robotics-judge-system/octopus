package ru.anarcom.octopus.calculaion.interpreter.node.parent;

import java.util.Map;
import ru.anarcom.octopus.calculaion.interpreter.OutputCalculator;

public abstract class AbstractNodeManyReturns extends AbstractNode{

  protected Map<String, OutputCalculator<Map<String, AbstractNode>, Map<String, Integer>>> functions;

  @Override
  public Integer calculate(Map<String, AbstractNode> nodeMap, Map<String, Integer> protocolData,
      String outputValue) {
    // TODO: id node выбросить в добавок
    if(functions == null){
      throw new RuntimeException("Node not ready");
    }

    // TODO: write key that has not been found
    if(!functions.containsKey(outputValue)){
      throw new RuntimeException("key not found");
    }

    return functions.get(outputValue).getValue(nodeMap, protocolData);
  }
}
