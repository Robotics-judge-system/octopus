package ru.anarcom.octopus.calculaion.interpreter.node;


import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import ru.anarcom.octopus.calculaion.interpreter.node.parent.AbstractNode;


@Getter
@Setter
public class IfNode extends AbstractNode {

  private IfData data;

  @Override
  public Integer calculate(
      Map<String, AbstractNode> nodeMap,
      Map<String, Integer> protocolData,
      String outputValue
  ) {

    if (!outputValue.equals("result")) {
      throw new RuntimeException("hello my dear boy next door");
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
        throw new RuntimeException("");
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

  @Getter
  @Setter
  static class IfData {

    private String cond;
    private String textCond;
  }
}
