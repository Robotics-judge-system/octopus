package ru.anarcom.octopus.calculaion.interpreter.env;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import ru.anarcom.octopus.calculaion.interpreter.node.parent.AbstractNode;

@Getter
@Setter
public class NodeGroup {
  private String id;
  public Map<String, AbstractNode> nodes;
}
