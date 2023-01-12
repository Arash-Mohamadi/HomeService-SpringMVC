package com.example.homeservicespringmvc.service.impl;

import com.example.homeservicespringmvc.entity.capability.*;
import com.example.homeservicespringmvc.entity.enums.OrderStatus;
import com.example.homeservicespringmvc.entity.enums.SpecialistStatus;
import com.example.homeservicespringmvc.entity.enums.UserRole;
import com.example.homeservicespringmvc.entity.users.Manager;
import com.example.homeservicespringmvc.entity.users.Specialist;
import com.example.homeservicespringmvc.exception.CustomizedEmailException;
import com.example.homeservicespringmvc.exception.CustomizedInvalidStatusException;
import com.example.homeservicespringmvc.exception.CustomizedNotFoundException;
import com.example.homeservicespringmvc.repository.SpecialistRepository;
import com.example.homeservicespringmvc.security.email.EmailSender;
import com.example.homeservicespringmvc.security.email.EmailSenderImpl;
import com.example.homeservicespringmvc.service.*;

import static com.example.homeservicespringmvc.specification.SpecialistSpecification.*;

import com.example.homeservicespringmvc.util.TokenUtil;
import com.example.homeservicespringmvc.validation.HibernateValidatorProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpecialistServiceImpl implements SpecialistService {

    private final ApplicationContext applicationContext;
    private final SpecialistRepository specialistRepository;
    private final CreditService creditService;
    private final OrderService orderService;
    private final SubServicesService subServicesService;

    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final EmailSender emailSender;

    @Override
    @Transactional
    public void signup(Specialist specialist) {
        HibernateValidatorProvider.checkEntity(specialist);
        if (!isExistSpecialist(specialist)) {
            fillSpecialist(specialist); // fill specialist and save
            String tokenValue = TokenUtil.getTokenUtil(tokenService).generate(null, specialist, null);
            String link = "http://localhost:8080/api/v1/registration/confirm?token=" + tokenValue;
            emailSender.send(specialist.getEmail(),
                    EmailSenderImpl.buildEmail(specialist.getFirstname(),link ));
        }
    }

    @Override
    @Transactional
    public void editPassword(String username, String password) {
        Specialist specialist = fetchSpecialist(username);
        specialist.setPassword(password);
        HibernateValidatorProvider.checkEntity(specialist);
        specialist.setPassword(passwordEncoder.encode(specialist.getPassword()));
        specialistRepository.save(specialist);
    }

    @Override
    @Transactional
    public void sendSuggestion(Suggestion suggestion, Long orderId, String specialistUsername) {
        Order order;
        Specialist specialist = fetchSpecialist(specialistUsername);
        if (specialist.getStatus() == SpecialistStatus.CONFIRMED) {
            order = fetchOrder(orderId);
            if (order != null && (order.getStatus() == OrderStatus.PENDING_FOR_SPECIALIST_SUGGESTION
                    || order.getStatus() == OrderStatus.PENDING_FOR_SPECIALIST_SELECTION)) {
                getSuggestionService().sendSuggestion(suggestion, order, specialist);
            }
        } else {
            throw new CustomizedNotFoundException("desired specialist not found");
        }
    }

    @Override
    @Transactional
    public void createSpecialist(Specialist specialist) {
        specialistRepository.save(specialist);
    }

    @Override
    @Transactional
    public void selectSpecialist(Long suggestionId, Long orderId) {
        Order order = fetchOrder(orderId);
        Suggestion suggestion = fetchSuggestion(suggestionId);
        order.setStatus(OrderStatus.PENDING_FOR_SPECIALIST_COMING);
        order.setSpecialist(suggestion.getSpecialist());
        order.setSelectedSuggestion(suggestion);
    }

    @Override
    @Transactional
    public void addSpecialistToSubService(String specialistUsername, String subServiceName) {
        SubServices subServicesFound = fetchSubService(subServiceName);
        Specialist specialistFound = fetchSpecialist(specialistUsername);
        if (specialistFound.getStatus() == SpecialistStatus.CONFIRMED) {
            specialistFound.addSubService(subServicesFound);
        } else
            throw new CustomizedInvalidStatusException("specialist should CONFIRMED");
    }


    @Override
    @Transactional
    public void removeSpecialistOfSubServices(String subServiceName, String specialistUsername) {
        SubServices subServicesFound = fetchSubService(subServiceName);
        Specialist specialistFound = fetchSpecialist(specialistUsername);
        if (specialistFound.getStatus() == SpecialistStatus.CONFIRMED) {
            specialistFound.getSubServicesSet().remove(subServicesFound);
            specialistRepository.save(specialistFound);  // update
        } else
            throw new CustomizedInvalidStatusException("specialist should CONFIRMED");
    }

    @Override
    @Transactional
    public void confirmSpecialist(String specialistUsername) {
        Specialist specialistFound = specialistRepository.findSpecialistByUsername(specialistUsername)
                .orElseThrow(() -> new CustomizedNotFoundException("specialist not found"));
        specialistFound.setStatus(SpecialistStatus.CONFIRMED);
        specialistRepository.save(specialistFound); // update
    }

    @Override
    @Transactional
    public void setAvatar(MultipartFile image, String username) {
        byte[] avatar;
        try {
            avatar = checkFile(image);
        } catch (IOException io) {
            throw new CustomizedNotFoundException("image not found ");
        }
        Specialist specialistFound = fetchSpecialist(username);
        specialistFound.setPhoto(avatar);
    }

    @Override
    @Transactional
    public List<Specialist> findSpecialistByScoreAvg(Integer score) {
        return specialistRepository.fetchSpecialistByScoreAvg(score);
    }

    @Override
    @Transactional
    public List<Specialist> loadAll() {
        return specialistRepository.findAll();
    }

    @Override
    @Transactional
    public Opinion showOpinion(Long orderId) {
        return orderService.fetchOrderById(orderId)
                .orElseThrow(() -> new CustomizedNotFoundException("order not exist"))
                .getOpinion();
    }

    @Override
    @Transactional
    public List<Order> showOrderBelongToSpecialist(Long id) {

        return orderService.showOrderBelongToSpecialist(id);
    }

    @Override
    @Transactional
    public List<Order> showOrderBelongToSubService(Long subServiceId) {
        return orderService.showOrderBelongToSubService(subServiceId);
    }

    @Override
    @Transactional
    public List<Specialist> searchSpecialistByPersonalInfo(Map<String, String> info) {
        return specialistRepository.findAll(searchSpecialist(info));
    }

    @Override
    @Transactional
    public List<Specialist> searchSpecialistByType(String type) {
        return specialistRepository.findAll(hasRole(type));
    }

    @Override
    @Transactional
    public List<Specialist> searchSpecialistByScore(String score) {
        return specialistRepository.findAll(greaterThanOrEqualToScore(Integer.parseInt(score)));
    }

    @Override
    @Transactional
    public List<Specialist> searchSpecialistBySubService(SubServices subServices) {
        return specialistRepository.findAll(containSubService(subServices));
    }

    @Override
    @Transactional
    public Credit showBalance(Long specialistId) {
        return specialistRepository.viewBalance(specialistId);
    }

    @Override
    @Transactional
    public List<Order> showOrders(String username) {
        return specialistRepository.viewOrders(username);
    }

    @Override
    @Transactional
    public List<Specialist> showAllSpecialistByDateOfSignup(LocalDateTime dateOfSignup) {
        return specialistRepository.findAll(dateOfSignupReport(dateOfSignup));
    }

    @Override
    @Transactional
    public List<Specialist> greaterThanOrEqualOrders(Long count) {
        return specialistRepository.findAll(greaterThanOrEqualOrder(count));
    }

    @Override
    @Transactional
    public String confirmTokenBySpecialist(String tokenValue) {
        Token tokenFound = tokenService.getToken(tokenValue);
        TokenUtil.getTokenUtil(tokenService).setConfirmationAtToken(tokenFound);
        enableSpecialist(tokenFound.getCustomer().getEmail());
        return " activated ,thank you , please waiting for confirm manager";
    }

    private boolean isExistSpecialist(Specialist specialist) {
        if (specialistRepository.findSpecialistByUsername(specialist.getUsername()).isPresent()) {
            throw new RuntimeException("username is duplicate");
        }
        if (specialistRepository.findSpecialistByEmail(specialist.getEmail()).isPresent()) {
            throw new RuntimeException("email is duplicate");
        }
        return false;
    }

    private void fillSpecialist(Specialist specialist) {
        Credit credit = new Credit();
        specialist.setUserType(UserRole.SPECIALIST);
        specialist.setStatus(SpecialistStatus.PENDING_CONFIRMATION);
        specialist.setCredit(credit);
        specialist.setAvg(0);
        specialist.setPassword(passwordEncoder.encode(specialist.getPassword()));
        specialistRepository.save(specialist); // save
    }

    private byte[] checkFile(MultipartFile file) throws IOException {
        byte[] bFile = null;
        if (file.getSize() / 1024 <= 300 && file.getOriginalFilename().endsWith(".jpg")) {
            bFile = file.getBytes();
        }
        return bFile;
    }

    private SuggestionService getSuggestionService() {
        return applicationContext.getBean(SuggestionServiceImpl.class);
    }

    private SubServices fetchSubService(String subServiceName) {
        return subServicesService.fetchSubServiceWithName(subServiceName).orElseThrow(
                () -> new CustomizedNotFoundException("this subService notfound"));
    }

    private Specialist fetchSpecialist(String specialistUsername) {
        return specialistRepository.findSpecialistByUsername(specialistUsername)
                .orElseThrow(() -> new CustomizedNotFoundException("this specialist notfound"));
    }

    private Order fetchOrder(Long orderId) {
        return orderService.fetchOrderById(orderId).orElseThrow(
                () -> new CustomizedNotFoundException("desired order not found ."));
    }

    private Suggestion fetchSuggestion(Long suggestionId) {
        return getSuggestionService().findSuggestionWithId(suggestionId)
                .orElseThrow(() -> new CustomizedNotFoundException("suggestion notfound"));
    }

    private void enableSpecialist(String email) {
        Specialist specialist = specialistRepository.findSpecialistByEmail(email)
                .orElseThrow(() -> new CustomizedEmailException("email noy found"));
        specialist.setEnabled(true);
        specialistRepository.save(specialist);

    }


}
