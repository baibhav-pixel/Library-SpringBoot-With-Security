package com.example.library.Services;

import com.example.library.Models.*;
import com.example.library.Repositories.StudentRepository;
import com.example.library.Requests.PlaceRequest;
import com.example.library.Requests.StudentCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;

@Service
public class StudentService {

    @Value("${STUDENT_ONLY_AUTHORITY}")
    private String STUDENT_ONLY_AUTHORITY;

    @Value("${BOOK_PLACE_REQUEST_AUTHORITY}")
    private String BOOK_PLACE_REQUEST_AUTHORITY;

    private String delimiter=":";
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AdminService adminService;

    @Autowired
    RequestService requestService;
    @Autowired
    BookService bookService;
    @Autowired
    MyUserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    public Student createStudent(StudentCreateRequest studentCreateRequest) {
        //Allow User to be created first and then Student
        User userFromControllerRequest = studentCreateRequest.toUser();
        attachAuthorities(userFromControllerRequest);
        encodePassword(userFromControllerRequest);
        User savedUser = userService.saveUser(userFromControllerRequest);
        return studentRepository.save(studentCreateRequest.to(savedUser));
    }

    public String placeRequest(PlaceRequest placeRequest, int studentId) throws Exception {

        List<Admin> adminList = adminService.getAdmins();
        Book book = bookService.getBookById(placeRequest.getBookId());

        Admin admin = adminList.stream()
                .min(Comparator.comparingInt(a -> a.getRequestsToProcess().size()))
                .orElse(null);

        //If someone returns an already returned book and admin approves then throw error
        if(placeRequest.toRequest(studentId).getRequestType() == RequestType.RETURN && book.getStudent()==null
        && placeRequest.toRequest(studentId).getRequestStatus()== RequestStatus.PENDING)
        {
            placeRequest.toRequest(studentId).setRequestStatus(RequestStatus.REJECTED);
            throw new Exception("Can't return a book which is not issued");
        }
        return requestService.saveOrUpdateRequest(placeRequest.toRequest(admin,studentId)).getRequestId();
    }

    public void attachAuthorities(User user){
        String authorities = STUDENT_ONLY_AUTHORITY + delimiter + BOOK_PLACE_REQUEST_AUTHORITY;
        user.setAuthorities(authorities);
    }

    public void encodePassword(User user){
        String rawPwd = user.getPassword();
        String encodedPwd = passwordEncoder.encode(rawPwd);
        user.setPassword(encodedPwd);
    }

    public Student getStudentById(Integer id) {
        return studentRepository.findById(id).orElse(null);
    }
}
