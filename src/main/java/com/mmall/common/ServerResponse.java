package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Created by 张凡 on 2019/9/24.
 * 使用jackson进行序列化时，往往会遇到后台某个实体对象的属性为null，
 * 当序列化成json时对应的属性也为null；这样在某些前端组件上应用该json对象会报错。
 * 保证序列化json的时候，如果null的对象，则key也会消失
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {
    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status) {

        this.status = status;
    }

    private ServerResponse(int status,T data) {
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status,String msg,T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int status,String msg) {
        this.status = status;
        this.msg = msg;
    }

    /*
    在序列化的时候就不会显示
    if 0 == 0 true
    else 1 == 0 false
     */
    @JsonIgnore
    public boolean isSuccess() {

        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    /*
    static 属于类级别的，不需要创建对象就可以直接使用
    全局唯一，内存中唯一，静态变量可以唯一标识某些状态
    在类加载时候初始化，常驻在内存中，调用快捷方便.
     */
    public static<T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static<T> ServerResponse<T> createBySuccessMessage(String msg) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static<T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }

    public static<T> ServerResponse<T> createBySuccess(String msg,T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    public static<T> ServerResponse<T> createByError() {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    public static<T> ServerResponse<T> createByErrorMessage(String errorMessage) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }

    public static<T> ServerResponse<T> createByErrorCodeMessage(int code,String errorMessage) {
        return new ServerResponse<T>(code,errorMessage);
    }
}
