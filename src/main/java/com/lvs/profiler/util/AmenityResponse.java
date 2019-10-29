package com.lvs.profiler.util;


import alexh.weak.Dynamic;
import com.lvs.profiler.controller.Response;

import java.net.UnknownServiceException;
import java.util.List;

class AmenityResponse implements Chain {

    private Chain nextInChain;

    @Override
    public void setNextChain(Chain nextInChain) {
        nextInChain = nextInChain;
    }

    @Override
    public Response getResponse(Dynamic payload) {
        if (payload.get("user").get("amenities").isPresent()) {
            return new Response(payload.get("user").get("doc_count").as(Integer.class), payload.dget("user.amenities.buckets").as(List.class));
        }  else {
            try {
                throw new UnknownServiceException("Entity chain not defined. Please contact support");
            } catch (UnknownServiceException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
