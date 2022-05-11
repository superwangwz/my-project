package com.bishe.util;

import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("restful接口调用返回的对象")
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 2914811086065973781L;
    @ApiModelProperty("成功标志")
    private boolean success = true;
    @ApiModelProperty("返回处理消息")
    private String message = "操作成功！";
    @ApiModelProperty("返回代码")
    private Integer code = 0;
    @ApiModelProperty("返回数据对象")
    private T result;
    @ApiModelProperty("时间戳")
    private long timestamp = System.currentTimeMillis();

    private static int SC_OK_200 = 200;

    private static int SC_INTERNAL_SERVER_ERROR_500 = 500;

    public Result() {
    }

    public Result<T> success(String message) {
        this.message = message;
        this.code = 200;
        this.success = true;
        return this;
    }

    public static <T> Result<T> ok() {
        Result<T> r = new Result();
        r.setSuccess(true);
        r.setCode(200);
        r.setMessage("成功");
        return r;
    }

    public static <T> Result<T> ok(String msg) {
        Result<T> r = new Result();
        r.setSuccess(true);
        r.setCode(SC_OK_200);
        r.setMessage(msg);
        return r;
    }

    public static <T> Result<T> ok(String msg, T data) {
        Result<T> r = new Result();
        r.setSuccess(true);
        r.setCode(SC_OK_200);
        r.setMessage(msg);
        r.setResult(data);
        return r;
    }

    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result();
        r.setSuccess(true);
        r.setCode(SC_OK_200);
        r.setResult(data);
        return r;
    }

    public static <T> Result<T> error(String msg) {
        return error(SC_INTERNAL_SERVER_ERROR_500, msg);
    }

    public static <T> Result<T> error(Exception e) {
        return error(SC_INTERNAL_SERVER_ERROR_500, getMessage(e));
    }

    public static <T> Result<T> error(int code, String msg) {
        Result<T> r = new Result();
        r.setCode(code);
        r.setMessage(msg);
        r.setSuccess(false);
        return r;
    }
    public static String getMessage(Throwable ex) {
        String message = ex.getMessage();
        return message == null ? ex.getClass().getName() : message;
    }

    public static <T> Result<T> error(int code, Exception e) {
        Result<T> r = new Result();
        r.setCode(code);
        r.setMessage(getMessage(e));
        r.setSuccess(false);
        return r;
    }

    public String toString() {
        return JSONUtil.toJsonStr(this);
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getMessage() {
        return this.message;
    }

    public Integer getCode() {
        return this.code;
    }

    public T getResult() {
        return this.result;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setCode(final Integer code) {
        this.code = code;
    }

    public void setResult(final T result) {
        this.result = result;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Result)) {
            return false;
        } else {
            Result<?> other = (Result)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.isSuccess() != other.isSuccess()) {
                return false;
            } else if (this.getTimestamp() != other.getTimestamp()) {
                return false;
            } else {
                label52: {
                    Object this$code = this.getCode();
                    Object other$code = other.getCode();
                    if (this$code == null) {
                        if (other$code == null) {
                            break label52;
                        }
                    } else if (this$code.equals(other$code)) {
                        break label52;
                    }

                    return false;
                }

                Object this$message = this.getMessage();
                Object other$message = other.getMessage();
                if (this$message == null) {
                    if (other$message != null) {
                        return false;
                    }
                } else if (!this$message.equals(other$message)) {
                    return false;
                }

                Object this$result = this.getResult();
                Object other$result = other.getResult();
                if (this$result == null) {
                    if (other$result != null) {
                        return false;
                    }
                } else if (!this$result.equals(other$result)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Result;
    }

}
