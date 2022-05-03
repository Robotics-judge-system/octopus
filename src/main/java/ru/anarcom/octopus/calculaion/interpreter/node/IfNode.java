package ru.anarcom.octopus.calculaion.interpreter.node;


import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import ru.anarcom.octopus.calculaion.interpreter.node.parent.AbstractNode;
import ru.anarcom.octopus.exceptions.CalculationException;


@Getter
@Setter
public class IfNode extends AbstractNode {

  @Getter
  @Setter
  private IfData data = new IfData();

  @Override
  public Integer calculate(
      Map<String, AbstractNode> nodeMap,
      Map<String, Integer> protocolData,
      String outputValue
  ) {

    if (!outputValue.equals("result")) {
      throw new CalculationException(
          String.format("No such field '%s'", outputValue)
      );
    }

    // throw if nothing connected
    Integer a = getValue("condVal1", nodeMap, protocolData);

    Integer b = getValue("condVal2", nodeMap, protocolData);

    boolean res;

    switch (data.textCond) {
      case "<":
        res = a < b;
        break;
      case "<=":
        res = a <= b;
        break;
      case "==":
        res = Objects.equals(a, b);
        break;
      case "!=":
        res = !Objects.equals(a, b);
        break;
      default:
        //TODO exception text
        throw new CalculationException(
            String.format("No such comparator '%s'", data.textCond)
        );
    }

    if (res) {
      return getValue("then", nodeMap, protocolData);
    } else {
      return getValue("else", nodeMap, protocolData);
    }
  }

  private Integer getValue(
      String valueName,
      Map<String, AbstractNode> nodeMap,
      Map<String, Integer> protocolData
  ) {
    if (getInputs().get(valueName).getConnections().length == 0) {
      throw new RuntimeException("Not connected value");
    }
    return nodeMap.get(
        getInputs().get(valueName).getConnections()[0].getNode().toString()
    ).calculate(
        nodeMap, protocolData, getInputs().get(valueName).getConnections()[0].getOutput()
    );
  }

  public static class IfData {

    public String cond;
    public String textCond;
  }
}
