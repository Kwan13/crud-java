package br.com.kawan.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity<Object> create(@RequestBody UserModel userModel) {
      var user = this.userRepository.findByUsername(userModel.getUsername());

      if(user != null){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já existe");
      }

      var passwordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

      userModel.setPassword(passwordHashred);

      var userCreated =  this.userRepository.save(userModel);

      return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }   
}
