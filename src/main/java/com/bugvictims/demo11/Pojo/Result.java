package com.bugvictims.demo11.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    int code;//业务状态码
    String message;
    Object data;
    public Result success(Object data) {
        return new Result(1,"success", data);
    }

    public Result success() {
        return new Result(1, "success", null);
    }

    public Result error(String message) {
        return new Result(0, message, null);
    }
}
