package com.lvs.profiler.controller.archive;

import com.lvs.profiler.model.Click;
import com.lvs.profiler.service.ClickService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("archive")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClickController {

    private final ClickService clickService;

    /**
     * This method fetches all click
     *
     * @param pageable
     * @method GET
     * @return result as list of paginated clickObject
     */
    @GetMapping(value="click", produces= MediaType.APPLICATION_JSON_VALUE)
    public Page<Click> findClicks(Pageable pageable) {

        return clickService.findAllClick(pageable);

    }

    /**
     * This method fetches all click
     *
     * @param pageable
     * @method GET
     * @return result as list of paginated clickObject
     */
    @GetMapping(value="click/{user_id}", produces= MediaType.APPLICATION_JSON_VALUE)
    public Page<Click> findClicksByUser(@PathVariable int user_id, Pageable pageable) {

        return clickService.findByUserId(user_id, pageable);

    }

}
