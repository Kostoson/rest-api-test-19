package models;

import lombok.Data;

@Data
public class LoginRequestBody {
    String email, password;
}
