package pl.lodz.p;

public enum DensityLevel {
    LOW(10, "niski poziom zagęszczenia"),
    MEDIUM(30, "średni poziom zagęszczenia"),
    HIGH(50, "wysoki poziom zagęszczenia");

    private final int percent;
    private final String description;

    DensityLevel(int percent, String description) {
        this.percent = percent;
        this.description = description;
    }

    public int getPercent() {
        return percent;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return percent + "% - " + description;
    }
}
