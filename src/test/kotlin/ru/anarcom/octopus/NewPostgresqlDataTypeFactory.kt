package ru.anarcom.octopus

import org.dbunit.dataset.datatype.AbstractDataType
import org.dbunit.dataset.datatype.DataType
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory
import org.postgresql.util.PGobject
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

class NewPostgresqlDataTypeFactory : PostgresqlDataTypeFactory() {
    override fun createDataType(sqlType: Int, sqlTypeName: String?): DataType {
        return when (sqlTypeName) {
            "jsonb" -> return JsonbDataType()
            else -> super.createDataType(sqlType, sqlTypeName)
        }
    }

    class JsonbDataType : AbstractDataType("jsonb", Types.OTHER, String::class.java, false) {
        override fun typeCast(obj: Any?): Any {
            return obj.toString()
        }

        override fun getSqlValue(column: Int, resultSet: ResultSet): Any {
            return resultSet.getString(column)
        }

        override fun setSqlValue(value: Any?, column: Int, statement: PreparedStatement) {
            val jsonObj = PGobject()
            jsonObj.type = "json"
            jsonObj.value = value?.toString()
            statement.setObject(column, jsonObj)
        }
    }
}