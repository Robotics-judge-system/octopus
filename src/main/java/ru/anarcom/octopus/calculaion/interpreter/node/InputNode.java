package ru.anarcom.octopus.calculaion.interpreter.node;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import ru.anarcom.octopus.calculaion.interpreter.node.parent.AbstractNode;

@Getter
@Setter
@JsonTypeName("Input")
public class InputNode extends AbstractNode {

  private InputNodeData data = new InputNodeData();

  @Override
  public Integer calculate(Map<String, AbstractNode> nodeMap, Map<String, Integer> protocolData,
      String outputValue) {
    if (!outputValue.equals("val")) {
      throw new RuntimeException("No such field");
    }
    if (!protocolData.containsKey(data.name)){
      throw new RuntimeException("Key not found");
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
