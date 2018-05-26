package com.yammer;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;


public class Representation <T> {


    private long code;
    @Length(max=5)
    private T data;

     public Representation(long code, T data) {
         this.data=data;
     }

     @JsonProperty
    public T getData() {
        return data;
    }

    @JsonProperty
    public long getCode() {
        return code;
    }
}
