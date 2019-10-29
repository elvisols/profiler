package com.lvs.profiler.util;

import alexh.weak.Dynamic;
import com.lvs.profiler.controller.Response;

public interface Chain {

    void setNextChain(Chain nextChain);

    Response getResponse(Dynamic payload);

}
