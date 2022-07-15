package com.teama.bioskop.Controllers;

import java.util.List;

import com.teama.bioskop.Handlers.ResponseHandler;
import com.teama.bioskop.Models.Films;
import com.teama.bioskop.Services.FilmsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.teama.bioskop.Helpers.DataNotFoundException;
import com.teama.bioskop.Models.Schedule;
import com.teama.bioskop.Services.ScheduleService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class ScheduleController {
    @Autowired
    private final ScheduleService scheduleService;
    private final FilmsService filmsService;
    private static final Logger logger = LogManager.getLogger();

    /***
     * Get all schedule from Schedule Table
     * @return List of schedule
     */
    @GetMapping("/crud/schedule/{pageNo}")
    public String getAll(Model model, @RequestParam(value="searchby", required = false) Double price, @RequestParam(value="sortby", required = false) String sortby, @RequestParam(value="order", required = false) String order, @PathVariable("pageNo") int pageNo) throws DataNotFoundException{
        try{
            int pageSize = 10;
            Page<Schedule> page = scheduleService.getAllSchedulePaged(price, pageNo, pageSize, sortby, order);
            List<Films> filmsList = this.filmsService.getAllFilms();
            List<Schedule> scheduleList = page.getContent();
            //Collections.reverse(scheduleList);
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("films",filmsList);
            model.addAttribute("schedules", scheduleList);
            model.addAttribute("searchby", price);
            model.addAttribute("sortby", sortby);
            model.addAttribute("order", order);
            model.addAttribute("reset", "/crud/schedule/1");
            model.addAttribute("newSchedule", new Schedule());
            model.addAttribute("updateSchedule", new Schedule());
            logger.info("-----------------------------------");
            logger.info("Get All Schedule Data "+scheduleList);
            logger.info("-----------------------------------");
            ResponseHandler.generateResponse("Successfully retrieve all Schedule!", HttpStatus.OK,scheduleList);
            return "schedule-crud";
        }catch(Throwable e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("errorStatus",HttpStatus.INTERNAL_SERVER_ERROR);
            ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,null);
            return "schedule-crud";
        }
    }

    /***
     * Create new Schedule
     * @param model
     * @return
     */
    @PostMapping("/create/schedule")
    public String createNew(Model model, @ModelAttribute Schedule schedule){
        try{
            this.scheduleService.createSchedule(schedule);
            logger.info("-----------------------------------");
            logger.info("Create Schedule "+schedule);
            logger.info("-----------------------------------");
            ResponseHandler.generateResponse("Successfully Create Schedule!", HttpStatus.OK, schedule);
            return "redirect:/crud/schedule/1";
        }catch (Throwable err){
            model.addAttribute("error", err.getMessage());
            model.addAttribute("errorStatus", HttpStatus.BAD_REQUEST);
            ResponseHandler.generateResponse(err.getMessage(), HttpStatus.BAD_REQUEST,null);
            return "error-page";
        }
    }
    /***
     * Update schedule
     * @param model
     * @param schedule
     * @return
     */
    @PutMapping("/update/schedule/{id}")
    public String updateById(Model model, @PathVariable("id") Integer id, @ModelAttribute Schedule schedule) throws DataNotFoundException{
        try{
            schedule.setScheduleId(id);
            this.scheduleService.updateScheduleById(schedule);
            logger.info("-----------------------------------");
            logger.info("Update Schedule "+schedule);
            logger.info("-----------------------------------");
            ResponseHandler.generateResponse("Successfully Update Schedule", HttpStatus.OK, schedule);
            return "redirect:/crud/schedule/1";
        }catch (Throwable err){
            model.addAttribute("error", err.getMessage());
            model.addAttribute("errorStatus", HttpStatus.BAD_REQUEST);
            ResponseHandler.generateResponse(err.getMessage(), HttpStatus.BAD_REQUEST, null);
            return "error-page";
        }
    }
    /***
     * Delete Schedule
     * @param model
     * @param id
     * @return
     */
    @DeleteMapping("/delete/schedule/{id}")
    public String deleteById(Model model, @PathVariable("id") Integer id) throws DataNotFoundException{
        try{
            Schedule schedule = this.scheduleService.getOneSchedule(id);
            this.scheduleService.deleteScheduleById(schedule);
            logger.info("-----------------------------------");
            ResponseHandler.generateResponse("Successfully Delete Schedule", HttpStatus.OK, null);
            return "redirect:/crud/schedule/1";
        }catch (Throwable err){
            model.addAttribute("error", err.getMessage());
            model.addAttribute("errorStatus", HttpStatus.INTERNAL_SERVER_ERROR);
            ResponseHandler.generateResponse(err.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
            return "error-page";
        }
    }
}
