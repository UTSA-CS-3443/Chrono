package edu.utsa.cs3443.chrono.models;

public class Theme extends Unlockable{

    private String name;
    private String themeCSS;
    private String buttonTheme;

    public Theme(int cost, boolean unlocked, String name, String themeCSS, String buttonTheme) {
        super(cost, unlocked);
        this.name = name;
        this.themeCSS = themeCSS;
        this.buttonTheme = buttonTheme;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThemeCSS() {
        return themeCSS;
    }

    public void setThemeCSS(String themeCSS) {
        this.themeCSS = themeCSS;
    }

    public String getButtonTheme() {
        return buttonTheme;
    }

    public void setButtonTheme(String buttonTheme) {
        this.buttonTheme = buttonTheme;
    }
}
