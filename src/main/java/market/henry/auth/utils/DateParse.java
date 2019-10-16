package market.henry.auth.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DateParse {
    LocalDateTime startDate;
    LocalDateTime endDate;
}
