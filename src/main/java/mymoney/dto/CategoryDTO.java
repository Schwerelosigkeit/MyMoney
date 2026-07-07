package mymoney.dto;

public class CategoryDTO {
    private String value;
    private String name;
    private String color;

    public CategoryDTO(String value, String name, String color) {
        this.value = value;
        this.name = name;
        this.color = color;
    }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

}
