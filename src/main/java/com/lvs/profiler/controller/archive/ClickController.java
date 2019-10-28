package com.lvs.profiler.controller.archive;

import com.lvs.profiler.model.Click;
import com.lvs.profiler.service.ClickService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchResponse;
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
     * This method fetches all click by user hotel
     *
     * @param top Top clicked hotel
     * @param user_id user id
     * @method GET
     * @return result as list of paginated clickObject
     */
    @GetMapping(value="click/{user_id}/hotel", produces= MediaType.APPLICATION_JSON_VALUE)
    public SearchResponse findByUserHotel(@PathVariable int user_id, @RequestParam(value = "top", defaultValue="5") int top) {

        return clickService.findByUserHotel(user_id, top);

    }

    /**
     * This method fetches all click by user hotel region
     *
     * @param top  top clicked hotel region
     * @param user_id
     * @method GET
     * @return result as list of paginated clickObject
     */
    @GetMapping(value="click/{user_id}/hotel-region", produces= MediaType.APPLICATION_JSON_VALUE)
    public SearchResponse findByUserHotelRegion(@PathVariable int user_id, @RequestParam(value = "top", defaultValue="5") int top) {

        return clickService.findByUserHotelRegion(user_id, top);

    }

}
