package com.userservice.controller;

import com.userservice.model.room.RoomResponse;
import com.userservice.model.room.StudentRoom;
import com.userservice.model.test.Test;
import com.userservice.model.user.Role;
import com.userservice.model.user.User;
import com.userservice.service.*;
import io.micrometer.core.annotation.Timed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/v1/test")
@RequiredArgsConstructor
public class RoomController {

    private final HashGenerator hashGenerator;
    private final RoomService roomService;
    private final UserService userService;
    private final TestService testService;

    @GetMapping("/home")
    @Timed("homeMethod")
    public String home(Model model) {
        List<Test> all = testService.findAll();
        model.addAttribute("tests", all);
        return "home";
    }

    @GetMapping("/personalArea")
    public String personalArea(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/v1/test/security/login";
        }
        User user = userService.findUserByToken(session);
        if (user.getRole() == Role.TEACHER) {
            List<StudentRoom> allStudentRoomsByEmail = roomService.findAllStudentRoomsByEmail(user.getEmail());
            model.addAttribute("rooms", allStudentRoomsByEmail);
            model.addAttribute("user", user);
            return "teacherPersonalArea";
        }
        if (user.getRole() == Role.STUDENT) {
            User userByEmail = userService.findUserByToken(session);
            List<StudentRoom> allStudentsRooms = roomService.getAllStudentsRooms((int) userByEmail.getId());
            model.addAttribute("user", user);
            model.addAttribute("rooms", allStudentsRooms);
            return "studentPersonalArea";
        }
        return "redirect:/v1/test/security/login";
    }


    @GetMapping("/createRoom")
    @Timed("createRoomMethod")
    public String createRoom(Model model) {
        model.addAttribute("room", new StudentRoom());
        return "createRoom";
    }

    @PostMapping("/createRoom")
    public String saveRoom(@Valid @ModelAttribute("room") StudentRoom room, BindingResult bindingResult,
                           Model model,HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "redirect:/v1/test/createRoom";
        }
        String email = (String) session.getAttribute("email");
        room.setUniqueCode(hashGenerator.generateHash());
        room.setOwnerEmail(email);
        roomService.createRoom(room);
        return "redirect:/v1/test/personalArea";
    }

    @GetMapping("/roomInfo/{uniqueCode}")
    public String roomInfo(@PathVariable String uniqueCode, Model model, HttpSession session) {
        StudentRoom studentRoom = roomService.getStudentRoomInfoByUniqueCode(uniqueCode);
        model.addAttribute("room", studentRoom);
        String email = (String) session.getAttribute("email");
        String ownerEmail = studentRoom.getOwnerEmail();
        List<Test> tests = testService.findAllByCreatorEmail(ownerEmail).orElse(Collections.emptyList());
        model.addAttribute("tests", tests);
        User userByEmail = userService.findUserByToken(session);
        model.addAttribute("user", userByEmail);
        return "roomInfo";
    }

    @GetMapping("/entrance")
    public String entrance(Model model) {
        model.addAttribute("roomUniqueCode",String.class);
        return "entrance";
    }

    @PostMapping("/comeInRoom")
    public String comeInRoom(@ModelAttribute("roomUniqueCode") String uniqueCode,Model model,HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/v1/test/security/login";
        }
        User userByEmail = userService.findUserByToken(session);
        roomService.saveStudentInRoom(uniqueCode, userByEmail);
        return "redirect:/v1/test/personalArea";
    }


}
