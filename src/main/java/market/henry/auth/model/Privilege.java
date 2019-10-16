package market.henry.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import market.henry.auth.enums.UserType;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Privilege implements Serializable, Comparable, GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<UserType> userTypes;

    public Privilege(String name, Set<UserType> userTypes) {
        this.name = name;
        this.userTypes = userTypes;
    }

    @Override
    public int compareTo(Object o) {
        return name.compareTo(o.toString());
    }

    @Override
    public String toString() {
        return name;
    }
    @JsonIgnore
    @Override
    public String getAuthority() {
        return name;
    }
}
