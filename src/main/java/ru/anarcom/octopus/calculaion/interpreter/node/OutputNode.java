package ru.anarcom.octopus.calculaion.interpreter.node;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import ru.anarcom.octopus.calculaion.interpreter.node.parent.AbstractNode;

@JsonTypeName("Output")
public class OutputNode extends AbstractNode {

  @Setter
  @Getter
  private OutputData data = new OutputData();

  @Override
  public Integer calculate(
      Map<String, AbstractNode> nodeMap,
      Map<String, Integer> protocolData,
      String outputValue
  ) {
    return getValueOfInput(outputValue, nodeMap, protocolData);
  }

  @Getter
  @Setter
  static class OutputData {

    private Integer previewRes;
    private Integer previewTime;
  }
}
