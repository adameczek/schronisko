package pl.inzynierka.schronisko.animals.types;

public enum Difficulty {
    EASY("Łatwe"), MEDIUM("Średnie"), HARD("Ciężkie");
    
    public final String value;
    
    Difficulty(String value) {
        this.value = value;
    }
}
