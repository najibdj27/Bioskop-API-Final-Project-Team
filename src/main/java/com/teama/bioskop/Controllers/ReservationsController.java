package com.teama.bioskop.Controllers;

// import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.teama.bioskop.Helpers.DataNotFoundException;
import com.teama.bioskop.Models.Reservations;
import com.teama.bioskop.Models.Schedule;
import com.teama.bioskop.Models.Users;
import com.teama.bioskop.Services.ReservationsService;
import com.teama.bioskop.Services.ScheduleService;
import com.teama.bioskop.Services.UsersService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class ReservationsController {
    private final ReservationsService reservationsService;
    private final UsersService usersService;
    private final ScheduleService scheduleService;

    /***
     * Get All data of reservations entity
     * @param model
     * @return
     * @throws DataNotFoundException
     */
    @GetMapping("/crud/reservations/{pageNo}")
    public String getAll(Model model,  @RequestParam(value="searchby", required = false) String username, @RequestParam(value="sortby", required = false) String sortby, @RequestParam(value="order", required = false) String order, @PathVariable("pageNo") int pageNo) throws DataNotFoundException{
        try {
            int pageSize = 10;
            Page<Reservations> page = reservationsService.getAllUsersPaged(username, pageNo, pageSize, sortby, order);
            List<Reservations> reservList = page.getContent();
            // Collections.reverse(reservList);
            List<Users> userList = usersService.getAllUsers();
            List<Schedule> scheduleList = scheduleService.getAllSchedule();
            model.addAttribute("error", false);
            model.addAttribute("message", null);
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("reservations", reservList);
            model.addAttribute("users", userList);
            model.addAttribute("searchby", username);
            model.addAttribute("sortby", sortby);
            model.addAttribute("order", order);
            model.addAttribute("reset", "/crud/users/1");
            model.addAttribute("newReserv", new Reservations());
            model.addAttribute("schedules", scheduleList);
            return "reservations-crud";
        }catch(Exception e) {
            model.addAttribute("error", false);
            model.addAttribute("message", e.getMessage());
            model.addAttribute("reservations", null);
            return "reservations-crud/1";
        }
    }

    /***
     * Create new data for reservations entity
     * @param model
     * @param reservations
     * @return
     * @throws DataNotFoundException
     */
    @PostMapping("/create/reservation")
    public String createNew(Model model, @ModelAttribute Reservations reservations) throws DataNotFoundException{
        try {
            reservationsService.createReservation(reservations);
            model.addAttribute("error", false);
            model.addAttribute("message", "Success created new reservation!");
            return "redirect:/crud/reservations/1";
        } catch (DataNotFoundException e) {
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            return "redirect:/crud/reservations/1";
        }
    }

    /***
     * Update reservations entity by id
     * @param model
     * @param id
     * @param reservations
     * @return
     * @throws DataNotFoundException
     */
    @PutMapping("/update/reservation/{id}")
    public String updateById(Model model, @PathVariable("id") Integer id, @ModelAttribute Reservations reservations) throws DataNotFoundException{
        try {
            reservations.setReservationId(id);
            reservationsService.updateReservation(reservations);
            model.addAttribute("error", false);
            model.addAttribute("message", "Successfully updated reservation with id "+id+"!");
            return "redirect:/crud/reservations/1";
        } catch (DataNotFoundException e) {
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            return "redirect:/crud/reservations/1";
        }
    }

    /***
     * Delete reservations entity by id
     * @param model
     * @param id
     * @param reservations
     * @return
     */
    @DeleteMapping("/delete/reservation/{id}")
    public String deleteById(Model model, @PathVariable("id") Integer id, @ModelAttribute Reservations reservations){
        try {
            reservations.setReservationId(id);
            reservationsService.deleteReservation(reservations);
            model.addAttribute("error", false);
            model.addAttribute("message", "Successfully deleted reservation with id "+id+"!");
            return "redirect:/crud/reservations/1";
        } catch (DataNotFoundException e) {
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            return "redirect:/crud/reservations/1";
        }
    }
}
