package ru.anarcom.octopus.calculaion.interpreter.node.parent;

import java.util.Map;
import ru.anarcom.octopus.calculaion.interpreter.OutputCalculator;
import ru.anarcom.octopus.exceptions.CalculationException;

public abstract class AbstractNodeManyReturns extends AbstractNode{

  protected Map<String, OutputCalculator<Map<String, AbstractNode>, Map<String, Integer>>> functions;

  @Override
  public Integer calculate(Map<String, AbstractNode> nodeMap, Map<String, Integer> protocolData,
      String outputValue) {
    // TODO: id node выбросить в добавок
    if(functions == null){
      throw new RuntimeException("Node not ready");
    }

    if(!functions.containsKey(outputValue)){
      throw new CalculationException(
          String.format("No such field '%s'", outputValue)
      );
    }

    return functions.get(outputValue).getValue(nodeMap, protocolData);
  }
}
