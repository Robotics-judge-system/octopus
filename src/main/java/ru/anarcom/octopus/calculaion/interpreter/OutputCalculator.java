package ru.anarcom.octopus.calculaion.interpreter;

@FunctionalInterface
public interface OutputCalculator<T,E> {
  Integer getValue(T a, E b);

}
