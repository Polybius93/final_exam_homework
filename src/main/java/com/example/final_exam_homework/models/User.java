package com.example.final_exam_homework.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seller")
    private List<Item> itemToSellList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "buyer")
    private List<Item> itemToBuyList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Bid> bidList;

    private Long greenBayDollars;

    public User() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Item> getItemToSellList() {
        return itemToSellList;
    }

    public void setItemToSellList(List<Item> itemList) {
        this.itemToSellList = itemList;
    }

    public Long getGreenBayDollars() {
        return greenBayDollars;
    }

    public void setGreenBayDollars(Long greenBayDollars) {
        this.greenBayDollars = greenBayDollars;
    }
}
