package com.shaojie.rabbit.mq.demo;

/**
 * @author ShaoJie
 * @Date 2019年12月02 15:38
 * @Description:
 */
public class Receive {

    public void listen(String message){
        System.out.println("-receive " + message);
    }
}
