package com.tianhui.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpaceException extends RuntimeException {

    private Integer code;
    private String msg;
}
