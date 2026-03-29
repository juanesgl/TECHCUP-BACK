package edu.dosw.proyect.core.models.enums;

public enum TacticalFormation {
    F_1_2_3_1("1-2-3-1", 1, 2, 3, 1),
    F_1_3_2_1("1-3-2-1", 1, 3, 2, 1),
    F_1_2_2_2("1-2-2-2", 1, 2, 2, 2),
    F_1_3_1_2("1-3-1-2", 1, 3, 1, 2),
    F_1_2_1_3("1-2-1-3", 1, 2, 1, 3);

    private final String displayName;
    private final int goalkeepers;
    private final int defenders;
    private final int midfielders;
    private final int forwards;

    TacticalFormation(String displayName, int goalkeepers, int defenders,
                      int midfielders, int forwards) {
        this.displayName  = displayName;
        this.goalkeepers  = goalkeepers;
        this.defenders    = defenders;
        this.midfielders  = midfielders;
        this.forwards     = forwards;
    }

    public String getDisplayName()  { return displayName;  }
    public int    getGoalkeepers()  { return goalkeepers;  }
    public int    getDefenders()    { return defenders;    }
    public int    getMidfielders()  { return midfielders;  }
    public int    getForwards()     { return forwards;     }

    public int getTotalStarters() {
        return goalkeepers + defenders + midfielders + forwards;
    }
}
