package ru.anarcom.octopus.calculaion.interpreter.node;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import ru.anarcom.octopus.calculaion.interpreter.node.parent.AbstractNode;
import ru.anarcom.octopus.exceptions.CalculationException;

@Getter
@Setter
@JsonTypeName("Input")
public class InputNode extends AbstractNode {

  private InputNodeData data = new InputNodeData();

  @Override
  public Integer calculate(Map<String, AbstractNode> nodeMap, Map<String, Integer> protocolData,
      String outputValue) {
    if (!outputValue.equals("val")) {
      throw new CalculationException(
          String.format("No such field '%s'", outputValue)
      );
    }
    if (!protocolData.containsKey(data.name)){
      throw new CalculationException(
          String.format("Key '%s' not found", data.name)
      );
    }

    return protocolData.get(data.name);
  }

  @Getter
  @Setter
  public static class InputNodeData {

    private String name;
    private Integer val;
  }
}
