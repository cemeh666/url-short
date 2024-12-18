package url.shortner.url_shortner.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUrlDto {
    @NotNull(message = "Url should not be null")
    @URL(message = "URL format incorrect")
    @Length(max = 255, message = "Maximum number of characters 255 chars")
    private String url;
}
