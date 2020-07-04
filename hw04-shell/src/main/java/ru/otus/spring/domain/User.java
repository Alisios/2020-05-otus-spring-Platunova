package ru.otus.spring.domain;


import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String name;
    private String surname;
    private String res;

    public boolean getIsTested() {
        return isTested;
    }

    public void setIsTested(boolean isTested) {
        this.isTested = isTested;
    }

    private boolean isTested;

    @Override
    public String toString() {
        return name + ' ' + surname + ":\n" + res;
    }
}
