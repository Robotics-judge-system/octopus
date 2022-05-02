package ru.anarcom.octopus.calculaion.interpreter.node;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import ru.anarcom.octopus.calculaion.interpreter.node.parent.AbstractNode;

@JsonTypeName("Add")
@Getter
@Setter
public class AddNode extends AbstractMathNode {

  @Override
  public Integer calculate(Map<String, AbstractNode> nodeMap, Map<String, Integer> protocolData,
      String outputValue) {
    if (!outputValue.equals("num")) {
      // TODO: normal text
      throw new RuntimeException("");
    }
    return getA(nodeMap, protocolData) + getB(nodeMap, protocolData);

  }
}
