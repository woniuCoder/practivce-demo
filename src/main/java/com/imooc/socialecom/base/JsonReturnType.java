package com.imooc.socialecom.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonReturnType {

    private String status;

    private Object data;

    public static JsonReturnType createType(Object model) {
        JsonReturnType jsonReturnType = new JsonReturnType();
        jsonReturnType.setData(model);
        jsonReturnType.setStatus("success");
        return jsonReturnType;
    }

    public static JsonReturnType createErrorType(String errMessage) {
        JsonReturnType jsonReturnType = new JsonReturnType();
        jsonReturnType.setData(errMessage);
        jsonReturnType.setStatus("fail");
        return jsonReturnType;
    }
}
