package nanox.products.products_test.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "meta_id")
    private Meta meta;

    private String title;

    @ElementCollection
    @CollectionTable(name = "image", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "path")
    private List<String> images;

    private double rating;
    private double price;

    @ElementCollection
    @CollectionTable(name = "tag", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "name")
    private List<String> tags;

}