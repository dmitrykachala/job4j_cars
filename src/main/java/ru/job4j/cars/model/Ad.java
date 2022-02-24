package ru.job4j.cars.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ad")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    private String description;
    private String pictureLink;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean sold;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ad ad = (Ad) o;
        return id == ad.id && sold == ad.sold && Objects.equals(created, ad.created)
                && Objects.equals(description, ad.description)
                && Objects.equals(pictureLink, ad.pictureLink)
                && Objects.equals(car, ad.car) && Objects.equals(user, ad.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created, description, pictureLink, car, user, sold);
    }

    @Override
    public String toString() {
        return "Ad{" + "id=" + id + ", created=" + created + ", description='" + description
                + '\'' + ", pictureLink='" + pictureLink + '\'' + ", car=" + car + ", user="
                + user + ", sold=" + sold + '}';
    }
}
