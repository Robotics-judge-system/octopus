package ru.anarcom.octopus.calculaion.interpreter.node;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import ru.anarcom.octopus.calculaion.interpreter.env.NodeData;
import ru.anarcom.octopus.calculaion.interpreter.node.parent.AbstractNode;
import ru.anarcom.octopus.calculaion.interpreter.node.parent.AbstractNodeManyReturns;

@Getter
@Setter
@JsonTypeName("Divide")
public class DivideNode extends AbstractNodeManyReturns {
  private NodeData data = new NodeData();

  public DivideNode() {
    functions = new HashMap<>();
    functions.put("out1", (x, y) -> Math.toIntExact(
            Math.round(((double) getA(x, y) / (double) getB(x, y)))
        )
    );

    functions.put("out2", (x, y) -> getA(x, y) / getB(x, y));

    functions.put("out3", (x, y) -> getA(x, y) % getB(x, y));
  }

  protected Integer getA(Map<String, AbstractNode> nodeMap, Map<String, Integer> protocolData) {
    return getValueOfInputOrDefault("num1", nodeMap, protocolData, data.getNum1());
  }

  protected Integer getB(Map<String, AbstractNode> nodeMap, Map<String, Integer> protocolData) {
    return getValueOfInputOrDefault("num2", nodeMap, protocolData, data.getNum2());
  }
}
