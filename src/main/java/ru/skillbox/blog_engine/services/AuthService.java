package ru.skillbox.blog_engine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import ru.skillbox.blog_engine.config.AppConfig;
import ru.skillbox.blog_engine.dto.AuthorizeUserRequest;
import ru.skillbox.blog_engine.dto.PasswordResetRequest;
import ru.skillbox.blog_engine.dto.RegisterUserRequest;
import ru.skillbox.blog_engine.model.User;
import ru.skillbox.blog_engine.repository.UserRepository;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private Environment environment;

    public User loginUser(AuthorizeUserRequest user) {
        final String email = user.getEmail();
        final String password = user.getPassword();
        User userFromDB = userRepository.findByEmail(email);
        final String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        appConfig.addSession(sessionId, userFromDB.getId());
        return userFromDB;
    }

    public User checkUserIsAuthorized() {
        return getAuthorizedUser().orElseThrow(IllegalAccessError::new);
    }

    public void logoutUser() {
        final String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        if (isAuthorized(sessionId)) {
            appConfig.deleteSessionById(sessionId);
        }
    }

    public Optional<User> getAuthorizedUser() {
        final String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        if (isAuthorized(sessionId)) {
            int userId = appConfig.getUserIdBySessionId(sessionId);
            return userRepository.findById(userId);
        }
        return Optional.empty();
    }

    public boolean restoreUserPassword(String userEmail) {
        User userFromDB = userRepository.findByEmail(userEmail);
        final String code = UUID.randomUUID().toString();
        if (userFromDB == null) {
            return false;
        }
        userFromDB.setCode(code);
        User updatedUser = userRepository.save(userFromDB);
        final String port = environment.getProperty("server.port");
        final String hostName = InetAddress.getLoopbackAddress().getHostName();
        final String url = String.format("http://%s:%s", hostName, port);

        mailSenderService.send(updatedUser.getEmail(), "Ссылка на восстановление пароля",
                String.format("Для восстановления пароля, пройдите по этой ссылке: " +
                        "%s/login/change-password/%s", url, code));
        return true;
    }

    public boolean resetUserPassword(PasswordResetRequest passwordResetRequest) {
        User userFromDB = userRepository.findByCode(passwordResetRequest.getCode());
        if (userFromDB == null) {
            return false;
        }
        userFromDB.setCode(null);
        userFromDB.setPassword(passwordResetRequest.getPassword());
        userRepository.save(userFromDB);
        return true;
    }

    public User registerUser(RegisterUserRequest request) {
        User newUser = new User();

        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());
        newUser.setRegTime(LocalDateTime.now());

        userRepository.save(newUser);
        return newUser;
    }

    private boolean isAuthorized(String sessionId) {
        return appConfig.getSessions().containsKey(sessionId);
    }

    private boolean isValidPassword(String passwordFromForm, String passwordFromDb) {
        return passwordFromForm.equals(passwordFromDb);
    }
}