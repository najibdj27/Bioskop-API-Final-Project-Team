package com.teama.bioskop.Controllers;

import com.teama.bioskop.Handlers.ResponseHandler;
import com.teama.bioskop.Helpers.DataNotFoundException;
import com.teama.bioskop.Models.Seats;
import com.teama.bioskop.Services.SeatsService;
import lombok.AllArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class SeatsController {

    private final SeatsService seatsService;
    private static final Logger logger = LogManager.getLogger(SeatsController.class);


    /***
     * Crud Seats
     * @param model
     * @return
     */
    @GetMapping("/crud/seats/{pageNo}")
    public String getAll(Model model, @RequestParam(value="searchby", required = false) String studioName, @RequestParam(value="sortby", required = false) String sortby, @RequestParam(value="order", required = false) String order,@PathVariable("pageNo") int pageNo) throws DataNotFoundException {
        try {
            int pageSize = 10;
            Page<Seats> page = seatsService.getAllSeatsPaged(studioName, pageNo, pageSize, sortby, order);
            List<Seats> seatsList = page.getContent();
//        Collections.reverse(seatsList);
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("seats", seatsList);
            model.addAttribute("searchby", studioName);
            model.addAttribute("sortby", sortby);
            model.addAttribute("order", order);
            model.addAttribute("reset", "/crud/seats/1");
            model.addAttribute("newSeats", new Seats());
            model.addAttribute("updateSeats", new Seats());
            logger.info("--------------------------");
            logger.info("Get All Seats Data " + seatsList);
            logger.info("--------------------------");
            ResponseHandler.generateResponse("Successfully retrieve all Seats!", HttpStatus.OK, seatsList);
            return "seats-crud";
        } catch (Throwable e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("errorStatus", HttpStatus.INTERNAL_SERVER_ERROR);
            ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
            return "seats-crud";
        }
    }

    /***
     * Create Seats
     * @param model
     * @param seats
     * @return
     */
    @PostMapping("/create/seat")
    public String createNew(Model model, @ModelAttribute Seats seats){
        try{
        seatsService.insertNewSeats(seats);
        logger.info("-----------------------------------");
        logger.info("Create seats "+seats);
        logger.info("-----------------------------------");
        ResponseHandler.generateResponse("Successfully Create Seats!", HttpStatus.OK, seats);
        return "redirect:/crud/seats/1";
        }catch (Throwable err){
            model.addAttribute("error", err.getMessage());
            model.addAttribute("errorStatus", HttpStatus.BAD_REQUEST);
            ResponseHandler.generateResponse(err.getMessage(), HttpStatus.BAD_REQUEST,null);
            return "error-page";
        }
    }

    /***
     * Update Seats
     * @param model
     * @param id
     * @param seats
     * @return
     */
    @PutMapping("/update/seat/{id}")
    public String updateById(Model model, @PathVariable("id") Integer id, @ModelAttribute Seats seats) {
        try{
        seats.setSeatId(id);
        seatsService.UpdateSeats(seats);
            logger.info("-----------------------------------");
            logger.info("Update Seats "+seats);
            logger.info("-----------------------------------");
            ResponseHandler.generateResponse("Successfully Update Seats", HttpStatus.OK, seats);
            return "redirect:/crud/seats/1";
        }catch (Throwable err){
            model.addAttribute("error", err.getMessage());
            model.addAttribute("errorStatus", HttpStatus.BAD_REQUEST);
            ResponseHandler.generateResponse(err.getMessage(), HttpStatus.BAD_REQUEST, null);
            return "error-page";
        }
    }

    /***
     * Delete Seats
     * @param model
     * @param id
     * @return
     * @throws DataNotFoundException
     */
    @DeleteMapping("/delete/seat/{id}")
    public String deleteById(Model model, @PathVariable("id") Integer id) throws DataNotFoundException {
        try{
        seatsService.deleteSeatById(id);
        logger.info("-----------------------------------");
        ResponseHandler.generateResponse("Successfully Delete Seats", HttpStatus.OK, null);
        return "redirect:/crud/seats/1";
        }catch (Throwable err){
            model.addAttribute("error", err.getMessage());
            model.addAttribute("errorStatus", HttpStatus.INTERNAL_SERVER_ERROR);
            ResponseHandler.generateResponse(err.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
            return "error-page";
        }

    }

}




