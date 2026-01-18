package mymoney.entity;

public enum ExpenseCategories {

    FOODSTUFF("Продукты", "#D45C6B"),
    TRANSPORT("Транспорт", "#64187f"),
    RESTAURANTS("Кафе и рестораны", "#C9899E"),
    ENTERTAINMENT("Развлечения", "#C67BF5"),
    HOUSE("Товары для дома", "#5C7298"),
    UTILITIES("Коммуналка", "#8B5F9E"),
    SPORT("Спорт", "#176A7C"),
    EDUCATION("Образование","#2B3A8C"),
    MEDICINE("Медицина", "#2ED4C6"),
    OTHER("Другое", "#362B44");

    private final String name;
    private final String color;

    ExpenseCategories(String name, String color){
        this.name = name;
        this.color = color;
    }

    public String getName(){
        return name;
    }

    public String getColor(){
        return color;
    }

}
