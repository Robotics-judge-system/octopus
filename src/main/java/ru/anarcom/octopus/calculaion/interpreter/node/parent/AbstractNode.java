package ru.anarcom.octopus.calculaion.interpreter.node.parent;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import ru.anarcom.octopus.calculaion.interpreter.env.InputConnection;
import ru.anarcom.octopus.calculaion.interpreter.env.OutputConnection;
import ru.anarcom.octopus.calculaion.interpreter.node.AddNode;
import ru.anarcom.octopus.calculaion.interpreter.node.DivideNode;
import ru.anarcom.octopus.calculaion.interpreter.node.IfNode;
import ru.anarcom.octopus.calculaion.interpreter.node.InputNode;
import ru.anarcom.octopus.calculaion.interpreter.node.MinusNode;
import ru.anarcom.octopus.calculaion.interpreter.node.MultiplyNode;
import ru.anarcom.octopus.calculaion.interpreter.node.NumberNode;
import ru.anarcom.octopus.calculaion.interpreter.node.OutputNode;
import ru.anarcom.octopus.exceptions.CalculationException;

@Getter
@Setter
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "name")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AddNode.class, name = "Add"),
    @JsonSubTypes.Type(value = OutputNode.class, name = "Output"),
    @JsonSubTypes.Type(value = NumberNode.class, name = "Number"),
    @JsonSubTypes.Type(value = MultiplyNode.class, name = "Multiply"),
    @JsonSubTypes.Type(value = MinusNode.class, name = "Minus"),
    @JsonSubTypes.Type(value = InputNode.class, name = "Input"),
    @JsonSubTypes.Type(value = DivideNode.class, name = "Divide"),
    @JsonSubTypes.Type(value = IfNode.class, name = "If")
})
public abstract class AbstractNode {

  Integer id;
  // data
  private Double[] position;
  private Map<String, InputConnection> inputs;
  private Map<String, OutputConnection> outputs;

  public abstract Integer calculate(
      Map<String, AbstractNode> nodeMap,
      Map<String, Integer> protocolData,
      String outputValue
  );

  protected Integer getValueOfInputOrDefault(
      String key,
      Map<String, AbstractNode> nodeMap,
      Map<String, Integer> protocolData,
      Integer def
  ) {
    if (getInputs().get(key).getConnections().length == 0) {
      return def;
    }
    return getValueOfInput(key, nodeMap, protocolData);
  }

  protected Integer getValueOfInput(
      String key,
      Map<String, AbstractNode> nodeMap,
      Map<String, Integer> protocolData) {

    if(!getInputs().containsKey(key)){
      throw new CalculationException(
          String.format("No such key '%s'", key)
      );
    }

    if (getInputs().get(key).getConnections().length == 0) {
      throw new CalculationException(
          String.format("'%s' cannot be computed", key)
      );
    }

    Integer res = nodeMap.get(
        getInputs().get(key).getConnections()[0].getNode().toString()
    ).calculate(nodeMap, protocolData, getInputs().get(key).getConnections()[0].getOutput());

    if (res == null) {
      // TODO add key name in exception
      throw new RuntimeException("can't read number");
    }

    return res;
  }
}
