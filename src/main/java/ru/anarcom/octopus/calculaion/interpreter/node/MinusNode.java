package ru.anarcom.octopus.calculaion.interpreter.node;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import ru.anarcom.octopus.calculaion.interpreter.node.parent.AbstractNode;
import ru.anarcom.octopus.exceptions.CalculationException;

@JsonTypeName("Minus")
@Getter
@Setter
public class MinusNode extends AbstractMathNode {


  @Override
  public Integer calculate(Map<String, AbstractNode> nodeMap, Map<String, Integer> protocolData,
      String outputValue) {
    if (!outputValue.equals("num")) {
      throw new CalculationException(
          String.format("No such field '%s'", outputValue)
      );
    }
    return getA(nodeMap, protocolData) - getB(nodeMap, protocolData);

  }
}
