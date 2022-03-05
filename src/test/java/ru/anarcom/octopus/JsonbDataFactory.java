package ru.anarcom.octopus;

import java.sql.Types;
import org.dbunit.dataset.datatype.AbstractDataType;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;

public class JsonbDataFactory extends DefaultDataTypeFactory {

  public JsonbDataFactory() {
  }

  @Override
  public DataType createDataType(int sqlType, String sqlTypeName) throws DataTypeException {

    if (sqlTypeName.equalsIgnoreCase("jsonb")) {
      return new JsonbDataType();
    }
    return super.createDataType(sqlType, sqlTypeName);
  }

  static class JsonbDataType extends AbstractDataType {
    JsonbDataType() {
      super("jsonb", Types.OTHER, String.class, false);
    }

    @Override
    public Object typeCast(Object value) {
      return value.toString();
    }
  }
}
