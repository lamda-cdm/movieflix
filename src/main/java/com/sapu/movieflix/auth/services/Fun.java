package com.sapu.movieflix.auth.services;

import java.util.function.Function;

interface myabs{
    void do_something();
}
public class Fun {
    public static void main(String args[]){
        myabs xx = ()->{
            System.out.println("This is saptarshi");
        };
        xx.do_something();
        Function<Integer,String>func = (a)->{
            return Integer.toString(a);
        };
    }
}
