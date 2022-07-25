package com.javakonst.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "numbers")
@Setter
@Getter
public class NumberByProp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String number;
    private String gender;
    private String case_i;
    private String case_r;
    private String case_d;
    private String case_v;
    private String case_t;
    private String case_p;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberByProp numberByProp = (NumberByProp) o;
        return number.equals(numberByProp.number) &&
                gender.equals(numberByProp.gender) &&
                case_i.equals(numberByProp.case_i) &&
                case_r.equals(numberByProp.case_r) &&
                case_d.equals(numberByProp.case_d) &&
                case_v.equals(numberByProp.case_v) &&
                case_t.equals(numberByProp.case_t) &&
                case_p.equals(numberByProp.case_p);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gender, case_i, case_r, case_d, case_v, case_t, case_p);
    }
}
