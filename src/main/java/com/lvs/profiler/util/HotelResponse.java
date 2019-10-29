package com.lvs.profiler.util;

import alexh.weak.Dynamic;
import com.lvs.profiler.controller.Response;

import java.util.List;

class HotelResponse implements Chain {

    private Chain nextInChain;

    @Override
    public void setNextChain(Chain nextInChain) {
        this.nextInChain = nextInChain;
    }

    @Override
    public Response getResponse(Dynamic payload) {
        if(payload.get("user").get("hotels").isPresent()) {
            return new Response(payload.get("user").get("doc_count").as(Integer.class), payload.dget("user.hotels.buckets").as(List.class));
        } else {
            return nextInChain.getResponse(payload);
        }
    }

}

