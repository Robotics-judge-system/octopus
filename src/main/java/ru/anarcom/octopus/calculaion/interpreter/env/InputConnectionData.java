package ru.anarcom.octopus.calculaion.interpreter.env;

import java.util.Map;
import lombok.Data;

@Data
public class InputConnectionData {

  private Integer node;
  private String output;
  private Map<String, Object> data;
}
