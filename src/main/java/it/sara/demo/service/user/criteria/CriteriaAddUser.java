package it.sara.demo.service.user.criteria;

import it.sara.demo.service.criteria.GenericCriteria;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriteriaAddUser extends GenericCriteria {
    private String firstName;
    private String lastName;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Email non valida")
    @Size(max = 254, message = "L'email non può superare 254 caratteri")
    private String email;

    @NotBlank(message = "Il numero di telefono è obbligatorio")
    @Pattern(
            // Standard italiano:
            // - prefisso +39 opzionale
            // - mobile: 3xx + 6–8 cifre (es. 333 1234567)
            // - fisso: 0 + distretto (1–4 cifre) + 5–8 cifre (es. 06 1234567, 011 1234567)
            regexp = "^(\\+39)?\\s?(0\\d{1,4}\\s?\\d{5,8}|3\\d{2}\\s?\\d{6,8})$",
            message = "Numero di telefono italiano non valido"
    )
    @Size(max = 20, message = "Il numero di telefono è troppo lungo")
    private String phoneNumber;

}
