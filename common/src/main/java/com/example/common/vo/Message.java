package com.example.common.vo;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.Date;

@Data
public class Message {


    private Long id;

    private String msg;

    private Date sendTime;
}
