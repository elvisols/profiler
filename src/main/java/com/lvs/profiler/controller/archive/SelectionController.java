package com.lvs.profiler.controller.archive;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lvs.profiler.controller.Response;
import com.lvs.profiler.model.Selection;
import com.lvs.profiler.service.SelectionService;
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
public class SelectionController {

    private final SelectionService selectionService;

    private final Response response;

    /**
     * This method fetches all selection
     *
     * @param pageable
     * @method GET
     * @return result as list of paginated selectionObject
     */
    @GetMapping(value="selection", produces= MediaType.APPLICATION_JSON_VALUE)
    public Page<Selection> findSelections(Pageable pageable) {

        return selectionService.findAllSelection(pageable);

    }

    /**
     * This method fetches all selection
     *
     * @param top top clicked Amenity
     * @param user_id
     * @method GET
     * @return result as list of paginated selectionObject
     */
    @GetMapping(value="selection/{user_id}", produces= MediaType.APPLICATION_JSON_VALUE)
    public Response findSelectionsByUser(@PathVariable int user_id, @RequestParam(value = "top", defaultValue="5") int top) throws JsonProcessingException {

        SearchResponse searchResponse = selectionService.findByUserAmenity(user_id, top);

        return response.fetch(searchResponse);

    }

}
