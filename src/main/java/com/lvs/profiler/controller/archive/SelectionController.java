package com.lvs.profiler.controller.archive;

import com.lvs.profiler.model.Selection;
import com.lvs.profiler.service.SelectionService;
import lombok.RequiredArgsConstructor;
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
     * @param pageable
     * @method GET
     * @return result as list of paginated selectionObject
     */
    @GetMapping(value="selection/{user_id}", produces= MediaType.APPLICATION_JSON_VALUE)
    public Page<Selection> findSelectionsByUser(@PathVariable int user_id, Pageable pageable) {

        return selectionService.findByUserId(user_id, pageable);

    }

}
