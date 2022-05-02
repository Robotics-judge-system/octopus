package ru.anarcom.octopus.calculaion.interpreter.node;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import ru.anarcom.octopus.calculaion.interpreter.node.parent.AbstractNode;

@JsonTypeName("Number")
public class NumberNode extends AbstractNode {

  @Getter
  @Setter
  private NumberData data = new NumberData();

  @Override
  public Integer calculate(
      Map<String, AbstractNode> nodeMap,
      Map<String, Integer> protocolData,
      String outputValue) {
    if (!outputValue.equals("num")) {
      throw new RuntimeException("unexpected output value");
    }
    // TODO add check for null
    return data.num;
  }

  @Getter
  @Setter
  static class NumberData {

    private Integer num;
  }
}
