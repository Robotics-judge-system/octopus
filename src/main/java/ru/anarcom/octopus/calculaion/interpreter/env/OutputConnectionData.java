package ru.anarcom.octopus.calculaion.interpreter.env;

import java.util.Map;
import lombok.Data;

@Data
public class OutputConnectionData {

  private Integer node;
  private String input;
  private Map<String, Object> data;
}
