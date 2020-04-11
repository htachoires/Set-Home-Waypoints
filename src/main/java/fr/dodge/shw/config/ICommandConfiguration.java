package fr.dodge.shw.config;

public interface ICommandConfiguration {

    void setCooldown(int cooldown);

    int getCooldown();

    void setTravelThroughDimension(boolean travelThroughDimension);

    boolean isTravelThroughDimension();

    void setMaxWaypoints(int maxWaypoints);

    int getMaxWaypoints();

}
