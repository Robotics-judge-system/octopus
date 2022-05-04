package ru.anarcom.octopus.calculaion.interpreter.node;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import ru.anarcom.octopus.calculaion.interpreter.env.NodeData;
import ru.anarcom.octopus.calculaion.interpreter.node.parent.AbstractNode;

public abstract class AbstractMathNode extends AbstractNode {

  @Getter
  @Setter
  protected NodeData data = new NodeData();

  protected Integer getA(Map<String, AbstractNode> nodeMap, Map<String, Integer> protocolData) {
    return getValueOfInputOrDefault("num1", nodeMap, protocolData, data.num1);
  }

  protected Integer getB(Map<String, AbstractNode> nodeMap, Map<String, Integer> protocolData) {
    return getValueOfInputOrDefault("num2", nodeMap, protocolData, data.num2);
  }
}
