package com.bishe.config;

import ch.qos.logback.classic.Level;
import com.bishe.util.Result;

/**
 * 自定义异常类
 */
public class EcoBootException extends RuntimeException {
    private static final long serialVersionUID = -5830813803055393727L;
    private int code = 200;
    private Level level;

    public EcoBootException(String message) {
        super(message);
        this.level = Level.DEBUG;
    }

    public EcoBootException(String message, Level level) {
        super(message);
        this.level = Level.DEBUG;
        this.level = level;
    }

    public EcoBootException(int code, String message) {
        super(message);
        this.level = Level.DEBUG;
        this.code = code;
    }

    public EcoBootException(int code, String message, Level level) {
        super(message);
        this.level = Level.DEBUG;
        this.code = code;
        this.level = level;
    }

    public int getCode() {
        return this.code;
    }

    public Level getLevel() {
        return this.level;
    }

    public Result toResult() {
        return Result.error(this.code, this.getMessage());
    }
}
